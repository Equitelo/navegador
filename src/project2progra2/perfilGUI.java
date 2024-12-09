package project2progra2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class perfilGUI extends JFrame implements ActionListener {
    private JPanel panelLeft, panelRight;
    private JLabel myMusicLbl, myGamesLbl;
    private JButton goBackBtn;
    private ArrayList<JButton> musicButtons;
    private JButton pauseResumeBtn; // Botón de Pausar/Reanudar
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private Lista playlist;
    private NodoMusic currentPlaying;

    Logica admin;

    public perfilGUI(Logica admin) {
        this.admin = admin;
        musicButtons = new ArrayList<>();
        playlist = new Lista();
        initComponents();
        cargarMusica();
        cargarJuegos();
    }

    private void initComponents() {
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        panelLeft = new JPanel();
        panelLeft.setPreferredSize(new Dimension(400, 800));
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS)); // Disposición vertical

        myMusicLbl = new JLabel("Mi Música");
        myMusicLbl.setFont(new Font("Consola", Font.PLAIN, 20));
        myMusicLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        goBackBtn = new JButton("<- Regresar");
        goBackBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        goBackBtn.addActionListener(this);

        pauseResumeBtn = new JButton("Pausar"); 
        pauseResumeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseResumeBtn.addActionListener(this);

        panelLeft.add(goBackBtn);
        panelLeft.add(Box.createRigidArea(new Dimension(0, 10))); 
        panelLeft.add(myMusicLbl);
        panelLeft.add(Box.createRigidArea(new Dimension(0, 10))); 
        panelLeft.add(pauseResumeBtn);

        panelRight = new JPanel();
        panelRight.setPreferredSize(new Dimension(400, 800));
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
        
        myGamesLbl = new JLabel("Mis Juegos");
        myGamesLbl.setFont(new Font("Consola", Font.PLAIN, 20));
        myGamesLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelRight.add(myGamesLbl);

        add(new JScrollPane(panelLeft), BorderLayout.WEST);
        add(new JScrollPane(panelRight), BorderLayout.EAST);
    }

    private void cargarMusica() {
        
        File musicaFolder = new File("Users/" + admin.getUserActual().getUser() + "/musica");
        if (!musicaFolder.exists()) {
            musicaFolder.mkdirs(); 
        }

        File[] archivosMusica = musicaFolder.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".dat"));
        if (archivosMusica != null) {
            for (File archivo : archivosMusica) {
                JButton btnCancion = new JButton(archivo.getName().replace(".mp3", "").replace(".dat", ""));
                btnCancion.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnCancion.addActionListener(evt -> reproducirCancion(archivo));
                panelLeft.add(btnCancion);
                panelLeft.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado
                musicButtons.add(btnCancion);
            }
        }

        panelLeft.revalidate();
        panelLeft.repaint();
    }

    private void reproducirCancion(File archivo) {
        try {
            if (isPlaying && currentPlaying != null) {
                playlist.stop();
                isPlaying = false;
                isPaused = false;
                pauseResumeBtn.setText("Pausar");
            }

            NodoMusic nodo = new NodoMusic(
                archivo.getName().replace(".mp3", ""), 
                "Desconocido", 
                0, 
                archivo.getAbsolutePath(), 
                "Desconocido", 
                null
            );

            playlist.addNodo(nodo);
            playlist.play(nodo.getNombre());
            currentPlaying = nodo;
            isPlaying = true;
            JOptionPane.showMessageDialog(this, "Reproduciendo: " + nodo.getNombre());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al reproducir la canción: " + e.getMessage());
        }
    }

    private void pausarOReanudar() {
        if (!isPlaying || currentPlaying == null) {
            JOptionPane.showMessageDialog(this, "No hay ninguna canción reproduciéndose.");
            return;
        }

        if (!isPaused) {
            playlist.pause(); 
            pauseResumeBtn.setText("Reanudar");
            isPaused = true;
        } else {
            playlist.resume(currentPlaying.getNombre());
            pauseResumeBtn.setText("Pausar");
            isPaused = false;
        }
    }
    
    private void cargarJuegos(){
        panelRight.removeAll();
        
        File userGamesFolder = new File("Users/" + admin.getUserActual().getUser() + "/juegos");
        
        if (!userGamesFolder.exists() || userGamesFolder.listFiles() == null) {
            panelRight.add(new JLabel("No hay juegos en tu carpeta."));
            panelRight.revalidate();
            panelRight.repaint();
            return;
        }
        
        for (File juego : userGamesFolder.listFiles()) {
            if (juego.isFile()) {
                panelRight.add(crearPanelJuego(juego));
            }
        }
        
        panelRight.revalidate();
        panelRight.repaint();
    }
    
    private JPanel crearPanelJuego(File juego){
        JPanel juegoPanel = new JPanel();
        juegoPanel.setLayout(new BorderLayout());
        juegoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        juegoPanel.setMaximumSize(new Dimension(380, 80));
        
        JLabel nombreJuego = new JLabel("Juego: " + juego.getName());
        nombreJuego.setFont(new Font("Consola", Font.PLAIN, 14));
        nombreJuego.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        juegoPanel.add(nombreJuego, BorderLayout.CENTER);
        
        JButton eliminarBtn = new JButton("Eliminar");
        eliminarBtn.addActionListener(evt -> eliminarJuego(juego));
        juegoPanel.add(eliminarBtn, BorderLayout.EAST);
        
        return juegoPanel;
    }
    
    private void eliminarJuego(File juego){
        int respuesta = JOptionPane.showConfirmDialog(this, 
                "Estas seguro de que deseas eliminar el juego "+juego.getName()+"?", 
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            if (juego.delete()) {
                JOptionPane.showMessageDialog(this, "Juego eliminado con exito.");
                cargarJuegos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el juego.");
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == goBackBtn) {
            PrincipalPage pPage = new PrincipalPage(admin);
            pPage.setVisible(true);
            pPage.setLocationRelativeTo(null);
            dispose();
        } else if (evt.getSource() == pauseResumeBtn) {
            pausarOReanudar(); 
        }
    }
}
