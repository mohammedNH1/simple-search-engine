
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InvertedIndex {

   
    
    private LinkedList<String> Invertedlist;
	
	public InvertedIndex() {
	    Invertedlist = new LinkedList<>();
        
    }

	public int getSize() {
		return Invertedlist.getSize();
	}
    public void readCsv(String file, String stop) {
        BufferedReader reader = null;
        String line;
        String[] stopWords = readStop(stop);
        boolean isStop;
        
        try {
            reader = new BufferedReader(new FileReader(file));
        
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",", 2);
                if (row.length < 2) continue; 
                
                String ID = row[0].trim();
                String content = row[1].toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").trim();
                
                if (ID.isEmpty() || content.isEmpty())
                	continue; 
                
                String[] words = content.split("\\s+");
        
                for (String word : words) {
                    isStop = false;
               

        
                    for (String stopWord : stopWords) {
                        if (word.equals(stopWord)) {
                            isStop = true;
                            break;
                        }
                    }
                    if (isStop) continue;
    

                    boolean isDuplicate = false;
                    
                    if (!Invertedlist.empty()) {
                        Invertedlist.findFirst();
                        while (!Invertedlist.last()) {
                            if (Invertedlist.retrieve().equals(word)) {
                                isDuplicate = true;
                                break;
                            }
                            Invertedlist.findNext();
                        }
                        if (Invertedlist.retrieve().equals(word)) { 
                            isDuplicate = true;
                        }
                    }
    
                    if (isDuplicate) {
                    	LinkedList<String> currentList = Invertedlist.retrieveList();
                        currentList.findFirst();
                        while(!currentList.last()) {
                        	if(currentList.retrieve().equals(ID)) {
                        		currentList.addScore(1);
                        		continue;
                        	}
                        	
                        	currentList.findNext();
                        }
                        if(currentList.retrieve().equals(ID)) {
                    		continue;
                    	}
                        Invertedlist.addID(ID);
                    } else {
                        Invertedlist.insert(word);
                        Invertedlist.addID(ID);
                    }
                }
            }
    
            if (!Invertedlist.empty()) {
                Invertedlist.findFirst(); 
                while (true) {
    
                    LinkedList<String> ids = Invertedlist.retrieveList(); 
                    if (ids != null && !ids.empty()) {
                        ids.findFirst();
                        while (true) {
                            if (ids.last()) {
                                break;
                            }
                            ids.findNext();
                        }
                    } else {
                        System.out.print("No IDs associated");
                    }
                    
                    if (Invertedlist.last()) {
                        break; 
                    }
                    Invertedlist.findNext(); 
                }
            } 
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    


    public String[] readStop(String stopWords) {
        String[] stopArr = null;
        
        BufferedReader reader = null;
        String line = "";
        String[] res;
        int count = 0;
        
        try {
            reader = new BufferedReader(new FileReader(stopWords));
            while ((line = reader.readLine()) != null) {
                count++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            reader = new BufferedReader(new FileReader(stopWords));
            stopArr = new String[count];
            for (int i = 0; i < stopArr.length; i++) {
                stopArr[i] = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {

        }
        return stopArr;

    }


    public void rankingRetrieval(String query) {
        if (Invertedlist.empty()) {
            System.out.println("Error: Invertedlist is empty. Cannot process query.");
        }
    
        String[] queryTerms = query.split(" ");
        LinkedList<String> rankingList = new LinkedList<>(); 
    
    
        Invertedlist.findFirst(); 
    
        while (true) {
          
            String currentWord = Invertedlist.retrieve();
            if (currentWord == null) {
                System.out.println("Error: Encountered a null word in the inverted list. Skipping...");
                if (Invertedlist.last()) break; 
                Invertedlist.findNext();
                continue;
            }
    
    
            boolean isQueryWord = false;
            for (String term : queryTerms) {
                if (currentWord.equalsIgnoreCase(term)) {
                    isQueryWord = true;
                    break;
                }
            }
    
            if (!isQueryWord) { 
                if (Invertedlist.last()) break; 
                Invertedlist.findNext();
                continue;
            }
    
            LinkedList<String> ids = Invertedlist.retrieveList();
            if (ids == null || ids.empty()) {
                System.out.println("No document IDs associated with the word: " + currentWord);
                if (Invertedlist.last()) break;
                Invertedlist.findNext();
                continue;
            }
    
            ids.findFirst();
            while (true) {
                String docID = ids.retrieve();
                if (docID == null) {
                    System.out.println("Error: Null document ID encountered. Skipping...");
                    if (ids.last())
                    	break;
                    ids.findNext();
                    continue;
                }
    
    
                boolean docFound = false;
                rankingList.findFirst();
                while (!rankingList.empty()) { 
                    if (rankingList.retrieve().equals(docID)) {
                        rankingList.addScore(1); 
                        docFound = true;
                        break; 
                    }
                    if (rankingList.last()) break;
                    rankingList.findNext();
                }

                if (!docFound) {
                    rankingList.insert(docID); 
                    
                    rankingList.findFirst();
                    while (!rankingList.empty()) {
                        if (rankingList.retrieve().equals(docID)) {
                            rankingList.addScore(1); 
                            break;
                        }
                        if (rankingList.last()) break; 
                        rankingList.findNext();
                    }
                }

                if (ids.last())
                    break; 
                ids.findNext();

            }
    
            if (Invertedlist.last()) break; 
            Invertedlist.findNext();
        }
    
         LinkedList<String> sortedList = new LinkedList<>();
		sortedList = rankingList.mergeSort();
		sortedList.display();
    }
    public LinkedList<String> invertBR(String query) {
		 LinkedStack<String> operatorStack = new LinkedStack<String>();
		 LinkedStack<LinkedList<String>> termStack = new LinkedStack<LinkedList<String>>();
		 LinkedList<String> result = new LinkedList<String>();
		 String[] arr = query.split(" ");
		 for(String term : arr) {
			 if (!(term.equals("AND") || term.equals("OR"))) {
				 Invertedlist.findFirst();
		            while(!Invertedlist.last()) { 
		            	if(Invertedlist.retrieve().equals(term)) {
		            		System.out.println("found the word: "+term);
		            		termStack.push(Invertedlist.retrieveList()); 
		            		break;
		            	}
		            	Invertedlist.findNext();
		            }
		        } 
			 else if (term.equals("OR")) {
				 while(!operatorStack.empty()) {
					 String data = operatorStack.pop();
					 if(data.equals("AND")) {
						 LinkedList<String> term1 = termStack.pop();
		                 LinkedList<String> term2 = termStack.pop();
		                 termStack.push(andQuery(term1, term2));
					 }else { // Must be "OR"
						 operatorStack.push(data);
						 break;
					 }
				 }
				 operatorStack.push("OR");
			 }else if(term.equals("AND")) {
				 	operatorStack.push("AND");	// Must be "AND"
			 				}
		 						}
		 while (!operatorStack.empty()) {
		        String operator = operatorStack.pop();
		        LinkedList<String> words1 = termStack.pop();
		        LinkedList<String> words2 = termStack.pop();
		        if (operator.equals("AND")) {
		            result = andQuery(words1, words2);
		        } else { 
		            result = orQuery(words1, words2);
		        }
		        termStack.push(result);
		 }
		 return result;
	}
    
	public LinkedList<String> andQuery(LinkedList<String> l1, LinkedList<String> l2) { 
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
    

}
