import java.io.BufferedReader;
import java.io.FileReader;

public class InvertedIndexBST {
	private BST<String> tree; 

	public InvertedIndexBST() {
	        tree = new BST<String>();
	    }
	public void readCsv(String file, String stop) {
		boolean isDuplicate;
		BufferedReader reader = null;
		String ID = "";
		String line = "";
		String[] res;
		String[] stopWords = readStop(stop);
		boolean isStop = false;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",", 2);
				int count = 0;
				for (String index : row) {
					index = index.toLowerCase();
					if (index.equals("")) {
						break;
					}
					index = index.replaceAll("[^a-zA-Z0-9\\s]", "");
					if (count++ % 2 == 0) {
						 ID = index;
					} else {
						res = index.split(" ");
						for (String indx : res) {
							isDuplicate = false;
							String word = indx.toLowerCase();
							if(tree.findkey(word)) { // you found it
								// current is on that node
								LinkedList<String> currentList = tree.retrieveList();
								currentList.findFirst();
								while(!currentList.last()) {
									if(currentList.retrieve().equals(ID)) {
										isDuplicate = true;
										break;
									}
									currentList.findNext();
								}
								if(currentList.retrieve().equals(ID))
									isDuplicate = true;
									if(isDuplicate) {
								continue;  // since it is in BST and it is in list of bst then continue
							}
									else {
										tree.insertToList(ID);
									}
							}
							
							for (int i = 0; i < stopWords.length; i++) {
								if (word.equals(stopWords[i])) {
									isStop = true;
									break;
								}
							}
							if (!isStop && !isDuplicate) { // && !isDuplicate not necessary since we checked before
								LinkedList<String> List = new LinkedList<String>();
								List.insert(ID);
								tree.insert(indx , List); 
							}
							isStop = false;
						}
						
					}
					
				}
			} // end of while
		}catch(Exception e) {
			System.out.println(e);
		}	
	}
	public String[] readStop(String file) {
		String[] stopArr = null;
		BufferedReader reader = null;
		String line = "";
		String[] res;
		int count = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				count++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			stopArr = new String[count];
			for (int i = 0; i < stopArr.length; i++) {
				stopArr[i] = reader.readLine();
			}
			reader.close();

		} catch (Exception e) {

		}
		return stopArr;

	}
	public LinkedList<String> BR(String query) {
		LinkedStack<String> operatorStack = new LinkedStack<String>();
		LinkedStack<LinkedList<String>> termStack = new LinkedStack<LinkedList<String>>();
		LinkedList<String> result = new LinkedList<String>();
		String words[] = query.split(" ");

		for (String term : words) { // market or sports and warming or CSC
			if (!(term.equals("AND") || term.equals("OR"))) {
				tree.findkey(term); 
				termStack.push(tree.retrieveList());
			} else if (term.equals("OR")) {
				while (!operatorStack.empty()) {
					String data = operatorStack.pop();
					if (data.equals("AND")) {
						LinkedList<String> term1 = termStack.pop();
						LinkedList<String> term2 = termStack.pop();
						termStack.push(andQuery(term1, term2));
					} else {
						operatorStack.push(data); // It's an "OR" operator (same precedence)
						break;
					}
				} 
				operatorStack.push("OR");
			} else if (term.equals("AND")) {
				operatorStack.push("AND");
			}
		}
		// Process remaining operators in the stack
		while (!operatorStack.empty()) {
			String operator = operatorStack.pop();
			LinkedList<String> IDS1 = termStack.pop();
			LinkedList<String> IDS2 = termStack.pop();
			if (operator.equals("AND")) {
				result = andQuery(IDS1, IDS2);
			} else {
				result = orQuery(IDS1, IDS2);
			}
			termStack.push(result);
		}
		return termStack.pop();
	}
	
	public LinkedList<String> andQuery(LinkedList<String> l1, LinkedList<String> l2) { // check generics !!!!
		LinkedList<String> interSection = new LinkedList<String>();

		l1.findFirst();
		while (!l1.last()) {
			l2.findFirst();
			while (!l2.last()) {
				if (l1.retrieve().equals(l2.retrieve())) {
					interSection.insert(l1.retrieve());
					break;
				}
				l2.findNext();
			}
			l1.findNext();
		}
		l2.findFirst();
		while (!l2.last()) {
			if (l1.retrieve().equals(l2.retrieve()))
				interSection.insert(l1.retrieve());
			l2.findNext();
		}
		l1.findFirst();
		while (!l1.last()) {
			if (l1.retrieve().equals(l2.retrieve())) {
				interSection.insert(l2.retrieve());
			}
			l1.findNext();
		}
		if (l1.retrieve().equals(l2.retrieve()))
			interSection.insert(l2.retrieve());

		return interSection;
	}

	public LinkedList<String> orQuery(LinkedList<String> l1, LinkedList<String> l2) {
		LinkedList<String> result = new LinkedList<String>();
		l1.findFirst();
		while (!l1.last()) {
			result.insert(l1.retrieve());
			l1.findNext();
		}
		result.insert(l1.retrieve());

		l1.findFirst();
		l2.findFirst();

		while (!l2.last()) {
			boolean isFound = false;

			l1.findFirst();

			while (!l1.last()) {
				if (l1.retrieve().equals(l2.retrieve())) {
					isFound = true;
					break;
				}
				l1.findNext();
			}

			if (l1.retrieve().equals(l2.retrieve())) {
				isFound = true;
			}

			if (!isFound) {
				result.insert(l2.retrieve());
			}

			l2.findNext();
		}

		boolean isFound = false;
		l1.findFirst();
		while (!l1.last()) {
			if (l1.retrieve().equals(l2.retrieve())) {
				isFound = true;
				break;
			}
			l1.findNext();
		}
		if (!isFound && !l1.retrieve().equals(l2.retrieve())) {
			result.insert(l2.retrieve());
		}

		result.findFirst();
		return result;
	}
	public void printTree() {
	    if (tree == null || tree.empty()) {
	        System.out.println("The tree is empty.");
	        return;
	    }
	    System.out.println("Inverted Index Tree (In-Order Traversal):");
	    printTree(tree.root); // Start from the root of the BST
	}

	// Helper method for recursive in-order traversal
	private void printTree(BSTNode<String> node) {
	    if (node == null) {
	        return;
	    }
	    // Visit the left subtree
	    printTree(node.left);

	    // Print the current node
	    System.out.print("Word: " + node.word + " -> Document IDs: ");
	    LinkedList<String> ids = node.IDS;
	    ids.findFirst();
	    while (!ids.last()) {
	        System.out.print(ids.retrieve() + ", ");
	        ids.findNext();
	    }
	    // Print the last ID
	    System.out.println(ids.retrieve());

	    // Visit the right subtree
	    printTree(node.right);
	}
}
