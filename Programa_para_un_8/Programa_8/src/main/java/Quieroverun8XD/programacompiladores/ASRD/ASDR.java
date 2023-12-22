
package main.java.Quieroverun8XD.programacompiladores.ASRD;
import main.java.Quieroverun8XD.programacompiladores.AnalizadorLexico.TipoToken;
import  main.java.Quieroverun8XD.programacompiladores.AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASDR implements Parser{

    private int i = 0;
    private Token preanalisis;
    private final List<Token> tokens;
    private boolean hayErrores = false;

    public ASDR(List<Token> tokens){

        this.tokens = tokens;
        preanalisis = tokens.get(i);

    }
       //Gramatica
    @Override
    public boolean parse() {

        List<Statement> arbol = PROGRAM();

        if(preanalisis.getTipo() == TipoToken.EOF && !hayErrores){
            System.out.println("Funciona");
            return  true;
        }
        else
            System.out.println("No funciona");

        return false;

    }