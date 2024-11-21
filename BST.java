public class BST <T> {
	BSTNode<T> root, current;
	
	public BST() {
		root = current = null;
	}
	
	public boolean empty() {
		return root == null;
	}
	
	public boolean full() {
		return false;
	}
	
	public String retrieveWord () {
		return current.word;
	}
	public LinkedList<String> retrieveList() {
		return current.IDS;
	}
	public boolean findkey(String tkey) {
		BSTNode<T> p = root , q = root;
				
		if(empty())
			return false;
		
		while(p != null) {
			q = p;
			if(p.word.compareToIgnoreCase(tkey) == 0) {
				current = p;
				return true;
			}
			else if(p.word.compareToIgnoreCase(tkey) < 0)
				p = p.left;
			else
				p = p.right;
		}
		
		current = q;
		return false;
	}
	public boolean insert(String k, LinkedList<String> val) {
		BSTNode<T> p, q = current;
		
		if(findkey(k)) {
			current = q;  // findkey() modified current
			return false; // word already in the BST
		}
		
		p = new BSTNode<T>(k, val);
		if (empty()) {
			root = current = p; // dont modify
			return true;
		}
		else {
			if (current.word.compareToIgnoreCase(k) < 0)
				current.left = p;
			else
				current.right = p;
			current = p;
			return true;
		}
	}
	public void insertToList(String ID) {
		current.IDS.insert(ID);
	}
	
}