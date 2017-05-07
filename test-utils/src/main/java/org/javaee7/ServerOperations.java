package org.javaee7;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Various high level Java EE 7 samples specific operations to execute against
 * the various servers used for running the samples
 * 
 * @author arjan
 *
 */
public class ServerOperations {

    /**
     * Add the default test user and credentials to the identity store of 
     * supported containers
     */
    public static void addUsersToContainerIdentityStore() {
        
        // TODO: abstract adding container managed users to utility class
        // TODO: consider PR for sending CLI commands to Arquillian
        
        String javaEEServer = System.getProperty("javaEEServer");
        
        if ("glassfish-remote".equals(javaEEServer) || "payara-remote".equals(javaEEServer)) {
            
            System.out.println("Adding user for glassfish-remote");
            
            List<String> cmd = new ArrayList<>();
            
            cmd.add("create-file-user");
            cmd.add("--groups");
            cmd.add("g1");
            cmd.add("--passwordfile");
            cmd.add(Paths.get("").toAbsolutePath() + "/src/test/resources/password.txt");
            
            cmd.add("u1");
            
            CliCommands.payaraGlassFish(cmd);
        } else {
            if (javaEEServer == null) {
                System.out.println("javaEEServer not specified");
            } else {
                System.out.println(javaEEServer + " not supported");
            }
        }
        
        // TODO: support other servers than Payara and GlassFish
        
        // WildFly ./bin/add-user.sh -a -u u1 -p p1 -g g1
    }
    
}
