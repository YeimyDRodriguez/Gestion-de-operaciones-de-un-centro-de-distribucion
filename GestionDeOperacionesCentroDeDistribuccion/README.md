# QuindíoExpress — Gestión de operaciones de un centro de distribución

Parcial 1 de Estructuras de Datos (2026-2). Aplicación en Java que organiza la jornada de un centro de distribución de paquetes del Quindío: registro, consulta, clasificación, priorización, asignación a repartidores e historial de entregas.

## Integrantes

- Yeimy Rodríguez
- Estefanía Montoya Dávila
- Juan José Herrera H.

## Requisitos

- Java 17 o superior (el proyecto usa `record` y expresiones `switch`).
- No usa librerías externas. Los datos iniciales están definidos en Java (`DatosIniciales`).

## Cómo ejecutar

Desde la carpeta `GestionDeOperacionesCentroDeDistribuccion`:

```bash
# Compilar
javac -encoding UTF-8 -d bin src/quindioexpress/*/*.java

# Programa principal (demuestra todas las operaciones)
java -cp bin quindioexpress.app.Main

# Pruebas automáticas
java -cp bin quindioexpress.pruebas.Pruebas
```

También se puede ejecutar desde cualquier IDE (Eclipse, IntelliJ, VS Code, etc.) ejecutando la clase `Main` o `Pruebas` como aplicación Java.

## Estructura del proyecto

```
src/quindioexpress/
├── app/
│   ├── Main.java                  Demostración de todas las operaciones (17 secciones)
│   └── DatosIniciales.java        31 paquetes, 7 municipios y 8 repartidores
├── modelo/
│   ├── Paquete.java               Entidad con validaciones; Comparable por código
│   ├── Repartidor.java            Entidad con validaciones
│   ├── EstadoPaquete.java         PENDIENTE, ASIGNADO, ENTREGADO
│   └── ComparadoresPaquete.java   Comparators: prioridad, peso, tiempo y despacho
├── servicio/
│   ├── CentroDistribucion.java    Lógica del sistema y colecciones
│   └── Asignacion.java            Resultado de asignar un paquete a un repartidor
├── estructuras/
│   ├── ColeccionPropia.java       Interfaz genérica (contrato de la colección)
│   └── ListaSimplementeEnlazada.java  Lista propia genérica con Iterator
├── algoritmos/
│   ├── Recursivos.java            Peso total por destino y conteo por prioridad mínima
│   ├── BusquedaBinaria.java       Búsqueda binaria recursiva por código
│   └── DivideYVenceras.java       Paquete de mayor peso
└── pruebas/
    └── Pruebas.java               Pruebas automáticas sin librerías externas
```

## Estructuras de datos utilizadas

| Necesidad | Estructura | Operación principal | Complejidad |
|---|---|---|---|
| Conservar el orden exacto de registro | `ArrayList<Paquete>` | `add` al final | O(1) amortizado |
| Consultar un paquete por código | `HashMap<String, Paquete>` | `get` / `containsKey` | O(1) promedio |
| Municipios sin repetidos (orden de aparición) | `LinkedHashSet<String>` | `add` | O(1) promedio |
| Municipios ordenados alfabéticamente | `TreeSet<String>` | `add` | O(log n) |
| Paquetes agrupados por municipio | `LinkedHashMap<String, List<Paquete>>` | `computeIfAbsent` + `add` | O(1) promedio |
| Atención por orden de llegada | `ArrayDeque<Paquete>` (Queue) | `offer` / `poll` | O(1) |
| Despacho prioritario | `PriorityQueue<Paquete>` | `offer` / `poll` | O(log n) |
| Repartidores por identificación | `LinkedHashMap<String, Repartidor>` | `get` | O(1) promedio |
| Historial de entregas | `ListaSimplementeEnlazada<Paquete>` (propia) | `agregar` | O(1) |

## Reglas del despacho prioritario

`ComparadoresPaquete.DESPACHO` ordena por:

1. Mayor prioridad.
2. Menor tiempo estimado.
3. Código alfabético (orden natural de `Paquete`).

Ejemplo del enunciado: PQ420 (p3, 30), PQ731 (p5, 45), PQ105 (p5, 25), PQ942 (p4, 20) → **PQ105, PQ731, PQ942, PQ420**.

## Lista simplemente enlazada propia

- `ListaSimplementeEnlazada<T>` implementa `ColeccionPropia<T>`, que extiende `Iterable<T>`.
- Internamente solo usa nodos (`valor` + `siguiente`), sin colecciones de Java.
- Operaciones: `agregar`, `eliminar`, `eliminarEnPosicion`, `obtener`, `tamanio`, `estaVacia`.
- Se recorre con `for-each` gracias a su `Iterator` interno; el código externo no accede a los nodos.
- Mantiene referencias a `cabeza` y `cola`, por lo que `agregar` es O(1).

## Algoritmos

| Algoritmo | Clase | Tiempo | Espacio (pila) |
|---|---|---|---|
| Peso total por municipio (recursivo) | `Recursivos.pesoTotalPorDestino` | O(n) | O(n) |
| Conteo de prioridad ≥ mínima (recursivo) | `Recursivos.contarPrioridadMinima` | O(n) | O(n) |
| Búsqueda binaria por código (recursiva) | `BusquedaBinaria.buscarPorCodigo` | O(log n) | O(log n) |
| Paquete de mayor peso (divide y vencerás) | `DivideYVenceras.paqueteMasPesado` | O(n) | O(log n) |

La búsqueda binaria tiene como **precondición** que la lista esté ordenada por código. `BusquedaBinaria.estaOrdenadaPorCodigo` permite verificarlo.

## Reglas de negocio validadas

- No se permiten paquetes con código repetido ni repartidores con identificación repetida.
- Peso > 0, tiempo estimado > 0, destino no nulo ni vacío.
- Prioridad entre 1 y 5 (5 por defecto).
- Un repartidor no disponible no recibe asignaciones.
- La zona del repartidor debe coincidir con el destino del paquete.
- Al entregar un paquete, sale de los pendientes, el repartidor vuelve a estar disponible y el paquete se agrega al historial en el orden real de entrega.