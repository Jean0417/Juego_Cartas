import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Carta {
    private int indice; 
    private boolean esNoSumable = false; // para marcar las cartas no sumables

    public boolean esNoSumable() {
        return esNoSumable;
    }

    public void marcarComoNoSumable() {
        this.esNoSumable = true;
    }

    public void desmarcarComoNoSumable() {
        this.esNoSumable = false;
    }


    public Carta(Random r) {
        indice = r.nextInt(52) + 1; // seleccionamos una carta aleatoria entre 1 y 52
    }

    public Pinta getPinta() {
        // Agrupamos las cartas por su pinta
        if (indice <= 13) {
            return Pinta.TREBOL;
        } else if (indice <= 26) {
            return Pinta.PICA;
        } else if (indice <= 39) {
            return Pinta.CORAZON;
        } else {
            return Pinta.DIAMANTE;
        }
    }

    public NombreCarta getNombre() {
        int posicion = indice % 13;  
        if (posicion == 0) {
            posicion = 13; 
        }
        return NombreCarta.values()[posicion - 1];
    }

    public void mostrar(JPanel pnl, int x, int y) {
        String nombreArchivo = "/imagenes/CARTA" + String.valueOf(indice) + ".jpg"; // Ruta de la imagen
        ImageIcon imagen = new ImageIcon(getClass().getResource(nombreArchivo)); // Cargar la imagen

        JLabel lbl = new JLabel(imagen);
        lbl.setBounds(x, y, imagen.getIconWidth(), imagen.getIconHeight());
        pnl.add(lbl); // Añadir la carta al panel
    }

    public int getValorEscalera() {
        // Ajustamos el valor de las cartas especiales para las secuencias
        switch (getNombre()) {
            case AS:
                return 14;  
            case JACK:
                return 11;  
            case QUEEN:
                return 12;  
            case KING:
                return 13;  
            default:
                return getValorNumerico(); 
        }
    }

    public int getValorNumerico() {
        switch (getNombre()) {
            case AS:
            case JACK:
            case QUEEN:
            case KING:
                return 10; 
            default:
                return getNombre().ordinal() + 1; 
        }
    }
}


