package quindioexpress.app;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;
import quindioexpress.servicio.CentroDistribucion;
public final class DatosIniciales {
	private DatosInciales() { }
	
	public static CentroDistribucion crearCentro() {
		CentroDistribucion centro = new CentroDistribucion();
		cargarPaquetes(centro);
		cargarRepartidores(centro);
		return centro;
	}
	
	private static void cargarPaquetes (CentroDistribucion c) {
		 c.registrarPaquete(new Paquete("PQ731", "Armenia",     4.5, 5, 45));
	        c.registrarPaquete(new Paquete("PQ105", "Salento",     3.0, 5, 25));
	        c.registrarPaquete(new Paquete("PQ942", "Armenia",    12.0, 4, 20));
	        c.registrarPaquete(new Paquete("PQ318", "Calarcá",     3.2, 3, 35));
	        c.registrarPaquete(new Paquete("PQ567", "Salento",    18.7, 2, 50));
	        c.registrarPaquete(new Paquete("PQ829", "Armenia",     7.4, 5, 30));
	        c.registrarPaquete(new Paquete("PQ420", "Calarcá",     5.1, 3, 30));
	        c.registrarPaquete(new Paquete("PQ214", "Montenegro",  6.3, 1, 60));
	        c.registrarPaquete(new Paquete("PQ421", "Quimbaya",    2.8, 4, 40));
	        c.registrarPaquete(new Paquete("PQ842", "Circasia",    9.6, 5, 25));
	        c.registrarPaquete(new Paquete("PQ077", "Filandia",    1.5, 2, 55));
	        c.registrarPaquete(new Paquete("PQ653", "Armenia",     8.9, 3, 15));
	        c.registrarPaquete(new Paquete("PQ390", "Montenegro",  4.0, 5, 45));
	        c.registrarPaquete(new Paquete("PQ188", "Quimbaya",   11.2, 4, 35));
	        c.registrarPaquete(new Paquete("PQ906", "Salento",     2.2, 1, 20));
	        c.registrarPaquete(new Paquete("PQ512", "Calarcá",     6.8, 5, 25));
	        c.registrarPaquete(new Paquete("PQ264", "Circasia",    3.7, 2, 40));
	        c.registrarPaquete(new Paquete("PQ735", "Filandia",   14.3, 4, 50));
	        c.registrarPaquete(new Paquete("PQ049", "Armenia",     1.9, 2, 10));
	        c.registrarPaquete(new Paquete("PQ881", "Quimbaya",    5.5, 3, 45));
	        c.registrarPaquete(new Paquete("PQ336", "Montenegro",  7.7, 4, 30));
	        c.registrarPaquete(new Paquete("PQ617", "Armenia",    10.4, 1, 20));
	        c.registrarPaquete(new Paquete("PQ158", "Salento",     4.8, 3, 35));
	        c.registrarPaquete(new Paquete("PQ970", "Calarcá",     2.6, 4, 15));
	        c.registrarPaquete(new Paquete("PQ443", "Circasia",   13.1, 5, 40));
	        c.registrarPaquete(new Paquete("PQ702", "Filandia",    3.3, 3, 25));
	        c.registrarPaquete(new Paquete("PQ295", "Armenia",     6.1, 4, 30));
	        c.registrarPaquete(new Paquete("PQ584", "Quimbaya",    9.0, 2, 60));
	        c.registrarPaquete(new Paquete("PQ127", "Montenegro",  1.2, 5, 20));
	        c.registrarPaquete(new Paquete("PQ866", "Calarcá",    15.6, 3, 40));
	        c.registrarPaquete(new Paquete("PQ359", "Armenia",     5.0, 35)); 
	}
	
	private static void cargarRepartidores(CentroDistribucion c) {
        c.registrarRepartidor(new Repartidor("R101", "Ana María Gómez",       "Armenia",    true));
        c.registrarRepartidor(new Repartidor("R102", "Carlos Restrepo",       "Salento",    true));
        c.registrarRepartidor(new Repartidor("R103", "Luisa Fernanda Ríos",   "Calarcá",    true));
        c.registrarRepartidor(new Repartidor("R104", "Jorge Iván Castaño",    "Montenegro", true));
        c.registrarRepartidor(new Repartidor("R105", "Paula Andrea Ospina",   "Quimbaya",   true));
        c.registrarRepartidor(new Repartidor("R106", "Andrés Felipe Marín",   "Circasia",   true));
        c.registrarRepartidor(new Repartidor("R107", "Diana Carolina López",  "Filandia",   false));
        c.registrarRepartidor(new Repartidor("R108", "Santiago Valencia",     "Armenia",    true));
    }

}
