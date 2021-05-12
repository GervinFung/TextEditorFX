package sample.textarea;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public final class PositionCaret extends Label {

    public PositionCaret() {
        super("Line: 1 Row: 1");
        this.setMaxWidth(Double.MAX_VALUE);
        this.setAlignment(Pos.CENTER);
    }

    public void updatePosition(final int line, final int row) {
        this.setText("Line: " + line + " Row: " + row);
    }
}