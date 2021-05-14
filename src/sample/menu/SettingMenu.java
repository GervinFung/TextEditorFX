package sample.menu;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.textarea.TextEditorArea;

import static sample.textarea.KeyBoardKey.KEYBOARD_KEY;

public final class SettingMenu extends AbstractMenu {
    public SettingMenu(final TextEditorArea textArea) {
        super(textArea,"Setting");
        this.getItems().addAll(
                this.changeTextAreaColor(),
                this.changeFontStyle(),
                new SeparatorMenuItem(),
                this.zoomOut(),
                this.zoomIn(),
                this.changeFontSize(),
                new SeparatorMenuItem(),
                this.showDate(),
                this.showSystemName(),
                new SeparatorMenuItem(),
                this.goToLine());
    }

    private Menu changeFontStyle() {
        final Menu changeFontStyle = new Menu("Change Font Style");
        for (final String font : Font.getFamilies()) {
            final MenuItem fontMenuItem = new MenuItem(font);
            fontMenuItem.setOnAction(a -> this.getTextArea().setFont(Font.font(font)));
            changeFontStyle.getItems().add(fontMenuItem);
        }
        return changeFontStyle;
    }

    private MenuItem changeTextAreaColor() {
        final MenuItem changeTextAreaColor = new MenuItem("Change Text Area Color");
        changeTextAreaColor.setOnAction(e -> this.showColorPickerScene());
        return changeTextAreaColor;
    }

    private void showColorPickerScene() {
        Platform.runLater(() -> {
            final Stage stage = new Stage();
            stage.setTitle("Color Picker");

            final BorderPane box = new BorderPane();
            final ColorPicker colorPicker = new ColorPicker();
            colorPicker.setOnAction(a -> {
                final String format = format(colorPicker.getValue());
                this.getTextArea().lookup(".content").setStyle("-fx-background-color:" + format + " !important;");
                this.getTextArea().setStyle("-fx-background-color:" + format + " !important;");
            });
            box.setCenter(colorPicker);

            final Button okButton = new Button("Ok");
            okButton.setOnAction(e -> stage.close());
            box.setCenterShape(true);
            BorderPane.setAlignment(okButton, Pos.CENTER);
            box.setBottom(okButton);

            stage.setScene(new Scene(box, 250, 100));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();
        });
    }

    private String format(final Color c) {
        return String.format("#%02x%02x%02x", (int) (255 * c.getRed()), (int) (255 * c.getGreen()), (int) (255 * c.getBlue()));
    }

    private MenuItem zoomOut() {
        final MenuItem zoomOut = new MenuItem("Decrease Font Size (" + KEYBOARD_KEY.DECREASE_FONT + ")");
        zoomOut.setOnAction(e -> getTextArea().decreaseFontSize());
        return zoomOut;
    }

    private MenuItem zoomIn() {
        final MenuItem zoomIn = new MenuItem("Increase Font Size (" + KEYBOARD_KEY.INCREASE_FONT + ")");
        zoomIn.setOnAction(e -> getTextArea().increaseFontSize());
        return zoomIn;
    }

    private MenuItem changeFontSize() {
        final Menu changeFontSize = new Menu("Change Font Size");
        for (int i = 1; i <= 200; i++) {
            final MenuItem fontMenuItem = new MenuItem(Integer.toString(i));
            int finalI = i;
            fontMenuItem.setOnAction(a -> this.getTextArea().setFontSize(finalI));
            changeFontSize.getItems().add(fontMenuItem);
        }
        return changeFontSize;
    }

    private MenuItem showSystemName() {
        final MenuItem showDate = new MenuItem("Show Computer Name (" + KEYBOARD_KEY.WRITE_COMPUTER_NAME + ")");
        showDate.setOnAction(e -> getTextArea().writeSystemName());
        return showDate;
    }

    private MenuItem showDate() {
        final MenuItem showDate = new MenuItem("Show Date (" + KEYBOARD_KEY.WRITE_DATE + ")");
        showDate.setOnAction(e -> getTextArea().writeDate());
        return showDate;
    }

    private MenuItem goToLine() {
        final MenuItem showDate = new MenuItem("Go To Line (" + KEYBOARD_KEY.GOTO + ")");
        showDate.setOnAction(e -> getTextArea().goToLine());
        return showDate;
    }
}