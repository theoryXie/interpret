/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;
import whu.se.interpret.po.Token;

import java.util.ArrayList;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:24:18 PM
 */

@Setter
@Getter
public class Join extends NoTerminal{

	private Token token;
	private ArrayList<Integer> trueList;
	private ArrayList<Integer> falseList;

	/**
	 * @param name
	 */
	public Join(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}
