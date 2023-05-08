import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Run {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromString("1+4*(5-1)/2^2");
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.input();

        CalculatorBaseVisitorImpl calcVisitor = new CalculatorBaseVisitorImpl();
        Double result = calcVisitor.visit(tree);
        System.out.println("Result: " + result);
    }
}