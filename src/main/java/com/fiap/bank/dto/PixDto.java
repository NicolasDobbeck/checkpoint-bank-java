package com.fiap.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PixDto {
    @JsonProperty("idOrigem")
    private Long idOrigem;
    @JsonProperty("idDestino")
    private Long idDestino;
    @JsonProperty("valor")
    private double valor;


    public Long getIdOrigem() {
        return idOrigem;
    }
    public void setIdOrigem(Long idOrigem) {
        this.idOrigem = idOrigem;
    }
    public Long getIdDestino() {
        return idDestino;
    }
    public void setIdDestino(Long idDestino) {
        this.idDestino = idDestino;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    
}
