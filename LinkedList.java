
public class LinkedList<T> implements List<T> {
	private IDNode<T> head;
	private IDNode<T> current;
	private int size;
	
	
	public LinkedList() {
		head = current = null;
		size = 0;
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
	public int getSize() {
		return size;
	}
	public void insert(String e) {
		IDNode<T> tmp1;
		size++;
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
	public void addScore(int s) {
		current.score += s;
	}
	public boolean empty() {
		if (head == null)
			return true;
		return false;
	}
	public void remove () {
		if (current == head) {
			head = head.next;
		}
		else {
			IDNode<T> tmp = head;

			while (tmp.next != current)
				tmp = tmp.next;

			tmp.next = current.next;
		}

		if (current.next == null)
			current = head;
		else
			current = current.next;
	}


	public boolean last() {
		if(current == null) {
			return true;
		}
		if (current.next == null) {
			return true;
		}
		return false;
	}
	public int retrieveScore() {
		return current.score;
	}
	public void sort() {
		LinkedList<String> newList = new LinkedList<String>();
		IDNode<T> tmp = head;
		findFirst();
		findNext();
		while(!empty()) {
			while(!last()) {
				tmp = head;
				if(retrieveScore() >  tmp.score) {
					tmp = current;
				}
				
				findNext();
			}
			if(retrieveScore() >  tmp.score) {
				tmp = current;
			}
			newList.insert(tmp.ID);
			newList.addScore(tmp.score);
			remove();
			findFirst();
		}
		
			
		
		
		
	}
	public void addID(String w) {
		// Ensure 'current.words' is initialized
		if (current.words == null) {
			current.words = new LinkedList<T>();
		}
		// Add the new word (or ID) to the LinkedList
		current.words.insert((String) w);
	}

	public String toString() {
		String A = "";
		findFirst();
		while(!last()) {
			A += retrieve();
			findNext();
		}
		A+= retrieve();
		return A;
	}

	public LinkedList<T> mergeSort() {
        if (head == null || head.next == null) {
            return this;
        }
        IDNode<T> m = middle(head);
        IDNode<T> afterM = m.next;
        m.next = null;

        LinkedList<T> l = new LinkedList<T>();
        l.head = head;
        LinkedList<T> r = new LinkedList<T>();
        r.head = afterM;

        l = l.mergeSort();
        r = r.mergeSort();

        return merge(l, r); 
    }
    
	
	private LinkedList<T> merge(LinkedList<T> l, LinkedList<T> r) {
        LinkedList<T> merged = new LinkedList<T>();
        IDNode<T> leftNode = l.head;
        IDNode<T> rightNode = r.head;

        while (leftNode != null && rightNode != null) {
            if (leftNode.score >= rightNode.score) {
                merged.insert(leftNode.ID);
                merged.addScore(leftNode.score);
                leftNode = leftNode.next;
            } else {
                merged.insert(rightNode.ID);
                merged.addScore(rightNode.score);

                rightNode = rightNode.next;
            }
        }
        while (leftNode != null) {
            merged.insert(leftNode.ID);
            merged.addScore(leftNode.score);
            leftNode = leftNode.next;
        }
        while (rightNode != null) {
            merged.insert(rightNode.ID);
            merged.addScore(rightNode.score);
            rightNode = rightNode.next;
        }

        return merged;
    }
    private IDNode<T> middle(IDNode<T> Head) {
        if (Head == null) {
            return Head;
        }
        IDNode<T> q = Head;
        IDNode<T> p = Head.next;

        while (p != null) {
            p = p.next;
            if (p != null) {
                q = q.next;
                p = p.next;
            }
        }
        return q;
    }


	public void display() {
		if (head == null) {
			System.out.println("The list is empty.");
			return;
		}
	
		IDNode<T> current = head;
		while (current != null) {
			System.out.println("ID: " + current.ID + ", Score: " + current.score);
			current = current.next;
		}
	}


}
