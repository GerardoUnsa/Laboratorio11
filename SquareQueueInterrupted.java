import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Squares integers.
 */
class Squarer {

    private final BlockingQueue<Integer> in;
    private final BlockingQueue<SquareResult> out;
    // Rep invariant: in, out != null

    /**
     * Make a squarer that will listen for requests and generate replies.
     *
     * @param requests queue to receive requests from
     * @param replies  queue to send replies to
     */
    public Squarer(BlockingQueue<Integer> requests, BlockingQueue<SquareResult> replies) {
        this.in = requests;
        this.out = replies;
    }

    /**
     * Start handling squaring requests.
     */
    public void start() {
        new Thread(new Runnable() {
            public void run() {
                // handle requests until we are interrupted
                while (!Thread.interrupted()) {
                    try {
                        // block until a request arrives
                        int x = in.take();
                        if(x==0){
                            throw new InterruptedException();
                        }
                        // compute the answer and send it back
                        int y = x * x;
                        out.put(new SquareResult(x, y));
                    } catch (InterruptedException ie) {
                        // stop
                        break;
                    }
                }
            }
        }).start();
    }
}

/**
 * An immutable squaring result message.
 */
class SquareResult {
    private final int input;
    private final int output;

    /**
     * Make a new result message.
     *
     * @param input  input number
     * @param output square of input
     */
    public SquareResult(int input, int output) {
        this.input = input;
        this.output = output;
    }

    // TODO: we will want more observers, but for now...

    @Override
    public String toString() {
        return input + "^2 = " + output;
    }
}

public class SquareQueueInterrupted {

    /**
     * Create and use a squarer.
     */
    public static void main(String[] args) {

        BlockingQueue<Integer> requests = new LinkedBlockingQueue<>();
        BlockingQueue<SquareResult> replies = new LinkedBlockingQueue<>();

        Squarer squarer = new Squarer(requests, replies);
        squarer.start();

        start(requests,replies,1);
        start(requests,replies,2);
    }

        public static void start(BlockingQueue<Integer> requests, BlockingQueue<SquareResult> replies,int n) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        int r = (int) (Math.random() * 101);
                        System.out.println("Enviando request : " + r + " desde el hilo: " + n);
                        requests.put(r);
                        System.out.println(replies.take());
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }).start();
    }

}