/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;

/**
 *
 * @author gabrielvitor
 */
public abstract class Cliente extends Object{
    
    private int id;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDate dataCadastro;
    private LocalDate dataNascimento;
    private String cpfCnpj;
    
    private PessoaFisica PessoaFisica;
    private PessoaJuridica PessoaJuridica;
    
    
    public Cliente(){
        //Vazio
    }
    
    public Cliente(String nome, String cpfCnpj, String telefone, String endereco, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.telefone = telefone;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public PessoaFisica getPessoaFisica() {
        return PessoaFisica;
    }

    public void setPessoaFisica(PessoaFisica PessoaFisica) {
        this.PessoaFisica = PessoaFisica;
    }

    public PessoaJuridica getPessoaJuridica() {
        return PessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridica PessoaJuridica) {
        this.PessoaJuridica = PessoaJuridica;
    }
    
}
