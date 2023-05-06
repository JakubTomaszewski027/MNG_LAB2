import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class Calculator extends CalculatorBaseListener
{

    Deque<Integer> numbers = new ArrayDeque<>();

    @Override
    public void exitExpression(CalculatorParser.ExpressionContext ctx)
    {
        System.out.println("exitExpression: "+ctx.getText());
        Integer value = numbers.pop();
        for (int i = 1; i < ctx.getChildCount(); i = i+2)
        {
            if (Objects.equals(ctx.getChild(i).getText(), "+"))
            {
                value = value + numbers.pop();
            } else {
                value = value - numbers.pop();
            }
        }
        numbers.add(value);
        super.exitExpression(ctx);
    }

    @Override
    public void exitIntegralExpression(CalculatorParser.IntegralExpressionContext ctx)
    {
        System.out.println("exitIntegralExpression: "+ctx.getText());
        if (ctx.MINUS() != null){
            numbers.add(-1 * Integer.valueOf(ctx.INT().toString()));
        } else {
            numbers.add(Integer.valueOf(ctx.INT().toString()));
        }
        super.exitIntegralExpression(ctx);
    }

    private Integer getResult()
    {
        return numbers.peek();
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx)
    {
        System.out.println("exitEveryRule: "+ctx.getText());
        super.exitEveryRule(ctx);
    }





    public static void main(String[] args) throws Exception
    {
      //  CharStream charStreams = CharStreams.fromFileName("./example.txt");
        Integer result = calc("1 + -2 - 3");
        System.out.println("Result = " + result);
    }

    public static Integer calc(String expression)
    {
        return calc(CharStreams.fromString(expression));
    }

    public static Integer calc(CharStream charStream)
    {
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
