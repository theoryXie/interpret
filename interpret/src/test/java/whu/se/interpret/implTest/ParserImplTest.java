package whu.se.interpret.implTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import whu.se.interpret.InterpretApplicationTests;
import whu.se.interpret.po.*;
import whu.se.interpret.service.impl.LexerImpl;
import whu.se.interpret.service.impl.ParserImpl;

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
    public void generateFamily(){
        try {
            //从文件读取测试代码
            String code = ReadFileByLine("code/TempSimple.txt");
            //读取完成


            parserImpl.init("grammar/grammar.txt");
            Family family = parserImpl.generateFamily(parserImpl.getGrammar());
            SLRTable slrTable = parserImpl.generateSLRTable(family);
            List<Token> tokens = lexerImpl.lexer(code);
            ParserResult parserResult = parserImpl.syntaxCheck(tokens,slrTable);

            //输出结果路径在target/classes/static下
            Write2FileByFileWriter("output/family",family.toString());
            Write2FileByFileWriter("output/slrTable",slrTable.toString());
            //qyr填入语义分析结果
            Write2FileByFileWriter("output/syntaxCheck","");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /** @Author: zfq
     * @Description:
     * @Date: 2019/9/30
     * @param: null
     * @return:
     */
    public static String ReadFileByLine(String filename) {
        StringBuilder stringBuilder = new StringBuilder();

        Resource resource = new ClassPathResource("static/" + filename);
        File file = null;
        InputStream is = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {
            file = resource.getFile();
            is = new FileInputStream(file);
            reader = new InputStreamReader(is);
            bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
               stringBuilder.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader)
                    bufferedReader.close();
                if (null != reader)
                    reader.close();
                if (null != is)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
    /** @Author: zfq
     * @Description:
     * @Date: 2019/9/30
     * @param: null
     * @return:
     */
    public static void Write2FileByFileWriter(String filename,String output) {
        Resource resource = new ClassPathResource("static/" + filename);
        File file;
        FileWriter fw = null;
        try {
            file = resource.getFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            fw.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fw) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
