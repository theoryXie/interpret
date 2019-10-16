package whu.se.interpret.implTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import whu.se.interpret.InterpretApplicationTests;
import whu.se.interpret.po.Token;
import whu.se.interpret.service.Lexer;
import whu.se.interpret.service.impl.LexerImpl;
import whu.se.interpret.utils.utils;

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
    public void testLexer1(){
        String code = utils.ReadFileByLine("code/Parser-test.txt");
        List<Token> tokens = lexerImpl.lexer(code);
        utils.Write2FileByFileWriter("output/lexer",tokens.toString());
    }
    /**
     * 测试词法分析的接口
     *
     * @author zfq更改
     **/
    @Test
    public void testLexer2(){
        String code = utils.ReadFileByLine("code/Lexer-test-wrong.txt");
        List<Token> tokens = lexerImpl.lexer(code);
        utils.Write2FileByFileWriter("output/lexer",tokens.toString());
    }
}
