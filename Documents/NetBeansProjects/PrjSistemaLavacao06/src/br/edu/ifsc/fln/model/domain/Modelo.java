package br.edu.ifsc.fln.model.domain;

public class Modelo {
    private int id;
    private String descricao;
    private ECategoria categoria; // Ligação a categoria
    private Motor motor; // Ligação ao motor
    private Marca marca; // Ligação à marca

    public Modelo() {
        this.createMotor();
    }

    private void createMotor() {
        this.motor = new Motor();
        this.motor.setModelo(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    
    @Override
    public String toString(){
        return descricao;
    }
}
