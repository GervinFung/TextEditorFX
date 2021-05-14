package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.menu.EditMenu;
import sample.menu.FileMenu;
import sample.menu.FindReplaceMenu;
import sample.menu.SettingMenu;
import sample.textarea.PositionCaret;
import sample.textarea.TextEditorArea;

public final class Main extends Application {

    @Override
    public void start(final Stage primaryStage) {
        final PositionCaret positionCaret = new PositionCaret();
        final TextEditorArea textEditorArea = new TextEditorArea(primaryStage, positionCaret);
        createNewWindow(primaryStage, textEditorArea, positionCaret);
    }

    private void createNewWindow(final Stage primaryStage, final TextEditorArea textArea, final PositionCaret positionCaret) {
        final BorderPane borderPane = new BorderPane();
        final Scene scene = new Scene(borderPane,600, 600);
        final MenuBar menuBar = this.createMenuBar(textArea);

        borderPane.setTop(menuBar);
        borderPane.setCenter(textArea);
        borderPane.setBottom(positionCaret);

        primaryStage.setOnCloseRequest(e -> {
            textArea.quitApplication();
            e.consume();
        });

        if (primaryStage.getTitle() == null) {
            primaryStage.setTitle("FX Text Editor");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void startNewInstance() {
        final Stage stage = new Stage();
        final PositionCaret positionCaret = new PositionCaret();
        final TextEditorArea textEditorArea = new TextEditorArea(stage, positionCaret);
        stage.setOnCloseRequest(e -> {
            textEditorArea.quitApplication();
            e.consume();
        });

        stage.initModality(Modality.NONE);
        openNewInstance(stage, textEditorArea, positionCaret);
    }

    public static void openNewInstance(final Stage stage, final TextEditorArea textArea, final PositionCaret positionCaret) {
        Platform.runLater(() -> {
            stage.initModality(Modality.NONE);
            new Main().createNewWindow(stage, textArea, positionCaret);
        });
    }

    private MenuBar createMenuBar(final TextEditorArea textArea) {
        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                new FileMenu(textArea),
                new EditMenu(textArea),
                new FindReplaceMenu(textArea),
                new SettingMenu(textArea));
        return menuBar;
    }

    public static void main(final String[] args) { Main.launch(args); }
}