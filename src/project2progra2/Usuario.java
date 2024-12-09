package project2progra2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Usuario {

    protected String userName;
    protected String password;
    protected Rol rol;
    private RandomAccessFile musicRAF;
    private RandomAccessFile gamesRAF;
    protected boolean activeUser;

    public Usuario(String userName, String password, Rol rol) {
        this.userName = userName;
        this.password = password;
        this.rol = rol;

        File userFolder = new File("Users/" + userName);
        File musicFolder = new File(userFolder, "musica");
        File gamesFolder = new File(userFolder, "juegos");

        try {
            if (!userFolder.exists()) {
                userFolder.mkdirs();
            }
            if (!musicFolder.exists()) {
                musicFolder.mkdir();
            }
            if (!gamesFolder.exists()) {
                gamesFolder.mkdir();
            }

            musicRAF = new RandomAccessFile(new File(musicFolder, "music.msc"), "rw");
            gamesRAF = new RandomAccessFile(new File(gamesFolder, "games.gms"), "rw");

        } catch (IOException e) {
            System.out.println("Error al crear carpetas o archivos: " + e.getMessage());
        }

        this.activeUser = true;
    }

    public String getUser() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void activarCuenta() {
        this.activeUser = true;
    }

    public void desactivarCuenta() {
        this.activeUser = false;
    }

    public void agregarJuego(Juego juego) {
        try {
            gamesRAF.seek(gamesRAF.length());
            juego.guardarEnArchivo(gamesRAF);
        } catch (IOException e) {
            System.out.println("Error al guardar juego: " + e.getMessage());
        }
    }
}