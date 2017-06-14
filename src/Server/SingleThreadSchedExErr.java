package Server;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;

/**
 * Created by fiore on 14/06/2017.
 */
public class SingleThreadSchedExErr extends ScheduledThreadPoolExecutor {

    private final Consumer<Throwable> exceptionHandler;

    public SingleThreadSchedExErr(Consumer<Throwable> exceptionHandler) {
        super(1);
        this.exceptionHandler = exceptionHandler;
    }

    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt(); // ignore/reset
            }
        }

        if (t != null) {
            exceptionHandler.accept(t);
        }
    }

}
