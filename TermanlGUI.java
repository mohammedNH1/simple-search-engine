import java.util.InputMismatchException;
import java.util.Scanner;

public class TermanlGUI {

    public static int dataStructureInterface(Scanner input) {
        int userDataStructure = -1;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("______________________________________________");
                System.out.println("     [Welcome to the Data Structure Project]\n");
                System.out.println("Please choose the data structure you want to use: ");
                System.out.println("1. Basic Index");
                System.out.println("2. Inverted Index");
                System.out.println("3. Inverted Index with BSTs");
                System.out.println("\nChoose the data structure you want to use or enter -1 to exit: ");
                userDataStructure = input.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                input.next(); // Clear the invalid input
            }
        }
        return userDataStructure;
    }

    public static int resultOptionsInterface(Scanner input, String selectedDataStructure) {
        int userResultOption = -1;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("______________________________________________");
                System.out.println("     [Result Options]\n");
                System.out.println("Please choose the result you want: ");
                System.out.println("1. Boolean Retrieval");
                System.out.println("2. Ranked Retrieval");
                System.out.println("3. Indexed Documents");
                System.out.println("4. Indexed Tokens");
                System.out.println("\nChoose the result option you want or enter 0 to go back: ");
                userResultOption = input.nextInt();
                validInput = true;
    
                if (userResultOption != 0) {
                    switch (userResultOption) {
                        case 1:
                            System.out.println("You have selected {Boolean Retrieval} using [" + selectedDataStructure + "]");
                            break;
                        case 2:
                            System.out.println("You have selected {Ranked Retrieval} using [" + selectedDataStructure + "]");
                            break;
                        case 3:
                            System.out.println("You have selected {Indexed Documents} using [" + selectedDataStructure + "]");
                            break;
                        case 4:
                            System.out.println("You have selected {Indexed Tokens} using [" + selectedDataStructure + "]");
                            break;
                        default:
                            System.out.println("Invalid choice, please try again.");
                            userResultOption = -1; // Invalid choice
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                input.next(); // Clear the invalid input
            }
        }
        return userResultOption;
    }
    

    public static int navigationInterface(Scanner input) {
        int userNavigationOption = -1;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("\nDo you want to go back to:");
                System.out.println("1. Data Structure interface");
                System.out.println("2. Result Options interface");
                System.out.println("3. Exit the program");
                userNavigationOption = input.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                input.next(); // Clear the invalid input
            }
        }
        return userNavigationOption;
    }

    // Method to get the query from the user for Ranked or Boolean Retrieval
    public static String getSearchQuery(Scanner input) {
        System.out.print("Enter your search query (separate words with spaces): ");
        return input.nextLine().trim();
    }
}
