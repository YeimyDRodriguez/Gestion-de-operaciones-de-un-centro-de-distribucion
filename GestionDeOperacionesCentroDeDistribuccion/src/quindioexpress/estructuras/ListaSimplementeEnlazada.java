package quindioexpress.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class ListaSimplementeEnlazada<T> implements ColeccionPropia<T> {


    private static class Nodo<T> {
        private T valor;
        private Nodo<T> siguiente;

        Nodo(T valor) {
            this.valor = valor;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    
    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (cabeza == null) {          
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamanio++;
    }

  
    @Override
    public boolean eliminar(T elemento) {
        if (cabeza == null) {          
            return false;
        }
        if (iguales(cabeza.valor, elemento)) {   
            eliminarCabeza();
            return true;
        }
        Nodo<T> anterior = cabeza;
        Nodo<T> actual = cabeza.siguiente;
        while (actual != null) {
            if (iguales(actual.valor, elemento)) {
                desenlazar(anterior, actual);    
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;                  
    }


    @Override
    public T eliminarEnPosicion(int posicion) {
        validarPosicion(posicion);
        if (posicion == 0) {
            return eliminarCabeza();
        }
        Nodo<T> anterior = nodoEn(posicion - 1);
        Nodo<T> actual = anterior.siguiente;
        desenlazar(anterior, actual);
        return actual.valor;
    }

    @Override
    public T obtener(int posicion) {
        validarPosicion(posicion);
        return nodoEn(posicion).valor;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }

    private class IteradorLista implements Iterator<T> {
        private Nodo<T> actual = cabeza;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (actual == null) {
                throw new NoSuchElementException("No hay mas elementos");
            }
            T valor = actual.valor;
            actual = actual.siguiente;
            return valor;
        }
    }


    private T eliminarCabeza() {
        T valor = cabeza.valor;
        cabeza = cabeza.siguiente;
        if (cabeza == null) {          
            cola = null;
        }
        tamanio--;
        return valor;
    }

    private void desenlazar(Nodo<T> anterior, Nodo<T> actual) {
        anterior.siguiente = actual.siguiente;
        if (actual == cola) {          
            cola = anterior;
        }
        tamanio--;
    }

    private Nodo<T> nodoEn(int posicion) {
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }
        return actual;
    }

    private void validarPosicion(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException("Posicion " + posicion + " invalida, tamanio " + tamanio);
        }
    }

    private boolean iguales(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (T valor : this) {
            if (sb.length() > 1) sb.append(" -> ");
            sb.append(valor);
        }
        return sb.append("]").toString();
    }
}
