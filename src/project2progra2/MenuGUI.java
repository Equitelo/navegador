package project2progra2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuGUI extends JFrame implements ActionListener{
    
    JPanel panel;
    JLabel titleMenu;
    JButton logInBtn;
    JButton signInBtn;
    JButton exitBtn;
    
    Logica admin;
    public MenuGUI(Logica admin){
        this.admin=admin;
        initComponents();
    }
    
    private void initComponents(){
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new JPanel();
        panel.setSize(800, 800);
        panel.setBackground(Color.cyan);
        panel.setLayout(null);
        
        titleMenu = new JLabel("MENU");
        titleMenu.setFont(new Font("Algerian",Font.PLAIN,50));
        titleMenu.setBounds(315, 50, 200, 60);
        
        logInBtn = new JButton("Log In");
        logInBtn.setFont(new Font("Algerian", Font.PLAIN, 25));
        logInBtn.setBounds(300, 150, 150, 40);
        logInBtn.setFocusable(false);
        logInBtn.addActionListener(this);
        
        signInBtn = new JButton("Sign In");
        signInBtn.setFont(new Font("Algerian", Font.PLAIN, 25));
        signInBtn.setBounds(300, 350, 150, 40);
        signInBtn.setFocusable(false);
        signInBtn.addActionListener(this);
        
        exitBtn = new JButton("Exit");
        exitBtn.setFont(new Font("Algerian", Font.PLAIN, 25));
        exitBtn.setBounds(300, 550, 150, 40);
        exitBtn.setFocusable(false);
        exitBtn.addActionListener(this);
        
        panel.add(titleMenu);
        panel.add(logInBtn);
        panel.add(signInBtn);
        panel.add(exitBtn);
        this.add(panel);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt){
        if (evt.getSource()==logInBtn) {
            LogInGUI login = new LogInGUI(admin);
            login.setVisible(true);
            this.dispose();
        } else if (evt.getSource()==signInBtn) {
            SignInGUI signin = new SignInGUI(admin);
            signin.setVisible(true);
            this.dispose();
        } else if (evt.getSource()==exitBtn) {
            System.exit(0);
        }
    }
    
}
