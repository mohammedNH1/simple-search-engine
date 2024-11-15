import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BasicIndexer indexer = new BasicIndexer();
        int userDataStructure;
        int userResultOption;
        String selectedDataStructure = "";

        try {
            // Load stop words and process documents once
            indexer.loadStopWords("src/stop.txt");
            indexer.processDocuments("src/dataset.csv");

            do {
                userDataStructure = TermanlGUI.dataStructureInterface(input);
                input.nextLine(); // Consume newline to prevent skipping input

                if (userDataStructure == -1) {
                    System.out.println("You have exited the program");
                    break;
                }

                switch (userDataStructure) {
                    case 1:
                        selectedDataStructure = "Basic Index";
                        System.out.println("You have selected Basic Index");
                        break;
                    case 2:
                        selectedDataStructure = "Inverted Index";
                        System.out.println("You have selected Inverted Index");
                        break;
                    case 3:
                        selectedDataStructure = "Inverted Index with BSTs";
                        System.out.println("You have selected Inverted Index with BSTs");
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                        continue;
                }

                boolean exitToDataStructure = false; // Flag to break out to the Data Structure interface

                do {
                    userResultOption = TermanlGUI.resultOptionsInterface(input, selectedDataStructure);
                    input.nextLine(); // Consume newline to prevent skipping input

                    if (userResultOption == 0) {
                        // Return directly to the Data Structure interface
                        break;
                    }

                    if (userDataStructure == 1) {  // Basic Index selected
                        boolean runQuery = true;  // Loop control for entering new queries

                        while (runQuery) {
                            // Run Boolean Retrieval for Basic Index if selected
                            if (userResultOption == 1) {  // Basic Index and Boolean Retrieval
                                String query = TermanlGUI.getSearchQuery(input);
                                CustomLinkedList<Integer> matchingDocs = indexer.booleanRetrieve(query);

                                // Display Boolean retrieval results in the specified format
                                System.out.println("\n# Q: " + query);
                                if (matchingDocs.empty()) {
                                    System.out.println("Result: {}");
                                } else {
                                    System.out.print("Result: {");
                                    matchingDocs.findFirst();
                                    while (!matchingDocs.last()) {
                                        System.out.print(matchingDocs.retrieve() + ", ");
                                        matchingDocs.findNext();
                                    }
                                    System.out.print(matchingDocs.retrieve()); // Print last element without trailing comma
                                    System.out.println("}");
                                }
                            }

                            // Run Ranked Retrieval if selected
                            else if (userResultOption == 2) {  // Ranked Retrieval
                                String query = TermanlGUI.getSearchQuery(input);
                                CustomLinkedList<String> queryWords = new CustomLinkedList<>();
                                for (String word : query.split(" ")) {
                                    queryWords.insert(word);
                                }
                                CustomLinkedList<BasicIndexer.DocumentScore> ranking = indexer.rankedRetrieve(queryWords);

                                // Display ranked results
                                System.out.println("DocID\tScore");
                                if (ranking == null || ranking.empty()) {
                                    System.out.println("No results found.");
                                } else {
                                    ranking.findFirst();
                                    while (ranking.retrieve() != null && !ranking.last()) {
                                        BasicIndexer.DocumentScore docScore = ranking.retrieve();
                                        System.out.printf("%d\t%d\n", docScore.docId, docScore.score);
                                        ranking.findNext();
                                    }

                                    // Print the last document score, if available
                                    if (ranking.retrieve() != null) {
                                        BasicIndexer.DocumentScore lastDocScore = ranking.retrieve();
                                        System.out.printf("%d\t%d\n", lastDocScore.docId, lastDocScore.score);
                                    }
                                }
                            }

                            // After query results are displayed, offer options to repeat or navigate back
                            System.out.println("\nDo you want to:");
                            System.out.println("1. Enter a new query");
                            System.out.println("2. Go back to Result Options interface");
                            System.out.println("3. Go back to Data Structure interface");
                            System.out.println("4. Exit the program");

                            int nextAction = input.nextInt();
                            input.nextLine();  // Consume the newline character

                            if (nextAction == 1) {
                                continue;  // Enter a new query without breaking out of the loop
                            } else if (nextAction == 2) {
                                runQuery = false;  // Exit to Result Options interface
                                break; // Exit the current query loop and return to Result Options
                            } else if (nextAction == 3) {
                                runQuery = false;
                                exitToDataStructure = true;  // Set flag to exit to Data Structure interface
                                break;
                            } else if (nextAction == 4) {
                                System.out.println("You have exited the program");
                                return;
                            } else {
                                System.out.println("Invalid choice, please try again.");
                            }
                        }
                    }

                    if (exitToDataStructure) {
                        break;  // Exit back to the Data Structure interface
                    }

                } while (true);

            } while (userDataStructure != -1);

        } catch (IOException e) {
            System.err.println("An error occurred while reading files: " + e.getMessage());
        } finally {
            input.close();
        }
    }
}
