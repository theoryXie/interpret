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
 * @date Oct 9, 2019 8:24:35 PM
 */
@Getter
@Setter
public class Equality extends NoTerminal{

	private Token token;

	/**
	 * @param name
	 */
	public Equality(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}
