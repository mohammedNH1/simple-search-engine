import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		
		boolean readBasic = true;
		boolean readInvert = true;
		boolean readInvertBST = true;
		boolean loop = true;
		BasicIndex basicIndex = new BasicIndex();
		InvertedIndex invertIndex = new InvertedIndex();
		InvertedIndexBST invertTree = new InvertedIndexBST();


		do {
			String csvFile = "dataset.csv";
			String stopFile = "stop.txt";
			System.out.println("------------------------------------------");
			System.out.println("Welcome to our data structre menu");
			System.out.println("Choose one of the following options:\n1-basic index\n2-invert index\n3-invert with bst\n4-show number of documents and vocab\n5-Exit");
			Scanner in = new Scanner(System.in);
			int option = in.nextInt();
			switch(option) {
			case 1: // Basic
				if(readBasic) {
					basicIndex.readCsv(csvFile, stopFile);
					readBasic = false;
				}
				
				secondOption();
				int secOption1 = in.nextInt();
				query();
				in.nextLine();
				String query = in.nextLine();
				switch(secOption1) {
				case 1:  // boolean
					printRes(basicIndex.BR(query));
					break;
				case 2: // ranking
					
					basicIndex.rankingRetrieval(query); 
					break;
				} 
				break; 
			case 2: // invert
				if(readInvert) {
						invertIndex.readCsv(csvFile, stopFile);
						readInvert = false;
				}
				secondOption();
				int secOption2 = in.nextInt();
				query();
				in.nextLine();
				 query = in.nextLine();
				switch(secOption2) {
				case 1:  // boolean
					printRes(invertIndex.invertBR(query));
					break;
				case 2: // ranking
					
					invertIndex.rankingRetrieval(query);
					
				}
				
				break;
			case 3:	//BST
				if(readInvertBST) {	
					invertTree.readCsv(csvFile, stopFile);
					readInvertBST = false;
			}
				secondOption();
				int secOptionTree = in.nextInt();
				query();
				in.nextLine();
			    query = in.nextLine();
				switch(secOptionTree) {
				case 1:  // boolean
					printRes(invertTree.BR(query));
					break;
				case 2: // ranking
					
					invertTree.rankingRetrieval(query);
					break;
				}
				break;
			case 4: // doc , vocab
				if(readBasic) {
					basicIndex.readCsv(csvFile, stopFile);
					readBasic = false;
				}
				if(readInvert) {
					invertIndex.readCsv(csvFile, stopFile);
					readInvert = false;
				}
				System.out.println("Number of documents is: "+basicIndex.getDocuments()); // -1 because of the header
				System.out.println("Number of vocab is: "+invertIndex.getSize());
				break;
			case 5:
				loop = false;
				break;
			}
			
		}while(loop);
		System.out.println("Bye!");
	
	}
	public static void query() {
		System.out.print("Enter a query: ");
	}
	public static void secondOption() {
		System.out.println("choose one of the following:");
		System.out.println("1-boolean retrieval");
		System.out.println("2-ranking retrieval");
	}
	public static void printRes(LinkedList<String> result) {
	System.out.print("----------------------------------\nResult doc IDs: {");
	result.findFirst();
	while (!result.last()) {
	System.out.print( result.retrieve()+", ");
	result.findNext();
}
	System.out.println( result.retrieve()+"}");
	}
}

