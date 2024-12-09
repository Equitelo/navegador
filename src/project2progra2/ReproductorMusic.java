package reproductormusic;

import java.io.File;

public class ReproductorMusic {

    private static final String mainpath = "inge/";

    public ReproductorMusic() {
        crearDirectorio(mainpath);
        crearDirectorio(mainpath + "musica");
        crearDirectorio(mainpath + "portadas");
    }

    private void crearDirectorio(String path) {
        File directorio = new File(path);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio creado: " + path);
            } 
        }
    }

    public static String getRutaMusica() {
        return mainpath + "musica/";
    }

    public static String getRutaPortadas() {
        return mainpath + "portadas/";
    }
    
}