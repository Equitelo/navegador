package project2progra2;

public class NodoJuego {
    private String nombre;
    private String genero;
    private String creador;
    private String rutaArchivo; 
    private String rutaImagen;  
    public NodoJuego siguiente;

    public NodoJuego(String nombre, String genero, String creador, String rutaArchivo, String rutaImagen) {
        this.nombre = nombre;
        this.genero = genero;
        this.creador = creador;
        this.rutaArchivo = rutaArchivo;
        this.rutaImagen = rutaImagen;
        this.siguiente = null;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public String getCreador() {
        return creador;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }
}
