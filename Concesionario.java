package trabajopsp;

import java.util.concurrent.Semaphore;


/*
* Trabajo de enfoque Programacion de servicios y procesos
* @author Alvaro jaime torres
 */
public class Concesionario {

    //Semáforo para limitar el acceso a 4 vehículos simultáneamente.
    //Un semáforo permite manejar permisos para controlar el acceso a recursos compartidos.
    private static final Semaphore semaforo = new Semaphore(4);

    // Array para la disponibilidad de los vehículos
    // Cada posición indica si un vehículo está ocupado (true) o disponible (false).
    private static final boolean[] vehiculos = new boolean[4];

    public static void main(String[] args) {

        // Lista de clientes que quieren probar los vehículos.
        String[] clientes = {"Ana", "Luis", "Carlos", "Maria", "Pedro", "Laura", "Jorge", "Sofia", "Elena"};

        // Crear e iniciar un hilo para cada cliente
        for (String cliente : clientes) {
            new Thread(new Cliente(cliente)).start();
        }
    }

    // Clase que representa la tarea del cliente
    // hereda de la clase Thread 
    static class Cliente extends Thread {

        private final String nombre; //nombre de los clientes

        //Contructor con parametros
        public Cliente(String nombre) {
            this.nombre = nombre;
        }

        // sobreescribir el metodo run 
        @Override
        public void run() {

            // Inicializamos el número del vehículo que el cliente usará.
            int numeroVehiculo = -1;
            try {
                // Intentar adquirir un permiso del semáforo
                semaforo.acquire();

                //Bloque synchronized para asegurar que solo un cliente accede a los vehículos a la vez.
                synchronized (vehiculos) {
                    for (int i = 0; i < vehiculos.length; i++) {
                        if (!vehiculos[i]) { // Si un vehiculo no esta siendo utilizado
                            vehiculos[i] = true; // Marcar como ocupado
                            numeroVehiculo = i + 1; // Asignar el número del vehículo
                            break;
                        }
                    }
                }

                // Simular el cliente probando el vehículo
                System.out.println(nombre + "  probando vehiculo  " + numeroVehiculo);

                // Simular tiempo de prueba
                Thread.sleep((long) (Math.random() * 5000) + 1000);

                // mensaje cuando el cliente termina
                System.out.println(nombre + " termino de probar el vehiculo  " + numeroVehiculo);

                // Liberar el vehículo
                synchronized (vehiculos) {
                    vehiculos[numeroVehiculo - 1] = false; // Marcar como disponible
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Liberar el permiso para que otros clientes puedan probar
                semaforo.release();
            }
        }
    }
}
