package project2progra2;

import java.io.*;

public class ChatClient {
    public String usuarioActual;
    public String destinatario;
    private File archivoChatActual; 
    private File archivoChatDestinatario; 

    public ChatClient(String usuarioActual, String destinatario) {
        this.usuarioActual = usuarioActual;
        this.destinatario = destinatario;

        File carpetaChatsActual = new File("usuarios/" + usuarioActual + "/chats");
        if (!carpetaChatsActual.exists()) {
            carpetaChatsActual.mkdirs();
        }
        this.archivoChatActual = new File(carpetaChatsActual, "chatcon" + destinatario + ".bin");

        File carpetaChatsDestinatario = new File("Users/" + destinatario + "/chats");
        if (!carpetaChatsDestinatario.exists()) {
            carpetaChatsDestinatario.mkdirs();
        }
        this.archivoChatDestinatario = new File(carpetaChatsDestinatario, "chatcon" + usuarioActual + ".bin");
    }

    public void enviarMensaje(String mensaje) {
        try {
            
            guardarMensaje(archivoChatActual, usuarioActual, mensaje);

            guardarMensaje(archivoChatDestinatario, usuarioActual, mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarMensaje(File archivo, String remitente, String mensaje) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(raf.length());
            raf.writeUTF(remitente);
            raf.writeUTF(mensaje); 
            raf.writeUTF(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())); // Timestamp
        }
    }

    public void cargarHistorial(chatGUI gui, boolean isActual) {
        File archivoChat = isActual ? archivoChatActual : archivoChatDestinatario;

        if (!archivoChat.exists()) {
            gui.actualizarMensajes("No hay mensajes previos.\n");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivoChat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                String remitente = raf.readUTF();
                String mensaje = raf.readUTF();
                String timestamp = raf.readUTF();
                gui.actualizarMensajes(String.format("[%s] %s: %s\n", timestamp, remitente, mensaje));
            }
        } catch (IOException e) {
            gui.actualizarMensajes("Error al cargar historial de chat.\n");
        }
    }
}