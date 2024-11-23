public class IDNode<T> { 
	public String ID;
	public LinkedList<T> words;
	public IDNode<T> next;
	public int score;

	public IDNode() {
		ID = null;
		next = null;
		score = 0;
		words = new LinkedList<>();		
	}

	public IDNode(String val) {
		ID = val;
		next = null;
		words = new LinkedList<>();
	}
    
}