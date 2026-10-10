package quindioexpress.app;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import quindioexpress.algoritmos.BusquedaBinaria;
import quindioexpress.algoritmos.DivideYVenceras;
import quindioexpress.algoritmos.Recursivos;
import quindioexpress.estructuras.ListaSimplementeEnlazada;
import quindioexpress.modelo.ComparadoresPaquete;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;
import quindioexpress.servicio.Asignacion;
import quindioexpress.servicio.CentroDistribucion;

public class Main {
    public static void main (String[] args) {
    	CentroDistribucion centro = DatosIniciales.crearCentro();
    	
    	titulo("1. Registro de paquetes (orden eacto de llegada)");
    	int i = 1;
    	for (Paquete p : centro.getRegistro()) {
    		System.out.printf("%2d. %s%n", i++, p);
   
    	}
    	 titulo("2. Validaciones de registro");
         intentar("Codigo duplicado PQ731", () -> {
             centro.registrarPaquete(new Paquete("PQ731", "Salento", 1.0, 3, 10));
             return null;
         });
         intentar("Peso cero", () -> new Paquete("PQ001", "Armenia", 0, 3, 10));
         intentar("Prioridad 7", () -> new Paquete("PQ002", "Armenia", 2.0, 7, 10));
         intentar("Tiempo negativo", () -> new Paquete("PQ003", "Armenia", 2.0, 3, -5));
         intentar("Destino vacio", () -> new Paquete("PQ004", "  ", 2.0, 3, 10));
         intentar("Repartidor duplicado R101", () -> {
             centro.registrarRepartidor(new Repartidor("R101", "Otro", "Armenia", true));
             return null;
         });
         
         titulo("3. Consulta eficiente por codigo (HashMap)");
         for (String codigo : new String[] {"PQ942", "PQ359", "PQ999"}) {
             System.out.println(codigo + " -> " + centro.buscarPaquete(codigo)
                     .map(Paquete::toString).orElse("no existe"));
         }
         
         titulo("4. Repartidores");
         centro.getRepartidores().forEach(System.out::println);
         System.out.println("Buscar R105 -> " + centro.buscarRepartidor("R105").orElse(null));
         
         titulo("5. Municipios sin repetidos (LinkedHashSet) y ordenados (TreeSet)");
         System.out.println("Sin repetidos: " + centro.getMunicipios());
         System.out.println("Ordenados:     " + centro.getMunicipiosOrdenados());
         
         titulo("6. Paquetes agrupados por destino (Map<String, List<Paquete>>)");
         centro.getAgrupacionPorDestino().forEach((municipio, lista) -> {
             List<String> codigos = new ArrayList<>();
             lista.forEach(p -> codigos.add(p.getCodigo()));
             System.out.println(municipio + " -> " + codigos);
         });
         
         titulo("7. Orden natural (Comparable por codigo) y criterios alternativos (Comparator)");
         System.out.println("Por codigo:        " + codigos(centro.getPaquetesOrdenadosPorCodigo()));
         System.out.println("Prioridad desc:    " + codigosCon(centro.getPaquetesOrdenados(
                 ComparadoresPaquete.POR_PRIORIDAD_DESC), "p"));
         System.out.println("Peso desc:         " + codigosCon(centro.getPaquetesOrdenados(
                 ComparadoresPaquete.POR_PESO_DESC), "kg"));
         System.out.println("Tiempo asc:        " + codigosCon(centro.getPaquetesOrdenados(
                 ComparadoresPaquete.POR_TIEMPO_ASC), "min"));
         System.out.println("El registro no cambio, primero sigue siendo: " + centro.getRegistro().get(0).getCodigo());

         titulo("8. Ejemplo del enunciado: orden de despacho prioritario");
         List<Paquete> ejemplo = new ArrayList<>(List.of(
                 new Paquete("PQ420", "Armenia", 1, 3, 30),
                 new Paquete("PQ731", "Armenia", 1, 5, 45),
                 new Paquete("PQ105", "Armenia", 1, 5, 25),
                 new Paquete("PQ942", "Armenia", 1, 4, 20)));
         ejemplo.sort(ComparadoresPaquete.DESPACHO);
         System.out.println("Esperado PQ105, PQ731, PQ942, PQ420 -> obtenido " + codigos(ejemplo));
         
         titulo("9. Atencion por orden de llegada (Queue/ArrayDeque)");
         System.out.println("Siguiente por llegada: " + centro.verSiguientePorLlegada().orElse(null));
         atenderYEntregar(centro, true, 3);

         titulo("10. Despacho prioritario (PriorityQueue: prioridad, tiempo, codigo)");
         System.out.println("Siguiente prioritario: " + centro.verSiguientePrioritario().orElse(null));
         atenderYEntregar(centro, false, 6);

         titulo("11. Reglas de asignacion");
         intentar("Asignar PQ077 (Filandia) a R107 no disponible", () -> centro.asignarPaquete("PQ077", "R107"));
         intentar("Asignar PQ077 (Filandia) a R101 de Armenia", () -> centro.asignarPaquete("PQ077", "R101"));
         System.out.println("R107 vuelve a estar disponible");
         centro.cambiarDisponibilidad("R107", true);
         Asignacion manual = centro.asignarPaquete("PQ077", "R107");
         System.out.println("Asignado: " + manual);
         System.out.println("R107 ahora: " + centro.buscarRepartidor("R107").orElse(null));
         intentar("Asignar PQ702 (Filandia) a R107, ocupado", () -> centro.asignarPaquete("PQ702", "R107"));
         centro.registrarEntrega("PQ077");
         System.out.println("Entregado PQ077; R107 libre de nuevo: "
                 + centro.buscarRepartidor("R107").map(Repartidor::isDisponible).orElse(false));
         intentar("Entregar PQ214, que no fue asignado", () -> {
             centro.registrarEntrega("PQ214");
             return null;
         });
         
         titulo("12. Historial de entregas (lista simplemente enlazada propia, recorrida con Iterator)");
         int n = 1;
         StringBuilder linea = new StringBuilder();
         for (Paquete p : centro.getHistorial()) {
             if (n > 1) linea.append(" -> ");
             linea.append(p.getCodigo());
             n++;
         }
         System.out.println(linea);
         System.out.println("Entregados: " + centro.cantidadEntregados()
                 + " | Pendientes: " + centro.cantidadPendientes());
         
         titulo("13. Lista propia generica con otros tipos");
         ListaSimplementeEnlazada<String> nombres = new ListaSimplementeEnlazada<>();
         nombres.agregar("Armenia");
         nombres.agregar("Salento");
         nombres.agregar("Calarcá");
         System.out.println("Strings: " + nombres + ", obtener(1) = " + nombres.obtener(1));
         ListaSimplementeEnlazada<Integer> numeros = new ListaSimplementeEnlazada<>();
         numeros.agregar(10);
         numeros.agregar(20);
         numeros.agregar(30);
         numeros.eliminar(20);
         System.out.println("Integers tras eliminar 20: " + numeros + ", tamanio = " + numeros.tamanio());

         titulo("14. Recursividad: peso total por municipio (ejemplo del enunciado)");
         List<Paquete> ejemploPeso = List.of(
                 new Paquete("PQ101", "Armenia", 4.5, 3, 10),
                 new Paquete("PQ203", "Salento", 3.0, 3, 10),
                 new Paquete("PQ415", "Armenia", 7.2, 3, 10),
                 new Paquete("PQ730", "Armenia", 2.3, 3, 10));
         double total = Recursivos.pesoTotalPorDestino(ejemploPeso, "Armenia", true);
         System.out.printf("Peso total Armenia (ejemplo) = %.1f kg%n", total);
         for (String m : centro.getMunicipiosOrdenados()) {
             System.out.printf("Peso total %-11s (jornada) = %.1f kg%n", m,
                     Recursivos.pesoTotalPorDestino(centro.getRegistro(), m, false));
         }
         
         titulo("15. Recursividad: paquetes con prioridad >= minima");
         int[] prioridades = {5, 2, 4, 1, 5, 3, 4};
         System.out.println("Ejemplo enunciado {5,2,4,1,5,3,4}, minima 4 = "
                 + Recursivos.contarPrioridadMinima(prioridades, 4, 0) + " paquetes");
         List<Paquete> ejemploPrioridad = new ArrayList<>();
         for (int k = 0; k < prioridades.length; k++) {
             ejemploPrioridad.add(new Paquete("PX" + k, "Armenia", 1, prioridades[k], 10));
         }
         Recursivos.contarPrioridadMinima(ejemploPrioridad, 4, true);
         System.out.println("Jornada, prioridad >= 4: "
                 + Recursivos.contarPrioridadMinima(centro.getRegistro(), 4, false) + " paquetes");

         titulo("16. Busqueda binaria por codigo (precondicion: lista ordenada por codigo)");
         List<Paquete> reporte = new ArrayList<>();
         for (String c : new String[] {"PQ105", "PQ214", "PQ318", "PQ421", "PQ567", "PQ731", "PQ842", "PQ942"}) {
             reporte.add(centro.buscarPaquete(c).orElseThrow());
         }
         System.out.println("Reporte: " + codigos(reporte) + " ordenado? "
                 + BusquedaBinaria.estaOrdenadaPorCodigo(reporte));
         System.out.println("Buscar PQ731:");
         System.out.println("Posicion = " + BusquedaBinaria.buscarPorCodigo(reporte, "PQ731", true));
         System.out.println("Buscar PQ500 (no existe):");
         System.out.println("Posicion = " + BusquedaBinaria.buscarPorCodigo(reporte, "PQ500", true));
         List<Paquete> todosOrdenados = centro.getPaquetesOrdenadosPorCodigo();
         System.out.println("En los 31 paquetes ordenados, PQ866 esta en la posicion "
                 + BusquedaBinaria.buscarPorCodigo(todosOrdenados, "PQ866", false));

         titulo("17. Divide y venceras: paquete de mayor peso");
         double[] pesos = {4.5, 12.0, 3.2, 18.7, 7.4, 5.1};
         List<Paquete> ejemploPesos = new ArrayList<>();
         for (int k = 0; k < pesos.length; k++) {
             ejemploPesos.add(new Paquete("PD" + k, "Armenia", pesos[k], 3, 10));
         }
         DivideYVenceras.paqueteMasPesado(ejemploPesos, true);
         Paquete masPesado = DivideYVenceras.paqueteMasPesado(centro.getRegistro(), false);
         System.out.println("Jornada completa: " + masPesado);
    }
    
