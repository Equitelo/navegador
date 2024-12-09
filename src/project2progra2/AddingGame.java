package project2progra2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AddingGame extends JFrame implements ActionListener {

    private JPanel panel;
    private JTextField nombreTxt, creadorTxt;
    private JComboBox<String> generoBox;
    private JLabel purchasedLbl, imagenLbl, archivoLbl;
    private JButton dateBtn, agregarImagenBtn, agregarArchivoBtn, agregarBtn;

    private gamesGUI parent;
    private Logica logic;

    private String rutaImagen; 
    private String rutaArchivo; 

    public AddingGame(gamesGUI parent, Logica logic) {
        this.parent = parent;
        this.logic = logic;
        initComponents();
    }

    private void initComponents() {
        setSize(600, 850);
        setTitle("Agregar Juego");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.cyan);

        JLabel nombreLbl = new JLabel("Nombre:");
        nombreLbl.setBounds(20, 20, 100, 25);
        nombreTxt = new JTextField();
        nombreTxt.setBounds(140, 20, 200, 25);

        JLabel generoLbl = new JLabel("Género:");
        generoLbl.setBounds(20, 60, 100, 25);
        String[] generos = {"Deportes", "Acción", "Aventura", "Estrategia"};
        generoBox = new JComboBox<>(generos);
        generoBox.setBounds(140, 60, 200, 25);

        JLabel creadorLbl = new JLabel("Creador:");
        creadorLbl.setBounds(20, 100, 100, 25);
        creadorTxt = new JTextField();
        creadorTxt.setBounds(140, 100, 200, 25);

        dateBtn = new JButton("Generar fecha");
        dateBtn.setBounds(20, 140, 150, 30);
        dateBtn.addActionListener(this);
        purchasedLbl = new JLabel("DD/MM/YY");
        purchasedLbl.setBounds(180, 140, 100, 25);

        agregarArchivoBtn = new JButton("Agregar Archivo");
        agregarArchivoBtn.setBounds(20, 180, 150, 30);
        agregarArchivoBtn.addActionListener(this);
        archivoLbl = new JLabel();
        archivoLbl.setBounds(180, 180, 300, 30);

        agregarImagenBtn = new JButton("Agregar Imagen");
        agregarImagenBtn.setBounds(20, 220, 150, 30);
        agregarImagenBtn.addActionListener(this);
        imagenLbl = new JLabel();
        imagenLbl.setBounds(180, 220, 150, 150);
        imagenLbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        agregarBtn = new JButton("Agregar");
        agregarBtn.setBounds(200, 400, 100, 30);
        agregarBtn.addActionListener(this);

        panel.add(nombreLbl);
        panel.add(nombreTxt);
        panel.add(generoLbl);
        panel.add(generoBox);
        panel.add(creadorLbl);
        panel.add(creadorTxt);
        panel.add(dateBtn);
        panel.add(purchasedLbl);
        panel.add(agregarArchivoBtn);
        panel.add(archivoLbl);
        panel.add(agregarImagenBtn);
        panel.add(imagenLbl);
        panel.add(agregarBtn);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == dateBtn) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            purchasedLbl.setText(day + "/" + month + "/" + year);
        } else if (evt.getSource() == agregarArchivoBtn) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Ejecutables", "exe", "jar"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
                archivoLbl.setText(rutaArchivo); // Mostrar la ruta seleccionada
            }
        } else if (evt.getSource() == agregarImagenBtn) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaImagen = fileChooser.getSelectedFile().getAbsolutePath();
                ImageIcon imgIcon = new ImageIcon(new ImageIcon(rutaImagen).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                imagenLbl.setIcon(imgIcon); // Mostrar la imagen seleccionada
            }
        } else if (evt.getSource() == agregarBtn) {
            if (!logic.esAdmin()) {
                JOptionPane.showMessageDialog(this, "Solo los administradores pueden agregar juegos.");
                return;
            }

            // Obtener datos del juego
            String nombre = nombreTxt.getText();
            String genero = generoBox.getSelectedItem().toString();
            String creador = creadorTxt.getText();
            String fecha = purchasedLbl.getText();
            String archivo = rutaArchivo;
            String imagen = rutaImagen;

            if (nombre.isEmpty() || creador.isEmpty() || fecha.equals("DD/MM/YY") || archivo == null || imagen == null) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.");
                return;
            }

            // Crear carpeta JuegosSteam si no existe
            File carpetaJuegosSteam = new File("JuegosSteam");
            if (!carpetaJuegosSteam.exists()) {
                carpetaJuegosSteam.mkdir();
            }

            // Guardar juego en la carpeta JuegosSteam
            File archivoJuego = new File(carpetaJuegosSteam, nombre + ".juego");
            try (RandomAccessFile raf = new RandomAccessFile(archivoJuego, "rw")) {
                raf.writeUTF(nombre);
                raf.writeUTF(genero);
                raf.writeUTF(creador);
                raf.writeUTF(fecha);
                raf.writeUTF(archivo); // Ruta original del archivo
                raf.writeUTF(imagen);  // Ruta original de la imagen
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el juego: " + e.getMessage());
                return;
            }

            // Agregar a la lista global
            logic.getListaJuegos().addJuego(new NodoJuego(nombre, genero, creador, archivo, imagen));

            // Actualizar la lista en gamesGUI
            parent.actualizarListaJuegos();

            JOptionPane.showMessageDialog(this, "Juego agregado con éxito.");
            dispose();
        }
    }
}
