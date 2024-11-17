public class IDNode<T> { 
	public String ID;
	public LinkedList<T> words;
	public IDNode<T> next;

	public IDNode() {
		ID = null;
		next = null;
		words = new LinkedList<>();		
	}

	public IDNode(String val) {
		ID = val;
		next = null;
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