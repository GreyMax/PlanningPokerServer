package planning.poker.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import planning.poker.Estimate;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static Controller instance = null;
    private Provider server;

    @FXML
    public TextArea itemDescriptionField;
    @FXML
    public Button newItemButton;
    @FXML
    public Button finishItemButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        server = new Provider();
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.run();
            }
        }).start();
    }

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();

        return instance;
    }

    public void newItem(ActionEvent actionEvent) {
        String itemDescription = itemDescriptionField.getText();
        server.sendItemForForecast(itemDescription);
    }

    public void finish(ActionEvent actionEvent) {
        server.sendStopAction();
    }

    public void addEstimate(Estimate estimate) {
        System.out.println("Successfully added:" + " opt=" + estimate.getOptimistic()
                + " pes=" + estimate.getPessimistic()
                + " more=" + estimate.getMoreLikely()
                + " avg=" + estimate.getWeightedAvg()
        );
    }
}
