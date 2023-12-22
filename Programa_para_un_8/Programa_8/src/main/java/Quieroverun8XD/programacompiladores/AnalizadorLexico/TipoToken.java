package main.java.Quieroverun8XD.programacompiladores.AnalizadorLexico;

// Enumeración que define los tipos de tokens posibles
public enum TipoToken {
    
    // Tokens de un sólo caracter
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // Tokens de uno o dos caracteres
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literales
    IDENTIFIER, // Identificador (nombre de variable, función, etc.)
    STRING, // Cadena de caracteres
    NUMBER, // Número

    // Palabras clave
    AND, ELSE, FALSE, FUN, FOR, IF, NULL, OR,
    PRINT, RETURN, TRUE, VAR, WHILE,

    // Token de fin de archivo
    EOF
    
}
