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
 * @date Oct 9, 2019 8:21:42 PM
 */
@Getter
@Setter
public class Block extends NoTerminal{

	private ArrayList<Integer> nextList;

	/**
	 * @param name
	 */
	public Block(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		nextList = new ArrayList<>();
	}}
