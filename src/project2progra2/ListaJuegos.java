package project2progra2;

public class ListaJuegos {
    private NodoJuego inicio;
    private int size;

    public ListaJuegos() {
        this.inicio = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return inicio == null;
    }

    public void addJuego(NodoJuego nuevoJuego) {
        if (isEmpty()) {
            inicio = nuevoJuego;
        } else {
            NodoJuego temp = inicio;
            while (temp.siguiente != null) {
                temp = temp.siguiente;
            }
            temp.siguiente = nuevoJuego;
        }
        size++;
    }
    
    public NodoJuego getInicio() {
        return inicio;
    }

    public void mostrarJuegos() {
        NodoJuego temp = inicio;
        while (temp != null) {
            System.out.println("Juego: " + temp.getNombre() + ", Género: " + temp.getGenero() + 
                               ", Creador: " + temp.getCreador() + ", Ruta: " + temp.getRutaArchivo());
            temp = temp.siguiente;
        }
    }
    
    public void eliminarJuego(NodoJuego juego) {
        if (inicio == null) {
            return;
        }

        if (inicio.equals(juego)) {
            inicio = inicio.siguiente;
            return;
        }

        NodoJuego actual = inicio;
        while (actual.siguiente != null) {
            if (actual.siguiente.equals(juego)) {
                actual.siguiente = actual.siguiente.siguiente;
                return;
            }
            actual = actual.siguiente;
        }
    }
    
    public int getSize() {
        return size;
    }
}
