package whu.se.interpret.controllerTest;


import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import whu.se.interpret.InterpretApplicationTests;
import whu.se.interpret.controller.IndexController;
import whu.se.interpret.po.Code;

/**
 * @author xsy
 * @description: controller测试类
 * @date 2019/9/26 0:05
 */
@AutoConfigureMockMvc
public class IndexControllerTest extends InterpretApplicationTests {


    @Autowired
    IndexController indexController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testController(){
        String s = "int main(){int a = 0; int b = @;}";
        Code code = new Code(s);
        Gson gs = new Gson();
        String jsonString = gs.toJson(code);

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("")
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
