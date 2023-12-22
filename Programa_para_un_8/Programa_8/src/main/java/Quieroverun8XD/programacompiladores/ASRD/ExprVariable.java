package main.java.Quieroverun8XD.programacompiladores.ASRD;
import  main.java.Quieroverun8XD.programacompiladores.AnalizadorLexico.Token;

class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }
}