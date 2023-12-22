package main.java.Quieroverun8XD.programacompiladores.ASRD;
import  main.java.Quieroverun8XD.programacompiladores.AnalizadorLexico.Token;

public class ExprLogical extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

