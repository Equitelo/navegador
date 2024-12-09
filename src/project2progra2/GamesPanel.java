/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project2progra2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamesPanel extends JPanel {

    private Logica logic;

    public GamesPanel(Logica logic) {
        this.logic = logic;
        setPreferredSize(new Dimension(800, 600)); // Tamaño preferido
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        NodoJuego nodoActual = logic.getListaJuegos().getInicio();
        int x = 10;  // Coordenada inicial x
        int y = 10;  // Coordenada inicial y
        int imageWidth = 100;
        int imageHeight = 100;

        while (nodoActual != null) {
            // Dibujar rectángulo alrededor del juego
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(x, y, 300, 120, 20, 20);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, 300, 120, 20, 20);

            // Dibujar información del juego
            g.drawString("Nombre: " + nodoActual.getNombre(), x + 110, y + 20);
            g.drawString("Género: " + nodoActual.getGenero(), x + 110, y + 40);
            g.drawString("Creador: " + nodoActual.getCreador(), x + 110, y + 60);

            // Dibujar imagen
            try {
                File imgFile = new File(nodoActual.getRutaImagen()); // Ruta de la imagen
                if (imgFile.exists()) {
                    BufferedImage img = ImageIO.read(imgFile);
                    g.drawImage(img, x + 5, y + 5, imageWidth, imageHeight, null);
                } else {
                    g.drawString("Imagen no disponible", x + 10, y + 50);
                }
            } catch (IOException e) {
                g.drawString("Error al cargar imagen", x + 10, y + 50);
            }

            // Incrementar y para el siguiente juego
            y += 140;
            nodoActual = nodoActual.siguiente;
        }
    }
}