package ru.amalnev.jnmsweb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JnmsWebApplicationTests
{

    @Test
    public void contextLoads()
    {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(" T(ru.amalnev.jnms.common.entities.DisplayName).class");
        Object result = exp.getValue();
        //List fields = (List) exp.getValue();
        System.out.println(result);
    }

}
