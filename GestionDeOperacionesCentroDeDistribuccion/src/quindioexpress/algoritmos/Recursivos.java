package quindioexpress.algoritmos;

import quindioexpress.modelo.Paquete;

import java.util.List;

public final class Recursivos {

    private Recursivos() {}

    public static double pesoTotalPorDestino(List<Paquete> paquetes, String destino, boolean traza) {
        return pesoTotal(paquetes, destino, 0, traza);
    }

    private static double pesoTotal(List<Paquete> paquetes, String destino, int i, boolean traza) {
        String sangria = " ".repeat(i);
        if (traza) System.out.println(sangria + "-> Peso Total(i = " + i + ")");
        if (i == paquetes.size()) {
            if (traza) System.out.println(sangria + "<- caso base, retorna 0.0");
        }
        Paquete paquete = paquetes.get(i);
        double aporte = paquete.getDestino().equalsIgnoreCase(destino) ? paquete.getPeso() : 0.0;
        double resultado = aporte + pesoTotal(paquetes, destino, i + 1, traza);
        if (traza) System.out.printf("%s<- pesoTotal(i=%d) [%s %s] = %.1f + ... = %.1f%n",
                sangria, i, paquete.getCodigo(), paquete.getDestino(), aporte, resultado);
        return resultado;
    }
    public static int contarPrioridadMinima(List<Paquete> paquetes, int prioridadMinima, boolean traza) {
        return contar(paquetes, prioridadMinima, 0, traza);
    }
    private static int contar(List<Paquete> paquetes, int minima, int i, boolean traza) {
        String sangria = " ".repeat(i);
        if (traza) System.out.println(sangria + "-> contar(i=" + i + ")");
        if (i == paquetes.size()) {                                   // caso base
            if (traza) System.out.println(sangria + "<- caso base, retorna 0");
            return 0;
        }
        int prioridad = paquetes.get(i).getPrioridad();
        int cuenta = prioridad >= minima ? 1 : 0;
        int resultado = cuenta + contar(paquetes, minima, i + 1, traza); // caso recursivo
        if (traza) System.out.printf("%s<- contar(i=%d) [prioridad %d] = %d + ... = %d%n",
                sangria, i, prioridad, cuenta, resultado);
        return resultado;
    }

    public static int contarPrioridadMinima(int[] prioridades, int minima, int i) {
        if (i == prioridades.length) {
            return 0;
        }
        return (prioridades[i] >= minima ? 1 : 0) + contarPrioridadMinima(prioridades, minima, i + 1);
    }
}
