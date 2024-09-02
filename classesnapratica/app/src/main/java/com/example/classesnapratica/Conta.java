package com.example.classesnapratica;

public class Conta {

    /* Modificadores de acesso
       public -> todas as classes terão acesso
       private -> apenas pode ser acessado de dentro da classe
       protected -> pode ser acessado estando dentro do mesmo pacote e/ou subclasses
       default -> Caso não tenha sido definido nenhum modificador, permitindo o acesso dentro do pacote
    **/

    protected int numeroConta;
    private double saldo = 100;

    public double recuperarSaldo() {
        return this.saldo;
    }

    public void depositar(double valorDeposito) {
        this.saldo = this.saldo + valorDeposito;
    }

    public void sacar(double valorSaque) {
        this.saldo = this.saldo - valorSaque;
    }
}
