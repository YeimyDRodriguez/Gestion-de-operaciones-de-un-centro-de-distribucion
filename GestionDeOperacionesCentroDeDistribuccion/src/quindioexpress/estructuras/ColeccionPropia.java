package quindioexpress.estructuras;

public interface ColeccionPropia<T> extends Iterable<T> {

    void agregar(T elemento);

    boolean eliminar(T elemento);

    T eliminarEnPosicion(int posicion);

    T obtener(int posicion);

    int tamanio();

    boolean estaVacia();
}
