package main.java.Quieroverun8XD.programacompiladores.ASRD;

import java.util.List;

public class StmtBlock extends Statement{
    final List<Statement> statements;

    StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }
}
