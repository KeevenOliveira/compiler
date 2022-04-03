
public class Compiler {
    public static void main(String[] args) {
        Lexic lexico = new Lexic("src\\example.txt");
        Token t = null;
        while ((t = lexico.nextToken()) != null) {
            System.out.println(t.toString());
        }

    }

}
