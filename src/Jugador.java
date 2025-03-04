import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;

public class Jugador {
    private int DISTANCIA = 40;
    private int MARGEN = 10;
    private int TOTAL_CARTAS = 10;
    private String MENSAJE_PREDERTEMINADO = "No se encontraron grupos";
    private String ENCABEZADO_MENSAJE = "Se encontraron los grupos: ";
    private int MINIMA_CANTIDAD_GRUPO = 2;
    private Carta[] cartas = new Carta[TOTAL_CARTAS]; // creamos las cartas que necesitamos
    private Random r = new Random(); // lo hacemos random para que no sepamos cual nos aparezca

    public void Repartir(){
        for(int i = 0; i < TOTAL_CARTAS; i++ ){ //Creamos el ciclo para ir repartiendo las cartas
            cartas[i] = new Carta(r);
        }
    }

    public void Mostrar(JPanel pnl){
        pnl.removeAll(); // para que una vez terminado el juego, vuelva y empiece
        int x = MARGEN + (TOTAL_CARTAS -1) * DISTANCIA;
        for(Carta carta: cartas){
            carta.mostrar(pnl, x, MARGEN); 
            x -= DISTANCIA; 
        }
        pnl.repaint();
    }

    public String getGrupos(){
        String mensaje = MENSAJE_PREDERTEMINADO;
        int [] contadores = new int[NombreCarta.values().length];
        for(Carta carta: cartas){
            contadores[carta.getNombre().ordinal()]++; //buscamos la posicion de la carta y lo incrementamos en 1, ordinal nos permite hayar la posicion de las cartas en nuestro enum
        }

        //Verificar si hay grupos de cartas iguales
        boolean hayGrupos = false;
        for ( int contador: contadores){
            if(contador >= MINIMA_CANTIDAD_GRUPO){
                hayGrupos = true;
                break;
            }
        }
        if(hayGrupos == true){
            mensaje = ENCABEZADO_MENSAJE;
            int posicion = 0;
            for( int contador : contadores){
                if (contador >=  MINIMA_CANTIDAD_GRUPO) {
                    mensaje += Grupo.values()[contador] + " de " + NombreCarta.values()[posicion] + "\n";
                }
                posicion ++;
            }
        }
        return mensaje;
    }

