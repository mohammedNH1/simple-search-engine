import java.io.*;

// BasicIndexer class using CustomLinkedList and List interface
public class BasicIndexer {

    private CustomLinkedList<String> stopWords = new CustomLinkedList<>();       // Stop words
    private CustomLinkedList<DocumentNode> documentIndex = new CustomLinkedList<>();   // Main index for documents

    // Node structure for a document in the index
    public static class DocumentNode {
        int docId;
        CustomLinkedList<String> words;

        DocumentNode(int docId, CustomLinkedList<String> words) {
            this.docId = docId;
            this.words = words;
        }
    }

    public static class DocumentScore {
        int docId;
        int score;

        public DocumentScore(int docId, int score) {
            this.docId = docId;
            this.score = score;
        }
    }

    // Load stop words into a CustomLinkedList
    public void loadStopWords(String stopFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(stopFilePath))) {
            String word;
            while ((word = reader.readLine()) != null) {
                stopWords.insert(word.trim().toLowerCase());
            }
        } catch(IOException e) {
            System.err.println("An error occurred while reading stop words file: " + e.getMessage());
        }
    }

    // Read and process documents from CSV
    public void processDocuments(String datasetPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(datasetPath))) {
            String line;
            line = reader.readLine();
            if (line != null && !Character.isDigit(line.trim().charAt(0))) { 
                line = reader.readLine(); 
            }

            while (line != null) {
                if (line.trim().isEmpty()) {
                    line = reader.readLine();
                    continue;
                }

                String[] parts = line.split(",", 2); 
                if (parts.length < 2) {             
                    line = reader.readLine(); 
                    continue;
                }

                String docIdStr = parts[0].trim();
                String textContent = parts[1].trim();

                if (docIdStr.isEmpty() || textContent.isEmpty()) {
                    line = reader.readLine();
                    continue;
                }

                try {
                    int docId = Integer.parseInt(docIdStr);
                    CustomLinkedList<String> wordList = cleanAndSplitText(textContent);
                    DocumentNode node = new DocumentNode(docId, wordList);
                    documentIndex.insert(node); 
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line with invalid document ID: " + line);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while processing the file: " + e.getMessage());
        }
    }

    // Clean and split text into words, remove stop words
    private CustomLinkedList<String> cleanAndSplitText(String text) {
        CustomLinkedList<String> words = new CustomLinkedList<>();
        String[] rawWords = text.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");
        for (String word : rawWords) {
            if (!isStopWord(word) && !word.isEmpty()) {
                words.insert(word);
            }
        }
        return words;
    }

    private boolean isStopWord(String word) {
        stopWords.findFirst();
        while (!stopWords.last()) {
            if (stopWords.retrieve().equals(word)) {
                return true;
            }
            stopWords.findNext();
        }
        return stopWords.retrieve() != null && stopWords.retrieve().equals(word); // Check the last element
    }

    // Find documents that contain a given word
    private CustomLinkedList<Integer> findDocsWithWord(String word) {
        CustomLinkedList<Integer> docIds = new CustomLinkedList<>();
        documentIndex.findFirst();
        while (!documentIndex.last()) {
            DocumentNode doc = documentIndex.retrieve();
            doc.words.findFirst();
            while (!doc.words.last()) {
                if (doc.words.retrieve().equals(word)) {
                    docIds.insert(doc.docId);
                    break;
                }
                doc.words.findNext();
            }
            if (doc.words.retrieve() != null && doc.words.retrieve().equals(word)) { // Check the last element
                docIds.insert(doc.docId);
            }
            documentIndex.findNext();
        }
        return docIds;
    }

    // Perform AND operation on two lists of document IDs
    private CustomLinkedList<Integer> andQuery(CustomLinkedList<Integer> list1, CustomLinkedList<Integer> list2) {
        CustomLinkedList<Integer> result = new CustomLinkedList<>();
        list1.findFirst();
        while (!list1.last()) {
            int docId1 = list1.retrieve();
            list2.findFirst();
            while (!list2.last()) {
                if (docId1 == list2.retrieve()) {
                    result.insert(docId1);
                    break;
                }
                list2.findNext();
            }
            if (docId1 == list2.retrieve()) { // Check the last element in list2
                result.insert(docId1);
            }
            list1.findNext();
        }
        return result;
    }

    // Perform OR operation on two lists of document IDs
    private CustomLinkedList<Integer> orQuery(CustomLinkedList<Integer> list1, CustomLinkedList<Integer> list2) {
        CustomLinkedList<Integer> result = new CustomLinkedList<>();
        list1.findFirst();
        while (!list1.last()) {
            result.insert(list1.retrieve());
            list1.findNext();
        }
        result.insert(list1.retrieve()); // Add the last element from list1

        list2.findFirst();
        while (!list2.last()) {
            int docId2 = list2.retrieve();
            if (!contains(result, docId2)) {
                result.insert(docId2);
            }
            list2.findNext();
        }
        if (!contains(result, list2.retrieve())) { // Check the last element of list2
            result.insert(list2.retrieve());
        }
        return result;
    }

    // Helper method to check if a list contains a specific element
    private boolean contains(CustomLinkedList<Integer> list, int value) {
        list.findFirst();
        while (!list.last()) {
            if (list.retrieve() == value) {
                return true;
            }
            list.findNext();
        }
        return list.retrieve() != null && list.retrieve() == value;
    }

    // Process a Boolean query with AND/OR operations
    public CustomLinkedList<Integer> booleanRetrieve(String query) {
        String[] terms = query.toLowerCase().split(" ");
        CustomLinkedList<Integer> resultList = new CustomLinkedList<>();
        CustomLinkedList<Integer> tempAndList = new CustomLinkedList<>();
        boolean isOrOperation = false;

        for (String term : terms) {
            if (term.equals("and")) {
                continue;
            } else if (term.equals("or")) {
                if (!tempAndList.empty()) {
                    resultList = resultList.empty() ? tempAndList : orQuery(resultList, tempAndList);
                }
                tempAndList = new CustomLinkedList<>();
                isOrOperation = true;
            } else {
                CustomLinkedList<Integer> currentDocList = findDocsWithWord(term);
                tempAndList = tempAndList.empty() ? currentDocList : andQuery(tempAndList, currentDocList);
            }
        }

        if (!tempAndList.empty()) {
            resultList = isOrOperation ? orQuery(resultList, tempAndList) : tempAndList;
        }
        resultList = removeDuplicates(resultList);
        return resultList;
    }
    public CustomLinkedList<Integer> removeDuplicates(CustomLinkedList<Integer> list) {
        CustomLinkedList<Integer> uniqueList = new CustomLinkedList<>();
        list.findFirst();
    
        while (!list.empty()) {
            Integer docId = list.retrieve();
            if (!contains(uniqueList, docId)) {
                uniqueList.insert(docId);
            }
            if (list.last()) break;
            list.findNext();
        }
        return uniqueList;
    }
    
    private boolean contains(CustomLinkedList<Integer> list, Integer value) {
        list.findFirst();
        while (!list.empty()) {
            if (list.retrieve().equals(value)) {
                return true;
            }
            if (list.last()) break;
            list.findNext();
        }
        return false;
    }
    

    // Ranked retrieval based on term frequency
