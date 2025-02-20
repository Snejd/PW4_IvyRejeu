package myIvyPkg;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class IvyRejeu
{

    private Ivy bus;

    public IvyRejeu() throws IvyException, InterruptedException
    {
        // initialize (set up the bus, name and ready message)
        bus = new Ivy("Test_Application", "Test_Application Ready", null);

        // subscribe to "Bye" messages
        bus.bindMsg("^Bye$", new IvyMessageListener()
        {
            // callback
            //public final synchronized int sendMsg(String string) throws IvyException

            @Override
            public void receive(IvyClient client, String[] strings)
            {
                System.out.println("IvyTest captured message and is disconnecting...");
                // leave the bus, and as it is the only thread, quit the app
                bus.stop();
            }
        });

        // subscribe to "Hello" messages matching everything
        bus.bindMsg("^Hello(.*)", new IvyMessageListener()
        {
            // callback
            //public final synchronized int sendMsg(String string) throws IvyException

            @Override
            public void receive(IvyClient client, String[] strings)
            {
                System.out.println("Received Hello msg from" + strings[0]);
                try {
                    bus.sendMsg("Nice to meet you Vladimir!");
                } catch (IvyException ex) {
                    Logger.getLogger(IvyRejeu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Subscribe to message 'Nice to meet you' matching letters 
        bus.bindMsg("^Nice to meet you ([a-zA-Z]+).*", new IvyMessageListener()
        {
            // callback
            //public final synchronized int sendMsg(String string) throws IvyException

            @Override
            public void receive(IvyClient client, String[] strings)
            {
                System.out.println("Received Nice to meet you msg from " + strings[0]);
            }
        });

        // start the bus on the default domain
        bus.start(null);
        bus.sendToSelf(true); // send on loopback
        //TimeUnit.SECONDS.sleep(1); // sleep to allow GUI to process the connection
        bus.sendMsg("Hello My love, my name is Vladimir");

        //bus.sendToSelf(true);
        bus.sendMsg("Bye");

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws InterruptedException
    {
        try {
            new IvyRejeu();
        } catch (IvyException ex) {
            // Print an explicit message in case of IvyException
            throw new RuntimeException("Ivy bus had a failure. Quitting Ex2_SimpleIvyApplication...");
        }
    }

}
