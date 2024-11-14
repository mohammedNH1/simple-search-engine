import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LinkedList<T> implements List<T> {   
	private IDNode<T> head;
	private IDNode<T> current;

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
	public void readCsv(LinkedList<T> D, String file , String stop) {
	        BufferedReader reader = null;
	        String line = "";
	        String[] res;
	        String[] stopWords = readStop(stop);
	        boolean isStop = false;
	        
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            while((line = reader.readLine())!= null ) { 
	                String[] row = line.split("," , 2);
	                int count = 0;
	                for(String index : row) {
	                	if(index == "") {
	                		break;
	                	}
	                	index = index.replaceAll("[^a-zA-Z0-9\\s]", "");	              
	                	if (count++ % 2 == 0) {
	                        D.insert(index);
	                    }
	                    else {
	                    	LinkedList<String> wordLinkedList = new LinkedList<String>();
	                        res = index.split(" ");
	                        for(String indx : res) {
	                        	String word = indx.toLowerCase();
	                        	for(int i = 0; i < stopWords.length;i++) {
	                        		if(word.equals(stopWords[i])) {
	                        			isStop = true;
	                        			break;
	                        		}
	                        	}
	                        	if(!isStop) {
	                        		wordLinkedList.insert(indx);
	                        	}
	                        	isStop = false;    
	                        }
	                        D.addWords(wordLinkedList);    
	                    }
	                }
	            }
	        }catch(Exception e) {
	        		e.printStackTrace();
	        }finally {
	        		D.findFirst();
	        		//D.printall();
	        		try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
	            while((line = reader.readLine())!= null ) {
	            	count++;
	            }
	            reader.close();
	            }catch(Exception e) {
	        		e.printStackTrace();
	        }
	        try {
	        	 reader = new BufferedReader(new FileReader(file));
	            stopArr = new String[count];
	            for(int i=0;i < stopArr.length;i++) {
	            	stopArr[i] = reader.readLine();
	            }
	            reader.close();
	            
	        }catch(Exception e) {
	        	
	        }
	           return stopArr;
       
	            
	        }
	public void addWords(LinkedList<String> w) {
		current.words = (LinkedList<T>) w;
	}																	 
			public LinkedList<String> StartingPoint(String query) { 
		    LinkedList<String> resultList = new LinkedList<String>();  
		    LinkedList<String> tempAndList = new LinkedList<String>(); 
		    boolean isOrOperation = false;  
		    String[] terms = query.toLowerCase().split(" ");
		    
		    for (String term : terms) {
		        if (term.equalsIgnoreCase("and")) {
		            // Continue to next term since "AND" just indicates merging with AND
		            continue;
		        } else if (term.equalsIgnoreCase("or")) {
		            // Process the current AND group
		            if (!tempAndList.empty()) { // if not empty enter
		                if (resultList.empty()) {
		                    // Manually insert each element from tempAndList to resultList
		                    IDNode<String> tmp = tempAndList.head;
		                    while (tmp != null) {
		                        resultList.insert(tmp.ID);
		                        tmp = tmp.next;
		                    }
		                } else {
		                    resultList = orQuery(resultList, tempAndList);
		                }
		            }
		            // Reset tempAndList for the next AND group and set OR mode
		            tempAndList = new LinkedList<String>();
		            isOrOperation = true;
		        } else {
		            LinkedList<String> currentIDList = findIDS(term);
		            if (tempAndList.empty()) {
		                // Insert each element from currentIDList into tempAndList
		                IDNode<String> tmp = currentIDList.head;
		                while (tmp != null) {
		                    tempAndList.insert(tmp.ID);
		                    tmp = tmp.next;
		                }
		            } else {
		                // Perform AND within the current AND group
		                tempAndList = andQuery(tempAndList, currentIDList);
		            }
		        }
		    }
		    
		    // Merge the last processed AND group with the resultList
		    if (!tempAndList.empty()) {
		        if (isOrOperation) {
		            resultList = orQuery(resultList, tempAndList);
		        } else {
		            // For single AND group or no "OR" in the query
		            resultList = tempAndList;
		        }
		    }

		    return resultList;
		}
	public LinkedList<String> findIDS(String word) { // 
		LinkedList<String> result = new LinkedList<String>();
		System.out.println("Starting search...");
		findFirst(); 
		current.words.findFirst();
		boolean isLast = true;

		while (!last()) {  
			 isLast = true;
		    System.out.println("Checking ID: " + current.ID);
		    
		    // Only proceed if the words list is not empty
		    if (!current.words.empty()) {
		        while (!current.words.last()) {  // Loop through words in the current object
		            String wordInList = current.words.retrieve();
		            System.out.println("Checking word: " + wordInList);
		            
		            if (wordInList.equalsIgnoreCase(word)) { 
		                result.insert(current.ID);
		                System.out.println("Match found for ID: " + current.ID);
		                isLast = false;
		                break;
		            }
		            current.words.findNext();
		        }
		    }
		    if(current.words.retrieve().equalsIgnoreCase(word) && isLast) {
		    	result.insert(current.ID);
                System.out.println("Match found for ID: " + current.ID);
		    }
		    findNext();  // Move to next main element
		    current.words.findFirst();  // Reset words list for the next element
		}
		while(!current.words.last()) {
			 String wordInList = current.words.retrieve();
	            System.out.println("Checking word: " + wordInList);
	            if (wordInList.equalsIgnoreCase(word) && isLast) { 
	                result.insert(current.ID);
	                System.out.println("Match found for ID: " + current.ID);
	                isLast = false;
	                break;
	            }
	            current.words.findNext(); 
		}
		System.out.println("FINISHED 49");
		 String wordInList = current.words.retrieve();
         if (wordInList.equalsIgnoreCase(word) && isLast) {
             result.insert(current.ID);
             System.out.println("Checking if last word");
         }



		result.findFirst();
		while(!result.last()) {
			System.out.println("ID+++= "+ result.retrieve());
			result.findNext();
		}
		System.out.println("Search complete.");
		return result;
		
		
	}
	public LinkedList<String> andQuery(LinkedList<String> l1 , LinkedList<String> l2 ) {    // check generics !!!!
		LinkedList<String> interSection = new LinkedList<String>();
		
		l1.findFirst();
		while(!l1.last()) {
			l2.findFirst();
			while(!l2.last()) {
				if(l1.retrieve().equals(l2.retrieve())) { 
					interSection.insert(l1.retrieve());
					break;
				}
				l2.findNext();
			}
			l1.findNext();
		}
		l2.findFirst();
		while(!l2.last()) {
			if(l1.retrieve().equals(l2.retrieve()))
				interSection.insert(l1.retrieve());
			    l2.findNext();
		}
		l1.findFirst();
		while(!l1.last()) {
			if(l1.retrieve().equals(l2.retrieve())) {
				interSection.insert(l2.retrieve());  
			}
			l1.findNext();
		}
		if(l1.retrieve().equals(l2.retrieve()))
			interSection.insert(l2.retrieve());

		return interSection;
	}
	public LinkedList<String> orQuery(LinkedList<String> l1 , LinkedList<String> l2 ) {
		LinkedList<String> result = new LinkedList<String>();
		l1.findFirst();
		while(!l1.last()) {
			result.insert(l1.retrieve());
			l1.findNext();
		}
		result.insert(l1.retrieve());
		
		l1.findFirst();
		l2.findFirst();

		while (!l2.last()) {
		    boolean isFound = false;
		    
		    // Start from the beginning of l1 for each element of l2
		    l1.findFirst();
		    
		    // Check if the current element of l2 is already in l1
		    while (!l1.last()) {
		        if (l1.retrieve().equals(l2.retrieve())) {
		            isFound = true;
		            break;  // No need to check further, element is found in l1
		        }
		        l1.findNext();
		    }
		    
		    // Check the last element of l1 for equality with l2.retrieve()
		    if (l1.retrieve().equals(l2.retrieve())) {
		        isFound = true;
		    }
		    
		    // If the element from l2 is not found in l1, insert it into l1
		    if (!isFound) {
		        result.insert(l2.retrieve());
		    }
		    
		    // Move to the next element in l2
		    l2.findNext();
		}

		// Final check on the last element of l2 after the loop ends
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

	public void printall() {
		current = head; 
	    while (current != null) {
	        System.out.print(retrieve()+": " );
	        if (current.words != null) { 
	            current.words.current = current.words.head; 
	            while (current.words.current != null) {
	                System.out.print(current.words.retrieve() + " "); 
	                current.words.current = current.words.current.next;
	            }
	        }
	        current = current.next; 
	        System.out.println(); 
	    }
	}
}