package abstractSyntaxTrees;

import java.util.ArrayList;
import java.util.Iterator;


public class StmtList implements Iterable<Stmt>{
	private ArrayList<Stmt> stmtList;

	public StmtList(){
		stmtList = new ArrayList<Stmt>();
	}

	public void add(Stmt stmt){
		stmtList.add(stmt);
	}

	public Stmt get(int i){
		return stmtList.get(i);
	}

	public int size(){
		return stmtList.size();
	}

	public Iterator<Stmt> iterator(){
		return stmtList.iterator();
	}

}
