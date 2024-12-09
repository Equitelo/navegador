package project2progra2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class Lista {

    public NodoMusic inicio = null;
    private int size = 0;
    private Player reproductor;
    private Thread hiloReproduccion;
    private boolean enPausa = false;
    private FileInputStream flujoArchivo;
    private long longitudTotal;
    private long posicionPausa;

    public boolean isEmpty() {
        return inicio == null;
    }
    
    public NodoMusic getInicio() {
        return inicio; 
    }

    public void addNodo(NodoMusic obj) {
        if (obj == null) {
            return;
        }
        if (isEmpty()) {
            inicio = obj;
        } else {
            NodoMusic tmp = inicio;
            while (tmp.siguiente != null) {
                tmp = tmp.siguiente;
            }
            tmp.siguiente = obj;
        }
        size++;
    }

    public void guardarListaEnArchivo(File archivo) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            NodoMusic tmp = inicio;
            while (tmp != null) {
                tmp.guardarEnArchivo(raf);
                tmp = tmp.siguiente;
            }
        } catch (IOException e) {
            System.err.println("Error al guardar lista en archivo: " + e.getMessage());
        }
    }

    public void cargarListaDeArchivo(File archivo) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                addNodo(NodoMusic.leerDeArchivo(raf));
            }
        } catch (IOException e) {
            System.err.println("Error al cargar lista desde archivo: " + e.getMessage());
        }
    }
    
    public void play(String nombre) {
        if (isEmpty()) {
            return;
        }

        NodoMusic tmp = inicio;
        while (tmp != null) {
            if (tmp.nombre.equals(nombre)) {
                try {
                    stop();

                    flujoArchivo = new FileInputStream(tmp.musicpath);
                    longitudTotal = flujoArchivo.available();
                    reproductor = new Player(flujoArchivo);

                    hiloReproduccion = new Thread(() -> {
                        try {
                            reproductor.play();
                        } catch (Exception e) {
                        }
                    });

                    hiloReproduccion.start();
                } catch (Exception e) {
                }
                return;
            }
            tmp = tmp.siguiente;
        }
    }

    public void pause() {
        if (reproductor != null && hiloReproduccion != null && hiloReproduccion.isAlive()) {
            try {
                enPausa = true;
                posicionPausa = longitudTotal - flujoArchivo.available();
                reproductor.close();
            } catch (Exception e) {
            }
        }
    }

    public void resume(String nombre) {
        if (enPausa) {
            try {
                flujoArchivo = new FileInputStream(findRuta(nombre));
                flujoArchivo.skip(posicionPausa);
                reproductor = new Player(flujoArchivo);

                hiloReproduccion = new Thread(() -> {
                    try {
                        reproductor.play();
                    } catch (Exception e) {
                    }
                });

                hiloReproduccion.start();
                enPausa = false;
            } catch (Exception e) {
            }
        }
    }

    public void stop() {
        if (reproductor != null) {
            try {
                reproductor.close();
                if (hiloReproduccion != null && hiloReproduccion.isAlive()) {
                    hiloReproduccion.interrupt();
                }
            } catch (Exception e) {
            }
        }
    }

    private String findRuta(String nombre) {
        NodoMusic tmp = inicio;
        while (tmp != null) {
            if (tmp.nombre.equals(nombre)) {
                return tmp.musicpath;
            }
            tmp = tmp.siguiente;
        }
        return null;
    }

    public NodoMusic findNodo(String nombre) {
        NodoMusic tmp = inicio;
        while (tmp != null) {
            if (tmp.nombre.equals(nombre)) {
                return tmp;
            }
            tmp = tmp.siguiente;
        }
        return null;
    }
    
    public void guardarCancionEnGeneral(NodoMusic nodo) {
        File musicaSpotify = new File("MusicaSpotify");
        if (!musicaSpotify.exists()) {
            musicaSpotify.mkdir();
        }
        File destino = new File(musicaSpotify, nodo.nombre + ".mp3");
        nodo.guardarCancionGeneral(destino);
    }
}
