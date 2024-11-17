import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BasicIndex {
	private LinkedList<String> list;
	
	public BasicIndex() {
	    list = new LinkedList<>();
	}
	public void printtt() {
		list.findFirst();
		while(!list.last()) {
			System.out.println(list.prints());
			list.findNext();
		}
		System.out.println(list.prints());
	}
	
	public void readCsv(String file, String stop) {
		BufferedReader reader = null;
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
					if (index.equals("")) {
						break;
					}
					index = index.replaceAll("[^a-zA-Z0-9\\s]", "");
					if (count++ % 2 == 0) {
						list.insert(index);
					} else {
						LinkedList<String> wordLinkedList = new LinkedList<String>();
						res = index.split(" ");
						for (String indx : res) {
							String word = indx.toLowerCase();
							for (int i = 0; i < stopWords.length; i++) {
								if (word.equals(stopWords[i])) {
									isStop = true;
									break;
								}
							}
							if (!isStop) {
								wordLinkedList.insert(indx);
							}
							isStop = false;
						}
						list.addWords(wordLinkedList);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		try {
			reader.close();
		} catch (IOException e) {
				e.printStackTrace();
			}
			//list.findFirst();

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

	public LinkedList<String> findIDS(String word) { 
	    LinkedList<String> result = new LinkedList<String>();  
	    System.out.println("Starting search...");
	    
	    list.findFirst();  // Start from the first element of the list
	    boolean matchFound;  // Flag to tell if a match is found for current ID
	    
	    while (!list.last()) {  // Loop through all the IDs
	        

	        LinkedList<String> innerList = list.retrieveList(); 
	        innerList.findFirst();  
	        matchFound = false;  // Reset match flag for each ID

	        while (!innerList.last()) {  // Loop through words in the current ID
	            String wordInList = innerList.retrieve();
	            

	            if (wordInList.equalsIgnoreCase(word)) {  
	                result.insert(list.retrieve()); 
	                matchFound = true;
	                break;  
	            }
	            innerList.findNext();  
	        }
	        
	        // If no match is found in the inner list, check if the last word matches
	        if (!matchFound && innerList.retrieve().equalsIgnoreCase(word)) {
	            result.insert(list.retrieve()); 
	           
	        }
	        
	        list.findNext();  // Move to the next ID
	    }
	    
	    // Handle the last ID and its words
	    LinkedList<String> innerList = list.retrieveList();
	    innerList.findFirst();
	    matchFound = false;
	    while (!innerList.last()) {
	        String wordInList = innerList.retrieve();

	        if (wordInList.equalsIgnoreCase(word)) {
	            result.insert(list.retrieve());
	            matchFound = true;
	            break;
	        }
	        innerList.findNext();
	    }

	    if (!matchFound && innerList.retrieve().equalsIgnoreCase(word)) {
	        result.insert(list.retrieve());
	    }

	   
	    return result;
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
	public LinkedList<String> BR(String query) {
		LinkedStack<String> operatorStack = new LinkedStack<String>();
		LinkedStack<LinkedList<String>> termStack = new LinkedStack<LinkedList<String>>();
		LinkedList<String> result = new LinkedList<String>();
		String words[] = query.split(" ");

		for (String term : words) { // market or sports and warming or CSC
			if (!(term.equals("AND") || term.equals("OR"))) {
				LinkedList<String> wordsList = findIDS(term);
				termStack.push(wordsList);
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
		// process remaining operators in the stack
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
}

