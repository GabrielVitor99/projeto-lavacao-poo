/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
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
public class FXMLAnchorPaneCadastroModeloController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btInserir;

    @FXML
    private Button btRemover;
    
    @FXML
    private Label lbModeloId;

    @FXML
    private Label lbMarcaModelo;
    
    @FXML
    private Label lbNomeModelo;
    
    @FXML
    private Label lbCategoria;
    
    @FXML
    private Label lbPotencia;
    
    @FXML
    private Label lbTipoCombustivel;
    
    @FXML
    private TableColumn<Modelo, String> tableColumnModelo;

    @FXML
    private TableView<Modelo> tableView;
    
    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;
    
    // Acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        carregarTableView();
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }
    
    public void carregarTableView() {
        // no Modelo.java ele retorna como descricao e não como modeo_descricao como está no banco
        tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        listaModelos = modeloDAO.listar();
        
        observableListModelos = FXCollections.observableArrayList(listaModelos);
        tableView.setItems(observableListModelos);
    }
    
    
    public void selecionarItemTableView(Modelo modelo) {
        if (modelo != null) {
            lbModeloId.setText(Integer.toString(modelo.getId()));
            lbNomeModelo.setText(modelo.getDescricao());
            lbMarcaModelo.setText(modelo.getMarca().getNome());
            lbCategoria.setText(modelo.getCategoria().toString());
            lbPotencia.setText(Integer.toString(modelo.getMotor().getPotencia()));
            lbTipoCombustivel.setText(modelo.getMotor().getTipoCombustivel().toString());
        } else {
            lbModeloId.setText("");
            lbNomeModelo.setText("");
            lbMarcaModelo.setText("");
            lbCategoria.setText("");
            lbPotencia.setText("");
            lbTipoCombustivel.setText("");
        }
    }
    

    // Botão para inserir novo modeo
    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
        if (buttonConfirmarClicked) {
            modeloDAO.inserir(modelo);
            carregarTableView();
        }
    }
    
    // Botão para alterar o modelo cadastrado
    @FXML
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableView.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
            if (buttonConfirmarClicked) {
                modeloDAO.alterar(modelo);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um modelo na Tabela.");
            alert.show();
        }
    }
    
    // Botão de excluir modelo
    @FXML
    public void handleBtExcluir() throws IOException {
    Modelo modelo = tableView.getSelectionModel().getSelectedItem();
    if (modelo != null) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza de que deseja excluir o modelo selecionado?");
        confirmDialog.setTitle("Confirmação");
        confirmDialog.setHeaderText("Confirmar exclusão");
        
        // Caixa de confirmação de exclusão
        if (confirmDialog.showAndWait().filter(response -> response == ButtonType.OK).isPresent()) {
            // O usuário confirmou a exclusão
            modeloDAO.remover(modelo);
            carregarTableView();
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Por favor, escolha um modelo na Tabela.");
        alert.show();
    }
}

    
    public boolean showFXMLAnchorPaneCadastroModeloDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloDialogController.class.getResource( 
            "../view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de modelo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o produto ao controller
        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }
}