public CustomLinkedList<DocumentScore> rankedRetrieve(CustomLinkedList<String> queryWords) {
    CustomLinkedList<DocumentScore> docScores = new CustomLinkedList<>();

    documentIndex.findFirst();
    while (!documentIndex.last()) {
        DocumentNode doc = documentIndex.retrieve();
        int score = 0;

        queryWords.findFirst();
        while (!queryWords.last()) {
            score += frequency(doc.words, queryWords.retrieve());
            queryWords.findNext();
        }
        score += frequency(doc.words, queryWords.retrieve()); // Check the last element in queryWords

        if (score > 0) {
            docScores.insert(new DocumentScore(doc.docId, score));
        }
        documentIndex.findNext();
    }

    // Sort the docScores list by score in descending order
    docScores = sortDocumentScores(docScores);

    return docScores;
}

// Custom method to sort DocumentScore list in descending order based on score
private CustomLinkedList<DocumentScore> sortDocumentScores(CustomLinkedList<DocumentScore> scores) {
    CustomLinkedList<DocumentScore> sortedScores = new CustomLinkedList<>();

    while (!scores.empty()) {
        scores.findFirst();
        DocumentScore maxScore = scores.retrieve();
        scores.findFirst();

        // Find the max score in the list
        while (!scores.last()) {
            DocumentScore currentScore = scores.retrieve();
            if (currentScore.score > maxScore.score) {
                maxScore = currentScore;
            }
            scores.findNext();
        }

        // Insert the maxScore in sortedScores
        sortedScores.insert(maxScore);

        // Remove maxScore from original list
        removeDocumentScore(scores, maxScore);
    }

    return sortedScores;
}

// Helper method to remove a specific DocumentScore from a CustomLinkedList
private void removeDocumentScore(CustomLinkedList<DocumentScore> scores, DocumentScore toRemove) {
    scores.findFirst();
    while (!scores.empty() && !scores.last()) {
        if (scores.retrieve().docId == toRemove.docId && scores.retrieve().score == toRemove.score) {
            scores.remove();
            return;
        }
        scores.findNext();
    }

    if (scores.retrieve() != null && scores.retrieve().docId == toRemove.docId && scores.retrieve().score == toRemove.score) {
        scores.remove();
    }
}

    

    // Helper method to calculate frequency of a word in a document
    private int frequency(CustomLinkedList<String> words, String word) {
        int count = 0;
        words.findFirst();
        while (!words.last()) {
            if (words.retrieve().equals(word)) {
                count++;
            }
            words.findNext();
        }
        if (words.retrieve() != null && words.retrieve().equals(word)) { // Check the last element
            count++;
        }
        return count;
    }
}
