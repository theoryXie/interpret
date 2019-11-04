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
 * @date Oct 9, 2019 8:20:56 PM
 */
@Getter
@Setter
public class Bool extends NoTerminal{

	private Token token;
	private ArrayList<Integer> trueList;
	private ArrayList<Integer> falseList;

	/**
	 * @param name
	 */
	public Bool(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}
