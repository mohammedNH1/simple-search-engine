public class BSTNode <T> {
	public String word; // key
	public LinkedList<String> IDS; // data
	public BSTNode<T> left, right;
	
	public BSTNode(String k, LinkedList<String> val) {
		word = k;
		IDS = val;
		left = right = null;
	}
	
	public BSTNode(String k, LinkedList<String> val, BSTNode<T> l, BSTNode<T> r) {
		word = k;
		IDS = val;
		left = l;
		right = r;
	}
}