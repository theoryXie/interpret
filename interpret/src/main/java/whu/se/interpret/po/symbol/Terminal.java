/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:15:33 PM
 */
@Getter
@Setter
public class Terminal extends Symbol{
	
	public Terminal(String name) {
		super(name);
	}
}
