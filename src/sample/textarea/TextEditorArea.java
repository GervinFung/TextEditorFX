package sample.textarea;

import javafx.application.Platform;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

import static sample.textarea.KeyBoardKey.KEYBOARD_KEY;

public final class TextEditorArea extends TextArea {

    private final static FileChooser FILE_CHOOSER = new FileChooser();
    private final static Clipboard CLIPBOARD = Clipboard.getSystemClipboard();
    private final Stage stage;
    private String currentFilePath, currentContent;

    public TextEditorArea(final Stage stage, final PositionCaret positionCaret) {
        this(stage, positionCaret, null, "");
    }

    private TextEditorArea(final Stage stage, final PositionCaret positionCaret, final String currentFilePath, final String currentContent) {
        this.stage = stage;
        this.currentFilePath = currentFilePath;
        this.currentContent = currentContent;
        this.setText(this.currentContent);
        this.setWrapText(true);
        this.getCombinationOfKeys();
        this.setBorder(Border.EMPTY);
        this.getStylesheets().add(this.getCSSFile());
        this.positionObserve(positionCaret);
    }

    private void positionObserve(final PositionCaret positionCaret) {
        this.caretPositionProperty().addListener((observableValue, oldVal, newVal) -> {
            try {
                final int caretPos = this.getCaretPosition();
                final List<String> contents = this.separateByNewLine();
                final int maxLength = contents.size();
                final Map<Integer, Integer> lineOffset = this.observeLineOffset(contents, maxLength);
                final int row = getLineNumber(lineOffset, caretPos), column = caretPos - lineOffset.get(row) + contents.get(row - 1).length();
                positionCaret.updatePosition(row, column);
            } catch (final NullPointerException e) {
                positionCaret.updatePosition(1, 1);
            }
        });
    }

    private List<String> separateByNewLine() {
        final String content = this.getText();
        final List<String> contents = new ArrayList<>();
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = content.length() - 1;
        for (int i = 0; i <= length; i++) {
            final char c = content.charAt(i);
            if (c == '\n') {
                contents.add(stringBuilder.toString());
                stringBuilder.setLength(0);
                if (i == length) {
                    contents.add("\n");
                } else {
                    stringBuilder.append(c);
                }
            } else if (i == length) {
                stringBuilder.append(c);
                contents.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            } else {
                stringBuilder.append(c);
            }
        }
        return Collections.unmodifiableList(contents);
    }
    
    private static int getLineNumber(final Map<Integer, Integer> lineOffset, final int caretPos) {
        for (final Map.Entry<Integer, Integer> entry : lineOffset.entrySet()) {
            if (caretPos <= entry.getValue()) {
                return entry.getKey();
            }
        }
        return 0;
    }

    private Map<Integer, Integer> observeLineOffset(final List<String> contents, final int maxLength) {
        final Map<Integer, Integer> lineOffset = new HashMap<>();
        int cumulativeLength = 0;
        for (int i = 0; i < maxLength; i++) {
            final String content = contents.get(i);
            cumulativeLength += content.length();
            lineOffset.putIfAbsent(i + 1, cumulativeLength);
        }
        return Collections.unmodifiableMap(lineOffset);
    }

    private String getCSSFile() {
        final URL url = this.getClass().getResource("TextEditorArea.css");
        if (url == null) {
            throw new IllegalStateException("CSS File is Missing!");
        }
        return url.toExternalForm();
    }

