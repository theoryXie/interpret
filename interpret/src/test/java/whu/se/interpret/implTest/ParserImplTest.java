package whu.se.interpret.implTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

            StringBuilder testOutput = null;

            parserImpl.init("grammar/grammar.txt");
            Family family = parserImpl.generateFamily(parserImpl.getGrammar());
            SLRTable slrTable = parserImpl.generateSLRTable(family);
            List<Token> tokens = lexerImpl.lexer(code.toString());
            ParserResult parserResult = parserImpl.syntaxCheck(tokens,slrTable);

            testOutput.append(family);
            testOutput.append(slrTable);
            testOutput.append("");

            Write2FileByOutputStream("testOutput",testOutput.toString());
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
        File file = new File(filename);
        InputStream is = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {
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
        return bufferedReader.toString();
    }
    /** @Author: zfq
     * @Description:
     * @Date: 2019/9/30
     * @param: null
     * @return:
     */
    public static void Write2FileByOutputStream(String filename,String output) {
        File file = new File(filename);
        FileOutputStream fos = null;
        // BufferedOutputStream bos = null;
        OutputStreamWriter osw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            osw.write(output);
            // bos = new BufferedOutputStream(fos);
            // bos.write("Write2FileByOutputStream".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
