package myIvyPkg.communication;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javafx.scene.control.TextArea;

/**
 *
 * @author vlada
 */
public class CommunicationManager
{

    private Ivy bus;
    private boolean run = false;
    private int simTime;
    private int startTime, stopTime;
    private final LocalTime now = LocalTime.now();
    private final String currtime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
    private final String FileName = "msgCapture_"+ currtime + ".txt";
    

    public CommunicationManager() throws IvyException, InterruptedException
    {
        CommunicationManagerHelper();
        subscribeToFileRead(true);
        subscribeToMoved(null, true);
    }
    
    public CommunicationManager(TextArea textArea, boolean log) throws IvyException, InterruptedException
    {
        CommunicationManagerHelper();
        subscribeToFileRead(false);
        subscribeToMoved(textArea, log);
    }
    
    private void CommunicationManagerHelper() throws IvyException
    {
        // initialize (set up the bus, name and ready message)
        bus = new Ivy("CommunicationManager", "CommunicationManager Ready", null);
        subsribeToQuit();
        subscribeToClock();
        subscribeToCurrFlghts();
        subscribeToReady();
        startBusLoopback();
    }

    public int getSimTime()
    {
        return simTime;
    }

    public int getStartTime()
    {
        return startTime;
    }

    public int getStopTime()
    {
        return stopTime;
    }

    public boolean busIsRunning()
    {
        return run;
    }

    public void sendMsg(String msg) throws IvyException, InterruptedException
    {
        bus.sendMsg(msg);
    }

    
    
    private void subsribeToQuit() throws IvyException
    {
        // subscribe to "Bye" messages
        bus.bindMsg("^quit$", (IvyClient client, String[] strings) -> {
            System.out.println("CommunicationManager captured quit message and is disconnecting...");
            // leave the bus, and as it is the only thread, quit the app
            bus.stop();
        } // callback
        //public final synchronized int sendMsg(String string) throws IvyException
        );
    }
    
