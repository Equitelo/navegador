package project2progra2;

import javax.swing.*;

public class shopGUI extends JFrame {

    private DefaultListModel<String> modeloJuegos;
    private JList<String> listaJuegos;
    private Logica logic; // Referencia a la lógica central

    public shopGUI(Logica logic) {
        this.logic = logic;
        initComponents();
    }

    private void initComponents() {
        setSize(800, 800);
        setTitle("Tienda de Juegos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        modeloJuegos = new DefaultListModel<>();
        listaJuegos = new JList<>(modeloJuegos);

        JScrollPane scrollPane = new JScrollPane(listaJuegos);
        scrollPane.setBounds(20, 20, 750, 700);

        add(scrollPane);
        setLayout(null);
    }

    public void agregarJuegoATienda(String nombre, String genero, String creador, String fecha, String archivo, String imagen) {
        String juegoInfo = "Nombre: " + nombre + ", Género: " + genero + ", Creador: " + creador + ", Fecha: " + fecha;
        modeloJuegos.addElement(juegoInfo);
    }

    public void cargarJuegosDesdeLogica() {
        logic.getListaJuegos().mostrarJuegos(); // Puedes personalizar cómo mostrar los juegos
    }
}
