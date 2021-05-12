package sample.menu;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import sample.textarea.TextEditorArea;

public final class EditMenu extends AbstractMenu {

    public EditMenu(final TextEditorArea textArea) {
        super(textArea, "Edit");
        this.getItems().addAll(
                this.cutText(),
                this.copyText(),
                this.pasteText(),
                this.deleteText(),
                this.selectAllText(),
                new SeparatorMenuItem(),
                this.undoText(),
                this.redoText());
    }

    private MenuItem cutText() {
        final MenuItem cut = new MenuItem("Cut (CTRL + X)");
        cut.setOnAction(actionEvent -> this.getTextArea().cutText());
        return cut;
    }

    private MenuItem copyText() {
        final MenuItem copy = new MenuItem("Copy (CTRL + C)");
        copy.setOnAction(actionEvent -> this.getTextArea().copyText());
        return copy;
    }

    private MenuItem pasteText() {
        final MenuItem paste = new MenuItem("Paste (CTRL + P)");
        paste.setOnAction(actionEvent -> this.getTextArea().pasteText());
        return paste;
    }

    private MenuItem deleteText() {
        final MenuItem delete = new MenuItem("Delete (Del)");
        delete.setOnAction(actionEvent -> this.getTextArea().deleteSelectedText());
        return delete;
    }

    private MenuItem selectAllText() {
        final MenuItem selectAll = new MenuItem("Select All (CTRL + A)");
        selectAll.setOnAction(actionEvent -> this.getTextArea().selectAllText());
        return selectAll;
    }

    private MenuItem undoText() {
        final MenuItem undo = new MenuItem("Undo (CTRL + Z)");
        undo.setOnAction(actionEvent -> this.getTextArea().undoText());
        return undo;
    }

    private MenuItem redoText() {
        final MenuItem redo = new MenuItem("Redo  (CTRL + Y)");
        redo.setOnAction(actionEvent -> this.getTextArea().redoText());
        return redo;
    }
}