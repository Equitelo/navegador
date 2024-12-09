package project2progra2;

import javax.swing.JOptionPane;

public class Admin extends Usuario{
    
    public Admin(String userName, String password, Rol rol){
        super(userName, password, rol);
    }
    
    public void agregarJuego(Juego juego, Shop shop){
       shop.agregarJuego(juego);
       JOptionPane.showMessageDialog(null, "El administrador "+this.getUser()+" agrego un juego a la tienda.");
    }
    
}
