/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:14:49 PM
 */

@Getter
@Setter
public class NoTerminal extends Symbol{

	public NoTerminal(String name) {
		super(name);
	}
}