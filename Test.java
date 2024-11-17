
public class Test {

	public static void main(String[] args) {

		String csvFile = "dataset.csv";
		String stopFile = "stop.txt";
		BasicIndex BI = new BasicIndex();
		BI.readCsv(csvFile, stopFile);
		//BI.printtt();
		
		int count = 1;
		System.out.println("----------------------------------");
		LinkedList<String> result = BI.BR("market OR sports");
		result.findFirst();
		while (!result.last()) {
			System.out.println("Result ID:" + count++ + " ---> " + result.retrieve());
			result.findNext();
}
		System.out.println("Result ID:" + count++ + " ---> " + result.retrieve());
	}
}
