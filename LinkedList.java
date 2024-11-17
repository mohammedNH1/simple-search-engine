
public class LinkedList<T> implements List<T> {
	private IDNode<T> head;
	private IDNode<T> current;
	
	public String prints() {
		return current.details();
	}
	public LinkedList() {
		head = current = null;
	}

	public void findFirst() {
		if (!empty())
			current = head;
	}

	public void findNext() {
		if (!empty() && !last()) {
			current = current.next;
		}
	}

	public String retrieve() {
		if (current != null)
		    return current.ID;	
		return null;
	}
	public LinkedList<T> retrieveList() {
	    if (current != null) {
	        return current.words;
	    }
	    throw new IllegalStateException("NO node selected.");
	}

	public void insert(String e) {
		IDNode<T> tmp1;
		if (empty()) {
			current = head = new IDNode<T>(e);
		} else {
			tmp1 = new IDNode<T>(e);
			tmp1.next = current.next;
			current.next = tmp1;
			findNext();
		}
	}
	public void addWords(LinkedList<String> w) {
	current.words = (LinkedList<T>) w;  
	}
	public void addList(LinkedList<String> tempAndList) {
		IDNode<String> tmp = tempAndList.head;
		while (tmp != null) {
			insert(tmp.ID);
			tmp = tmp.next;
		}
		
	}

	public boolean empty() {
		if (head == null)
			return true;
		return false;
	}

	public boolean last() {
		if(current == null) {
			System.out.println("current is null and you calles last");
			return true;
		}
		if (current.next == null) {
			return true;
		}
		return false;
	}
}
