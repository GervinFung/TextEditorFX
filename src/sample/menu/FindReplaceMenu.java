package sample.menu;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import sample.textarea.TextEditorArea;

public final class FindReplaceMenu extends AbstractMenu {

    public FindReplaceMenu(final TextEditorArea textArea) {
        super(textArea,"Find or Replace");
        this.getItems().addAll(
                this.find(),
                this.findPrevious(),
                this.findNext(),
                new SeparatorMenuItem(),
                this.replaceAll(),
                this.replaceWord());
    }

    private MenuItem find() {
        final MenuItem find = new MenuItem("Find Word (CTRL + F)");
        find.setOnAction(e -> this.getTextArea().findReplace());
        return find;
    }

    private MenuItem replaceAll() {
        final MenuItem replace = new MenuItem("Replace All (CTRL + SHIFT + R)");
        replace.setOnAction(e -> this.getTextArea().findReplace());
        return replace;
    }

    private MenuItem findNext() {
        final MenuItem findNext = new MenuItem("Find Next (F3)");
        findNext.setOnAction(e -> this.getTextArea().findReplace());
        return findNext;
    }

    private MenuItem findPrevious() {
        final MenuItem findPrevious = new MenuItem("Find Previous (SHIFT + F3)");
        findPrevious.setOnAction(e -> this.getTextArea().findReplace());
        return findPrevious;
    }

    private MenuItem replaceWord() {
        final MenuItem replace = new MenuItem("Replace Word (CTRL + R)");
        replace.setOnAction(e -> this.getTextArea().findReplace());
        return replace;
    }
}