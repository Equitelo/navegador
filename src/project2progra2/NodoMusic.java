package project2progra2;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.imageio.ImageIO;

public class NodoMusic {

    String nombre;
    String artista;
    String musicpath;
    String genero;
    String rutaImagen;
    Image image;
    NodoMusic siguiente;

    public NodoMusic(String nombre, String artista, int duracion, String musicpath, String genero, String imagepath) {
        this.nombre = nombre;
        this.artista = artista;
        this.musicpath = musicpath;
        this.genero = genero;
        this.rutaImagen = imagepath;
        this.siguiente = null;
        this.image = cargarImagen(imagepath);
    }

    private Image cargarImagen(String imagepath) {
        try {
            return ImageIO.read(new File(imagepath));
        } catch (Exception e) {
            return null; 
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getArtista() {
        return artista;
    }

    public String getMusicpath() {
        return musicpath;
    }

    public String getGenero() {
        return genero;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public Image getImage() {
        return image;
    }
    
    public void guardarComoArchivoIndividual(File carpetaDestino) {
    try {
        if (!carpetaDestino.exists()) {
            carpetaDestino.mkdirs();
        }

        File archivoCancion = new File(carpetaDestino, this.nombre + ".dat");

        try (RandomAccessFile raf = new RandomAccessFile(archivoCancion, "rw")) {
            raf.writeUTF(nombre);
            raf.writeUTF(artista);
            raf.writeUTF(genero);
            raf.writeUTF(musicpath);
            raf.writeUTF(rutaImagen);
        }
    } catch (IOException e) {
        System.err.println("Error al guardar la canción en archivo individual: " + e.getMessage());
    }
}
    
    public void guardarCancionGeneral(File destino) {
        try (RandomAccessFile raf = new RandomAccessFile(destino, "rw")) {
            File original = new File(this.musicpath);
            try (FileInputStream fis = new FileInputStream(original)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    raf.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la canción en carpeta general: " + e.getMessage());
        }
    }

    public void guardarEnArchivo(RandomAccessFile raf) throws IOException {
        raf.writeUTF(nombre);
        raf.writeUTF(artista);
        raf.writeUTF(genero);
        raf.writeUTF(musicpath);
        raf.writeUTF(rutaImagen);
    }

    public static NodoMusic leerDeArchivo(RandomAccessFile raf) throws IOException {
        String nombre = raf.readUTF();
        String artista = raf.readUTF();
        String genero = raf.readUTF();
        String rutaCancion = raf.readUTF();
        String rutaImagen = raf.readUTF();
        return new NodoMusic(nombre, artista, 0, rutaCancion, genero, rutaImagen);
    }
}