    private void getCombinationOfKeys() {
        final KeyCombination newFile = KeyCombination.keyCombination(KEYBOARD_KEY.NEW_FILE);
        final KeyCombination newWindow = KeyCombination.keyCombination(KEYBOARD_KEY.NEW_WINDOW);
        final KeyCombination openFile = KeyCombination.keyCombination(KEYBOARD_KEY.OPEN_FILE);
        final KeyCombination openWindow = KeyCombination.keyCombination(KEYBOARD_KEY.OPEN_WINDOW);
        final KeyCombination saveFile =  KeyCombination.keyCombination(KEYBOARD_KEY.SAVE_FILE);
        final KeyCombination saveAs = KeyCombination.keyCombination(KEYBOARD_KEY.SAVE_AS);
        final KeyCombination printFile = KeyCombination.keyCombination(KEYBOARD_KEY.PRINT_FILE);
        final KeyCombination quit = KeyCombination.keyCombination(KEYBOARD_KEY.QUIT_FILE);

        final KeyCombination showDate = KeyCombination.keyCombination(KEYBOARD_KEY.WRITE_DATE);
        final KeyCombination showComputer = KeyCombination.keyCombination(KEYBOARD_KEY.WRITE_COMPUTER_NAME);
        final KeyCombination increaseFont = KeyCombination.keyCombination(KEYBOARD_KEY.INCREASE_FONT);
        final KeyCombination decreaseFont = KeyCombination.keyCombination(KEYBOARD_KEY.DECREASE_FONT);
        final KeyCombination findWord = KeyCombination.keyCombination(KEYBOARD_KEY.FIND_WORD);
        final KeyCombination gotoLine = KeyCombination.keyCombination(KEYBOARD_KEY.GOTO);
        final KeyCombination findNext = KeyCombination.keyCombination(KEYBOARD_KEY.FIND_NEXT);
        final KeyCombination findPrevious = KeyCombination.keyCombination(KEYBOARD_KEY.FIND_PREVIOUS);
        final KeyCombination replaceWord = KeyCombination.keyCombination(KEYBOARD_KEY.REPLACE);
        final KeyCombination replaceAllWord = KeyCombination.keyCombination(KEYBOARD_KEY.REPLACE_ALL);

        this.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (newFile.match(e)) {
                this.createNewFile();
            } else if (newWindow.match(e)) {
                this.createNewWindow();
            } else if (openFile.match(e)) {
                this.openFile();
            } else if (openWindow.match(e)) {
                this.openFileInNewWindow();
            } else if (saveFile.match(e)) {
                this.saveFile();
            } else if (saveAs.match(e)) {
                this.saveAs();
            } else if (printFile.match(e)) {
                this.printFile();
            } else if (quit.match(e)) {
                this.quitApplication();
            } else if (showDate.match(e)) {
                this.writeDate();
            } else if (showComputer.match(e)) {
                this.writeSystemName();
            } else if (increaseFont.match(e)) {
                this.increaseFontSize();
            } else if (decreaseFont.match(e)) {
                this.decreaseFontSize();
            } else if (findWord.match(e) || findNext.match(e) || findPrevious.match(e) || replaceWord.match(e) || replaceAllWord.match(e)) {
                this.findReplace();
            } else if (gotoLine.match(e)) {
                this.goToLine();
            }
        });
    }

    public void cutText() {
        if (!this.checkStringNullOrEmpty()) { this.cut(); }
    }

    public void copyText() {
        if (!this.checkStringNullOrEmpty()) { this.copy(); }
    }

    public void pasteText() {
        if (!(CLIPBOARD.getContentTypes().isEmpty() || null == CLIPBOARD.getContentTypes())) { this.paste(); }
    }

    public void selectAllText() {
        if (!this.checkStringNullOrEmpty()) { this.selectAll(); }
    }

    public void undoText() {
        this.undo();
    }

    public void redoText() {
        this.redo();
    }

    public void deleteSelectedText() {
        if (!this.checkStringNullOrEmpty()) { this.deleteText(this.getSelection()); }
    }

    public void createNewFile() {
        if (this.checkStringNullOrEmpty()) {
            final Dialog<String> dialog = new Dialog<>();
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("New File");
            dialog.initOwner(this.stage);
            dialog.setContentText("A new file is already created!");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
            return;
        }
        final Alert alert = this.createAlertDialog("New File", "Do you want to save before opening new file?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.CANCEL) { return; }
            if (type == ButtonType.YES) {
                final File file = FILE_CHOOSER.showSaveDialog(this.stage);
                if (file == null) { return; }
                this.save(file.getAbsolutePath(), file.getName());
            }
            this.setText("");
        });
    }

    public void createNewWindow() { Main.startNewInstance(); }

    public void saveAs() {
        final File file = FILE_CHOOSER.showSaveDialog(this.stage);
        if (file == null) { return; }
        final String absolutePath = file.getAbsolutePath(), fileName = file.getName();
        this.save(absolutePath, fileName);
    }

    public void saveFile() { this.save(this.currentFilePath, null); }

    private void save(final String absolutePath, final String fileName) {
        if (absolutePath == null) {
            this.saveAs();
            return;
        }
        try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absolutePath, false))) {

            final String text = this.getText();

            bufferedWriter.write(text);
            bufferedWriter.flush();

            this.currentFilePath = absolutePath;
            if (fileName != null) { this.stage.setTitle(fileName); }
            this.currentContent = text;
            this.displayDialog("File Successfully Saved!", "Save File");

        } catch (final IOException e) { this.displayErrorMessage(e); }
    }

    public void openFile() {
        if (this.checkStringNullOrEmpty() || this.getText().equals(this.currentContent)) {
            this.readFile();
            return;
        }
        final Alert alert = this.createAlertDialog("Open File", "Do you want to save before opening saved file?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.CANCEL) { return; }
            if (type == ButtonType.YES) { this.saveFile(); }
            this.readFile();
        });
    }

    private void readFile() {
        final File file = FILE_CHOOSER.showOpenDialog(this.stage);

        if (file == null) { return; }

        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            final StringBuilder stringBuilder = new StringBuilder();
            final List<String> allLines = bufferedReader.lines().collect(Collectors.toList());
            final int size = allLines.size() - 1;
            for (int i = 0; i <= size; i++) {
                if (i == size) {
                    stringBuilder.append(allLines.get(i));
                } else {
                    stringBuilder.append(allLines.get(i)).append("\n");
                }
            }
            this.currentFilePath = file.getAbsolutePath();
            this.stage.setTitle(file.getName());
            this.currentContent = stringBuilder.toString();
            this.setText(this.currentContent);

            return;

        } catch (final IOException exception) { this.displayErrorMessage(exception); }

        throw new IllegalStateException("Should not reach here");
    }

    public void openFileInNewWindow() {
        Platform.runLater(() -> {
            final Stage stage = new Stage();
            final PositionCaret positionCaret = new PositionCaret();
            final TextEditorArea textEditorArea = new TextEditorArea(stage, positionCaret);
            textEditorArea.readFile();
            Main.openNewInstance(stage, textEditorArea, positionCaret);
        });
    }

    public void printFile() {
        final PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) { return; }
        if (job.printPage(this)) {
            job.endJob();
        } else {
            this.displayDialog("Printing Failed!", "Printing Error");
        }
    }

    public void quitApplication() {
        if (this.checkStringNullOrEmpty() || this.getText().equals(this.currentContent)) {
            final Alert alert = this.createAlertDialog("Quit Application", "Do you want to quit application?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(type -> {
                if (type == ButtonType.YES) { this.stage.close();}
            });
            return;
        }
        saveFileBeforeQuit();
    }

    private void saveFileBeforeQuit() {
        final Alert alert = this.createAlertDialog("Quit Application", "Do you want to save before quitting application?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.CANCEL) { return; }
            if (type == ButtonType.YES) {
                if (this.currentFilePath == null) { this.saveAs(); }
                else { this.saveFile(); }
            }
            this.stage.close();
        });
    }

    public void writeDate() { this.appendText(new Date().toString()); }

    public void writeSystemName() {
        try { this.appendText(InetAddress.getLocalHost().getHostName()); }
        catch (final UnknownHostException e) { this.displayErrorMessage(e); }
    }

    public void increaseFontSize() { this.setFont(Font.font(this.getFont().getSize() + 1)); }
    public void decreaseFontSize() { this.setFont(Font.font(this.getFont().getSize() - 1)); }
    public void setFontSize(final int size) { this.setFont(Font.font(size)); }

    public void goToLine() {
        final List<String> texts = this.separateByNewLine();
        final int maxLength = texts.size();
        final String contentMsg;
        if (maxLength == 0) {
            contentMsg = "There are no line available";
            this.displayDialog(contentMsg, "No Line Available");
            return;
        } else {
            contentMsg =  "Please enter " + (maxLength == 1 ? "1 only" : "from 1 to " + maxLength + " only");
        }
        final TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.initModality(Modality.WINDOW_MODAL);
        textInputDialog.initOwner(this.stage);
        textInputDialog.setContentText(contentMsg);
        final Map<Integer, Integer> lineOffset = this.getLines(texts, maxLength);
        textInputDialog.showAndWait().ifPresentOrElse(result -> {
            if (tryParseIntInRange(result, maxLength)) {
                this.positionCaret(lineOffset.get(Integer.parseInt(result)));
                textInputDialog.close();
            } else {
                this.displayDialog(contentMsg, "Input Error");
            }
        }, textInputDialog::close);
    }

    private Map<Integer, Integer> getLines(final List<String> texts, final int maxLength) {
        final Map<Integer, Integer> lineOffset = new HashMap<>();
        int cumulativeLength = 0;
        for (int i = 0; i < maxLength; i++) {
            lineOffset.putIfAbsent(i + 1, cumulativeLength);
            cumulativeLength += texts.get(i).length() + 1;
        }
        return Collections.unmodifiableMap(lineOffset);
    }

    private static boolean tryParseIntInRange(final String data, final int max) {
        try {
            final int temp = Integer.parseInt(data);
            if (temp >= 1 && temp <= max) {
                return true;
            }
        } catch (final NumberFormatException ignored) {}
        return false;
    }

    public void findReplace() {
        Search.startSearch(this);
    }

    private void displayErrorMessage(final Exception e) {
        this.displayDialog(e.getMessage(), "Error");
    }

    private void displayDialog(final String message, final String title) {
        final Dialog<String> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(this.stage);
        dialog.setContentText(message);
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setResizable(false);
        dialog.showAndWait();
    }

    private Alert createAlertDialog(final String title, final String message) {
        final Alert alert = new Alert(null);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(this.stage);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert;
    }

    public boolean checkStringNullOrEmpty() {
        final String text = this.getText();
        return text == null || text.isEmpty();
    }
}