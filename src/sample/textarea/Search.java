package sample.textarea;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class Search extends Stage {

    private static final String SEARCH = "Enter Word To Search", REPLACE = "Enter Word To Replace";

    private final TextEditorArea textEditorArea;
    private final TextField wordToSearch, replaceWord;

    public static void startSearch(final TextEditorArea textEditorArea) {
        Platform.runLater(() -> new Search(textEditorArea));
    }

    private Search(final TextEditorArea textEditorArea) {
        this.textEditorArea = textEditorArea;

        this.wordToSearch = new TextField();
        this.wordToSearch.setPromptText(SEARCH);

        this.replaceWord = new TextField();
        this.replaceWord.setPromptText(REPLACE);

        final BorderPane borderPane = new BorderPane();

        borderPane.setTop(this.createTopPane());
        borderPane.setBottom(this.createBottomPane());

        this.setScene(new Scene(borderPane));
        this.initModality(Modality.WINDOW_MODAL);
        this.show();
    }

    private Pane createTopPane() {
        final BorderPane topPane = new BorderPane();
        topPane.setTop(this.wordToSearch);
        topPane.setBottom(this.replaceWord);
        return topPane;
    }
    
    private final static class PaneButton extends Button {
        private final static double WIDTH = 100, HEIGHT = 10;
        private PaneButton(final String title) {
            super(title);
            this.setPrefSize(WIDTH, HEIGHT);
        }
    }

    private GridPane createBottomPane() {
        final GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.BASELINE_CENTER);

        final Button previousButton = new PaneButton("Search Previous");
        previousButton.setOnAction(e -> {
            final String searchText = this.wordToSearch.getText();
            final int length = searchText.length();
            final int index = this.textEditorArea.getText().lastIndexOf(searchText, this.textEditorArea.getCaretPosition() - length - 1);
            if (index >= 0) {
                this.textEditorArea.selectRange(index, index + length);
            }
        });
        final Button nextButton = new PaneButton("Search Next");
        nextButton.setOnAction(e -> {
            final String searchText = this.wordToSearch.getText();
            final int length = searchText.length();
            final int index = this.textEditorArea.getText().indexOf(searchText, this.textEditorArea.getCaretPosition());
            if (index >= 0) {
                this.textEditorArea.selectRange(index, index + length);
            }
        });
        gridPane.addRow(0, previousButton, nextButton);

        final Button replace = new PaneButton("Replace");
        replace.setOnAction(e -> this.replaceWord());
        final Button replaceAll = new PaneButton("Replace All");
        replaceAll.setOnAction(e -> this.replaceAllWord());

        gridPane.addRow(1, replace, replaceAll);

        return gridPane;
    }

    private void replaceAllWord() {
        final String searchText = this.wordToSearch.getText();
        final String textAreaText = this.textEditorArea.getText();
        int index = textAreaText.indexOf(searchText);
        if (index >= 0) {
            while(true){
                final int length = searchText.length();
                if (index >= 0) {
                    this.textEditorArea.selectRange(index, index + length);
                    this.textEditorArea.replaceText(index, index + searchText.length(), this.replaceWord.getText());
                }
                else {
                    return;
                }
                index = this.textEditorArea.getText().indexOf(searchText, this.textEditorArea.getCaretPosition());
            }
        }
    }

    private void replaceWord() {
        final String searchText = this.wordToSearch.getText();
        final String textAreaText = this.textEditorArea.getText();
        final int searchLength = searchText.length();
        final int index = textAreaText.indexOf(searchText, this.textEditorArea.getCaretPosition() - searchLength);
        if (index >= 0) {
            this.textEditorArea.replaceText(index, index + searchLength, this.replaceWord.getText());
        }
    }
}