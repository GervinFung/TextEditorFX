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
        Platform.runLater(() -> {
            if (textEditorArea.getSearch() == null) {
                textEditorArea.updateSearchDialog(new Search(textEditorArea));
            } else if (textEditorArea.getSearch().isIconified()) {
                textEditorArea.getSearch().setIconified(false);
            } else {
                textEditorArea.getSearch().toFront();
            }
        });
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

        this.setOnCloseRequest(e -> this.textEditorArea.updateSearchDialog(null));
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
            final int index = this.findPreviousWordIndex();
            if (index >= 0) {
                this.findWord(index);
            } else {
                final int caretPos = this.textEditorArea.getCaretPosition();
                this.textEditorArea.setCaretPostAtEnd();
                final int anotherIndex = this.findPreviousWordIndex();
                if (anotherIndex >= 0) {
                    this.findWord(anotherIndex);
                } else {
                    this.textEditorArea.positionCaret(caretPos);
                    this.textEditorArea.displayDialog("No such keyword", "Warning");
                }
            }
        });
        final Button nextButton = new PaneButton("Search Next");
        nextButton.setOnAction(e -> {
            final int index = this.findNextWordIndex();
            if (index >= 0) {
                this.findWord(index);
            } else {
                final int caretPos = this.textEditorArea.getCaretPosition();
                this.textEditorArea.setCarePostAtBegin();
                final int anotherIndex = this.findNextWordIndex();
                if (anotherIndex >= 0) {
                    this.findWord(anotherIndex);
                } else {
                    this.textEditorArea.positionCaret(caretPos);
                    this.textEditorArea.displayDialog("No such keyword", "Warning");
                }
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

    private int findPreviousWordIndex() {
        final String searchText = this.wordToSearch.getText();
        return this.textEditorArea.getText().lastIndexOf(searchText, this.textEditorArea.getCaretPosition() - searchText.length() - 1);
    }

    private int findNextWordIndex() {
        final String searchText = this.wordToSearch.getText();
        return this.textEditorArea.getText().indexOf(searchText, this.textEditorArea.getCaretPosition());
    }

    private void findWord(final int index) {
        if (index == -1) {
            throw new IllegalArgumentException("Index cannot be less than 0");
        }
        this.textEditorArea.selectRange(index, index + this.wordToSearch.getText().length());
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