package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Objects;
import java.util.Optional;
import processor.XMLProcessor;

public class ExportManagerGUI extends Application {
    private final XMLProcessor xmlProcessor = new XMLProcessor();
    private final Label title = new Label("AniManga List Customizer");
    private final GridPane grid = new GridPane();
    private ChoiceBox<String> listTypeChoiceBox;
    private ChoiceBox<String> statusTypeChoiceBox;
    private ChoiceBox<String> importableChoiceBox;
    String filePath;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Export List Filter");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/MAL.png"))));

        title.setFont(new Font("Arial", 24));
        title.setTextFill(Color.DARKSLATEGREY);

        createGridPane();

        VBox buttons = new VBox();
        buttons.setSpacing(30);
        buttons.setAlignment(Pos.CENTER);

        buttons.getChildren().add(createFileChooser(primaryStage));
        buttons.getChildren().add(createStartButton());

        grid.add(buttons, 0, 3, 2, 1);

        VBox vbox = new VBox(15, title, grid);
        vbox.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(vbox, 550, 425));
        primaryStage.show();
    }

    private void createGridPane() {
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(40, 40, 40, 40));
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Choose the type of list (anime/manga):"), 0, 0);
        listTypeChoiceBox = createChoiceBox("Anime", "Manga");
        grid.add(listTypeChoiceBox, 1, 0);

        grid.add(new Label("Choose the type of show status to be removed:"), 0, 1);
        statusTypeChoiceBox = createChoiceBox("Watching", "Completed", "On-Hold",
                                                "Dropped", "Plan to Watch", "none");
        grid.add(statusTypeChoiceBox, 1, 1);

        grid.add(new Label("Make all entries be updated on import?"), 0, 2);
        importableChoiceBox = createChoiceBox("Yes", "No");
        grid.add(importableChoiceBox, 1, 2);
    }

    private ChoiceBox<String> createChoiceBox(String... items) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(items);
        return choiceBox;
    }

    private Button createFileChooser(Stage primaryStage) {
        Button fileChooserButton = new Button("Choose File");
        fileChooserButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml-files", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                filePath = selectedFile.getAbsolutePath();
                fileChooserButton.setText("File Chosen: " + selectedFile.getName());
            }
        });
        return fileChooserButton;
    }

    private Button createStartButton() {
        Button startButton = new Button("Start");
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px;");
        startButton.setOnAction(e -> {
            String listType = listTypeChoiceBox.getValue();
            String statusType = statusTypeChoiceBox.getValue();
            String importable = importableChoiceBox.getValue();

            if (listType == null || statusType == null || importable == null || filePath==null ) {
                createAlert(new Alert(
                        Alert.AlertType.ERROR, "All fields must be filled out!"), "Error!"
                ).showAndWait();
                return;
            }

            listType = listType.toLowerCase();

            Alert alert = createAlert(new Alert(Alert.AlertType.CONFIRMATION,
                    "This process will overwrite your current .xml-file!"), "Confirmation");
            alert.setHeaderText("Are you sure you want to proceed?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK){
                listType = listType.toLowerCase();
                if (!"none".equals(statusType)) xmlProcessor.removeStatus(listType, filePath, statusType);
                if ("Yes".equals(importable)) xmlProcessor.updateOnImport(listType, filePath, "1");
                else xmlProcessor.updateOnImport(listType, filePath, "0");
                alert = createAlert(new Alert(Alert.AlertType.INFORMATION,
                        "Congrats! You're ready to re-import it now to https://myanimelist.net/import.php"), "Info");
                alert.setHeaderText("Process successfully finished!");
                alert.showAndWait();
            }
        });
        return startButton;
    }

    private Alert createAlert(Alert alert, String s) {
        alert.setTitle("Export List Filter: " + s);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/MAL.png"))));
        return alert;
    }
}
