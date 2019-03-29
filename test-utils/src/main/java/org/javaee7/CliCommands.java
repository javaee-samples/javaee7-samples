package org.javaee7;

import static java.lang.Runtime.getRuntime;
import static java.lang.Thread.currentThread;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Methods to execute "cli commands" against various servers.
 * 
 * @author Arjan Tijms
 *
 */
public class CliCommands {
    
    private static final Logger logger = Logger.getLogger(CliCommands.class.getName()); 
    private static final String OS = System.getProperty("os.name").toLowerCase();
    
    public static int payaraGlassFish(List<String> cliCommands) {
        return payaraGlassFish(cliCommands, null);
    }

    public static int payaraGlassFish(List<String> cliCommands, List<String> output) {
        
        String gfHome = System.getProperty("glassfishRemote_gfHome");
        if (gfHome == null) {
            logger.info("glassfishRemote_gfHome not specified");
            return -1;
        }
        
        Path gfHomePath = Paths.get(gfHome);
        if (!gfHomePath.toFile().exists()) {
            logger.severe("glassfishRemote_gfHome at " + gfHome + " does not exists");
            return -1;
        }
        
        if (!gfHomePath.toFile().isDirectory()) {
            logger.severe("glassfishRemote_gfHome at " + gfHome + " is not a directory");
            return -1;
        }
           
        Path asadminPath = gfHomePath.resolve(isWindows()? "bin/asadmin.bat" : "bin/asadmin");
        
        if (!asadminPath.toFile().exists()) {
            logger.severe("asadmin command at " + asadminPath.toAbsolutePath() + " does not exists");
            return -1;
        }
        
        List<String> cmd = new ArrayList<>();
        
        cmd.add(asadminPath.toAbsolutePath().toString());
        cmd.addAll(cliCommands);
        
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectErrorStream(true);
        
        try {
            return 
                waitToFinish(
                    readAllInput(
                        output,
                        destroyAtShutDown(
                            processBuilder.start())));
        } catch (IOException e) {
            return -1;
        }
    }
    
    public static Process destroyAtShutDown(final Process process) {
        getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (process != null) {
                    process.destroy();
                    try {
                        process.waitFor();
                    } catch (InterruptedException e) {
                        currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }
            }
        }));
        
        return process;
    }
    
    public static Process readAllInput(List<String> output, Process process) {
        // Read any output from the process
        try (Scanner scanner = new Scanner(process.getInputStream())) {
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                System.out.println(nextLine);
                if (output != null) {
                    output.add(nextLine);
                }
            }
        }
        
        return process;
    }
    
    public static int waitToFinish(Process process) {
        
        // Wait up to 30s for the process to finish
        int startupTimeout = 30 * 1000;
        while (startupTimeout > 0) {
           startupTimeout -= 200;
           try {
               Thread.sleep(200);
           } catch (InterruptedException e1) {
               // Ignore
           }
           
           try {
              int exitValue = process.exitValue();
              
              System.out.println("Asadmin process exited with status " + exitValue);
              return exitValue;
              
           } catch (IllegalThreadStateException e) {
              // process is still running
           }
        }
        
        throw new IllegalStateException("Asadmin process seems stuck after waiting for 30 seconds");
    }
    
    public static boolean isWindows() {
        return OS.contains("win");
    }
    
}
