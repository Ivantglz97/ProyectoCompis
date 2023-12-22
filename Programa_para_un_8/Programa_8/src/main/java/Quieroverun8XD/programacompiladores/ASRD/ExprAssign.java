package main.java.Quieroverun8XD.programacompiladores.ASRD;
import  main.java.Quieroverun8XD.programacompiladores.AnalizadorLexico.Token;

public class ExprAssign extends Expression{
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }
}
