
public class Compiler {
    public static void main(String[] args) {
        Lexic lexico = new Lexic("src\\example.txt");
        Token token = null;
        while ((token = lexico.getNextToken()) != null) {
            System.out.println(token.toString());
        }

    }

}