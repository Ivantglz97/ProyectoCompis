
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
       //PROGRAM -> DECLARATION
    private List<Statement> PROGRAM() {

        List<Statement> stmts = new ArrayList<>();

        if(hayErrores)
            return null;

        return DECLARATION(stmts);

    }

       /*Declaraciones*/
    //DECLARATION -> FUN_DECL DECLARATION || VAR_DECL DECLARATION || STATEMENT DECLARATION || EMPTY
    private List<Statement> DECLARATION(List<Statement> stmts) {

        Statement statement;

        if(hayErrores)
            return null;

        switch(preanalisis.getTipo()){
            case FUN:
                statement = FUN_DECL();
                stmts.add(statement);
                return DECLARATION(stmts);
            case VAR:
                statement = VAR_DECL();
                stmts.add(statement);
                return DECLARATION(stmts);
            case BANG:
            case MINUS:
            case FALSE:
            case TRUE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
            case PRINT:
            case FOR:
            case IF:
            case RETURN:
            case WHILE:
            case LEFT_BRACE:
                statement = STATEMENT();
                stmts.add(statement);
                return DECLARATION(stmts);
        }
        return stmts;
    }
        //FUN_DECL -> fun FUNCTION
    private Statement FUN_DECL() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case FUN:
                match(TipoToken.FUN);
                return FUNCTION();
            default:
                hayErrores = true;
                return null;
        }
    }

    //VAR_DECL -> var id VAR_INIT ;
    private Statement VAR_DECL() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case VAR:
                match(TipoToken.VAR);
                match(TipoToken.IDENTIFIER);
                Token name = previous();
                Expression init = VAR_INIT();
                match(TipoToken.SEMICOLON);
                return new StmtVar(name,init);
            default:
                hayErrores = true;
                return null;
        }
    }

       //VAR_INIT -> = EXPRESSION || EMPTY
    private Expression VAR_INIT() {

        if(hayErrores)
            return null;

        if(preanalisis.getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            return EXPRESSION();
        }

        return null;

    }

        /*Sentencias*/
    //STATEMENT -> EXPR_STMT || FOR_STMT || IF_STMT || PRINT_STMT || RETURN_STMT || WHILE_STMT || BLOCK
    private Statement STATEMENT() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case BANG:
            case MINUS:
            case FALSE:
            case TRUE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
                return EXPR_STMT();
            case FOR:
                return FOR_STMT();
            case IF:
                return IF_STMT();
            case PRINT:
                return PRINT_STMT();
            case RETURN:
                return RETURN_STMT();
            case WHILE:
                return WHILE_STMT();
            case LEFT_BRACE:
                return BLOCK();
            default:
                hayErrores = true;
                return null;
        }
    }
        //EXPR_STMT -> EXPRESSION ;
    private Statement EXPR_STMT() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case BANG:
            case MINUS:
            case FALSE:
            case TRUE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
                Expression expr = EXPRESSION();
                match(TipoToken.SEMICOLON);
                return new StmtExpression(expr);
            default:
                hayErrores = true;
                return null;
        }
    }
        //FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Statement FOR_STMT() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case FOR:
                match(TipoToken.FOR);
                match(TipoToken.LEFT_PAREN);
                Statement init = FOR_STMT_1();
                Expression con = FOR_STMT_2();
                Expression inc = FOR_STMT_3();
                match(TipoToken.RIGHT_PAREN);
                Statement body = STATEMENT();

                if(inc != null)
                    body = new StmtBlock(Arrays.asList(body,new StmtExpression(inc)));

                if(con == null)
                    con = new ExprLiteral(true);

                body = new StmtLoop(con,body);

                if(init != null)
                    body = new StmtBlock(Arrays.asList(init,body));

                return body;
            default:
                hayErrores = true;
                return null;
        }
    }

        //FOR_STMT_1 -> VAR_DECL || EXPR_STMT || ;
    private Statement FOR_STMT_1() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case VAR:
                return VAR_DECL();
            case BANG:
            case MINUS:
            case FALSE:
            case TRUE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
                return EXPR_STMT();
            case SEMICOLON:
                match(TipoToken.SEMICOLON);
                return null;
            default:
                hayErrores = true;
                return null;
        }
    }

    //FOR_STMT_2 -> EXPRESSION; || ;
    private Expression FOR_STMT_2() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case BANG:
            case MINUS:
            case FALSE:
            case TRUE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
                Expression expr = EXPRESSION();
                match(TipoToken.SEMICOLON);
                return expr;
            case SEMICOLON:
                match(TipoToken.SEMICOLON);
                return null;
            default:
                hayErrores = true;
                return null;
        }
    }
       //FOR_STMT_3 -> EXPRESSION || EMPTY
    private Expression FOR_STMT_3() {

        if(hayErrores)
            return null;
        if(preanalisis.getTipo() == TipoToken.BANG || preanalisis.getTipo() == TipoToken.MINUS || preanalisis.getTipo() == TipoToken.FALSE || preanalisis.getTipo() == TipoToken.TRUE|| preanalisis.getTipo() == TipoToken.NULL
                || preanalisis.getTipo() == TipoToken.NUMBER || preanalisis.getTipo() == TipoToken.STRING || preanalisis.getTipo() == TipoToken.IDENTIFIER || preanalisis.getTipo() == TipoToken.LEFT_PAREN)
            return EXPRESSION();


        return null;
    }