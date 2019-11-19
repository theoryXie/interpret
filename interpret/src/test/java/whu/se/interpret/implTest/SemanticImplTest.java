package whu.se.interpret.implTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import whu.se.interpret.InterpretApplicationTests;
import whu.se.interpret.po.FiveParam;
import whu.se.interpret.po.ParserResult;
import whu.se.interpret.po.SymbolTable;
import whu.se.interpret.po.Token;
import whu.se.interpret.service.Semantic;
import whu.se.interpret.service.impl.LexerImpl;
import whu.se.interpret.service.impl.ParserImpl;
import whu.se.interpret.service.impl.SemanticImpl;
import whu.se.interpret.utils.utils;

import java.util.List;

/**
 * @author xsy
 * @description: TODO
 * @date 10/27/2019 3:35 PM
 */
public class SemanticImplTest extends InterpretApplicationTests {
    @Autowired
    LexerImpl lexerImpl;
    @Autowired
    ParserImpl parserImpl;
    @Autowired
    SemanticImpl semanticImpl;

    @Test
    public void test(){}

    //测试生成五元式
    @Test
    public void test_semantic_analysis() throws Exception {
        //String code = utils.ReadFileByLine("code/semantic-test.txt");
        String code = utils.ReadFileByLine("code/test_while_ifelse.txt");
        List<Token> tokens = lexerImpl.lexer(code);
        utils.Write2FileByFileWriter("output/lexer",tokens.toString());
        ParserResult parserResult = parserImpl.syntaxCheck(tokens);
        utils.Write2FileByFileWriter("output/syntaxCheck",parserResult.getResult().getOutput());
        List<FiveParam> fiveParams = semanticImpl.semantic_analysis(parserResult,parserImpl.getGrammar());
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < fiveParams.size(); i++) {
            ans.append(i).append("\t").append(fiveParams.get(i));
        }
        utils.Write2FileByFileWriter("output/semantic",ans.toString());
    }


    //测试debug
    @Test
    public void test_debug() throws Exception {
        String code = utils.ReadFileByLine("code/semantic-test.txt");
        StringBuilder s = new StringBuilder();
        List<Token> tokens = lexerImpl.lexer(code);
        utils.Write2FileByFileWriter("output/lexer",tokens.toString());
        for(int row = -2; row <= 27; row++) { // row<=25
            ParserResult parserResult = parserImpl.syntaxCheck(tokens);
            utils.Write2FileByFileWriter("output/syntaxCheck",parserResult.getResult().getOutput());
            SymbolTable ans = semanticImpl.debug(parserResult,parserImpl.getGrammar(),row);
            s.append("第").append(row).append("行即将运行，符号表:\n");
            s.append(ans.toString()).append("\n");
        }
        utils.Write2FileByFileWriter("output/debug",s.toString());

    }


}
