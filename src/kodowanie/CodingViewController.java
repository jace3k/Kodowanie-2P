package kodowanie;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CodingViewController implements Initializable {

    @FXML private TextField n_field;
    @FXML private TextField k_field;
    @FXML private TextField gen_field;
    @FXML private Button gen_code_words;
    @FXML private Button gen_matrixG;
    @FXML private Button gen_matrixH;
    @FXML private Button check_btn;
    @FXML private Button about;
    @FXML private Button ok_body;
    @FXML private TextArea result_field_1;
    @FXML private TextArea result_field_2;
    @FXML private TextField code_word_field;

    private Code code;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonActions();

        code = new Code();
        code.setBody(Integer.parseInt(n_field.getText()),Integer.parseInt(k_field.getText()),gen_field.getText());
    }

    private void setButtonActions() {
        about.setOnAction(event -> modalWindow("aboutView.fxml", event));

        ok_body.setOnAction(event -> {
            code.setBody(Integer.parseInt(n_field.getText()),Integer.parseInt(k_field.getText()),gen_field.getText());
            result_field_1.clear();
            result_field_2.clear();
            result_field_1.appendText("Nowe ciało utworzone.\n");
            result_field_2.appendText("Nowe ciało utworzone.\n");
            if(!code.isCodeCyclic()) result_field_1.appendText("Uwaga! Kod nie jest cykliczny.\n");

            gen_matrixG.setDisable(false);
            gen_matrixH.setDisable(true);
            gen_code_words.setDisable(true);
            check_btn.setDisable(true);
        });

        gen_matrixG.setOnAction(event -> {
            code.createMatrixG();
            result_field_1.appendText("G = \n");
            result_field_1.appendText(code.getMatrixG().present());
            gen_matrixH.setDisable(false);
            gen_matrixG.setDisable(true);
            gen_code_words.setDisable(false);
        });

        gen_matrixH.setOnAction(event -> {
            code.createMatrixH();
            result_field_1.appendText("H = \n");
            result_field_1.appendText(code.getMatrixH().present());
            result_field_2.clear();
            gen_matrixH.setDisable(true);
            check_btn.setDisable(false);
        });

        gen_code_words.setOnAction(event -> {
            code.createCodeWords();
            result_field_1.appendText("Słowa kodowe = \n");
            result_field_1.appendText(code.getCodeWords().present());
            result_field_1.appendText(code.present());
            gen_code_words.setDisable(true);
        });

        check_btn.setOnAction(event -> {
            String checked = code.checkCodeWord(new Polynominal(code_word_field.getText()));
            result_field_2.appendText(checked);
        });

        gen_field.setOnKeyReleased(event -> {
            checkGenField();
            checkBinaryTyped(gen_field, event);
        });
        n_field.setOnKeyReleased(event -> checkGenField());
        k_field.setOnKeyReleased(event -> checkGenField());
        code_word_field.setOnKeyReleased(event -> checkBinaryTyped(code_word_field, event));
    }

    private void modalWindow(String resource, Event event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(resource));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResource("icon.png").toString()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkGenField() {
        if(n_field.getText().equals("")) {
            n_field.setText("0");
            n_field.selectAll();
        }
        if(k_field.getText().equals("")) {
            k_field.setText("0");
            k_field.selectAll();
        }
        int n = tryParseInt(n_field);
        int k = tryParseInt(k_field);

        if((gen_field.getText().length() != n-k+1) || (n_field.getText().equals("0") || k_field.getText().equals("0"))) {
            ok_body.setDisable(true);
        } else {
            ok_body.setDisable(false);
        }

    }

    private void checkBinaryTyped(TextField field, KeyEvent event) {
        if(        event.getCode() != KeyCode.DIGIT0
                && event.getCode() != KeyCode.DIGIT1
                && event.getCode() != KeyCode.BACK_SPACE
                && event.getCode() != KeyCode.ENTER
                && event.getCode() != KeyCode.LEFT
                && event.getCode() != KeyCode.RIGHT
                && event.getCode() != KeyCode.UP
                && event.getCode() != KeyCode.DOWN
                ) field.setText("");
    }

    private int tryParseInt(TextField field) {
        int nOrK;
        try {
            nOrK = Integer.parseInt(field.getText());
        } catch (NumberFormatException e) {
            field.setText("0");
            field.selectAll();
            nOrK = 0;
        }
        return nOrK;
    }
}
