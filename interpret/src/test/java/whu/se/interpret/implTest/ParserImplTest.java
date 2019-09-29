package whu.se.interpret.implTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import whu.se.interpret.InterpretApplicationTests;
import whu.se.interpret.po.Node;
import whu.se.interpret.service.impl.ParserImpl;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Administrator
 * @title: xsy
 * @projectName interpret
 * @description: TODO
 * @date 2019/9/269:04
 */
public class ParserImplTest extends InterpretApplicationTests {

    @Autowired
    ParserImpl parserImpl;

    @Test
    public void testGetGrammar() throws FileNotFoundException {
        ArrayList<Node> grammar = parserImpl.getGrammar("grammar.txt");
        for (Node node : grammar) {
            System.out.println(node);
        }
    }


    @Test
    public void testInit() throws FileNotFoundException {
        parserImpl.init();
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
}
