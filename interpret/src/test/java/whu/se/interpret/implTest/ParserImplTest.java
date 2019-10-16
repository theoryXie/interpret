package whu.se.interpret.implTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
        //parserImpl.init("grammar");
        parserImpl.init("grammar/grammar.txt");
        HashMap<String, HashSet<String>> firstSet = parserImpl.getAllFirst();
        HashMap<String, HashSet<String>> followSet = parserImpl.getAllFollow();
//        Iterator iter = firstSet.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            System.out.printf("%-20s:%s\n",entry.getKey(),entry.getValue());
//        }
        Iterator iter = followSet.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            System.out.printf("%-20s:%s\n",entry.getKey(),entry.getValue());
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
            //String code = ReadFileByLine("code/Lexer-test.txt");
            String code = utils.ReadFileByLine("code/Parser-test.txt");
            //读取完成

            parserImpl.init("grammar/grammar.txt");
            Family family = parserImpl.generateFamily(parserImpl.getGrammar());
            SLRTable slrTable = parserImpl.generateSLRTable(family);
            List<Token> tokens = lexerImpl.lexer(code);
            Result result = parserImpl.syntaxCheck(tokens);

            //输出结果路径在target/classes/static下
            utils.Write2FileByFileWriter("output/family",family.toString());
            utils.Write2FileByFileWriter("output/slrTable",slrTable.toString());
            utils.Write2FileByFileWriter("output/syntaxCheck",result.getOutput());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
