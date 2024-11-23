
public class LinkedStack<T> implements Stack<T> {
	private Node<T> top;

	public LinkedStack() {
		top = null;
	}

	@Override
	public T pop() {
		T e = top.data;
		top = top.next;
		return e;
	}

	@Override
	public void push(T e) {
		Node<T> tmp = new Node<T>(e);
		tmp.next = top;
		top = tmp;
	}

	@Override
	public boolean empty() {
		return top==null;
	}
	
	@Override
	public boolean equals(Object o) {
	       // if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        if(top == null) return false;
	        Stack<T> otherStack = (Stack<T>) o;

	        // Check if each element is equal (in the same order)
	        Node<T> tmp = top;
	        T e;
	      
	        Stack<T> tmpStack = new LinkedStack<>();
	        while(tmp != null) {	
	        	if(!otherStack.empty()) { 
	        		e  = otherStack.pop();
	        		tmpStack.push(e);
	        		if (!tmp.data.equals(e)) { 
	        			while(!tmpStack.empty()) otherStack.push(tmpStack.pop());
	        			return false;
	        		}
	        		tmp = tmp.next;
	        	}
	        	else {
	        		while(!tmpStack.empty()) otherStack.push(tmpStack.pop());
        			return false;
	        	}
	        }
	        
	        while(!tmpStack.empty()) otherStack.push(tmpStack.pop());
	        return true;
	 }
	
	
	@Override
	public boolean full() {
		return false;
	}
}