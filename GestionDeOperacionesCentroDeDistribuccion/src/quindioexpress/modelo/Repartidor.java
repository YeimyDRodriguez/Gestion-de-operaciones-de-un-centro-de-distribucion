package quindioexpress.modelo;

import java.util.Objects;

public class Repartidor {

    private final String identificacion;
    private final String nombre;
    private final String zona;
    private boolean disponible;

    public Repartidor(String identificacion, String nombre, String zona) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("Digitar la identificacion es obligatoria!");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Digitar el nombre es obligatorio!");
        }
        if (zona == null || zona.isBlank()) {
            throw new IllegalArgumentException("Definir la zona es obligatorio!");
        }
        this.identificacion = identificacion.trim();
        this.nombre = nombre.trim();
        this.zona = zona.trim();
        this.disponible = disponible;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getZona() {
        return zona;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Repartidor)) return false;
        return identificacion.equals(((Repartidor) o).identificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacion);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s, %s)", identificacion, nombre, zona,
                disponible ? "Disponible" : "No Disponible");
    }
}
