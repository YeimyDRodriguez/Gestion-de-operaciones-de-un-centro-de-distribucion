package quindioexpress.servicio;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import quindioexpress.estructuras.ColeccionPropia;
import quindioexpress.estructuras.ListaSimplementeEnlazada;
import quindioexpress.modelo.ComparadoresPaquete;
import quindioexpress.modelo.EstadoPaquete;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;

public class CentroDistribucion {

    private final List<Paquete> registro = new ArrayList<>();

    private final Map<String, Paquete> paquetesPorCodigo = new HashMap<>();

    private final Set<String> municipiosOrdenados = new TreeSet<>();

    private final SortedSet<String> municipiosOrdenadosSet = new TreeSet<>();

    private final Map<String, List<Paquete>> paquetesPorDestino = new HashMap<>();

    private final Queue<Paquete> colaLllegada = new ArrayDeque<>();

    private final PriorityQueue<Paquete> colaPrioridad = new PriorityQueue<>(ComparadoresPaquete.DESPACHO);

    private final Map<String, Repartidor> repartidores = new LinkedHashMap<>();

    
    private final Map<String, Repartidor> asignaciones = new HashMap<>();

    private final ColeccionPropia<Paquete> historial = new ListaSimplementeEnlazada<>();

    private int pendientes;

