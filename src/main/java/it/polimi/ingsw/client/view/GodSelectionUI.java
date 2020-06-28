package it.polimi.ingsw.client.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Arrays;

public class GodSelectionUI {

    private ArrayList<String> selectedGods;

    /*public static void main(String[] args) {
        launch(args);
    }*/

    private ArrayList<ToggleButton> godButtons;

    private FlowPane fp;

    private final Scene godSelectionScene;

    private Label info;

    private Button b;

    private int nGodPicks = 0;

    /**
     * Creates the scene for the god selection scene.
     */
    public GodSelectionUI() {
        godButtons = createGodButtons();
        godButtons.stream().forEach(x -> x.setDisable(true));
        ScrollPane sp = new ScrollPane();
        fp = new FlowPane(Orientation.HORIZONTAL);
        fp.getStylesheets().add("style.css");
        GridPane root = new GridPane();
        root.getStylesheets().add("style.css");
        root.setHgap(25);
        root.setVgap(25);
        fp.setHgap(8);
        fp.setVgap(8);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);

        godSelectionScene = new Scene(root, 800, 700);
        root.setStyle("-fx-background-color: #CBE1EF");

        ColumnConstraints c = new ColumnConstraints();
        c.setHgrow(Priority.ALWAYS);
        root.getColumnConstraints().add(c);
        RowConstraints r1 = new RowConstraints();
        r1.setVgrow(Priority.NEVER);
        RowConstraints r2 = new RowConstraints();
        r2.setVgrow(Priority.ALWAYS);
        RowConstraints r3 = new RowConstraints();
        r3.setVgrow(Priority.NEVER);
        root.getRowConstraints().addAll(r1, r2, r3);

        //godButtons.forEach(x -> fp.getChildren().add(x));

        fp.setPadding(new Insets(25));
        sp.setContent(fp);

        Label l = new Label("Select the gods");
        l.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        GridPane.setHalignment(l, HPos.CENTER);

        b = new Button("Continue");
        b.setId("button");
        b.setDisable(true);
        b.setFont(Font.font("Futura", FontWeight.NORMAL, 12));
        b.setOnAction((ActionEvent event) -> {
            sendGods(selectedGods);
        });
        GridPane.setHalignment(b, HPos.CENTER);

        info = new Label();
        info.setId("infoLabel");
        info.setWrapText(true);
        info.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
        GridPane.setHalignment(info, HPos.CENTER);

        root.add(l, 0, 0);
        root.add(sp, 0, 1);
        root.add(b, 0, 2);
        root.add(info, 0, 3);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
    }

    /**
     * Creates one card for every implemented god.
     * @return List of god cards (selectable).
     */
    private ArrayList<ToggleButton> createGodButtons() {
        ArrayList<ToggleButton> buttons = new ArrayList<>();
        selectedGods = new ArrayList<>();

        //ToggleButton b;
        ArrayList<String> gods = new ArrayList<>(Arrays.asList("Apollo", "Artemis", "Athena", "Atlas", "Demeter", "Hephaestus", "Hermes", "Minotaur", "Pan", "Prometheus", "Aphrodite", "Ares", "Bia", "Chaos", "Charon", "Chronus", "Circe", "Dionysus", "Eros", "Hera", "Hestia", "Hypnus", "Limus", "Medusa", "Morpheus", "Persephone", "Poseidon", "Selene", "Triton", "Zeus"));
        gods.stream().forEach(x -> {
            ToggleButton b = new ToggleButton();
            b.setId(x);
            buttons.add(b);
        });

//        b.setId("Apollo");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Artemis");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Athena");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Atlas");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Demeter");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Hephaestus");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Hermes");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Minotaur");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Pan");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Prometheus");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Zeus");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Ares");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Charon");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Hestia");
//        buttons.add(b);
//        b = new ToggleButton();
//        b.setId("Poseidon");
//        buttons.add(b);

        //int playersNum = ClientGUI.getInstance().getPlayersNum();

        ToggleButton x;
        for(int a = 1 ; a <= buttons.size() ;  a++){
            x = buttons.get(a-1);
            x.setFont(Font.font("Futura", 12));
            x.setPrefSize(200, 300);
            x.setOnAction((ActionEvent event) -> {
                if(selectedGods.contains(((Control) event.getSource()).getId())) {
                    selectedGods.remove(String.valueOf(((Control) event.getSource()).getId()));
                    //System.out.println("Remove: " + ((Control) event.getSource()).getId());
                } else {
                    if(selectedGods.size() < nGodPicks) {
                        selectedGods.add(String.valueOf(((Control) event.getSource()).getId()));
                        //System.out.println("Add: " + ((Control) event.getSource()).getId());
                    } else {
                        ((ToggleButton) event.getSource()).setSelected(false);
                    }
                }
            });
            //System.out.println(String.format("%02d", a));
            x.setBackground(new Background(new BackgroundImage(new Image("/graphic_resources/godCards/"+String.format("%02d", a)+".png", x.getWidth(), x.getHeight(), false, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(x.getWidth(), x.getHeight(), true, true, true, false))));
        }

        return buttons;
    }

    /**
     * Starts the scene using only the cards allowed by the server
     * @param options List of gods the player can choose from
     */
    public void startGodSelection(ArrayList<String> options, int nPicks) {
        godButtons.stream().forEach(x -> {
            if (options.contains(x.getId())) {
                x.setDisable(false);
                fp.getChildren().add(x);
            }
        });
        b.setDisable(false);
        nGodPicks = nPicks;
    }

    /**
     * Calls the function in ClientGUI.class to send information about the selected god.
     * @param gods List containing a list of selected gods.
     */
    public void sendGods(ArrayList<String> gods) {
        //disable
        b.setDisable(true);
        godButtons.stream().forEach(x -> x.setDisable(true));
        info.setText("Waiting for other player(s)");
        //send
        if(nGodPicks == 1) {
            ClientGUI.getInstance().sendGods(gods, false);
        } else if (nGodPicks > 1) {
            ClientGUI.getInstance().sendGods(gods, true);
        }
    }

    /**
     * Returns reference to the god selection scene, used the change scene of the main stage.
     * @return Reference to the god selection scene.
     */
    public Scene getScene() {
        return this.godSelectionScene;
    }
}
