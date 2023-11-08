package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private ComboBox<Modelo> cbModelo;

    @FXML
    private ComboBox<Cor> cbCor;

    @FXML
    private TextField tfPlaca;

    @FXML
    private TextArea tfObservacoes;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO(); //REMOVER, FAZER LIGACAO POR MODELO
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final CorDAO corDAO = new CorDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Veiculo veiculo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        corDAO.setConnection(connection);
        modeloDAO.setConnection(connection);
        carregarComboBoxMarcas();
        carregarComboBoxModelos();
        carregarComboBoxCores();
    }

    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;

    // ver se o erro está aqui depois
    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores;

    public void carregarComboBoxMarcas() {
        listaMarcas = marcaDAO.listar();
        observableListMarcas = FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
    }

    // ver depois porque está bugado
    public void carregarComboBoxModelos() {
        listaModelos = modeloDAO.listar();
        observableListModelos = FXCollections.observableArrayList(listaModelos);
        cbModelo.setItems(observableListModelos);
    }

    public void carregarComboBoxCores() {
        listaCores = corDAO.listar();
        observableListCores = FXCollections.observableArrayList(listaCores);
        cbCor.setItems(observableListCores);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        // dar uma atenção aqui depois
        cbModelo.getSelectionModel().select(veiculo.getModelo());
        cbCor.getSelectionModel().select(veiculo.getCor());
        tfPlaca.setText(veiculo.getPlaca());
        tfObservacoes.setText(veiculo.getObservacoes());
    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setModelo(cbModelo.getSelectionModel().getSelectedItem());
            veiculo.setCor(cbCor.getSelectionModel().getSelectedItem());
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacoes(tfObservacoes.getText());
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

        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma Marca!\n";
        }

        if (cbModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um Modelo!\n";
        }

        if (cbCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma Cor!\n";
        }

        if (tfPlaca.getText() == null || tfPlaca.getText().isEmpty()) {
            errorMessage += "Placa inválida!\n";
        }
        
        if (tfObservacoes.getText() == null || tfPlaca.getText().isEmpty()) {
            errorMessage += "Observação inválida!\n";
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
