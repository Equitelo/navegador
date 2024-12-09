package project2progra2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LogInGUI extends JFrame implements ActionListener{
    
    private JPanel panel;
    private JLabel titleLbl;
    private JLabel userLbl;
    private JLabel passwordLbl;
    private JTextField userTxt;
    private JPasswordField passwordTxt;
    private JButton logInBtn;
    
    Logica admin;
    
    public LogInGUI(Logica admin){
        this.admin=admin;
        initComponents();
    }
    
    private void initComponents(){
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        panel = new JPanel();
        panel.setSize(800, 800);
        panel.setBackground(Color.cyan);
        panel.setLayout(null);
        
        titleLbl = new JLabel("Log In");
        titleLbl.setFont(new Font("Algerian", Font.PLAIN, 50));
        titleLbl.setBounds(300, 50, 200, 60);
        
        userLbl = new JLabel("Username: ");
        userLbl.setFont(new Font("Algerian", Font.PLAIN, 25));
        userLbl.setBounds(200, 200, 300, 40);
        
        userTxt = new JTextField();
        userTxt.setFont(new Font("Calibri", Font.PLAIN, 20));
        userTxt.setBounds(400, 200, 150, 40);
        
        passwordLbl = new JLabel("Password: ");
        passwordLbl.setFont(new Font("Algerian", Font.PLAIN, 25));
        passwordLbl.setBounds(200, 400, 150, 40);
        
        passwordTxt = new JPasswordField();
        passwordTxt.setFont(new Font("Calibri", Font.PLAIN, 20));
        passwordTxt.setBounds(400, 400, 150, 40);
        
        logInBtn = new JButton("Log In");
        logInBtn.setFont(new Font("Algerian", Font.PLAIN, 25));
        logInBtn.setBounds(300, 600, 150, 40);
        logInBtn.setFocusable(false);
        logInBtn.addActionListener(this);
        
        panel.add(titleLbl);
        panel.add(userLbl);
        panel.add(passwordLbl);
        panel.add(userTxt);
        panel.add(passwordTxt);
        panel.add(logInBtn);
        this.add(panel);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt){
        if (evt.getSource()==logInBtn) {
            String getterName = userTxt.getText();
            String getterPassword = passwordTxt.getText();
            
            if (!getterName.isEmpty() || !getterPassword.isEmpty()) {
                if (admin.logIn(getterName, getterPassword)) {
                    JOptionPane.showMessageDialog(null, "Se ha encontrado el usuario!");
                    PrincipalPage principalPg = new PrincipalPage(admin);
                    principalPg.setLocationRelativeTo(null);
                    principalPg.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "No encontrado, intente de nuevo.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Favor llenar todos los campos.");
            }
        }
    }
}