    private static void atenderYEntregar(CentroDistribucion centro, boolean porLlegada, int cantidad) {
        for (int k = 0; k < cantidad; k++) {
            try {
                Asignacion a = porLlegada ? centro.atenderPorLlegada() : centro.despacharPorPrioridad();
                System.out.println("Asignado: " + a + " | prioridad " + a.paquete().getPrioridad()
                        + ", " + a.paquete().getTiempoEstimado() + " min");
                centro.registrarEntrega(a.paquete().getCodigo());
                System.out.println("   entregado " + a.paquete().getCodigo());
            } catch (IllegalStateException e) {
                System.out.println("No se pudo despachar: " + e.getMessage());
            }
        }
    }
    
    private static void intentar(String descripcion, Supplier<?> accion) {
        try {
            accion.get();
            System.out.println("[ACEPTADO] " + descripcion);
        } catch (RuntimeException e) {
            System.out.println("[RECHAZADO] " + descripcion + ": " + e.getMessage());
        }
    }
    
    private static String codigos(List<Paquete> lista) {
        List<String> r = new ArrayList<>();
        lista.forEach(p -> r.add(p.getCodigo()));
        return String.join(", ", r);
    }
    
    private static String codigosCon(List<Paquete> lista, String unidad) {
        List<String> r = new ArrayList<>();
        for (Paquete p : lista) {
            String valor = switch (unidad) {
                case "p" -> String.valueOf(p.getPrioridad());
                case "kg" -> String.valueOf(p.getPeso());
                default -> String.valueOf(p.getTiempoEstimado());
            };
            r.add(p.getCodigo() + "(" + valor + ")");
        }
        return String.join(" ", r);
    }
    
    private static void titulo(String texto) {
        System.out.println();
        System.out.println("==== " + texto + " ====");
    }
    
}
