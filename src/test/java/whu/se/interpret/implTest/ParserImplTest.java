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
        for(int i = 0; i < grammar.size(); i++){
            System.out.println(grammar.get(i));
        }
    }


    @Test
    public void testInit() throws FileNotFoundException {
        parserImpl.init();
        HashMap<String, HashSet<String>> firstSet = parserImpl.getAllFirst();
        Iterator iter = firstSet.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            System.out.println(entry.getKey()+"\t\t\t\t:\t\t\t\t"+entry.getValue());
        }
    }
}
