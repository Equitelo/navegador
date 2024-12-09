package project2progra2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class Logica {
    
    RandomAccessFile usrs;
    private final File archivoMensajes;
    private Usuario []userList;
    private int tamanio;
    private ListaJuegos listaJuegosGlobal;
    public Usuario usuarioActual;
    
    public Logica () {
        userList = new Usuario[30];
        tamanio = 0;
        this.usuarioActual=null;
        File myFile = new File("admin");
        myFile.mkdir();
        try{
            usrs = new RandomAccessFile("admin/usrs.usrs","rw");
        }catch(IOException ex){
            System.out.println("Error "+ex.getMessage());
        }
        listaJuegosGlobal = new ListaJuegos();
        this.archivoMensajes = new File("chat_mensajes.dat");
        this.usuarioActual=null;
        try{
            if (!archivoMensajes.exists()) {
                archivoMensajes.createNewFile();
            }
        }catch(IOException e){
            System.err.println("Error al crear el archivo de mensajes: " + e.getMessage());
        }
    }
    
    public ListaJuegos getListaJuegos() {
        return listaJuegosGlobal;
    }
    
    public boolean esAdmin() {
        return usuarioActual instanceof Admin;
    }
    
    public void agregarJuegoGlobal(String nombre, String genero, String creador, String ruta, String rutaImagen) {
        if (!esAdmin()) {
            JOptionPane.showMessageDialog(null, "Solo los administradores pueden agregar juegos.");
            return;
        }
        NodoJuego nuevoJuego = new NodoJuego(nombre, genero, creador, ruta, rutaImagen);
        listaJuegosGlobal.addJuego(nuevoJuego);
        JOptionPane.showMessageDialog(null, "Juego agregado a la lista global.");
    }
    public void mostrarJuegosGlobales() {
        JOptionPane.showMessageDialog(null, "Lista global de juegos: ");
        listaJuegosGlobal.mostrarJuegos();
    }
    
    public String getFileUsers(){
        return "admin/usrs.usrs";
    }
    
    public void createAccount(String name, String password) throws IOException {
        if (buscarUsuario(name, 0) != null) {
            JOptionPane.showMessageDialog(null, "El usuario ya existe.");
            return;
        }

        Rol rol = existeAdmin() ? Rol.USUARIO : Rol.ADMINISTRADOR;

        Usuario user;
        if (rol == Rol.ADMINISTRADOR) {
            user = new Admin(name, password, rol);
        } else {
            user = new Usuario(name, password, rol);
        }

        File userFolder = new File("Users/" + name);
        File musicFolder = new File(userFolder, "musica");
        File gamesFolder = new File(userFolder, "juegos");

        if (!userFolder.exists()) {
            userFolder.mkdirs(); 
        }
        if (!musicFolder.exists()) {
            musicFolder.mkdir(); 
        }
        if (!gamesFolder.exists()) {
            gamesFolder.mkdir(); 
        }

        agregarUser(user);
        usuarioActual = user;

        JOptionPane.showMessageDialog(null, "Usuario registrado como "
                + (rol == Rol.ADMINISTRADOR ? "Administrador" : "Usuario Regular"));
    }
    
    public boolean existeAdmin(){
        for (int i = 0; i < tamanio; i++) {
            if (userList[i]!=null && userList[i].getRol()==Rol.ADMINISTRADOR) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList getUsers(){
        ArrayList<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < tamanio; i++) {
            if (userList[i]!=null) {
               usuarios.add(userList[i]);
            }
        }
        return usuarios;
    }
    
    public void employeeList()throws IOException{
        usrs.seek(0);
        while(usrs.getFilePointer ()<= usrs.length()){
            String name=usrs.readUTF();
            String password=usrs.readUTF();
            Rol rol = Rol.valueOf(usrs.readUTF());
            Date dateH= new Date(usrs.readLong());
            Usuario user = new Usuario(name, password,rol);
            this.agregarUser(user);
        }
        
    }
    
    public Usuario buscarUsuario(String usuario, int pos){
        
        Usuario user = userList[pos];
        
        if (pos >= tamanio) {
            return null;
        }
        
        if (user != null && user.getUser().equals(usuario)) {
            return user;
        }
        
        return buscarUsuario(usuario, pos+1);
    }
    
    public boolean agregarUser(Usuario usuario) {
        Usuario validar = this.buscarUsuario(usuario.getUser(), 0);
        
        if (validar == null) {
            userList[tamanio] = usuario;
            tamanio++;
            return true;
        }
        
        return false;
    }
    
    public boolean logIn(String name, String password) {
        Usuario user = buscarUsuario(name, 0);
        if (user != null && user.getPassword().equals(password)) {
            usuarioActual = user;
            JOptionPane.showMessageDialog(null, "Inicio de sesion exitoso como "+(user.getRol() == Rol.ADMINISTRADOR?"Administrador":"Usuario") );
            return true;
        }
        JOptionPane.showMessageDialog(null, "Usuario o password incorrectos");
        return false;
    }
    
    public boolean logOut(String password) {
        if (usuarioActual != null && usuarioActual.getPassword().equals(password)) {
            usuarioActual = null;
            JOptionPane.showMessageDialog(null, "Cierre de sesión exitoso.");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta o no hay sesión activa.");
            return false;
        }
    }
    
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
    
    public Usuario getUserActual(){
        return this.usuarioActual;
    }
    
    public void enviarMensaje(String mensaje) {
        String usuario = (usuarioActual != null) ? usuarioActual.getUser() : "Sistema";
        String mensajeCompleto = usuario + ": " + mensaje;

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivoMensajes, true))) {
            dos.writeUTF(mensajeCompleto);
            dos.writeLong(Calendar.getInstance().getTimeInMillis()); // Timestamp
        } catch (IOException e) {
            System.err.println("Error al escribir el mensaje: " + e.getMessage());
        }
    }

    public String obtenerMensajes() {
        StringBuilder builder = new StringBuilder();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(archivoMensajes))) {
            while (dis.available() > 0) {
                String mensaje = dis.readUTF();
                long timestamp = dis.readLong();
                builder.append(mensaje).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error al leer los mensajes: " + e.getMessage());
        }

        return builder.toString();
    }
}
