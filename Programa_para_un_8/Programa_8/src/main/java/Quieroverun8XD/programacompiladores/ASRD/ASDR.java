
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

    //IF_STMT -> if (EXPRESSION) STATEMENT ELSE_STATEMENT
    private Statement IF_STMT() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case IF:
                match(TipoToken.IF);
                match(TipoToken.LEFT_PAREN);
                Expression condition = EXPRESSION();
                match(TipoToken.RIGHT_PAREN);
                Statement thenBranch = STATEMENT();
                Statement elseBranch = ELSE_STMT();
                return new StmtIf(condition,thenBranch,elseBranch);
            default:
                hayErrores = true;
                return null;
        }
    }

    //ELSE_STATEMENT -> else STATEMENT || EMPTY
    private Statement ELSE_STMT() {

        if(hayErrores)
            return null;

        if(preanalisis.getTipo() == TipoToken.ELSE){
            match(TipoToken.ELSE);
            return STATEMENT();
        }
        return null;

    }

    //PRINT_STMT -> print EXPRESSION ;
    private Statement PRINT_STMT() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case PRINT:
                match(TipoToken.PRINT);
                StmtPrint stmtprint = new StmtPrint(EXPRESSION());
                match(TipoToken.SEMICOLON);
                return stmtprint;
            default:
                hayErrores = true;
                return null;
        }
    }

    //RETURN_STMT -> return RETURN_EXP_OPC ;
    private Statement RETURN_STMT() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case RETURN:
                match(TipoToken.RETURN);
                Expression expr = RETURN_EXP_OPC();
                match(TipoToken.SEMICOLON);
                return new StmtReturn(expr);
            default:
                hayErrores = true;
                return null;
        }
    }

    //RETURN_EXP_OPC -> EXPRESSION || EMPTY
    private Expression RETURN_EXP_OPC() {

        if(hayErrores)
            return null;

        if (preanalisis.getTipo() == TipoToken.BANG || preanalisis.getTipo() == TipoToken.MINUS || preanalisis.getTipo() == TipoToken.FALSE || preanalisis.getTipo() == TipoToken.TRUE|| preanalisis.getTipo() == TipoToken.NULL
                || preanalisis.getTipo() == TipoToken.NUMBER || preanalisis.getTipo() == TipoToken.STRING || preanalisis.getTipo() == TipoToken.IDENTIFIER || preanalisis.getTipo() == TipoToken.LEFT_PAREN)
            return EXPRESSION();

        return null;
    }

    //WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private Statement WHILE_STMT() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case WHILE:
                match(TipoToken.WHILE);
                match(TipoToken.LEFT_PAREN);
                Expression condition = EXPRESSION();
                match(TipoToken.RIGHT_PAREN);
                Statement body = STATEMENT();
                return new StmtLoop(condition,body);
            default:
                hayErrores = true;
                return null;
        }
    }

    //BLOCK -> { DECLARATION }
    private Statement BLOCK() {

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case LEFT_BRACE:
                match(TipoToken.LEFT_BRACE);
                List<Statement> statements = new ArrayList<>();
                Statement block = new StmtBlock(DECLARATION(statements));
                match(TipoToken.RIGHT_BRACE);
                return block;
            default:
                hayErrores = true;
                return null;
        }
    }

    /*Expresiones*/
    //EXPRESSION -> ASSIGNMENT
    private Expression EXPRESSION() {

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
                return ASSIGNMENT();
            default:
                hayErrores = true;
                return null;
        }
    }

    //ASSIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private Expression ASSIGNMENT() {

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
                return ASSIGNMENT_OPC(LOGIC_OR());
            default:
                hayErrores = true;
                return null;
        }
    }

    //ASSIGNMENT_OPC -> = EXPRESSION || EMPTY
    private Expression ASSIGNMENT_OPC(Expression expr) {

        if(hayErrores)
            return null;

        if(preanalisis.getTipo() == TipoToken.EQUAL){
            Token name = previous();
            match(TipoToken.EQUAL);
            Expression val = EXPRESSION();
            return new ExprAssign(name,val);
        }

        return expr;

    }

    //LOGIC_OR -> LOGIC_AND LOGIC_OR_2
    private Expression LOGIC_OR() {

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
                return LOGIC_OR_2(LOGIC_AND());
            default:
                hayErrores = true;
                return null;
        }
    }

    //LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2 || EMPTY
    private Expression LOGIC_OR_2(Expression expr) {

        if(hayErrores)
            return null;

        if(preanalisis.getTipo() == TipoToken.OR){
            match(TipoToken.OR);
            Token operator = previous();
            Expression expr2 = LOGIC_AND();
            ExprLogical exprLogical = new ExprLogical(expr,operator,expr2);
            return LOGIC_OR_2(exprLogical);
        }

        return expr;

    }

    //LOGIC_AND -> EQUALITY LOGIC_AND_2
    private Expression LOGIC_AND() {

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
                return LOGIC_AND_2(EQUALITY());
            default:
                hayErrores = true;
                return null;
        }
    }

    //LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 || EMPTY
    private Expression LOGIC_AND_2(Expression expr) {

        if(hayErrores)
            return null;
        if(preanalisis.getTipo() == TipoToken.AND){
            match(TipoToken.AND);
            Token operator = previous();
            Expression expr2 = EQUALITY();
            ExprLogical exprLogical = new ExprLogical(expr,operator,expr2);
            return LOGIC_AND_2(exprLogical);
        }

        return expr;

    }

    //EQUALITY -> COMPARISON EQUALITY_2
    private Expression EQUALITY() {

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
                return EQUALITY_2(COMPARISON());
            default:
                hayErrores = true;
                return null;
        }
    }

    //EQUALITY_2 -> != COMPARISON EQUALITY_2 || == COMPARISON EQUALITY_2 || EMPTY
    private Expression EQUALITY_2(Expression expr) {

        Token operator;
        Expression expr2;
        ExprBinary exprBinary;

        if(hayErrores)
            return null;

        switch (preanalisis.getTipo()){
            case BANG_EQUAL:
                match(TipoToken.BANG_EQUAL);
                operator = previous();
                expr2 = COMPARISON();
                exprBinary = new ExprBinary(expr,operator,expr2);
                return EQUALITY_2(exprBinary);
            case EQUAL_EQUAL:
                match(TipoToken.EQUAL_EQUAL);
                operator = previous();
                expr2 = COMPARISON();
                exprBinary = new ExprBinary(expr,operator,expr2);
                return EQUALITY_2(exprBinary);
        }
        return expr;

    }