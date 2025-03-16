package com.fiap.bank.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransacaoDao {
    @JsonProperty("id")
    private Long idConta;
    @JsonProperty("valor")
    private double valor;

    public Long getIdConta() {
        return idConta;
    }
    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    
}