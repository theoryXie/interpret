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
}

