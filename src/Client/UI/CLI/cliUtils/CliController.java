package Client.UI.CLI.cliUtils;

import com.budhash.cliche.Command;
import com.budhash.cliche.Param;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by andrea on 16/06/17.
 * <p>
 * https://github.com/aBeautifulMind/COFfee/blob/master/src/ClientPackage/View/CLIResources/CLIParser.java
 * Thanks manu <3
 */
public class CliController implements Runnable {
    private BufferedReader sysIn;
    private Object cliPage;
    private boolean printHelpMessage;

    public CliController(Object cliPage, boolean printHelpMessage) {
        sysIn = new BufferedReader(new InputStreamReader(System.in));
        this.cliPage = cliPage;
        this.printHelpMessage = printHelpMessage;
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Used to change page we are serving
     *
     * @param cliPage
     */
    public void setCliPage(Object cliPage, boolean printHelpMessage) {
        this.cliPage = cliPage;
        if (printHelpMessage) printHelpMessage();
    }

    @Override
    public void run() {
        if (printHelpMessage) printHelpMessage();
        while (true) {
            try {
                String line = sysIn.readLine();
                if (line.equals("help")) {
                    printHelpMessage();
                } else tryToCallMethod(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tries to invoke a method using RMI
     *
     * @param line method to invoke followed by parameters
     */
    public void tryToCallMethod(String line) {
        String[] words = line.split(" ");
        Class<?> c = cliPage.getClass();

        //Search in class method that user wants to call
        Method methodUserWants = null;
        Method[] allMethods = c.getDeclaredMethods();
        for (Method method : allMethods) {
            if (method.getName().toLowerCase().equals(words[0].toLowerCase()) && method.getParameterCount() == (words.length - 1)) {
                methodUserWants = method;
                break;
            }
        }

        //No method found
        if (methodUserWants == null) {
            CliSout.log(CliSout.LogLevel.Errore, "Comando non valido, scrivi 'help' per la lista completa.");
            return;
        }

        //If we have a method, invoke it.
        Object[] arguments = new Object[words.length - 1];
        System.arraycopy(words, 1, arguments, 0, words.length - 1);//Copy parameters passed from cli to array
        try {
            methodUserWants.invoke(cliPage, arguments);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a help message with all commands you can use
     */
    private void printHelpMessage() {
        Class<?> c = cliPage.getClass();
        Method[] allMethods = c.getDeclaredMethods();

        System.out.println("\nComandi utilizzabili");
        //Print helper for each method
        for (Method method : allMethods) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof Command) {
                    //This is an annotated command, we have smthing to print
                    Command commandAnnotation = (Command) annotation;
                    System.out.print(
                            "Comando: " +
                                    CliSout.LogLevel.Informazione.colorCode + method.getName() +
                                    CliSout.LogLevel.colorReset + " (" +
                                    commandAnnotation.description() + ")\n"
                    );

                    //Print stuff related to arguments
                    Annotation[][] annotations = method.getParameterAnnotations();
                    if (annotations.length > 0) {
                        System.out.print(
                                CliSout.LogLevel.Avvertimento.colorCode +
                                        "\nParameters: " +
                                        CliSout.LogLevel.colorReset + "\n"
                        );
                    }
                    for (Annotation[] ann : annotations) {
                        for (Annotation paramAnn : ann) {
                            if (paramAnn instanceof Param) {
                                Param param = (Param) paramAnn;
                                System.out.print("\t" +
                                        CliSout.LogLevel.Informazione.colorCode + param.name() +
                                        CliSout.LogLevel.colorReset +
                                        ": " + param.description() + "\n"
                                );
                            }
                        }
                    }
                    System.out.println("\n");//This method is finished, change line.
                }
            }
        }
    }
}
