package project2progra2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class chatGUI extends JFrame implements ActionListener {
    private JPanel panelChat, panelMessage;
    private JTextArea chatArea;
    private JTextField messageTxt;
    private JButton enviarBtn;
    private ChatClient client;
    private chatGUI ventanaSincronizada; // Referencia a la otra ventana

    public chatGUI(String usuarioActual, String destinatario) {
        this.client = new ChatClient(usuarioActual, destinatario);
        initComponents();
        client.cargarHistorial(this, true); // Cargar historial del usuario actual
    }

    private void initComponents() {
        setTitle("Chat con " + client.destinatario);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Panel para enviar mensajes
        panelMessage = new JPanel();
        panelMessage.setLayout(new BorderLayout());
        messageTxt = new JTextField();
        enviarBtn = new JButton("Enviar");
        enviarBtn.addActionListener(this);

        panelMessage.add(messageTxt, BorderLayout.CENTER);
        panelMessage.add(enviarBtn, BorderLayout.EAST);
        add(panelMessage, BorderLayout.SOUTH);
    }

    public void actualizarMensajes(String mensaje) {
        chatArea.append(mensaje);
    }

    public void sincronizarVentana(chatGUI otraVentana) {
        this.ventanaSincronizada = otraVentana;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String mensaje = messageTxt.getText().trim();
        if (!mensaje.isEmpty()) {
            client.enviarMensaje(mensaje); // Guardar mensaje en archivos locales
            actualizarMensajes("Tú: " + mensaje + "\n");

            // Actualizar la ventana sincronizada
            if (ventanaSincronizada != null) {
                ventanaSincronizada.actualizarMensajes(client.usuarioActual + ": " + mensaje + "\n");
            }

            messageTxt.setText("");
        }
    }
}
