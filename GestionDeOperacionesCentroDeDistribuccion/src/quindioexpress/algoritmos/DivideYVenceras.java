package quindioexpress.algoritmos;

import quindioexpress.modelo.Paquete;

import java.util.List;

public final class DivideYVenceras {

    private DivideYVenceras() {}

    public static Paquete paqueteMasPesado(List<Paquete> paquetes, boolean traza) {
        if (paquetes == null || paquetes.isEmpty()) {
            throw new IllegalArgumentException("La  lista de paquetes no puede estar vacia");
        }
        Paquete resultadoFinal = masPesado(paquetes, 0, paquetes.size() -1, 0, traza);
        if (traza) System.out.printf("Resultado final: %s con %.1f kg%n",
                resultadoFinal.getCodigo(), resultadoFinal.getPeso());
        return resultadoFinal;
    }
    private static Paquete masPesado(List<Paquete> paquete, int inicio, int fin, int nivel, boolean traza) {
        String sangria = " ".repeat(nivel);

        if (inicio == fin) {
            if (traza) System.out.printf("%sbase [%d] -> %s (%.1f)%n", sangria, inicio,
                    paquete.get(inicio).getCodigo(), paquete.get(inicio).getPeso());
            return paquete.get(inicio);
        }

        int medio = inicio + (fin - inicio) / 2;
        if (traza) System.out.printf("%sdividir [%d..%d] en [%d..%d] y [%d..%d]%n",
                sangria, inicio, fin, inicio, medio, medio + 1, fin);

        Paquete izquierdo = masPesado(paquete, inicio, medio, nivel + 1, traza);
        Paquete derecho = masPesado(paquete, medio + 1, fin, nivel + 1, traza);
        Paquete ganador = izquierdo.getPeso() >= derecho.getPeso() ? izquierdo : derecho;
        if (traza) System.out.printf("%scombinar [%d..%d]: max(%.1f, %.1f) = %.1f (%s)%n", sangria, inicio, fin,
                izquierdo.getPeso(), derecho.getPeso(), ganador.getPeso(), ganador.getCodigo());
        return ganador;
    }
}
