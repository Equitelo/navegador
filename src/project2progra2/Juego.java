package project2progra2;

import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;

public class Juego {
    private String nombre;
    private String genero;
    private String creador;
    private String fecha;
    private String ruta;

    public Juego(String nombre, String genero, String creador, String fecha, String ruta) {
        this.nombre = nombre;
        this.genero = genero;
        this.creador = creador;
        this.fecha=fecha;
        this.ruta = ruta;
    }

    public String getNombre() {
        return nombre;
    }

    public void mostrarInfo() {
        JOptionPane.showMessageDialog(null, "Juego: " + nombre + "\nGénero: " + genero + "\nCreador: " + creador + "\nFecha Lanzamiento: "+fecha);
    }

    public void guardarEnArchivo(RandomAccessFile file) throws IOException {
        file.writeUTF(nombre);
        file.writeUTF(genero);
        file.writeUTF(creador);
        file.writeUTF(fecha);
        file.writeUTF(ruta);
    }

    public static Juego leerDeArchivo(RandomAccessFile file) throws IOException {
        String nombre = file.readUTF();
        String genero = file.readUTF();
        String creador = file.readUTF();
        String fecha = file.readUTF();
        String ruta = file.readUTF();
        return new Juego(nombre, genero, creador, fecha, ruta);
    }
}
