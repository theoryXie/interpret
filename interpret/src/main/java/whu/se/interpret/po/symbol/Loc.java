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
 * @date Oct 9, 2019 8:24:04 PM
 */

@Setter
@Getter
public class Loc extends NoTerminal{

	private Token token;

	/**
	 * @param name
	 */
	public Loc(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}