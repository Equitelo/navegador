package project2progra2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class gamesGUI extends JFrame {

    private JPanel panel;
    private JPanel gamesListPanel; 
    private JScrollPane scrollPane;

    private JButton goBackBtn;
    private JButton addGameBtn;

    private Logica admin;

    public gamesGUI(Logica admin) {
        this.admin = admin;
        initComponents();
        cargarJuegos();
    }

    private void initComponents() {
        setSize(800, 600);
        setTitle("Gestión de Juegos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(Color.cyan);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        gamesListPanel = new JPanel();
        gamesListPanel.setLayout(new BoxLayout(gamesListPanel, BoxLayout.Y_AXIS)); // Mostrar juegos en columnas

        scrollPane = new JScrollPane(gamesListPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        goBackBtn = new JButton("<- Regresar");
        goBackBtn.addActionListener(evt -> {
            PrincipalPage pPage = new PrincipalPage(admin);
            pPage.setVisible(true);
            pPage.setLocationRelativeTo(null);
            dispose();
        });

        addGameBtn = new JButton("Agregar Juego");
        addGameBtn.addActionListener(evt -> {
            AddingGame agregarJuego = new AddingGame(this, admin);
            agregarJuego.setVisible(true);
        });

        buttonPanel.add(goBackBtn);
        buttonPanel.add(addGameBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(panel);
    }

    private void cargarJuegos() {
        
        gamesListPanel.removeAll();

        NodoJuego nodoActual = admin.getListaJuegos().getInicio();
        while (nodoActual != null) {
            
            JPanel juegoPanel = crearPanelJuego(nodoActual);
            gamesListPanel.add(juegoPanel);

            nodoActual = nodoActual.siguiente;
        }

        gamesListPanel.revalidate();
        gamesListPanel.repaint();
    }
    
    public void actualizarListaJuegos() {
        gamesListPanel.removeAll();
        cargarJuegos();
    }

    private JPanel crearPanelJuego(NodoJuego nodoJuego) {
        JPanel juegoPanel = new JPanel();
        juegoPanel.setLayout(new BorderLayout());
        juegoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        juegoPanel.setPreferredSize(new Dimension(750, 150));

        JLabel imagenLabel = new JLabel();
        imagenLabel.setPreferredSize(new Dimension(150, 150));

        try {
            File imgFile = new File(nodoJuego.getRutaImagen());
            if (imgFile.exists()) {
                Image img = ImageIO.read(imgFile).getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imagenLabel.setIcon(new ImageIcon(img));
            } else {
                imagenLabel.setText("Sin imagen");
            }
        } catch (IOException e) {
            imagenLabel.setText("Error al cargar imagen");
        }

        juegoPanel.add(imagenLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Nombre: " + nodoJuego.getNombre()));
        infoPanel.add(new JLabel("Género: " + nodoJuego.getGenero()));
        infoPanel.add(new JLabel("Creador: " + nodoJuego.getCreador()));

        juegoPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));

        JButton descargarBtn = new JButton("Descargar");
        descargarBtn.addActionListener(evt -> descargarJuego(nodoJuego));
        buttonPanel.add(descargarBtn);

        JButton eliminarBtn = new JButton("Eliminar");
        eliminarBtn.addActionListener(evt -> eliminarJuego(nodoJuego));
        buttonPanel.add(eliminarBtn);

        juegoPanel.add(buttonPanel, BorderLayout.EAST);

        return juegoPanel;
    }
    
    private void eliminarJuego(NodoJuego juego) {
        
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar el juego " + juego.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return; 
        }

        File archivoJuegoSteam = new File("JuegosSteam", juego.getNombre() + ".juego");
        if (archivoJuegoSteam.exists()) {
            archivoJuegoSteam.delete();
        }

        admin.getListaJuegos().eliminarJuego(juego);

        actualizarListaJuegos();

        JOptionPane.showMessageDialog(this, "Juego eliminado con éxito.");
    }

    private void descargarJuego(NodoJuego juego) {
        File archivoJuegoSteam = new File("JuegosSteam", juego.getNombre() + ".juego");
        if (!archivoJuegoSteam.exists()) {
            JOptionPane.showMessageDialog(this, "El juego no se encontró en JuegosSteam.");
            return;
        }

        File carpetaUsuario = new File("Users/" + admin.getUserActual().getUser() + "/juegos");
        if (!carpetaUsuario.exists()) {
            carpetaUsuario.mkdirs();
        }

        File archivoDestino = new File(carpetaUsuario, juego.getNombre() + ".juego");

        try (RandomAccessFile rafSteam = new RandomAccessFile(archivoJuegoSteam, "r");
             RandomAccessFile rafUsuario = new RandomAccessFile(archivoDestino, "rw")) {

            rafUsuario.writeUTF(rafSteam.readUTF());
            rafUsuario.writeUTF(rafSteam.readUTF());
            rafUsuario.writeUTF(rafSteam.readUTF());
            rafUsuario.writeUTF(rafSteam.readUTF());
            rafUsuario.writeUTF(rafSteam.readUTF());
            rafUsuario.writeUTF(rafSteam.readUTF());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al descargar el juego: " + e.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(this, "Juego descargado en tu carpeta de juegos.");
    }
}