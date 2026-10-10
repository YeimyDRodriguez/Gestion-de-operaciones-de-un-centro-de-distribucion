package quindioexpress.servicio;

import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;

public record Asignacion(Paquete paquete, Repartidor repartidor) {

    @Override
    public String toString() {
        return paquete.getCodigo() + "( " + paquete.getDestino() + ") ->" + repartidor.getNombre() + " "
                + repartidor.getNombre();
    }

}
