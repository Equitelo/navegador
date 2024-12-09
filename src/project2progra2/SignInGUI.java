package project2progra2;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class SignInGUI extends JFrame implements ActionListener{
    private JPanel panel;
    private JLabel titleLbl;
    private JLabel userLbl;
    private JLabel passwordLbl;
    private JTextField userTxt;
    private JPasswordField passwordTxt;
    private JButton signInBtn;
    
    Logica admin;
    
    public SignInGUI(Logica admin){
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
        
        titleLbl = new JLabel("Sign In");
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
        
        signInBtn = new JButton("Sign In");
        signInBtn.setFont(new Font("Algerian", Font.PLAIN, 25));
        signInBtn.setBounds(300, 600, 150, 40);
        signInBtn.setFocusable(false);
        signInBtn.addActionListener(this);
        
        panel.add(titleLbl);
        panel.add(userLbl);
        panel.add(passwordLbl);
        panel.add(userTxt);
        panel.add(passwordTxt);
        panel.add(signInBtn);
        this.add(panel);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == signInBtn) {
            String getterName = userTxt.getText();
            String getterPassword = passwordTxt.getText();
            
            if (!getterName.isEmpty() || !getterPassword.isEmpty()) {
                
                try{
                    Rol rol = admin.existeAdmin() ? Rol.USUARIO : Rol.ADMINISTRADOR;
                    admin.createAccount(getterName, getterPassword);
                    JOptionPane.showMessageDialog(null, "Cuenta creada existosamente como "+(rol == Rol.ADMINISTRADOR ? "Administrador":"Usuario")+"!");
                    if (rol == Rol.ADMINISTRADOR) {
                        PrincipalPage pPage = new PrincipalPage(admin);
                        pPage.setLocationRelativeTo(null);
                        pPage.setVisible(true);
                        dispose();
                    } else {
                       PrincipalPage pPage = new PrincipalPage(admin);
                        pPage.setLocationRelativeTo(null);
                        pPage.setVisible(true);
                        dispose();
                    }
                } catch (IOException e){
                    JOptionPane.showMessageDialog(null, "Error al registrar el usuario: "+e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Favor llenar todos los campos.");
            }
        }
    }

}
