package whu.se.interpret.po;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Code {
    private String code;//代码区代码
    private String input;//前端输入的值
    public String cmd;//debug需要用到的行号  eg: 1,3,5

    public Code() {

    }

    public Code(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}

