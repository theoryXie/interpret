package whu.se.interpret.implTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import whu.se.interpret.InterpretApplicationTests;
import whu.se.interpret.po.Token;
import whu.se.interpret.service.Lexer;
import whu.se.interpret.service.impl.LexerImpl;

import java.util.List;

/**
 * @author xsy
 * @description: 词法分析测试
 * @date 2019/9/25 22:20
 */
public class LexerImplTest extends InterpretApplicationTests {

    @Autowired
    LexerImpl lexerImpl;

    /**
     * 测试词法分析的接口
     *
     * @author xsy
     **/
    @Test
    public void testLexer(){
        String code = "int main(){\n" +
                "    int a = 0;\n" +
                "    if(a<100){\n" +
                "        a=a+1;\n" +
                "    } else {\n" +
                "        a=a-1;\n" +
                "    }\n" +
                "}";
        List<Token> tokens = lexerImpl.lexer(code);
        /*for (Token token : tokens) {
            System.out.println(token);
        }*/
        System.out.println(tokens.toString());
    }
}
