public class Compiler {
    public static void main(String[] args) {
        Lexic lexic = new Lexic("src//example.txt");
        // Token token = null;
        // while ((token = lexico.getNextToken()) != null) {
        // System.out.println(token.toString());
        // }
        Syntactic syntactic = new Syntactic(lexic);
        syntactic.S();
    }
}