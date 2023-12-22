package main.java.Quieroverun8XD.programacompiladores.ASRD;

public class StmtReturn extends Statement {
    final Expression value;

    StmtReturn(Expression value) {
        this.value = value;
    }
}
