package quindioexpress.pruebas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import quindioexpress.algoritmos.BusquedaBinaria;
import quindioexpress.algoritmos.DivideYVenceras;
import quindioexpress.algoritmos.Recursivos;
import quindioexpress.app.DatosIniciales;
import quindioexpress.estructuras.ListaSimplementeEnlazada;
import quindioexpress.modelo.ComparadoresPaquete;
import quindioexpress.modelo.Paquete;
import quindioexpress.servicio.CentroDistribucion;

/** Pruebas automaticas sin librerias externas. Termina con codigo 1 si alguna falla. */
public class Pruebas {

    private static int pasan;
    private static int fallan;

    public static void main(String[] args) {
        probarListaEnlazada();
        probarComparadores();
        probarAlgoritmos();
        probarCentro();
        System.out.println();
        System.out.println("Resultado: " + pasan + " pasan, " + fallan + " fallan");
        if (fallan > 0) System.exit(1);
    }

    // ---------------------------------------------------------------
    private static void probarListaEnlazada() {
        System.out.println("== Lista simplemente enlazada ==");
        ListaSimplementeEnlazada<String> l = new ListaSimplementeEnlazada<>();
        verificar("lista nueva esta vacia", l.estaVacia() && l.tamanio() == 0);
        verificar("eliminar en lista vacia retorna false", !l.eliminar("A"));
        verificar("obtener en lista vacia lanza excepcion", lanza(() -> l.obtener(0), IndexOutOfBoundsException.class));
        verificar("iterator en lista vacia no tiene siguiente", !l.iterator().hasNext());

        l.agregar("A");
        verificar("un elemento: tamanio 1 y obtener(0)=A", l.tamanio() == 1 && "A".equals(l.obtener(0)));
        verificar("un elemento: eliminar unico", l.eliminar("A") && l.estaVacia());
        l.agregar("B");
        verificar("tras vaciar, agregar funciona (cola reiniciada)", "B".equals(l.obtener(0)) && l.tamanio() == 1);
        l.eliminar("B");

        for (String s : new String[] {"A", "B", "C", "D", "E"}) l.agregar(s);
        verificar("eliminar primero (A)", l.eliminar("A") && "B".equals(l.obtener(0)) && l.tamanio() == 4);
        verificar("eliminar intermedio (C)", l.eliminar("C") && "D".equals(l.obtener(1)) && l.tamanio() == 3);
        verificar("eliminar ultimo (E)", l.eliminar("E") && "D".equals(l.obtener(l.tamanio() - 1)));
        l.agregar("F");
        verificar("agregar despues de eliminar el ultimo queda al final", "F".equals(l.obtener(2)));
        verificar("eliminar inexistente retorna false", !l.eliminar("Z") && l.tamanio() == 3);
        verificar("obtener posicion negativa lanza excepcion", lanza(() -> l.obtener(-1), IndexOutOfBoundsException.class));
        verificar("obtener posicion = tamanio lanza excepcion", lanza(() -> l.obtener(3), IndexOutOfBoundsException.class));
        verificar("eliminarEnPosicion(1) retorna D", "D".equals(l.eliminarEnPosicion(1)) && l.toString().equals("[B -> F]"));

        StringBuilder recorrido = new StringBuilder();
        for (String s : l) recorrido.append(s);
        verificar("for-each recorre en orden", recorrido.toString().equals("BF"));
        Iterator<String> it = l.iterator();
        it.next();
        it.next();
        verificar("next() sin elementos lanza NoSuchElementException", lanza(it::next, NoSuchElementException.class));

        ListaSimplementeEnlazada<Integer> enteros = new ListaSimplementeEnlazada<>();
        enteros.agregar(1);
        enteros.agregar(2);
        verificar("lista generica con Integer", enteros.obtener(1) == 2);
    }

    private static void probarComparadores() {
        System.out.println("== Comparable y Comparator ==");
        List<Paquete> ps = new ArrayList<>(List.of(
                new Paquete("PQ420", "Armenia", 1, 3, 30),
                new Paquete("PQ731", "Armenia", 1, 5, 45),
                new Paquete("PQ105", "Armenia", 1, 5, 25),
                new Paquete("PQ942", "Armenia", 1, 4, 20)));
        ps.sort(ComparadoresPaquete.DESPACHO);
        verificar("orden de despacho del enunciado", codigos(ps).equals("PQ105,PQ731,PQ942,PQ420"));
        List<Paquete> empate = new ArrayList<>(List.of(
                new Paquete("PQ842", "Armenia", 1, 5, 25),
                new Paquete("PQ105", "Armenia", 1, 5, 25)));
        empate.sort(ComparadoresPaquete.DESPACHO);
        verificar("desempate por codigo", empate.get(0).getCodigo().equals("PQ105"));
        ps.sort(null);
        verificar("orden natural por codigo", codigos(ps).equals("PQ105,PQ420,PQ731,PQ942"));
        verificar("prioridad por defecto = 5", new Paquete("X1", "Armenia", 1, 10).getPrioridad() == 5);
    }

