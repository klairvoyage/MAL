package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
import java.util.Objects;
import java.util.Optional;
import processor.XMLProcessor;

public class ExportManagerGUI extends Application {
    private final XMLProcessor xmlProcessor = new XMLProcessor();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Export List Filter");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/MAL.png"))));

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(40, 40, 40, 40));
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("AniManga List Customizer");
        title.setFont(new Font("Arial", 24));
        title.setTextFill(Color.DARKSLATEGREY);

        Label listTypeLabel = new Label("Choose the type of list (anime/manga):");
        ChoiceBox<String> listTypeChoiceBox = new ChoiceBox<>();
        listTypeChoiceBox.getItems().addAll("Anime", "Manga");

        Label filePathLabel = new Label("Enter the file path:");
        TextField filePathTextField = new TextField();

        Label statusTypeLabel = new Label("Choose the type of show status to be removed:");
        ChoiceBox<String> statusTypeChoiceBox = new ChoiceBox<>();
        statusTypeChoiceBox.getItems().addAll("Watching", "Completed", "On-Hold", "Dropped", "Plan to Watch", "none");

        Label importableLabel = new Label("Make all entries be updated on import?");
        ChoiceBox<String> importableChoiceBox = new ChoiceBox<>();
        importableChoiceBox.getItems().addAll("Yes", "No");

        grid.add(listTypeLabel, 0, 0);
        grid.add(listTypeChoiceBox, 1, 0);
        grid.add(filePathLabel, 0, 1);
        grid.add(filePathTextField, 1, 1);
        grid.add(statusTypeLabel, 0, 2);
        grid.add(statusTypeChoiceBox, 1, 2);
        grid.add(importableLabel, 0, 3);
        grid.add(importableChoiceBox, 1, 3);

        Button startButton = new Button("Start");
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px;");
        startButton.setOnAction(e -> {
            String listType = listTypeChoiceBox.getValue();
            String filePath = filePathTextField.getText();
            String statusType = statusTypeChoiceBox.getValue();
            String importable = importableChoiceBox.getValue();

            if (listType == null || filePath.isEmpty() || statusType == null || importable == null) {
                alertWithTitleAndIcon(new Alert(
                        Alert.AlertType.ERROR, "All fields must be filled out!"), "Error!"
                ).showAndWait();
                return;
            }
            if (!filePath.toLowerCase().endsWith(".xml") || !new File(filePath).exists()) {
                alertWithTitleAndIcon(new Alert(
                        Alert.AlertType.WARNING, "File path must point to a valid .xml-file!"), "Warning!"
                ).showAndWait();
                return;
            }

            Alert alert = alertWithTitleAndIcon(new Alert(Alert.AlertType.CONFIRMATION,
                    "This process will overwrite your current .xml-file!"), "Confirmation");
            alert.setHeaderText("Are you sure you want to proceed?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK){
                if (!"none".equalsIgnoreCase(statusType)) xmlProcessor.removeStatus(listType, filePath, statusType);
                if ("Yes".equalsIgnoreCase(importable)) xmlProcessor.updateOnImport(listType, filePath);
                alert = alertWithTitleAndIcon(new Alert(Alert.AlertType.INFORMATION,
                        "Congrats! You're ready to re-import it now: https://myanimelist.net/import.php"), "Info");
                alert.setHeaderText("Process successfully finished!");
                alert.showAndWait();
            }
        });
        grid.add(startButton, 0, 4);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(startButton);
        grid.add(hbox, 0, 4, 2, 1);
        VBox vbox = new VBox(20, title, grid);
        vbox.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(vbox, 550, 450));
        primaryStage.show();
    }

    private Alert alertWithTitleAndIcon(Alert alert, String s) {
        alert.setTitle("Export List Filter: " + s);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/MAL.png"))));
        return alert;
    }
}
