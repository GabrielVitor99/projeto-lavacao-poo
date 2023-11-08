package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Motor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeloDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

public boolean inserir(Modelo modelo) {
        String sqlModelo = "INSERT INTO modelo(modelo_descricao, marca_id, categoria) VALUES (?, ?, ?)";
        String sqlMotor = "INSERT INTO motor(id_modelo, potencia, tipoCombustivel) VALUES (?, ?, ?)";

        try {
            // Inserir o modelo
            PreparedStatement stmtModelo = connection.prepareStatement(sqlModelo, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtModelo.setString(1, modelo.getDescricao());
            stmtModelo.setInt(2, modelo.getMarca().getId());
            stmtModelo.setString(3, modelo.getCategoria().name());
            stmtModelo.executeUpdate();

        
            ResultSet generatedKeys = stmtModelo.getGeneratedKeys();
            int idModelo = -1;

   
            if (generatedKeys.next()) {
            // Se há chaves geradas, recupera o valor do ID do modelo a partir da primeira coluna
            idModelo = generatedKeys.getInt(1);
            } else {
            return false;
            }

            // Inserir o motor associado ao modelo
            PreparedStatement stmtMotor = connection.prepareStatement(sqlMotor);
            stmtMotor.setInt(1, idModelo);
            stmtMotor.setInt(2, modelo.getMotor().getPotencia());
            stmtMotor.setString(3, modelo.getMotor().getTipoCombustivel().name());
            stmtMotor.executeUpdate();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Modelo modelo) {
        String sqlModelo = "UPDATE modelo SET modelo_descricao=?, marca_id=?, categoria=? WHERE id=?";
        String sqlMotor = "UPDATE motor SET potencia=?, tipoCombustivel=? WHERE id_modelo=?";

        try {
            PreparedStatement stmtModelo = connection.prepareStatement(sqlModelo);
            stmtModelo.setString(1, modelo.getDescricao());
            stmtModelo.setInt(2, modelo.getMarca().getId());
            stmtModelo.setString(3, modelo.getCategoria().name());
            stmtModelo.setInt(4, modelo.getId());
            stmtModelo.executeUpdate();

            PreparedStatement stmtMotor = connection.prepareStatement(sqlMotor);
            stmtMotor.setInt(1, modelo.getMotor().getPotencia());
            stmtMotor.setString(2, modelo.getMotor().getTipoCombustivel().name());
            stmtMotor.setInt(3, modelo.getId());
            stmtMotor.executeUpdate();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public List<Modelo> listar() {
        String sql = "SELECT m.id AS modelo_id, m.modelo_descricao, m.marca_id, ma.nome AS marca_nome, m.categoria, " +
                     "mo.potencia AS potencia, mo.tipoCombustivel AS tipoCombustivel " +
                     "FROM modelo m " +
                     "INNER JOIN marca ma ON m.marca_id = ma.id " +
                     "INNER JOIN motor mo ON m.id = mo.id_modelo"; // Adiciona a junção com a tabela motor

        List<Modelo> retorno = new ArrayList<>();
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            
            while (resultado.next()) {
                Modelo modelo = new Modelo();
                modelo.setId(resultado.getInt("modelo_id"));
                modelo.setDescricao(resultado.getString("modelo_descricao"));
                modelo.setCategoria(Enum.valueOf(ECategoria.class, resultado.getString("categoria")));
                
                // Crie uma instância de Marca e defina seus valores
                Marca marca = new Marca();
                marca.setId(resultado.getInt("marca_id"));
                marca.setNome(resultado.getString("marca_nome"));
                
                // Associe a marca ao modelo
                modelo.setMarca(marca);
                
                // Crie uma instância de Motor e defina seus valores
                Motor motor = new Motor();
                motor.setPotencia(resultado.getInt("potencia"));
                motor.setTipoCombustivel(Enum.valueOf(ETipoCombustivel.class, resultado.getString("tipoCombustivel")));

                // Associe o motor ao modelo
                modelo.setMotor(motor);
                
                retorno.add(modelo);
            }
            
            /*
            // Imprima os modelos na lista
            for (Modelo modelo : retorno) {
                System.out.println("ID: " + modelo.getId());
                System.out.println("Descrição: " + modelo.getDescricao());
                System.out.println("Categoria: " + modelo.getCategoria());
                System.out.println("Marca: " + modelo.getMarca().getNome());
                System.out.println("Potência: " + modelo.getMotor().getPotencia());
                System.out.println("Tipo de Combustível: " + modelo.getMotor().getTipoCombustivel());
                System.out.println();
            }*/
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retorno;
    }       
  
}
