package whu.se.interpret.po;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Code {
    private String code;

    public Code(){

    }

    public Code(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