    private void subscribeToClock() throws IvyException
    {
        bus.bindMsg("^ClockEvent Time=([0-9]{2}):([0-9]{2}):([0-9]{2}).*$", (IvyClient client, String[] strings) -> {
            int simHH = Integer.parseInt(strings[0]);
            int simMM = Integer.parseInt(strings[1]);
            int simSS = Integer.parseInt(strings[2]);
            simTime = simHH * 3600 + simMM * 60 + simSS;
            System.out.println("Time event: " + strings[0] + ":" + strings[1] + ":" + strings[2]);
        });
    }
    
    
        /*
        * TrackMovedEvent Flight=324 CallSign=LB114JQ Ssr=0452 Sector=SL Layers=F,I X=87.00 Y=-128.88 Vx=-197 Vy=265 Afl=195 Rate=2098 Heading=323 GroundSpeed=330 Tendency=1 Time=12:08:32
        * Flight= 324 is the flight number of the aircraft whose elements are given at this time.
        *   (under normal operation, once on average every 8 seconds of simulated time)
        * CallSign= Aircraft call sign. "Unknown" if unknown.
        * Ssr= Transponder code. "0000" if unknown.
        * Sector= The current sector (deduced from the sector entry and exit times in the Courage file)
        *   is indicated. "--" if the name is not known.
        * Layers= The layers in which this flight is currently located are provided as a comma-separated list of "layers".
        *   reminder: an aircraft can be in multiple layers, as it is physically at any moment in one layer
        *   (space slice between 2 flight levels) given its current flight level, but must also be visualized
        *   in others that it will soon reach given its current evolution profile (climb or descent).
        * The coordinates X, Y are expressed in NM CAUTRA coordinates (or lat/long: degrees,
        *   tenths of degrees currently not fully and correctly implemented). The format used by replay is 2 digits after the decimal point.
        * Vx, Vy represent the speed components in Kts (the module is directly provided by GroundSpeed). The values provided are integers.
        * Afl= Current flight level
        * Rate= provides the climb rate in feet/minute (if negative, descent). Integer format
        * Tendency gives the vertical tendency 0 = level stable, 1 = climbing, -1 = descending, -2 unknown
        * Time= The last field of this message is used to timestamp it (HH:MM:SS).
         */
    private void subscribeToMoved(TextArea textArea, boolean log) throws IvyException
    {
        bus.bindMsg("^TrackMovedEvent Flight=([0-9]{3,5}) CallSign=([0-9A-Z]{7}) Ssr=([0-9]{4}) Sector=([A-Z]{2}|--) Layers=([A-Z,]+) X=(-?[0-9]+\\.[0-9]{2}) Y=(-?[0-9]+\\.[0-9]{2}) Vx=(-?[0-9]+) Vy=(-?[0-9]+) Afl=([0-9]+) Rate=([0-9]+) Heading=([0-9]+) GroundSpeed=([0-9]+) Tendency=([0-9]+) Time=([0-9]{2}:[0-9]{2}:[0-9]{2}).*$", (IvyClient client, String[] strings) -> {
            System.out.println("TrackMovedEvent Flight: " + strings[0]
                    + ", CallSign: " + strings[1] + ", Ssr: " + strings[2]
                    + ", Sector: " + strings[3] + ", Layers: " + strings[4]
                    + ", X: " + strings[5] + ", Y: " + strings[6] + ", Vx: "
                    + strings[7] + ", Vy: " + strings[8] + ", Afl: " + strings[9]
                    + ", Rate: " + strings[10] + ", Heading: " + strings[11]
                    + ", GroundSpeed: " + strings[12] + ", Tendency: " + strings[13]
                    + ", Time: " + strings[14]);
            
            if(textArea != null){
                textArea.appendText("Time " + strings[14]+":\n");
                textArea.appendText("TrackMovedEvent Flight: " + strings[0]
                        + ", CallSign: " + strings[1] + ", Ssr: " + strings[2]
                        + ", Sector: " + strings[3] + ", Layers: " + strings[4]
                        + ", X: " + strings[5] + ", Y: " + strings[6] + ", Vx: "
                        + strings[7] + ", Vy: " + strings[8] + ", Afl: " + strings[9]
                        + ", Rate: " + strings[10] + ", Heading: " + strings[11]
                        + ", GroundSpeed: " + strings[12] + ", Tendency: " + strings[13]+"\n");
            }
            
            if(log == true){
                try {            
                    try (FileWriter myWriter = new FileWriter(FileName, true)) {
                        myWriter.write("TrackMovedEvent Flight: " + strings[0]
                                + ", CallSign: " + strings[1] + ", Ssr: " + strings[2]
                                + ", Sector: " + strings[3] + ", Layers: " + strings[4]
                                + ", X: " + strings[5] + ", Y: " + strings[6] + ", Vx: "
                                + strings[7] + ", Vy: " + strings[8] + ", Afl: " + strings[9]
                                + ", Rate: " + strings[10] + ", Heading: " + strings[11]
                                + ", GroundSpeed: " + strings[12] + ", Tendency: " + strings[13]
                                + ", Time: " + strings[14] + "\n");
                    }
                    //System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                }
            }
        });
    }

    private void subscribeToCurrFlghts() throws IvyException
    {
        // Catch answer to GetCurrentFlights
        // CurrentFlights ForMe Time=10:16:35 List=45 876 13 6543 568 90 146
        bus.bindMsg("^CurrentFlights ForMe Time=([0-9]{2}:[0-9]{2}:[0-9]{2}) List=([0-9 ]+)$", (IvyClient client, String[] strings) -> {
            String time = strings[0];
            String[] flightNumbers = strings[1].split(" ");
            System.out.println("CurrentFlights at Time: " + time + " with List: " + Arrays.toString(flightNumbers));
        });
    }

    private void subscribeToReady() throws IvyException
    {
        // Catch rejeu READY message
        bus.bindMsg("^rejeu READY$", (IvyClient client, String[] strings) -> {
            System.out.println("Rejeu is ready.");
        });
    }

    private void subscribeToFileRead(boolean log) throws IvyException
    {
        // Catch FileReadEvent
        bus.bindMsg("^FileReadEvent Type=REJEU Name=([a-zA-Z0-9_\\.]+) StartTime=([0-9]{2}):([0-9]{2}):([0-9]{2}) EndTime=([0-9]{2}):([0-9]{2}):([0-9]{2})$", (IvyClient client, String[] strings) -> {
            int startHH = Integer.parseInt(strings[1]);
            int startMM = Integer.parseInt(strings[2]);
            int startSS = Integer.parseInt(strings[3]);
            int endHH = Integer.parseInt(strings[4]);
            int endMM = Integer.parseInt(strings[5]);
            int endSS = Integer.parseInt(strings[6]);
            startTime = startHH * 3600 + startMM * 60 + startSS;
            stopTime = endHH * 3600 + endMM * 60 + endSS;
            System.out.println("FileReadEvent Type=REJEU Name=" + strings[0] + " StartTime=" + strings[1] + ":" + strings[2] + ":" + strings[3] + " EndTime=" + strings[4] + ":" + strings[5] + ":" + strings[6]);

            
            if(log == true){
            try {
                File myObj = new File(FileName);
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
            }
            
                try {
                    try (FileWriter myWriter = new FileWriter(FileName, true)) {
                        myWriter.write("Name=" + strings[0]
                                + "\nStartTime=" + strings[1] + ":" + strings[2] + ":" + strings[3]
                                + "\nEndTime=" + strings[4] + ":" + strings[5] + ":" + strings[6] + "\n"+
                                "*******************\n\n");
                    }
                    //System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                }
            }

        });
    }

    private void startBusLoopback() throws IvyException
    {
        bus.start(null);
        run = true;
        bus.sendToSelf(true); // send on loopback
    }

}
