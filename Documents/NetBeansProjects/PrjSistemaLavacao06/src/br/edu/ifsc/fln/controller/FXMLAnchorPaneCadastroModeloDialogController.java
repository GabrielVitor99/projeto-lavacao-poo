package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Marca> cbMarca;
   
    @FXML
    private TextField tfModeloNome;
   
    @FXML
    private ChoiceBox<ECategoria> cbCategoria;

    @FXML
    private Spinner spPotencia;
   
    @FXML
    private ChoiceBox<ETipoCombustivel> cbTipoCombustivel;
   
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
   
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Modelo modelo;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarComboBoxMarcas();
        carregarSpinner();
        carregarChoiceBoxCategoria();
        carregarChoiceBoxCombustivel();
        setFocusLostHandle();
    }

    private void setFocusLostHandle() {
        tfModeloNome.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // focus lost
                if (tfModeloNome.getText() == null || tfModeloNome.getText().isEmpty()) {
                    tfModeloNome.requestFocus();
                }
            }
        });
    }
   
    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;
   
    public void carregarComboBoxMarcas() {
        listaMarcas = marcaDAO.listar();
        observableListMarcas = FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
        cbMarca.getSelectionModel().select(0);
    }
   
    public void carregarChoiceBoxCategoria() {
        cbCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
        cbCategoria.getSelectionModel().select(4);
    }
   
    public void carregarChoiceBoxCombustivel() {
        cbTipoCombustivel.setItems(FXCollections.observableArrayList(ETipoCombustivel.values()));
        cbTipoCombustivel.getSelectionModel().select(0);
    }
   
    public void carregarSpinner(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2000, 100);
        spPotencia.setValueFactory(valueFactory);
    }
   
    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }
   
    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        tfModeloNome.setText(modelo.getDescricao());
        cbMarca.getSelectionModel().select(modelo.getMarca());
        modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
        spPotencia.getValueFactory().setValue(modelo.getMotor().getPotencia());
        cbTipoCombustivel.getSelectionModel().select(modelo.getMotor().getTipoCombustivel());
    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(tfModeloNome.getText());
            modelo.setMarca(cbMarca.getSelectionModel().getSelectedItem());
            modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            modelo.getMotor().setPotencia((int) spPotencia.getValue());
            modelo.getMotor().setTipoCombustivel(cbTipoCombustivel.getValue());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }
   
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (tfModeloNome.getText() == null || tfModeloNome.getText().isEmpty()) {
            errorMessage += "Modelo inválido!\n";
        }

        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma Marca!\n";
        }

        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma Categoria!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}