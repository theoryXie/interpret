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
    public void test_semantic_analysis() throws Exception {
//        String code = utils.ReadFileByLine("code/semantic-test.txt");
//        List<Token> tokens = lexerImpl.lexer(code);
//        utils.Write2FileByFileWriter("output/lexer",tokens.toString());
//        ParserResult parserResult = parserImpl.syntaxCheck(tokens);
//        utils.Write2FileByFileWriter("output/syntaxCheck",parserResult.getResult().getOutput());
//        List<FiveParam> fiveParams = semanticImpl.semantic_analysis(parserResult,parserImpl.getGrammar());
//        fiveParams = semanticImpl.executeFiveParam(fiveParams);
//        StringBuilder ans = new StringBuilder();
//        for (int i = 0; i < fiveParams.size(); i++) {
//            ans.append(i+"\t"+fiveParams.get(i));
//        }
//        utils.Write2FileByFileWriter("output/semantic",ans.toString());
    }



    @Test
    public void test_debug() throws Exception {
        String code = utils.ReadFileByLine("code/semantic-test.txt");
        List<Token> tokens = lexerImpl.lexer(code);
        utils.Write2FileByFileWriter("output/lexer",tokens.toString());
        ParserResult parserResult = parserImpl.syntaxCheck(tokens);
        utils.Write2FileByFileWriter("output/syntaxCheck",parserResult.getResult().getOutput());

        int row = 23;

        SymbolTable ans = semanticImpl.debug(parserResult,parserImpl.getGrammar(),row);
        utils.Write2FileByFileWriter("output/debug",ans.toString());

    }


}
