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
 * @date Oct 9, 2019 8:23:23 PM
 */
@Getter
@Setter
public class Stmts extends NoTerminal{
	private ArrayList<Integer> nextList;

	/**
	 * @param name
	 */
	public Stmts(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		nextList = new ArrayList<>();
	}}
