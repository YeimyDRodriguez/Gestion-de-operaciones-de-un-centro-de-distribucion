package quindioexpress.modelo;

import java.util.Objects;

public class Paquete implements Comparable<Paquete> {

    // Prioridades del paquete
    public static final int PRIORIDAD_MIN = 1;
    public static final int PRIORIDAD_MAX = 5;
    public static final int PRIORIDAD_POR_DEFECTO = 5;

    private final String codigo;
    private final String destino;
    private final double peso;
    private final int prioridad;
    private final int tiempoEstimado;
    private EstadoPaquete estado;

    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {

        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("El código del paquete no puede ser nulo o vacío");
        }

        if (destino == null || destino.isBlank()) {
            throw new IllegalArgumentException("El destino no puede ser nulo ni vacio (" + codigo + ")");
        }

        if (peso <= 0) {
            throw new IllegalArgumentException("El peso del paquete debe ser mayor que cero (" + codigo + ")");
        }

        if (prioridad < PRIORIDAD_MIN || prioridad > PRIORIDAD_MAX) {
            throw new IllegalArgumentException("La prioridad del paquete debe estar entre " + PRIORIDAD_MIN + " y "
                    + PRIORIDAD_MAX + " (" + codigo + ")");
        }

        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero (" + codigo + ")");
        }

        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
        this.estado = EstadoPaquete.PENDIENTE;
    }

    public Paquete(String codigo, String destino, double peso, int tiempoEstimado) {
        this(codigo, destino, peso, PRIORIDAD_POR_DEFECTO, tiempoEstimado);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public EstadoPaquete getEstado() {
        return estado;
    }

    public void setEstado(EstadoPaquete estado) {
        this.estado = estado;
    }

    @Override
    public int compareTo(Paquete o) {
        return this.codigo.compareTo(o.codigo); 
    }

    @Override 
    public boolean equals(Object o){
        if (this == o) return true; 
        if (!(o instanceof Paquete)) return false;
        return codigo.equals(((Paquete)o).codigo); 
    }

    @Override
    public int hashCode(){
        return Objects.hash(codigo); 
    }

    @Override 
    public String toString(){
        return "Paquete{" +
                "codigo='" + codigo + '\'' +
                ", destino='" + destino + '\'' +
                ", peso=" + peso +
                ", prioridad=" + prioridad +
                ", tiempoEstimado=" + tiempoEstimado +
                ", estado=" + estado +
                '}';
    }
}
