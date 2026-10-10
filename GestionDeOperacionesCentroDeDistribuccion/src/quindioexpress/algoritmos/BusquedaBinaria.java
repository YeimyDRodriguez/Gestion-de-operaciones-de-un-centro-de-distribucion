package quindioexpress.algoritmos;

import quindioexpress.modelo.Paquete;

import java.util.List;

public class BusquedaBinaria {

    private BusquedaBinaria() {}

    public static int buscarPorCodigo(List<Paquete> ordenados, String codigo, boolean traza) {
        return buscar(ordenados, codigo, 0, ordenados.size() -1, traza);
    }

    private static int buscar(List<Paquete> ordenados, String codigo, int bajo, int alto, boolean traza) {
        if (bajo > alto) {
            if (traza) System.out.println(" Bajo= " + bajo + " > Alto= " + alto + " -> No encontrado.");
            return -1;
        }
        int medio = bajo + (alto - bajo) / 2;
        String codigoMedio = ordenados.get(medio).getCodigo();
        int cmp = codigo.compareTo(codigoMedio);
        if (traza) System.out.printf("  bajo=%d alto=%d medio=%d (%s) -> %s%n", bajo, alto, medio, codigoMedio,
                cmp == 0 ? "encontrado" : cmp < 0 ? "buscar a la izquierda" : "buscar a la derecha");
        if (cmp == 0) {
            return medio;
        }
        if (cmp < 0) {
            return buscar(ordenados, codigo, bajo, medio -1, traza);
        }
        return buscar(ordenados, codigo, medio + 1, alto, traza);
    }

    public static boolean estaOrdenadaPorCodigo(List<Paquete> lista) {
        return verificarOrden(lista, 1);
    }

    private static boolean verificarOrden(List<Paquete> lista, int ordenacion) {
        if (ordenacion >= lista.size()) {
            return true;
        }
        return lista.get(ordenacion - 1).compareTo(lista.get(ordenacion)) <= 0
                && verificarOrden(lista, ordenacion + 1);
    }
    
}
