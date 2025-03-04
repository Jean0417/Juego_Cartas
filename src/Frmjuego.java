import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

    

public class Frmjuego extends JFrame {

    JPanel pnlJugador1; 
    JPanel pnlJugador2;
    JTabbedPane tpJugadores;
    public Frmjuego(){
        setSize(700,250); 
        setTitle("Juguemos al apuntado");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnRepartir = new JButton("Repartir");
        btnRepartir.setBounds(10, 10, 100, 25);
        getContentPane().add(btnRepartir);

        JButton btnVerificar = new JButton("Verificar");
        btnVerificar.setBounds(120, 10, 100, 25);
        getContentPane().add(btnVerificar);

        tpJugadores = new JTabbedPane(); 
        tpJugadores.setBounds(10, 40, 650, 150);
        getContentPane().add(tpJugadores);

        pnlJugador1 = new JPanel();
        pnlJugador1.setBackground(new Color(16,139,37));
        pnlJugador1.setLayout(null);

        pnlJugador2 = new JPanel(); 
        pnlJugador2.setBackground(new Color(0,255,255));
        pnlJugador2.setLayout(null);

        tpJugadores.addTab("Leonel Messi", pnlJugador1); 
        tpJugadores.addTab("Cristiano Ronaldo", pnlJugador2);

        btnRepartir.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                repartirCartas();
            }
            
        });

        btnVerificar.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
                verificarJugador();

            }
                
        });
    }

    Jugador jugador1 = new Jugador();
    Jugador jugador2 = new Jugador();
    private void repartirCartas(){  

        jugador1.Repartir();
        jugador1.Mostrar(pnlJugador1);
        jugador2.Repartir();
        jugador2.Mostrar(pnlJugador2);

    }

    private void verificarJugador(){
        int pestaña_Seleccionadas = tpJugadores.getSelectedIndex();
        String mensajeFinal = ""; 
    
        switch (pestaña_Seleccionadas) {
            case 0:
                
                mensajeFinal += jugador1.getGrupos();
                mensajeFinal += "\n\n"; 
                mensajeFinal += jugador1.obtenerEscaleras();
                mensajeFinal += "\n\n";
                mensajeFinal += "Puntaje de Leonel Messi: " + jugador1.obtenerPuntaje();
                break;
        
            case 1:

                mensajeFinal += jugador2.getGrupos();
                mensajeFinal += "\n\n"; 
                mensajeFinal += jugador2.obtenerEscaleras();
                mensajeFinal += "\n\n";
                mensajeFinal += "Puntaje de Cristiano Ronaldo: " + jugador2.obtenerPuntaje();
                break;
        }
    
        JOptionPane.showMessageDialog(null, mensajeFinal);
    }
    

}
