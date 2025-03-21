package myIvyPkg;

import fr.dgac.ivy.IvyException;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import myIvyPkg.communication.CommunicationManager;

public class IvyRejeuFXApplication extends Application {
    
    // Interface de communication avec Rejeu
    private CommunicationManager communicationManager;
    private TextArea textArea;
    private boolean logging = false;
    
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        // Retrieve command-line arguments
        List<String> args = getParameters().getRaw();
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("log")) {
            textArea.appendText("Log mode enabled.\n");
            logging = true;
        }
    
        // Create and set up a text area to display the messages sent by Rejeu
        textArea = new TextArea("Messages from Rejeu :\n");
        textArea.setPrefSize(800, 600);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        ScrollPane scrollPane = new ScrollPane(textArea);
        
        // Create and set up the scene graph root container
        StackPane root = new StackPane();
        root.getChildren().add(scrollPane);
        
        // Create and set up the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ex3Question4_IvyRejeuJavaFXApplication");
        primaryStage.show();
        
        // Launch communication with Rejeu
        
        try {
            communicationManager = new CommunicationManager(textArea, logging);
        } catch (IvyException e) {
            // Display an explicit message in case of IvyException
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ivy bus had a failure.");
            alert.setHeaderText(null);
            alert.setContentText("Quitting Ex3Question4_IvyRejeuJavaFXApplication...");
            alert.showAndWait();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
