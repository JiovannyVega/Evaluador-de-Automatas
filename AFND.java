import java.util.*;

public class AFND {
    private Map<String, Map<String, Set<String>>> transiciones = new HashMap<>();
    private String estadoInicial;
    private Set<String> estadosFinales = new HashSet<>();

    public void agregarTransicion(String estado, String simbolo, String nuevoEstado) {
        transiciones.putIfAbsent(estado, new HashMap<>());
        transiciones.get(estado).putIfAbsent(simbolo, new HashSet<>());
        transiciones.get(estado).get(simbolo).add(nuevoEstado);
    }

    public void setEstadoInicial(String estado) {
        estadoInicial = estado;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void agregarEstadoFinal(String estado) {
        estadosFinales.add(estado);
    }

    public Set<String> getEstadosFinales() {
        return estadosFinales;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Set<String>>> estado : transiciones.entrySet()) {
            for (Map.Entry<String, Set<String>> transicion : estado.getValue().entrySet()) {
                for (String nuevoEstado : transicion.getValue()) {
                    sb.append(estado.getKey()).append(" -- ").append(transicion.getKey()).append(" --> ")
                            .append(nuevoEstado).append("\n");
                }
            }
        }
        return sb.toString();
    }

    public boolean acepta(String entrada) {
        Set<String> estadosActuales = new HashSet<>();
        estadosActuales.add(estadoInicial);

        for (char simbolo : entrada.toCharArray()) {
            Set<String> nuevosEstados = new HashSet<>();
            for (String estado : estadosActuales) {
                Map<String, Set<String>> transicionesDesdeEstado = transiciones.get(estado);
                if (transicionesDesdeEstado != null) {
                    Set<String> transicionesConSimbolo = transicionesDesdeEstado.get(String.valueOf(simbolo));
                    if (transicionesConSimbolo != null) {
                        nuevosEstados.addAll(transicionesConSimbolo);
                    }
                }
            }
            estadosActuales = nuevosEstados;
        }

        // Comprueba si alguno de los estados actuales es un estado final
        estadosActuales.retainAll(estadosFinales);
        return !estadosActuales.isEmpty();
    }
}