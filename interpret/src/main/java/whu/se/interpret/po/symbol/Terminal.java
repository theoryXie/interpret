/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;
import whu.se.interpret.po.Token;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:15:33 PM
 */
@Getter
@Setter
public class Terminal extends Symbol{

	private Token token;

	private String val;//保存本来的值，eg: a ,保存val = a
	
	public Terminal(String name) {
		super(name);
	}
}
