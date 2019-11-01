package whu.se.interpret.implTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import whu.se.interpret.InterpretApplicationTests;
import whu.se.interpret.po.*;
import whu.se.interpret.result.Result;
import whu.se.interpret.service.impl.LexerImpl;
import whu.se.interpret.service.impl.ParserImpl;
import whu.se.interpret.utils.utils;

import java.io.*;
import java.util.*;

/**
 * @author xsy
 * @description: 语法分析测试类
 * @date 2019/9/269:04
 */
public class ParserImplTest extends InterpretApplicationTests {

    @Autowired
    ParserImpl parserImpl;

    @Autowired
    LexerImpl lexerImpl;

    @Test
    public void testGetGrammar() throws IOException {
        ArrayList<Node> grammar = parserImpl.getGrammar("grammar.txt");
        for (Node node : grammar) {
            System.out.println(node);
        }
    }


    @Test
    public void testInit() throws IOException {
        parserImpl.init("grammar/grammar.txt");
        HashMap<String, HashSet<String>> firstSet = parserImpl.getAllFirst();
        HashMap<String, HashSet<String>> followSet = parserImpl.getAllFollow();
        for (Map.Entry<String, HashSet<String>> entry : firstSet.entrySet()) {
            System.out.printf("%-20s:%s\n", entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, HashSet<String>> entry : followSet.entrySet()) {
            System.out.printf("%-20s:%s\n", entry.getKey(), entry.getValue());
        }
    }

/** @Author: zfq
 * @Description:
 * @Date: 2019/9/30
 * @param: null
 * @return:
 */
    @Test
    public void grammarAnalysis(){
        try {
            //从文件读取测试代码
            String code = utils.ReadFileByLine("code/semantic-test.txt");

            parserImpl.init("grammar/grammar.txt");
            Family family = parserImpl.generateFamily(parserImpl.getGrammar());
            SLRTable slrTable = parserImpl.generateSLRTable(family);
            List<Token> tokens = lexerImpl.lexer(code);
            ParserResult parserResult = parserImpl.syntaxCheck(tokens);

            //输出结果路径在target/classes/static下
            utils.Write2FileByFileWriter("output/family",family.toString());
            utils.Write2FileByFileWriter("output/slrTable",slrTable.toString());
            utils.Write2FileByFileWriter("output/lexer",tokens.toString());
            utils.Write2FileByFileWriter("output/syntaxCheck",parserResult.getResult().getOutput());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
