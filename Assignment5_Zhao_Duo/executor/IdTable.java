package executor;

import java.util.HashMap;

/**
 * Encapsulate a HashMap<String, Double> table for identifier lookup
 * @author duozhao
 *
 */
public class IdTable {
	
	private HashMap<String, Double> table;
	
	public IdTable(){
		table = new HashMap<String, Double>();
	}
	
	public Double put(String key, Double value){
		return table.put(key, value);
	}
	
	public Double get(String key){
		return table.get(key);
	}
	
	public int size(){
		return table.size();
	}
	
	public void print(){
		System.out.println(this.table);
	}
	
}
