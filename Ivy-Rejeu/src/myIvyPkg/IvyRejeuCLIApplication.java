package myIvyPkg;

import myIvyPkg.communication.CommunicationManager;
import fr.dgac.ivy.IvyException;
import java.util.concurrent.TimeUnit;

public class IvyRejeuCLIApplication {

    public static void main(String[] args) throws InterruptedException, IvyException {
        
        // Launch communications with Rejeu and do nothing else in the main
        CommunicationManager communicationManager;
        try {
            communicationManager = new CommunicationManager();
        } catch (IvyException e) {
            // Print an explicit message in case of IvyException
            throw new RuntimeException("Ivy bus had a failure. Quitting Ex3Question3_IvyRejeuApplication...");
        }
        
        
        
    }
    
}
