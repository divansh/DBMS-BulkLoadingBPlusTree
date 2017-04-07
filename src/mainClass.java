import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Scanner;

public class mainClass {
	
	public static void main(String[] args) throws IOException {
		
		Scanner in = new Scanner(System.in);
		int a;
		System.out.println("Enter input filename");
		String file= in.nextLine();
		System.out.println("External Sorting ");
		
		ExternalSort obj = new ExternalSort();
		obj.sortAndWrite(file);
		
		System.out.println("Enter 1 for manual entry 2 for automatic");
		a = in.nextInt();
		System.out.print(" Enter order: ");
		int order = in.nextInt();
		if(a==1)
		{
			System.out.println("TOP DOWN -----------------------------------");
				Block curr = null;
				Block dummy = new Block();
				while(a!=-1)
				{
					System.out.print("Enter element(-1 to end): ");
					a = in.nextInt();
					curr = Block.mainRoot;
					dummy.myinsert(a, order,curr);
					dummy.printer();
				}
				System.out.println("BOTTOM UP ---------------------------------");
				dummy= new Block();
				dummy.printBottomUp(dummy.insertBottomUp("final_sorted.out", order));
		}
		else
		{
			System.out.println("TOP DOWN -----------------------------------");
			Block curr = null;
			Block dummy = new Block();
			BufferedReader xReader = new BufferedReader(new FileReader("final_sorted.out"));
			String uString = xReader.readLine();
			while(uString!=null)
			{
				System.out.print("Entering element " + uString);
				curr = Block.mainRoot;
				dummy.myinsert(Integer.parseInt(uString), order,curr);
				dummy.printer();
				uString = xReader.readLine();
			}
			System.out.println("BOTTOM UP ---------------------------------");
			dummy= new Block();
			dummy.printBottomUp(dummy.insertBottomUp("final_sorted.out", order));
			
		}
		
	
	}

}