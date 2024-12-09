package project2progra2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PrincipalPage extends JFrame implements ActionListener{
    
    private JPanel generalPanel;
    private JPanel panelBtns;
    private JButton appGames;
    private JButton appMusic;
    private JButton appChat;
    private JButton appPerfil;
    private JButton appLogOut;
    private JButton appAdmin;
    private ImageIcon gamesImg;
    private ImageIcon musicImg;
    private ImageIcon chatImg;
    private ImageIcon perfilImg;
    private ImageIcon logOutImg;
    private ImageIcon adminImg;
    
    Logica admin;
    
    public PrincipalPage (Logica admin) {
        this.admin=admin;
        initComponents();
    }
    
    private void initComponents(){
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        generalPanel = new JPanel();
        generalPanel.setSize(800, 800);
        generalPanel.setBackground(Color.black);
        generalPanel.setLayout(null);
        
        panelBtns = new JPanel();
        panelBtns.setBackground(Color.black);
        panelBtns.setBounds(300, 200, 200, 200);
        panelBtns.setLayout(new GridLayout(3, 3, 10, 10));
        
        gamesImg = new ImageIcon("src/steam.png");
        appGames = new JButton();
        appGames.setIcon(gamesImg);
        appGames.addActionListener(this);
        
        musicImg = new ImageIcon("src/music.png");
        appMusic = new JButton();
        appMusic.setIcon(musicImg);
        appMusic.addActionListener(this);
        
        chatImg = new ImageIcon("src/chat.png");
        appChat = new JButton();
        appChat.setIcon(chatImg);
        appChat.addActionListener(this);
        
        perfilImg = new ImageIcon("src/profile.png");
        appPerfil = new JButton();
        appPerfil.setIcon(perfilImg);
        appPerfil.addActionListener(this);
        
        logOutImg = new ImageIcon("src/logOut.png");
        appLogOut = new JButton();
        appLogOut.setIcon(logOutImg);
        appLogOut.addActionListener(this);
        
        adminImg = new ImageIcon("src/admin.png");
        appAdmin = new JButton();
        appAdmin.setIcon(adminImg);
        appAdmin.addActionListener(this);
        
        panelBtns.add(appGames);
        panelBtns.add(appMusic);
        panelBtns.add(appChat);
        panelBtns.add(appPerfil);
        panelBtns.add(appLogOut);
        panelBtns.add(appAdmin);
        generalPanel.add(panelBtns);
        this.add(generalPanel);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt){
        if (evt.getSource()==appGames) {
            gamesGUI gGUI = new gamesGUI(admin);
            gGUI.setVisible(true);
            gGUI.setLocationRelativeTo(null);
            dispose();
        } else if (evt.getSource()==appMusic) {
            musicGUI mGUI = new musicGUI(admin);
            mGUI.setVisible(true);
            mGUI.setLocationRelativeTo(null);
            dispose();
        } else if (evt.getSource()==appChat) {
            seleccionarDestinatario();
        } else if (evt.getSource()==appPerfil) {
            perfilGUI pGUI = new perfilGUI(admin);
            pGUI.setVisible(true);
            pGUI.setLocationRelativeTo(null);
            dispose();
        } else if (evt.getSource()==appLogOut) {
            String password = JOptionPane.showInputDialog("Ingrese su contraseña para cerrar sesion: ");
            if (admin.logOut(password)) {
                MenuGUI mGUI = new MenuGUI(admin);
                mGUI.setVisible(true);
                mGUI.setLocationRelativeTo(null);
                dispose();
            }
        } else if (evt.getSource()==appAdmin) {
            adminGUI aGUI = new adminGUI(admin);
            aGUI.setVisible(true);
            aGUI.setLocationRelativeTo(null);
            dispose();
        }
    }
    
    private void seleccionarDestinatario() {
        ArrayList<Usuario> usuarios = admin.getUsers(); 
        usuarios.removeIf(u -> u.getUser().equals(admin.getUserActual().getUser()));
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay otros usuarios disponibles para chatear.");
            return;
        }

        String[] nombresUsuarios = usuarios.stream().map(Usuario::getUser).toArray(String[]::new);
        String destinatario = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione un usuario para chatear:",
                "Iniciar Chat",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresUsuarios,
                nombresUsuarios[0]
        );

        if (destinatario != null) {
            iniciarChat(destinatario);
        }
    }

    private void iniciarChat(String destinatario) {
    try {
        String userName = admin.getUserActual().getUser(); 

        chatGUI ventanaUsuarioActual = new chatGUI(userName, destinatario);
        chatGUI ventanaDestinatario = new chatGUI(destinatario, userName);

        ventanaUsuarioActual.sincronizarVentana(ventanaDestinatario);
        ventanaDestinatario.sincronizarVentana(ventanaUsuarioActual);

        ventanaUsuarioActual.setVisible(true);
        ventanaDestinatario.setVisible(true);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al iniciar el chat: " + e.getMessage());
    }
}
    
}
