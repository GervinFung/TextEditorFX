package sample.menu;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import sample.textarea.TextEditorArea;

import static sample.textarea.KeyBoardKey.KEYBOARD_KEY;

public final class FileMenu extends AbstractMenu {
    public FileMenu(final TextEditorArea textArea) {
        super(textArea,"File");
        this.getItems().addAll(
                this.newFileMenuItem(),
                this.newWindowMenuItem(),
                new SeparatorMenuItem(),
                this.saveFileMenuItem(),
                this.saveAsMenuItem(),
                new SeparatorMenuItem(),
                this.openFileMenuItem(),
                this.openWindowMenuItem(),
                new SeparatorMenuItem(),
                this.printFile(),
                this.quitFileMenuItem());
    }

    private MenuItem newFileMenuItem() {
        final MenuItem newFile = new MenuItem("New File (" + KEYBOARD_KEY.NEW_FILE + ")");
        newFile.setOnAction(e -> super.getTextArea().createNewFile());
        return newFile;
    }

    private MenuItem newWindowMenuItem() {
        final MenuItem newWindow = new MenuItem("New Window (" + KEYBOARD_KEY.NEW_WINDOW + ")");
        newWindow.setOnAction(e -> this.getTextArea().createNewWindow());
        return newWindow;
    }

    private MenuItem saveFileMenuItem() {
        final MenuItem saveFile = new MenuItem("Save File (" + KEYBOARD_KEY.SAVE_FILE + ")");
        saveFile.setOnAction(e -> this.getTextArea().saveFile());
        return saveFile;
    }

    private MenuItem saveAsMenuItem() {
        final MenuItem saveFile = new MenuItem("Save As (" + KEYBOARD_KEY.SAVE_AS + ")");
        saveFile.setOnAction(e -> this.getTextArea().saveAs());
        return saveFile;
    }

    private MenuItem openFileMenuItem() {
        final MenuItem openFile = new MenuItem("Open File (" + KEYBOARD_KEY.OPEN_FILE + ")");
        openFile.setOnAction(e -> this.getTextArea().openFile());
        return openFile;
    }

    private MenuItem openWindowMenuItem() {
        final MenuItem openWindow = new MenuItem("Open File In New Window (" + KEYBOARD_KEY.OPEN_WINDOW + ")");
        openWindow.setOnAction(e -> this.getTextArea().openFileInNewWindow());
        return openWindow;
    }

    private MenuItem printFile() {
        final MenuItem printFile = new MenuItem("Print File Content (" + KEYBOARD_KEY.PRINT_FILE + ")");
        printFile.setOnAction(e -> this.getTextArea().printFile());
        return printFile;
    }

    private MenuItem quitFileMenuItem() {
        final MenuItem quitFile = new MenuItem("Quit Application (" + KEYBOARD_KEY.QUIT_FILE + ")");
        quitFile.setOnAction(e -> this.getTextArea().quitApplication());
        return quitFile;
    }
}