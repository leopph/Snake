package hu.alkfejl.controller;

import hu.alkfejl.model.Result;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;


public class ScoreEditController
{
    @FXML private TextField m_NameField;
    @FXML private TextField m_ScoreField;
    @FXML private Button m_ConfirmButton;

    private Dialog<Boolean> m_Dialog;


    void start(Result result, Dialog<Boolean> dialog)
    {
        m_Dialog = dialog;

        m_NameField.textProperty().bindBidirectional(result.playerNameProperty());

        m_ScoreField.textProperty().bindBidirectional(result.scoreProperty(), new StringConverter<>()
        {
            @Override
            public String toString(Number object)
            {
                return object.toString();
            }

            @Override
            public Number fromString(String string)
            {
                return Integer.parseInt(string);
            }
        });

        m_ScoreField.textProperty().addListener((observable, newValue, oldValue) ->
        {
            if (!newValue.matches("\\d*")) {
                m_ScoreField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        m_ConfirmButton.disableProperty().bind(m_ScoreField.textProperty().isEmpty()
            .or(m_NameField.textProperty().isEmpty()));
    }


    @FXML
    private void onConfirm()
    {
        m_Dialog.setResult(true);
    }


    @FXML
    private void onCancel()
    {
        m_Dialog.setResult(false);
    }
}
