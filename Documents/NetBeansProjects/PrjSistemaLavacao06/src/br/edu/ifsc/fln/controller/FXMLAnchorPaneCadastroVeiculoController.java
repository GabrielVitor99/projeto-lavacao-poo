/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.model.domain.Motor;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btInserir;

    @FXML
    private Button btRemover;
    
    @FXML
    private Label lbVeiculoId;
    
    @FXML
    private Label lbModelo;
    
    @FXML
    private Label lbPlaca;
    
    @FXML
    private Label lbObservacoes;
    
    @FXML
    private Label lbNomeModelo;
    
    @FXML
    private TableColumn<Veiculo, String> tableColumnVeiculo;

    @FXML
    private TableView<Veiculo> tableView;
    
    private List<Veiculo> listaVeiculos;
    private List<Modelo> listaModelos;
    private ObservableList<Veiculo> observableListVeiculos;
    
    // Acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        modeloDAO.setConnection(connection);
        carregarTableView();
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }
    
    
    public void carregarTableView() {
        tableColumnVeiculo.setCellValueFactory(cellData -> {
            Veiculo veiculo = cellData.getValue();
            if (veiculo.getModelo() != null) {
                return new SimpleStringProperty(veiculo.getModelo().getDescricao());
            } else {
                return new SimpleStringProperty("");
            }
        });

        listaModelos = modeloDAO.listar();
        listaVeiculos = veiculoDAO.listar();

        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        tableView.setItems(observableListVeiculos);
    }


    
    public void selecionarItemTableView(Veiculo veiculo) {
    if (veiculo != null) {
        lbVeiculoId.setText(Integer.toString(veiculo.getId()));
        lbModelo.setText(veiculo.getModelo().getDescricao());
        lbPlaca.setText(veiculo.getPlaca());
        lbObservacoes.setText(veiculo.getObservacoes());
    } else {
        lbVeiculoId.setText("");
        lbModelo.setText("");
        lbPlaca.setText("");
        lbObservacoes.setText("");
    }
    }

    // Botão para inserir novo modeo
    @FXML
    public void handleBtInserir() throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
        if (buttonConfirmarClicked) {
            veiculoDAO.inserir(veiculo);
            carregarTableView();
        }
    }
    
    // Botão para alterar o veiculo cadastrado
    @FXML
    public void handleBtAlterar() throws IOException {
        Veiculo veiculo = tableView.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
            if (buttonConfirmarClicked) {
                veiculoDAO.alterar(veiculo);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um veiculo na Tabela.");
            alert.show();
        }
    }
    
    // Botão de excluir veiculo
    @FXML
    public void handleBtExcluir() throws IOException {
    Veiculo veiculo = tableView.getSelectionModel().getSelectedItem();
    if (veiculo != null) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza de que deseja excluir o veiculo selecionado?");
        confirmDialog.setTitle("Confirmação");
        confirmDialog.setHeaderText("Confirmar exclusão");
        
        // Caixa de confirmação de exclusão
        if (confirmDialog.showAndWait().filter(response -> response == ButtonType.OK).isPresent()) {
            // O usuário confirmou a exclusão
            veiculoDAO.remover(veiculo);
            carregarTableView();
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Por favor, escolha um veiculo na Tabela.");
        alert.show();
    }
}

    
    public boolean showFXMLAnchorPaneCadastroVeiculoDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoDialogController.class.getResource( 
            "../view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de veiculo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o produto ao controller
        FXMLAnchorPaneCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }
}
