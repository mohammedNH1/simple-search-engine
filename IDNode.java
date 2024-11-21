public class IDNode<T> { 
	public String ID;
	public LinkedList<T> words;
	public int score;
	public IDNode<T> next;

	public IDNode() {
		ID = null;
		next = null;
		words = new LinkedList<>();		
	}

	public IDNode(String val) {
		ID = val;
		next = null;
		score = -1;
		words = new LinkedList<>();
	}

    public String details() {
    	String A = "";
    	
    	words.findFirst();
    	while(!words.last()) {
    		A += words.retrieve()+" " ;
    		words.findNext();
    	}
    	A += words.retrieve();
        return "ID: " + ID + ", Words: " + A;
    }
}

