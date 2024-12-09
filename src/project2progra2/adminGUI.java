package project2progra2;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class adminGUI extends JFrame {

    private Logica admin;
    private JPanel usersPanel;
    private JScrollPane usersScrollPane;
    private JTree usersTree;
    private DefaultTreeModel treeModel;

    public adminGUI(Logica admin) {
        this.admin = admin;
        initComponents();
        cargarUsuarios();
    }

    private void initComponents() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("<- Regresar al Menú Principal");
        backButton.addActionListener(e -> regresarAlMenuPrincipal());
        topPanel.add(backButton);
        this.add(topPanel, BorderLayout.NORTH);

        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        usersScrollPane = new JScrollPane(usersPanel);
        usersScrollPane.setPreferredSize(new Dimension(200, 600));
        this.add(usersScrollPane, BorderLayout.WEST);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Usuarios");
        treeModel = new DefaultTreeModel(root);
        usersTree = new JTree(treeModel);
        JScrollPane treeScrollPane = new JScrollPane(usersTree);
        this.add(treeScrollPane, BorderLayout.CENTER);

        this.setTitle("Admin GUI");
        this.setVisible(true);
    }

    private void cargarUsuarios() {
        usersPanel.removeAll();

        for (Object obj : admin.getUsers()) {
            if (obj instanceof Usuario) {
                Usuario usuario = (Usuario) obj;
                JButton userButton = new JButton(usuario.getUser());
                userButton.addActionListener(e -> cargarArbolUsuario(usuario));
                usersPanel.add(userButton);
            }
        }

        usersPanel.revalidate();
        usersPanel.repaint();
    }

    private void cargarArbolUsuario(Usuario usuario) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Usuario: " + usuario.getUser());
        treeModel.setRoot(root);

        File userFolder = new File("Users/" + usuario.getUser());
        if (userFolder.exists()) {
            agregarContenidoAlArbol(userFolder, root);
        }

        expandirArbolCompleto();
    }

    private void agregarContenidoAlArbol(File folder, DefaultMutableTreeNode parentNode) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getName());
                parentNode.add(childNode);
                if (file.isDirectory()) {
                    agregarContenidoAlArbol(file, childNode);
                }
            }
        }
    }

    private void expandirArbolCompleto() {
        for (int i = 0; i < usersTree.getRowCount(); i++) {
            usersTree.expandRow(i);
        }
    }

    private void regresarAlMenuPrincipal() {
        
        PrincipalPage pPage = new PrincipalPage(admin); 
        pPage.setVisible(true);
        pPage.setLocationRelativeTo(null);
        this.dispose();
    }
}