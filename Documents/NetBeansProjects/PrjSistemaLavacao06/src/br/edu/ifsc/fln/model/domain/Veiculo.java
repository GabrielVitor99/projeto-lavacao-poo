package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author Gustavo
 */

public class Veiculo {
    private int id;
    private String placa;
    private String observacoes;
    
    private Cor cor; // Atributo para a relação com a Cor
    private Modelo modelo; // Atributo para a relação com o Modelo
    

    public Veiculo() {
        // Construtor vazio
    }

    public Veiculo(int id, String placa, String observacoes) {
        this.id = id;
        this.placa = placa;
        this.observacoes = observacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    @Override
    public String toString() {
        return "Veiculo [id=" + id + ", placa=" + placa + ", observacoes=" + observacoes + ", cor=" + cor + ", modelo=" + modelo + "]";
    }
}
