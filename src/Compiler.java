import java.io.FileNotFoundException;

public class Compiler {
    public static void main(String[] args) {
        Lexic lexic = new Lexic("src//example.txt");
        Syntactic syntactic = new Syntactic(lexic);
        try {
            syntactic.S();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}