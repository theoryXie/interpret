package whu.se.interpret.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

public class utils {
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
                stringBuilder.append(line+"\n");
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
