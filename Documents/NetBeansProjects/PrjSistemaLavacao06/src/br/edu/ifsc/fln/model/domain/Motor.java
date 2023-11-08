package br.edu.ifsc.fln.model.domain;

public class Motor {
    private int potencia;
    private ETipoCombustivel tipoCombustivel;
    private Modelo modelo; // Ligação ao modelo

    public Motor() {
        this.potencia = 0; // Configura a potência no construtor padrão
        this.tipoCombustivel = ETipoCombustivel.GASOLINA; // Configura o tipo de combustível no construtor padrão
    }

    // Construtor com parâmetros para criar um objeto Motor com potência e tipo de combustível
    public Motor(int potencia, ETipoCombustivel tipoCombustivel) {
        this.potencia = potencia;
        this.tipoCombustivel = tipoCombustivel;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public ETipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(ETipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
}
