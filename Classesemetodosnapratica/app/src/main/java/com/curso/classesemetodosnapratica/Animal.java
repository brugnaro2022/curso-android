package com.curso.classesemetodosnapratica;

class Animal {

    int tamanho;
    String cor;
    double peso;

    //Getter e Setter
    void setCor(String cor){
        // Formatacao ou validacao
        this.cor = cor;
    }

    String getCor() {
        return this.cor;
    }

    void dormir() {
        System.out.println("Dormir como um animal");
    }

    void correr() {
        System.out.println("Correr como um ");
    }
}
