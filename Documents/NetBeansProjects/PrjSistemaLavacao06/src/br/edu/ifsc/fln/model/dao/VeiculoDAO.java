package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VeiculoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    // Incluir o motor
    public boolean inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo(placa, observacoes, cor_id, modelo_id) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // Incluir o motor
    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET placa=?, observacoes=?, cor_id=?, modelo_id=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Veiculo veiculo) {
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    
    public List<Veiculo> listar() {
    String sql = "SELECT v.id AS veiculo_id, v.placa, v.observacoes, v.cor_id, v.modelo_id, " +
                 "c.nome AS cor_nome, m.modelo_descricao " +
                 "FROM veiculo v " +
                 "INNER JOIN cor c ON v.cor_id = c.id " +
                 "INNER JOIN modelo m ON v.modelo_id = m.id";

    List<Veiculo> retorno = new ArrayList<>();

    try {
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultado = stmt.executeQuery();

        while (resultado.next()) {
            Veiculo veiculo = new Veiculo();
            veiculo.setId(resultado.getInt("veiculo_id"));
            veiculo.setPlaca(resultado.getString("placa"));
            veiculo.setObservacoes(resultado.getString("observacoes"));

            Cor cor = new Cor();
            cor.setId(resultado.getInt("cor_id"));
            cor.setNome(resultado.getString("cor_nome"));

            veiculo.setCor(cor);

            Modelo modelo = new Modelo();
            modelo.setId(resultado.getInt("modelo_id"));
            modelo.setDescricao(resultado.getString("modelo_descricao"));

            veiculo.setModelo(modelo);

            retorno.add(veiculo);
        }

    } catch (SQLException ex) {
        Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
    }

    return retorno;
}

}
