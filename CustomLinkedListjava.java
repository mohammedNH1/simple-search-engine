// CustomLinkedList.java

public class CustomLinkedList<T> implements List<T> {  

    private Node<T> head;  
    private Node<T> current;  

    public CustomLinkedList() {  
        head = current = null;  
    }  

    // Node structure for the linked list
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // Check if the list is empty
    public boolean empty() {       
        return head == null;
    }  

    // Check if the current node is the last node
    public boolean last() {         
        return current != null && current.next == null;
    }  

    // Since a linked list can't be "full" in practical terms, this always returns false
    public boolean full() {        
        return false;  
    }  

    // Move the current pointer to the first node
    public void findFirst() {       
        current = head;
    }  

    // Move the current pointer to the next node
    public void findNext() {        
        if (current != null) {
            current = current.next;
        }
    }  

    // Retrieve data at the current node
    public T retrieve() {           
        return current != null ? current.data : null;
    }   

    // Update the data at the current node
    public void update(T val) {     
        if (current != null) {
            current.data = val;
        }
    }

    // Insert a new node after the current node
    public void insert(T val) {      
        Node<T> newNode = new Node<>(val);
        if (empty()) {
            head = current = newNode;  // Insert at head if list is empty
        } else {
            newNode.next = current.next;
            current.next = newNode;
            current = newNode;  // Move current to the new node
        }
    }

    // Remove the current node
    public void remove() {             
        if (current == null) return;  // No operation if current is null

        if (current == head) {  // Special case for removing the head
            head = head.next;
            current = head;
        } else {  
            // Find the previous node
            Node<T> tmp = head;
            while (tmp != null && tmp.next != current) {
                tmp = tmp.next;
            }

            if (tmp != null) {  // Unlink the current node
                tmp.next = current.next;
                current = current.next != null ? current.next : head;
            }
        }
    }
}
