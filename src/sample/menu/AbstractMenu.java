package sample.menu;

import javafx.scene.control.Menu;
import sample.textarea.TextEditorArea;

public abstract class AbstractMenu extends Menu {

    private final TextEditorArea textArea;

    protected AbstractMenu(final TextEditorArea textArea, final String title) {
        super(title);
        this.textArea = textArea;
    }

    protected TextEditorArea getTextArea() { return this.textArea; }
}