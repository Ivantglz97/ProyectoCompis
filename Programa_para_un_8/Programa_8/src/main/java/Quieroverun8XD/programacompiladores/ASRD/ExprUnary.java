package main.java.Quieroverun8XD.programacompiladores.ASRD;
import  main.java.Quieroverun8XD.programacompiladores.AnalizadorLexico.Token;

public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
}
