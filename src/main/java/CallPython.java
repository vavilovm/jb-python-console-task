import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CallPython {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            // Reading data using readLine
            String pythonPath = reader.readLine();

            Process p = Runtime.getRuntime().exec(pythonPath.concat(" -m timeit -r 10"));

            Runnable printCurrentSecond = new Runnable() {
                int i = 0;

                public void run() {
                    System.out.println(i);
                    i++;
                }
            };
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(printCurrentSecond, 0, 1, TimeUnit.SECONDS);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            // read the output from the command
            String s;
            while ((s = stdInput.readLine()) != null) {
                if (!executor.isShutdown()) {
                    executor.shutdown();
                }
                System.out.println(s);
            }

            System.exit(0);
        } catch (IOException e) {
            System.out.println("bad input. Try different path to python executable");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}