    public void registrarPaquete(Paquete paquete) {
        if (paquetesPorCodigo.containsKey(paquete.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un paquete con codigo " + paquete.getCodigo());
        }
        registro.add(paquete);
        paquetesPorCodigo.put(paquete.getCodigo(), paquete);
        municipios.add(paquete.getDestino());
        municipiosOrdenados.add(paquete.getDestino());
        paquetesPorDestino.computeIfAbsent(paquete.getDestino(), d -> new ArrayList<>()).add(paquete);
        colaLlegada.offer(paquete);
        colaPrioridad.offer(paquete);
        pendientes++;
    }

    public boolean existePaquete(String codigo) {
        return paquetesPorCodigo.containsKey(codigo);
    }

    public Optional<Paquete> buscarPaquete(String codigo) {
        return Optional.ofNullable(paquetesPorCodigo.get(codigo));
    }

    public List<Paquete> getRegistro() {
        return Collections.unmodifiableList(registro);
    }

    public List<Paquete> getPendientes() {
        List<Paquete> resultado = new ArrayList<>();
        for (Paquete p : colaLlegada) {
            if (p.getEstado() == EstadoPaquete.PENDIENTE) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public int cantidadPendientes() {
        return pendientes;
    }

    public Set<String> getMunicipios() {
        return Collections.unmodifiableSet(municipios);
    }

    public SortedSet<String> getMunicipiosOrdenados() {
        return Collections.unmodifiableSortedSet(municipiosOrdenados);
    }

    public List<Paquete> getPaquetesPorDestino(String municipio) {
        return Collections.unmodifiableList(paquetesPorDestino.getOrDefault(municipio, List.of()));
    }

    public Map<String, List<Paquete>> getAgrupacionPorDestino() {
        return Collections.unmodifiableMap(paquetesPorDestino);
    }

    public List<Paquete> getPaquetesOrdenadosPorCodigo() {
        List<Paquete> copia = new ArrayList<>(registro);
        Collections.sort(copia);
        return copia;
    }

    public List<Paquete> getPaquetesOrdenados(Comparator<Paquete> criterio) {
        List<Paquete> copia = new ArrayList<>(registro);
        copia.sort(criterio);
        return copia;
    }

    public void registrarRepartidor(Repartidor repartidor) {
        if (repartidores.containsKey(repartidor.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe un repartidor con identificacion "
                    + repartidor.getIdentificacion());
        }
        repartidores.put(repartidor.getIdentificacion(), repartidor);
    }

    public Optional<Repartidor> buscarRepartidor(String identificacion) {
        return Optional.ofNullable(repartidores.get(identificacion));
    }

    public Collection<Repartidor> getRepartidores() {
        return Collections.unmodifiableCollection(repartidores.values());
    }

    public void cambiarDisponibilidad(String identificacion, boolean disponible) {
        obtenerRepartidor(identificacion).setDisponible(disponible);
    }

    public Optional<Repartidor> buscarRepartidorDisponible(String zona) {
        for (Repartidor r : repartidores.values()) {
            if (r.isDisponible() && r.getZona().equalsIgnoreCase(zona)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public Optional<Paquete> verSiguientePorLlegada() {
        limpiarFrente(colaLlegada);
        return Optional.ofNullable(colaLlegada.peek());
    }

    public Optional<Paquete> verSiguientePrioritario() {
        limpiarFrente(colaPrioridad);
        return Optional.ofNullable(colaPrioridad.peek());
    }

    public Asignacion atenderPorLlegada() {
        return despacharDesde(colaLlegada);
    }

    public Asignacion despacharPorPrioridad() {
        return despacharDesde(colaPrioridad);
    }

    private Asignacion despacharDesde(Queue<Paquete> cola) {
        limpiarFrente(cola);
        Paquete siguiente = cola.peek();
        if (siguiente == null) {
            throw new IllegalStateException("No hay paquetes pendientes");
        }
        Repartidor repartidor = buscarRepartidorDisponible(siguiente.getDestino())
                .orElseThrow(() -> new IllegalStateException("No hay repartidor disponible en "
                        + siguiente.getDestino() + "; " + siguiente.getCodigo() + " sigue en espera"));
        cola.poll();
        return asignar(siguiente, repartidor);
    }

    public Asignacion asignarPaquete(String codigo, String idRepartidor) {
        return asignar(obtenerPaquete(codigo), obtenerRepartidor(idRepartidor));
    }

    private Asignacion asignar(Paquete paquete, Repartidor repartidor) {
        if (paquete.getEstado() != EstadoPaquete.PENDIENTE) {
            throw new IllegalStateException("El paquete " + paquete.getCodigo() + " no esta pendiente");
        }
        if (!repartidor.isDisponible()) {
            throw new IllegalStateException("El repartidor " + repartidor.getIdentificacion()
                    + " no esta disponible");
        }
        if (!repartidor.getZona().equalsIgnoreCase(paquete.getDestino())) {
            throw new IllegalStateException("La zona de " + repartidor.getIdentificacion() + " ("
                    + repartidor.getZona() + ") no corresponde al destino de " + paquete.getCodigo()
                    + " (" + paquete.getDestino() + ")");
        }
        paquete.setEstado(EstadoPaquete.ASIGNADO);
        repartidor.setDisponible(false);
        asignaciones.put(paquete.getCodigo(), repartidor);
        pendientes--;
        return new Asignacion(paquete, repartidor);
    }

    private void limpiarFrente(Queue<Paquete> cola) {
        while (!cola.isEmpty() && cola.peek().getEstado() != EstadoPaquete.PENDIENTE) {
            cola.poll();
        }
    }

    public void registrarEntrega(String codigo) {
        Paquete paquete = obtenerPaquete(codigo);
        Repartidor repartidor = asignaciones.remove(codigo);
        if (repartidor == null) {
            throw new IllegalStateException("El paquete " + codigo + " no esta asignado a ningun repartidor");
        }
        paquete.setEstado(EstadoPaquete.ENTREGADO);
        repartidor.setDisponible(true);
        historial.agregar(paquete);
    }

    public Iterable<Paquete> getHistorial() {
        return historial;
    }

    public int cantidadEntregados() {
        return historial.tamanio();
    }

    private Paquete obtenerPaquete(String codigo) {
        return buscarPaquete(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No existe el paquete " + codigo));
    }

    private Repartidor obtenerRepartidor(String id) {
        return buscarRepartidor(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el repartidor " + id));
    }
}
