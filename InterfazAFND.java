import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazAFND extends JFrame {
    private JTable tablaTransiciones;
    private JButton crearAutomataButton;

    public InterfazAFND() {
        setTitle("Interfaz AFND");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String numEstadosStr = JOptionPane.showInputDialog("¿Cuántos estados hay en total?");
        int numEstados = Integer.parseInt(numEstadosStr);
        String valoresStr = JOptionPane.showInputDialog("¿Cuáles son los valores posibles?");
        String[] valores = valoresStr.split(", ");

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Estado");
        for (String valor : valores) {
            model.addColumn(valor);
        }

        for (int i = 0; i < numEstados; i++) {
            model.addRow(new Object[numEstados + 1]);
            model.setValueAt("q" + i, i, 0);
        }

        tablaTransiciones = new JTable(model);

        AFND afnd = new AFND();

        crearAutomataButton = new JButton("Crear Autómata");
        crearAutomataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpiar el mapa de transiciones y el conjunto de estados finales
                afnd.transiciones.clear();
                afnd.estadosFinales.clear();

                for (int i = 0; i < tablaTransiciones.getRowCount(); i++) {
                    String estado = (String) tablaTransiciones.getValueAt(i, 0);
                    String estadoSinMarca = estado;
                    if (estado.endsWith("I")) {
                        estadoSinMarca = estado.substring(0, estado.length() - 1);
                        afnd.setEstadoInicial(estadoSinMarca);
                    }
                    if (estado.endsWith("F")) {
                        estadoSinMarca = estado.substring(0, estado.length() - 1);
                        afnd.agregarEstadoFinal(estadoSinMarca);
                    }
                    for (int j = 1; j < tablaTransiciones.getColumnCount(); j++) {
                        String simbolo = tablaTransiciones.getColumnName(j);
                        String nuevosEstados = (String) tablaTransiciones.getValueAt(i, j);
                        if (nuevosEstados != null && !nuevosEstados.isEmpty()) {
                            for (String nuevoEstado : nuevosEstados.split(",")) {
                                afnd.agregarTransicion(estadoSinMarca, simbolo, nuevoEstado.trim());
                            }
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Autómata creado:\n" + afnd.toString() +
                        "\nEstado inicial: " + afnd.getEstadoInicial() +
                        "\nEstados finales: " + afnd.getEstadosFinales());
                // Crear y mostrar el panel del autómata
                AutomataPanel automataPanel = new AutomataPanel(afnd);
                JFrame frame = new JFrame();
                frame.add(automataPanel);
                frame.setSize(300, 300);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);

                // Forzar un nuevo dibujo del autómata
                automataPanel.repaint();
            }
        });

        JTextField entradaField = new JTextField(20);
        JButton evaluarButton = new JButton("Evaluar Entrada");
        evaluarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String entrada = entradaField.getText();
                String aceptada = afnd.acepta(entrada);
                JOptionPane.showMessageDialog(null, aceptada);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(tablaTransiciones));
        panel.add(crearAutomataButton);
        panel.add(entradaField);
        panel.add(evaluarButton);

        this.add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InterfazAFND().setVisible(true);
            }
        });
    }
}