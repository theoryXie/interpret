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
 * @date 2019/9/2522:20
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
        String code = "/*xsy write in 2019*/\nint main(){int a = 0;\nwhile(a<100){\na++;\nwrite(a);\n}\nreturn 0;\n}";
        List<Token> tokens = lexerImpl.lexer(code);
        for(int i = 0;i <tokens.size(); i++){
            System.out.println(tokens.get(i));
        }
    }
}
