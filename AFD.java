public class AFD {
    private String estado;

    public AFD() {
        this.estado = "q0";
    }

    public void transicion(char caracter) {
        switch (this.estado) {
            case "q0":
                if (caracter == 'a') {
                    this.estado = "q1";
                } else {
                    this.estado = "q0";
                }
                break;
            case "q1":
                if (caracter == 'b') {
                    this.estado = "q2";
                } else {
                    this.estado = "q1";
                }
                break;
            case "q2":
                if (caracter == 'a') {
                    this.estado = "q1";
                } else {
                    this.estado = "q0";
                }
                break;
        }
    }

    public boolean evaluarCadena(String cadena) {
        for (char caracter : cadena.toCharArray()) {
            this.transicion(caracter);
        }
        return this.estado.equals("q2");
    }

    public static void main(String[] args) {
        AFD afd = new AFD();
        String cadena = "aabab";
        System.out.println(afd.evaluarCadena(cadena)); // Devuelve true si la cadena termina en 'ab'
    }
}