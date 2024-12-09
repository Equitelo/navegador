package project2progra2;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Shop {
    
    private List<Juego> juegosDisponibles;
    
    public Shop() {
        this.juegosDisponibles = new ArrayList<>();
    }
    
    public void agregarJuego(Juego juego){
        juegosDisponibles.add(juego);
        JOptionPane.showMessageDialog(null, "Juego agregado correctamente: "+juego.getNombre());
    }
    
    public List<Juego> listarJuegos(){
        return juegosDisponibles;
    }
    
    public void mostrarJuegos(){
        if (juegosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay juegos disponibles en la tienda.");
        } else {
            JOptionPane.showMessageDialog(null, "Juegos disponibles en la tienda: ");
            for (Juego juego:juegosDisponibles) {
                juego.mostrarInfo();
            }
        }
    }
    
}