    private static void probarAlgoritmos() {
        System.out.println("== Algoritmos ==");
        List<Paquete> ejemplo = List.of(
                new Paquete("PQ101", "Armenia", 4.5, 3, 10),
                new Paquete("PQ203", "Salento", 3.0, 3, 10),
                new Paquete("PQ415", "Armenia", 7.2, 3, 10),
                new Paquete("PQ730", "Armenia", 2.3, 3, 10));
        verificar("peso total Armenia = 14.0",
                Math.abs(Recursivos.pesoTotalPorDestino(ejemplo, "Armenia", false) - 14.0) < 1e-9);
        verificar("peso total en lista vacia = 0", Recursivos.pesoTotalPorDestino(List.of(), "Armenia", false) == 0);
        verificar("prioridad >= 4 en {5,2,4,1,5,3,4} = 4",
                Recursivos.contarPrioridadMinima(new int[] {5, 2, 4, 1, 5, 3, 4}, 4, 0) == 4);

        List<Paquete> ord = new ArrayList<>();
        for (String c : new String[] {"PQ105", "PQ214", "PQ318", "PQ421", "PQ567", "PQ731", "PQ842", "PQ942"}) {
            ord.add(new Paquete(c, "Armenia", 1, 3, 10));
        }
        verificar("binaria encuentra PQ731 en posicion 5", BusquedaBinaria.buscarPorCodigo(ord, "PQ731", false) == 5);
        verificar("binaria encuentra el primero", BusquedaBinaria.buscarPorCodigo(ord, "PQ105", false) == 0);
        verificar("binaria encuentra el ultimo", BusquedaBinaria.buscarPorCodigo(ord, "PQ942", false) == 7);
        verificar("binaria no encuentra PQ500", BusquedaBinaria.buscarPorCodigo(ord, "PQ500", false) == -1);
        verificar("binaria en lista vacia", BusquedaBinaria.buscarPorCodigo(List.of(), "PQ1", false) == -1);

        double[] pesos = {4.5, 12.0, 3.2, 18.7, 7.4, 5.1};
        List<Paquete> lp = new ArrayList<>();
        for (int i = 0; i < pesos.length; i++) lp.add(new Paquete("P" + i, "Armenia", pesos[i], 3, 10));
        verificar("divide y venceras: mayor peso 18.7", DivideYVenceras.paqueteMasPesado(lp, false).getPeso() == 18.7);
        verificar("divide y venceras con un elemento",
                DivideYVenceras.paqueteMasPesado(lp.subList(0, 1), false).getPeso() == 4.5);
    }

    private static void probarCentro() {
        System.out.println("== Centro de distribucion ==");
        CentroDistribucion c = DatosIniciales.crearCentro();
        verificar("al menos 30 paquetes", c.getRegistro().size() >= 30);
        verificar("al menos 5 destinos", c.getMunicipios().size() >= 5);
        verificar("al menos 6 repartidores", c.getRepartidores().size() >= 6);
        verificar("codigo duplicado rechazado",
                lanza(() -> { c.registrarPaquete(new Paquete("PQ731", "Armenia", 1, 3, 10)); return null; },
                        IllegalArgumentException.class));
        verificar("prioridad fuera de rango rechazada",
                lanza(() -> new Paquete("Z", "Armenia", 1, 6, 10), IllegalArgumentException.class));
        verificar("primer municipio ordenado = Armenia", c.getMunicipiosOrdenados().first().equals("Armenia"));
        verificar("primero por llegada = PQ731", c.verSiguientePorLlegada().orElseThrow().getCodigo().equals("PQ731"));
        verificar("primero prioritario = PQ127 (p5, 20 min)",
                c.verSiguientePrioritario().orElseThrow().getCodigo().equals("PQ127"));

        c.despacharPorPrioridad();          // PQ127 -> R104
        c.registrarEntrega("PQ127");
        c.atenderPorLlegada();               // PQ731 -> R101
        c.registrarEntrega("PQ731");
        c.despacharPorPrioridad();           // PQ105 (p5, 25) -> R102
        c.registrarEntrega("PQ105");
        verificar("despachado por prioridad no reaparece por llegada",
                c.verSiguientePorLlegada().orElseThrow().getCodigo().equals("PQ942"));
        StringBuilder h = new StringBuilder();
        for (Paquete p : c.getHistorial()) h.append(p.getCodigo()).append(',');
        verificar("historial conserva orden de entrega", h.toString().equals("PQ127,PQ731,PQ105,"));
        verificar("repartidor no disponible rechazado",
                lanza(() -> c.asignarPaquete("PQ077", "R107"), IllegalStateException.class));
        verificar("zona distinta rechazada",
                lanza(() -> c.asignarPaquete("PQ077", "R101"), IllegalStateException.class));
        verificar("pendientes = 28", c.cantidadPendientes() == 28 && c.getPendientes().size() == 28);
    }

    // ---------------------------------------------------------------
    private static void verificar(String nombre, boolean condicion) {
        if (condicion) {
            pasan++;
            System.out.println("  PASA  " + nombre);
        } else {
            fallan++;
            System.out.println("  FALLA " + nombre);
        }
    }

    private interface Accion { Object ejecutar(); }

    private static boolean lanza(Accion accion, Class<? extends Throwable> tipo) {
        try {
            accion.ejecutar();
            return false;
        } catch (Throwable t) {
            return tipo.isInstance(t);
        }
    }

    private static String codigos(List<Paquete> ps) {
        List<String> r = new ArrayList<>();
        ps.forEach(p -> r.add(p.getCodigo()));
        return String.join(",", r);
    }
}