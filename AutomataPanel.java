import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AutomataPanel extends JPanel {
    private AFND afnd;

    public AutomataPanel(AFND afnd) {
        this.afnd = afnd;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Limpiar el panel
        g.clearRect(0, 0, this.getWidth(), this.getHeight());

        int radius = 100; // Radio del círculo donde se distribuirán los estados
        int centerX = getWidth() / 2; // Centro del panel
        int centerY = getHeight() / 2;

        // Mapa para almacenar las posiciones de los estados
        Map<String, Point> statePositions = new HashMap<>();

        // Dibujar estados
        int i = 0;
        for (String estado : afnd.getEstados()) {
            double angle = 2 * Math.PI * i / afnd.getEstados().size();
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));

            // Si el estado es el estado inicial, cambia el color a azul
            if (estado.equals(afnd.getEstadoInicial())) {
                g.setColor(Color.BLUE);
            }

            g.drawOval(x, y, 30, 30);
            g.drawString(estado, x + 10, y + 20);

            // Si el estado es un estado final, dibuja un segundo círculo
            if (afnd.getEstadosFinales().contains(estado)) {
                g.drawOval(x - 5, y - 5, 40, 40); // Ajusta los valores según sea necesario
            }

            // Cambiar el color de vuelta a negro (o cualquier otro color que estés usando)
            g.setColor(Color.BLACK);

            // Almacenar la posición del estado como el centro del círculo
            statePositions.put(estado, new Point(x + 15, y + 15));

            i++;
        }
        // Dibujar transiciones
        Map<Pair<String, String>, Integer> transitionCounts = new HashMap<>();
        for (String estado : afnd.getEstados()) {
            for (String simbolo : afnd.getSimbolos()) {
                for (String estadoDestino : afnd.getTransiciones(estado, simbolo)) {
                    Point inicio = statePositions.get(estado);
                    Point fin = statePositions.get(estadoDestino);

                    // Calcular el punto de inicio en el borde del círculo del estado
                    double angleInicio = Math.atan2(fin.y - inicio.y, fin.x - inicio.x);
                    int startX = inicio.x + (int) (15 * Math.cos(angleInicio));
                    int startY = inicio.y + (int) (15 * Math.sin(angleInicio));

                    // Calcular el punto de fin en el borde del círculo del estado de destino
                    double angleFin = Math.atan2(inicio.y - fin.y, inicio.x - fin.x);
                    int endX = fin.x + (int) (15 * Math.cos(angleFin));
                    int endY = fin.y + (int) (15 * Math.sin(angleFin));

                    if (estado.equals(estadoDestino)) {
                        // Dibujar un bucle para una transición hacia el mismo estado
                        g.drawOval(startX, startY - 10, 20, 20);
                    } else {
                        // Dibujar una línea para una transición hacia un estado diferente
                        g.drawLine(startX, startY, endX, endY);

                        // Dibujar la flecha al final de la línea
                        int arrowLength = 10; // puedes ajustar este valor
                        double angle1 = angleFin + Math.PI / 6; // puedes ajustar este valor
                        double angle2 = angleFin - Math.PI / 6; // puedes ajustar este valor
                        int arrowX1 = endX + (int) (arrowLength * Math.cos(angle1));
                        int arrowY1 = endY + (int) (arrowLength * Math.sin(angle1));
                        int arrowX2 = endX + (int) (arrowLength * Math.cos(angle2));
                        int arrowY2 = endY + (int) (arrowLength * Math.sin(angle2));
                        g.drawLine(endX, endY, arrowX1, arrowY1);
                        g.drawLine(endX, endY, arrowX2, arrowY2);
                    }

                    // Dibujar el valor de la transición
                    g.setColor(Color.RED);
                    int x = startX + (endX - startX) / 4;
                    int y = startY + (endY - startY) / 4;

                    // Añadir desplazamiento para transiciones múltiples
                    Pair<String, String> transition = new Pair<>(estado, estadoDestino);
                    int offset = transitionCounts.getOrDefault(transition, 0) * 10;
                    g.drawString(simbolo, x + offset, y);

                    // Actualizar el conteo de transiciones
                    transitionCounts.put(transition, offset + 1);

                    g.setColor(Color.BLACK);
                }
            }
        }

    }
}