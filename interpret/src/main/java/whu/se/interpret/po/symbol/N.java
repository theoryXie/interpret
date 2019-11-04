/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:25:54 PM
 */

@Setter
@Getter
public class N extends NoTerminal{

	private ArrayList<Integer> nextList;

	/**
	 * @param name
	 */
	public N(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}