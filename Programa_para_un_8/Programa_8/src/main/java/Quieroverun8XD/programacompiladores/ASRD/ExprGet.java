package main.java.Quieroverun8XD.programacompiladores.ASRD;
import  main.java.Quieroverun8XD.programacompiladores.AnalizadorLexico.Token;

public class ExprGet extends Expression{
    final Expression object;
    final Token name;

    ExprGet(Expression object, Token name) {
        this.object = object;
        this.name = name;
    }
}
