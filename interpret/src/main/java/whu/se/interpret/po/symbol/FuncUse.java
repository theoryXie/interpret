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
 * @date Oct 9, 2019 8:22:40 PM
 */

@Getter
@Setter
public class FuncUse extends NoTerminal{

	private Token token;
	/**
	 * @param name
	 */
	public FuncUse(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}
