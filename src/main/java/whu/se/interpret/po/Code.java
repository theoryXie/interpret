package whu.se.interpret.po;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Code {
    private String code;
    private String input;

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

