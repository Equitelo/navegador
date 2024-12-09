package project2progra2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class musicGUI extends JFrame implements ActionListener {

    private JPanel panelLeft;
    private JPanel panelCenter;
    private JList<String> musicList;
    private DefaultListModel<String> listModel;
    private JButton addBtn;
    private JButton downloadBtn;
    private JButton stopBtn;
    private JButton goBackBtn;
    private JLabel imageLbl;
    private JButton playBtn;
    private boolean playing = false;
    private ImageIcon playImg;
    private ImageIcon pauseImg;
    private Lista playlist;
    private File archivoCanciones;
    
    Logica logica;
    
    public musicGUI(Logica logica) {
        this.logica=logica;
        playlist = new Lista();
        archivoCanciones = new File("MusicaSpotify/canciones.dat");
        if (archivoCanciones.exists()) {
            playlist.cargarListaDeArchivo(archivoCanciones);
        }
        initComponents();
        cargarCancionesEnLista();
    }

    private void initComponents() {
        playImg = new ImageIcon("src/play.png");
        pauseImg = new ImageIcon("src/pause.png");

        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelLeft = new JPanel();
        panelLeft.setBackground(new Color(245, 245, 245));
        panelLeft.setPreferredSize(new Dimension(250, 600));
        panelLeft.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        musicList = new JList<>(listModel);
        musicList.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(musicList);
        panelLeft.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3, 1, 10, 10));
        addBtn = new JButton("Agregar Música");
        addBtn.setFont(new Font("Consola", Font.PLAIN, 12));
        addBtn.addActionListener(this);
        
        downloadBtn = new JButton("Descargar");
        downloadBtn.setFont(new Font("Consola", Font.PLAIN, 12));
        downloadBtn.addActionListener(this);
        
        stopBtn = new JButton("Detener Canción");
        stopBtn.setFont(new Font("Consola", Font.PLAIN, 12));
        stopBtn.addActionListener(this);
        
        goBackBtn = new JButton("<- Regresar");
        goBackBtn.setFont(new Font("Consola", Font.PLAIN, 12));
        goBackBtn.addActionListener(this);
        
        buttonsPanel.add(addBtn);
        buttonsPanel.add(downloadBtn);
        buttonsPanel.add(stopBtn);
        buttonsPanel.add(goBackBtn);
        panelLeft.add(buttonsPanel, BorderLayout.SOUTH);

        panelCenter = new JPanel();
        panelCenter.setBackground(new Color(28, 28, 28));
        panelCenter.setPreferredSize(new Dimension(650, 600));
        panelCenter.setLayout(null);

        imageLbl = new JLabel();
        imageLbl.setBounds(100, 50, 450, 450);
        imageLbl.setHorizontalAlignment(SwingConstants.CENTER);

        playBtn = new JButton();
        playBtn.addActionListener(this);
        playBtn.setBounds(285, 500, 80, 80);
        playBtn.setIcon(playImg);
        playBtn.setContentAreaFilled(false);
        playBtn.setBorderPainted(false);

        panelCenter.add(imageLbl);
        panelCenter.add(playBtn);

        add(panelLeft, BorderLayout.WEST);
        add(panelCenter, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == addBtn) {
            agregarCancion();
        } else if (evt.getSource() == playBtn) {
            reproducirOpausar();
        } else if (evt.getSource() == stopBtn) {
            detenerCancion();
        } else if (evt.getSource() == downloadBtn) {
            descargarCancion();
        } else if (evt.getSource() == goBackBtn) {
            PrincipalPage pPage = new PrincipalPage(logica);
            pPage.setVisible(true);
            pPage.setLocationRelativeTo(null);
            dispose();
        }
    }
    
    private void descargarCancion() {
        String selectedSong = musicList.getSelectedValue();
        if (selectedSong == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una canción para descargar.");
            return;
        }

        NodoMusic nodo = playlist.findNodo(selectedSong);
        if (nodo != null) {
            try {
                // Cambiar a la carpeta de música del usuario
                File musicaFolder = new File("Users/" + logica.getUserActual().getUser() + "/musica");
                if (!musicaFolder.exists()) {
                    musicaFolder.mkdirs(); // Crear carpeta si no existe
                }

                File destino = new File(musicaFolder, nodo.nombre + ".mp3"); // Guardar en música
                nodo.guardarCancionGeneral(destino); // Reutilizamos método de guardado

                JOptionPane.showMessageDialog(this, "Canción descargada en la carpeta de música.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al descargar la canción: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró la canción seleccionada.");
        }
    }
    
    private void cargarCancionesEnLista() {
        NodoMusic tmp = playlist.getInicio();
        while (tmp != null) {
            listModel.addElement(tmp.nombre);
            tmp = tmp.siguiente;
        }
    }
    
    private void agregarCancion() {
        try {
            String artista = JOptionPane.showInputDialog(this, "Ingrese nombre del artista:");
            String nombre = JOptionPane.showInputDialog(this, "Ingrese nombre de la canción:");
            String genero = JOptionPane.showInputDialog(this, "Ingrese el género de la música:");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos MP3", "mp3"));
            fileChooser.setDialogTitle("Seleccionar canción");
            if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String rutaSong = fileChooser.getSelectedFile().getAbsolutePath();

            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png"));
            fileChooser.setDialogTitle("Seleccionar portada");
            if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String rutaImg = fileChooser.getSelectedFile().getAbsolutePath();

            NodoMusic nuevoNodo = new NodoMusic(nombre, artista, 0, rutaSong, genero, rutaImg);
            playlist.addNodo(nuevoNodo);
            listModel.addElement(nombre);

            File carpetaGeneral = new File("MusicaSpotify");
            nuevoNodo.guardarComoArchivoIndividual(carpetaGeneral);

            JOptionPane.showMessageDialog(this, "Canción agregada con éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar la canción.");
        }
    }

    private void reproducirOpausar() {
        String selectedSong = musicList.getSelectedValue();
        if (selectedSong == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una canción para reproducir.");
            return;
        }

        if (!playing) {
            playlist.play(selectedSong);
            actualizarPortada(selectedSong);
            playBtn.setIcon(pauseImg);
        } else {
            playlist.pause();
            playBtn.setIcon(playImg);
        }
        playing = !playing;
    }

    private void detenerCancion() {
        playlist.stop();
        playBtn.setIcon(playImg);
        playing = false;
        imageLbl.setIcon(null);
    }

    private void actualizarPortada(String nombreCancion) {
        NodoMusic tmp = playlist.findNodo(nombreCancion);
        if (tmp != null && tmp.image != null) {
            ImageIcon portada = new ImageIcon(tmp.image);
            Image img = portada.getImage().getScaledInstance(imageLbl.getWidth(), imageLbl.getHeight(), Image.SCALE_SMOOTH);
            imageLbl.setIcon(new ImageIcon(img));
        } else {
            imageLbl.setIcon(null);
        }
    }
}
