import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class Calculator extends CalculatorBaseListener {

    Deque<Integer> numbers = new ArrayDeque<>();

    @Override
    public void exitExpression(CalculatorParser.ExpressionContext ctx) {
        System.out.println("exitExpression: " + ctx.getText());
        Integer value = numbers.pop();
        for (int i = 1; i < ctx.getChildCount(); i += 2) {
            String operator = ctx.getChild(i).getText();
            Integer operand = numbers.pop();
            switch (operator) {
                case "+":
                    value += operand;
                    break;
                case "-":
                    value -= operand;
                    break;
                case "*":
                    value *= operand;
                    break;
                case "/":
                    value /= operand;
                    break;
            }
        }
        numbers.add(value);
        super.exitExpression(ctx);
    }

    @Override
    public void exitIntegralExpression(CalculatorParser.IntegralExpressionContext ctx) {
        System.out.println("exitIntegralExpression: " + ctx.getText());
        if (ctx.MINUS() != null) {
            numbers.add(-1 * Integer.valueOf(ctx.INT().toString()));
        } else {
            numbers.add(Integer.valueOf(ctx.INT().toString()));
        }
        super.exitIntegralExpression(ctx);
    }

    @Override
    public void exitMultiplyingExpression(CalculatorParser.MultiplyingExpressionContext ctx) {
        System.out.println("exitMultiplyingExpression: " + ctx.getText());
        Integer value = numbers.pop();
        for (int i = 1; i < ctx.getChildCount(); i += 2) {
            String operator = ctx.getChild(i).toString();
            Integer operand = numbers.pop();
            switch (operator) {
                case "*":
                    value *= operand;
                    break;
                case "/":
                    value /= operand;
                    break;
            }
        }
        numbers.add(value);
        super.exitMultiplyingExpression(ctx);
    }

    private Integer getResult() {
        return numbers.peek();
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        System.out.println("exitEveryRule: " + ctx.getText());
        super.exitEveryRule(ctx);
    }

    public static void main(String[] args) throws Exception {
        Integer result = calc("1 + 2 * 3 - 4 / 2");
        System.out.println("Result = " + result);
    }

    public static Integer calc(String expression) {
        return calc(CharStreams.fromString(expression));
    }

    public static Integer calc(CharStream charStream) {
        CalculatorLexer lexer = new CalculatorLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.expression();

        ParseTreeWalker walker = new ParseTreeWalker();
        Calculator mainListener = new Calculator();
        walker.walk(mainListener, tree);
        return mainListener.getResult();
    }

}