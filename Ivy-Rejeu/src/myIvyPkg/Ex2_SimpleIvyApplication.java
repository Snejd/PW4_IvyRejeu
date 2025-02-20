package myIvyPkg;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

class Ex2_SimpleIvyApplication {

    private Ivy bus;

    public Ex2_SimpleIvyApplication() throws IvyException {
        // initialize (set up the bus, name and ready message)
        bus = new Ivy("Ex2_SimpleIvyApplication", "Ex2_SimpleIvyApplication Ready", null);

       
        // subscribe to "Bye" messages
        bus.bindMsg("^Bye$", new IvyMessageListener() {
            // callback
            //public final synchronized int sendMsg(String string) throws IvyException
   
            @Override
            public void receive(IvyClient client, String[] strings) {
                System.out.println("IvyTest captured message and is disconnecting...");
                // leave the bus, and as it is the only thread, quit the app
                bus.stop();
            }
        });
        
        // subscribe to "Hello" messages
        bus.bindMsg("^Hello(.*)", new IvyMessageListener() {
            // callback
            //public final synchronized int sendMsg(String string) throws IvyException
   
            @Override
            public void receive(IvyClient client, String[] strings) {
                System.out.println("Received Hello msg: "+ strings[0]);
                try {
                    bus.sendMsg("Nice to meet you Vladimir!");
                } catch (IvyException ex) {
                    Logger.getLogger(Ex2_SimpleIvyApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // subscribe to "Hello" messages
        bus.bindMsg("^Nice to meet you (.*)", new IvyMessageListener() {
            // callback
            //public final synchronized int sendMsg(String string) throws IvyException
   
            @Override
            public void receive(IvyClient client, String[] strings) {
                System.out.println("Received Nice to meet you msg from "+strings[0]);
            }
        });
        
        
        
        // start the bus on the default domain
        
        bus.start("127.255.255.255:2010");
        bus.sendToSelf(true);
        //bus.start("10.0.0.255:1234");
        bus.sendMsg("Hello My love, my name is Vladimir");
        
        
        bus.sendMsg("Bye");
        
        
    }

    
}
