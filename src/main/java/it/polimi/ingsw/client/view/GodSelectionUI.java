package it.polimi.ingsw.client.view;

import javafx.application.Application;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GodSelectionUI {

    private ArrayList<String> selectedGods;

    /*public static void main(String[] args) {
        launch(args);
    }*/

    private final Scene godSelectionScene;

    public GodSelectionUI() {
        ArrayList<ToggleButton> godButtons = createGodButtons();
        ScrollPane sp = new ScrollPane();
        FlowPane fp = new FlowPane(Orientation.HORIZONTAL);
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

        godButtons.forEach(x -> fp.getChildren().add(x));

        fp.setPadding(new Insets(25));
        sp.setContent(fp);

        Label l = new Label("Select the gods");
        l.setFont(Font.font("Futura", FontWeight.NORMAL, 35));
        GridPane.setHalignment(l, HPos.CENTER);

        Button b = new Button("Continue");
        b.setFont(Font.font("Futura", FontWeight.NORMAL, 12));
        b.setOnAction((ActionEvent event) -> {
            //sendGods(selectedGods);
            clientCLI.getInstance().sendGods(selectedGods);
            Platform.exit();
        });
        GridPane.setHalignment(b, HPos.CENTER);

        root.add(l, 0, 0);
        root.add(sp, 0, 1);
        root.add(b, 0, 2);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        /*stage.setTitle("God selection");
        stage.setScene(scene);
        stage.show();*/
    }

    private ArrayList<ToggleButton> createGodButtons() {
        ArrayList<ToggleButton> buttons = new ArrayList<>();
        selectedGods = new ArrayList<>();

        ToggleButton b = new ToggleButton();
        b.setId("apollo");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("artemis");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("athena");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("atlas");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("demeter");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("hephaestus");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("hermes");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("minotaur");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("pan");
        buttons.add(b);
        b = new ToggleButton();
        b.setId("prometheus");
        buttons.add(b);

        int playersNum = clientCLI.getInstance().getPlayersNum();

        ToggleButton x;
        for(int a = 1 ; a <= buttons.size() ;  a++){
            x = buttons.get(a-1);
            x.setFont(Font.font("Futura", 12));
            x.setPrefSize(200, 300);
            x.setOnAction((ActionEvent event) -> {
                if(selectedGods.contains(((Control) event.getSource()).getId())) {
                    selectedGods.remove(String.valueOf(((Control) event.getSource()).getId()));
                    System.out.println("Remove: " + ((Control) event.getSource()).getId());
                } else {
                    if(selectedGods.size() < playersNum) {
                        selectedGods.add(String.valueOf(((Control) event.getSource()).getId()));
                        System.out.println("Add: " + ((Control) event.getSource()).getId());
                    } else {
                        ((ToggleButton) event.getSource()).setSelected(false);
                    }
                }
            });
            System.out.println(String.format("%02d", a));
            x.setBackground(new Background(new BackgroundImage(new Image("/graphic_resources/godCards/"+String.format("%02d", a)+".png", x.getWidth(), x.getHeight(), false, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(x.getWidth(), x.getHeight(), true, true, true, false))));
        }

        return buttons;
    }

    /*public void sendGods(ArrayList<String> gods) {
        System.out.println("Gods: "+selectedGods.stream().collect(Collectors.joining(", ")));
        //send
        View.getInstance().sendGods(gods);
    }*/

    public Scene getScene() {
        return this.godSelectionScene;
    }
}
