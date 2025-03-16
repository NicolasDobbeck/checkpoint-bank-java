package com.fiap.bank.model;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ContaCorrente {
    private Long id;
    private String nome;
    private String cpf;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Double saldo;
    private TipoConta tipoConta;
    private boolean status;

    public ContaCorrente(Long id, String nome, String cpf, LocalDate date, Double saldo, TipoConta tipoConta, boolean status) {
        this.id = (id == null) ? Math.abs(new Random().nextLong()) : id;
        this.nome = isNotNull(nome);
        this.cpf = isNotNull(cpf);
        setDate(date);
        setSaldo(saldo);
        this.tipoConta = tipoConta;
        this.status = status;
    }

    private String isNotNull(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new InvalidParameterException("O valor não pode ser nulo ou vazio");
        }
        return valor;
    }

    private static boolean isDateValid(LocalDate date) {        
        return !date.isAfter(LocalDate.now());
    }

    private boolean isSaldoValid(Double saldo) { 
        if (saldo == null) {
            throw new InvalidParameterException("O valor do saldo não pode ser nulo");
        }
        return saldo > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = isNotNull(nome);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = isNotNull(cpf);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null || !isDateValid(date)) {
            throw new InvalidParameterException("Data inválida! inserir data no formato YYYY-mm-DD");
        }
        this.date = date;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        if (!isSaldoValid(saldo)) {
            throw new InvalidParameterException("Saldo inválido! Deve ser maior que zero.");
        }
        this.saldo = saldo;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
            "ContaCorrente [id=%d, nome=%s, cpf=%s, date=%s, saldo=%.2f, tipoConta=%s, status=%b]",
            id, nome, cpf, date, saldo, tipoConta, status
        );
    }
}
