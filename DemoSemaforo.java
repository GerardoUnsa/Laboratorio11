import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Semaforo {
    public static int numeroDeSemaforo=1;
    private final BlockingQueue<Integer> in;

    public Semaforo(BlockingQueue<Integer> requests) {
        this.in = requests;
        System.out.println("Semaforo " + numeroDeSemaforo + " se encuentra en rojo");
        start(numeroDeSemaforo);
        numeroDeSemaforo++;
    }

    public void start(int n) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    // TODO: we may want a way to stop the thread
                    try {
                        //Toma un entero
                        int x = in.take();
                        //Si el numero tomado es de otro semaforo
                        if(x!=n){
                            System.out.println("Semaforo " + n + " cambio a verde");
                            Thread.sleep(1000);
                            System.out.println("Semaforo " + n + " cambio a amarillo");
                            Thread.sleep(1000);
                            System.out.println("Semaforo " + n + " cambio a rojo");
                            //Avisa a los demas semaforos que esta en rojo
                            in.put(n);
                            Thread.sleep(1000);
                        }
                        //Si el numero tomado es del mismo semaforo
                        else {
                            //Regresa el x tomado
                            in.put(x);
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

public class DemoSemaforo {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Integer> requests = new LinkedBlockingQueue<>();
        //Indica que el semaforo 1 acaba de cambiar a rojo
        requests.put(1);
        Semaforo semaforo1 = new Semaforo(requests);
        Semaforo semaforo2 = new Semaforo(requests);

    }
}