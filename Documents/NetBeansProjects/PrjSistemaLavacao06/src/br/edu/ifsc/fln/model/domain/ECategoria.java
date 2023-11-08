package br.edu.ifsc.fln.model.domain;

public enum ECategoria {
    PEQUENO(1, "Pequeno", "Small"),
    MEDIO(2, "Médio", "Medium"),
    GRANDE(3, "Grande", "Large"),
    MOTO(4, "Moto", "Motorcycle"),
    PADRAO(5, "Padrão", "Standard");

    private int id;
    private String descricao;
    private String description;

    private ECategoria(int id, String descricao, String description) {
        this.id = id;
        this.descricao = descricao;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDescription() {
        return description;
    }
}