    // Obtener las escaleras
    public String obtenerEscaleras() {
        Map<Pinta, List<Carta>> cartasPorPinta = new HashMap<>();
    
        // Agrupar cartas por su pinta
        for (Carta carta : cartas) {
            cartasPorPinta.computeIfAbsent(carta.getPinta(), k -> new ArrayList<>()).add(carta);
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Escaleras encontradas: \n");

        
        for (Map.Entry<Pinta, List<Carta>> entry : cartasPorPinta.entrySet()) {
            List<Carta> cartasDePinta = entry.getValue();
            
            Collections.sort(cartasDePinta, (c1, c2) -> Integer.compare(c1.getValorEscalera(), c2.getValorEscalera()));

            List<Carta> escaleraActual = new ArrayList<>();
            int valorAnterior = -1;
            
            for (Carta carta : cartasDePinta) {
                int valorActual = carta.getValorEscalera();  

                if (escaleraActual.isEmpty() || valorActual == valorAnterior + 1) {
                    escaleraActual.add(carta);
                } else {
                    
                    if (escaleraActual.size() >= 2) { 
                        mensaje.append("Escalera de ").append(entry.getKey()).append(": ");
                        for (Carta c : escaleraActual) {
                            mensaje.append(c.getNombre()).append(" ");
                        }
                        mensaje.append("\n");
                    }

                    escaleraActual.clear();
                    escaleraActual.add(carta);
                }
                valorAnterior = valorActual;
            }

           
            if (escaleraActual.size() >= 2) {
                mensaje.append("Escalera de ").append(entry.getKey()).append(": ");
                for (Carta c : escaleraActual) {
                    mensaje.append(c.getNombre()).append(" ");
                }
                mensaje.append("\n");
            }
        }

        if (mensaje.length() == 0) {
            mensaje.append("No se encontraron escaleras.");
        }

        return mensaje.toString();
    }

    // Método para obtener el puntaje
public int obtenerPuntaje() {
    int puntaje = 0;

    // Obtener las cartas que forman parte de escaleras (incluyendo las cartas especiales)
    Set<Carta> cartasEnEscalera = obtenerCartasEnEscalera();

    // Recorremos las cartas para calcular el puntaje
    for (Carta carta : cartas) {
        // Si la carta está en una escalera o en un grupo, no la contamos
        if (cartasEnEscalera.contains(carta) || estaEnGrupo(carta)) {
            continue; // No sumamos nada y pasamos a la siguiente carta
        }

        // Si es una carta especial, sumamos 10 puntos
        if (esCartaEspecial(carta)) {
            puntaje += 10;
        } else {
            // Si no es carta especial, sumamos el valor numérico
            puntaje += carta.getValorNumerico();
        }
    }

    return puntaje;
}


private Set<Carta> obtenerCartasEnEscalera() {
    Set<Carta> cartasEnEscalera = new HashSet<>();
    Map<Pinta, List<Carta>> cartasPorPinta = new HashMap<>();
    
    for (Carta carta : cartas) {
        cartasPorPinta.computeIfAbsent(carta.getPinta(), k -> new ArrayList<>()).add(carta);
    }

    for (Map.Entry<Pinta, List<Carta>> entry : cartasPorPinta.entrySet()) {
        List<Carta> cartasDePinta = entry.getValue();
        Collections.sort(cartasDePinta, (c1, c2) -> Integer.compare(c1.getValorEscalera(), c2.getValorEscalera()));  // Ordenar por valor de escalera

        List<Carta> escaleraActual = new ArrayList<>();
        int valorAnterior = -1;

        for (Carta carta : cartasDePinta) {
            int valorActual = carta.getValorEscalera();  

            if (escaleraActual.isEmpty() || valorActual == valorAnterior + 1) {
                escaleraActual.add(carta);
            } else {
                
                if (escaleraActual.size() >= 2) {
                    cartasEnEscalera.addAll(escaleraActual);
                }
                escaleraActual.clear();
                escaleraActual.add(carta);
            }

            valorAnterior = valorActual;
        }

        if (escaleraActual.size() >= 2) {
            cartasEnEscalera.addAll(escaleraActual);
        }
    }

    return cartasEnEscalera;
}

// Método para verificar si una carta es especial (JACK, QUEEN, KING, AS)
private boolean esCartaEspecial(Carta carta) {
    NombreCarta nombre = carta.getNombre();
    return nombre == NombreCarta.JACK || nombre == NombreCarta.QUEEN || nombre == NombreCarta.KING || nombre == NombreCarta.AS;
}

    // Método para saber si la carta forma parte de un grupo
    private boolean estaEnGrupo(Carta carta) {
        // Verificamos si el valor de la carta aparece más de una vez en el conjunto
        Map<NombreCarta, Integer> conteo = new HashMap<>();
        for (Carta c : cartas) {
            conteo.put(c.getNombre(), conteo.getOrDefault(c.getNombre(), 0) + 1);
        }
        
        // Si el conteo es mayor que 1, la carta está en un grupo
        return conteo.getOrDefault(carta.getNombre(), 0) > 1;
    }

    private void marcarCartasEnEscalera() {
        Map<Pinta, List<Carta>> cartasPorPinta = new HashMap<>();
        
        // Agrupar cartas por su pinta
        for (Carta carta : cartas) {
            if (!esCartaEspecial(carta)) { // No incluir cartas especiales
                cartasPorPinta.computeIfAbsent(carta.getPinta(), k -> new ArrayList<>()).add(carta);
            }
        }
        
        // Comprobar secuencias dentro de cada grupo de cartas por pinta
        for (Map.Entry<Pinta, List<Carta>> entry : cartasPorPinta.entrySet()) {
            List<Carta> cartasDePinta = entry.getValue();
            Collections.sort(cartasDePinta, Comparator.comparingInt(Carta::getValorNumerico));
        
            List<Carta> escaleraActual = new ArrayList<>();
            int valorAnterior = -1;
            
            for (Carta carta : cartasDePinta) {
                int valorActual = carta.getValorNumerico();
                // Añadir a la escalera si las cartas son consecutivas
                if (escaleraActual.isEmpty() || valorActual == valorAnterior + 1) {
                    escaleraActual.add(carta);
                } else {
                    // Si encontramos una secuencia válida, marcar las cartas en escalera
                    if (escaleraActual.size() >= 2) {
                        for (Carta c : escaleraActual) {
                            c.marcarComoNoSumable(); // Marcamos como no sumable
                        }
                    }
                    // Reiniciar la escalera
                    escaleraActual.clear();
                    escaleraActual.add(carta);
                }
                valorAnterior = valorActual;
            }
            
            // Verificar la última escalera encontrada
            if (escaleraActual.size() >= 2) {
                for (Carta c : escaleraActual) {
                    c.marcarComoNoSumable(); 
                }
            }
        }
    }
    
    
}
