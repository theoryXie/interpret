package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;
import whu.se.interpret.po.Token;

import java.util.ArrayList;

/**
 * @author xsy
 * @description: TODO
 * @date 11/1/2019 5:37 PM
 */
@Getter
@Setter
public class Bools extends NoTerminal{

    private ArrayList<Token> tokens;

    /**
     * @param name
     */
    public Bools(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }}