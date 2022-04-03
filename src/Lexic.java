
// import java.io.File;
// import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
// import java.util.Scanner;
// import java.util.logging.Level;
// import java.util.logging.Logger;

public class Lexic {
    private char[] content;
    private int indiceConteudo;

    public Lexic(String caminhoCodigoFonte) {
        try {
            String conteudoStr;
            conteudoStr = new String(Files.readAllBytes(Paths.get(caminhoCodigoFonte)));
            this.content = conteudoStr.toCharArray();
            this.indiceConteudo = 0;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Retorna próximo char
    private char nextChar() {
        return this.content[this.indiceConteudo++];
    }

    // Verifica existe próximo char ou chegou ao final do código fonte
    private boolean hasNextChar() {
        return indiceConteudo < this.content.length;
    }

    // Retrocede o índice que aponta para o "char da vez" em uma unidade
    private void back() {
        this.indiceConteudo--;
    }

    // Identificar se char é letra minúscula
    // colocar maiúsculo
    private boolean isLetter(char c) {
        if ((c >= 'a') && (c <= 'z')) {
            return true;
        } else if ((c >= 'A') && (c <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }

    // Identificar se char é dígito
    private boolean isDigit(char c) {
        return (c >= '0') && (c <= '9');
    }

    // Método retorna próximo token válido ou retorna mensagem de erro.
    public Token getNextToken() {
        Token token = null;
        char character;
        int state = 0;

        StringBuffer lexeme = new StringBuffer();
        while (this.hasNextChar()) {
            character = this.nextChar();
            switch (state) {
                case 0:
                    if (character == ' ' || character == '\t' || character == '\n' || character == '\r') { // caracteres
                                                                                                           // de espaço
                                                                                                           // em branco
                                                                                                           // ASCII
                        // tradicionais
                        state = 0;
                    } else if (this.isLetter(character) || character == '_') {
                        lexeme.append(character);
                        state = 1;
                    } else if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 2;
                    } else if (character == ')' ||
                            character == '(' ||
                            character == '{' ||
                            character == '}' ||
                            character == ',' ||
                            character == ';') {
                        lexeme.append(character);
                        state = 5;
                    } else if (character == '$') {
                        lexeme.append(character);
                        state = 99;
                        this.back();
                    } else {
                        lexeme.append(character);
                        throw new RuntimeException("Erro: token inválido \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 1:
                    if (this.isLetter(character) || this.isDigit(character) || character == '_') {
                        lexeme.append(character);
                        state = 1;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.TIPO_IDENTIFICADOR);
                    }
                    break;
                case 2:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 2;
                    } else if (character == '.') {
                        lexeme.append(character);
                        state = 3;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.TIPO_INTEIRO);
                    }
                    break;
                case 3:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 4;
                    } else {
                        throw new RuntimeException("Erro: número float inválido \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 4:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 4;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.TIPO_REAL);
                    }
                    break;
                case 5:
                    this.back();
                    return new Token(lexeme.toString(), Token.TIPO_CARACTER_ESPECIAL);
                case 99:
                    return new Token(lexeme.toString(), Token.TIPO_FIM_CODIGO);
            }
        }
        return token;
    }
}
