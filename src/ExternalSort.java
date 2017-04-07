import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.PriorityQueue;

import javax.print.DocFlavor.INPUT_STREAM;
//http://exceptional-code.blogspot.in/2011/07/external-sorting-for-sorting-large.html
public class ExternalSort {

	int maxArrSz = 5;//(Integer.MAX_VALUE -5);
	long fileSize = 0; // in bytes // equate to num records
	long num_records =0;
	public void sortAndWrite(String fileName) throws IOException {

//		File file = new File(fileName);
//		fileSize =  file.length();
//		fileSize = (long)Math.ceil( (( double) fileSize)/Integer.BYTES);
//		System.out.println(fileSize);
		BufferedReader temp = new BufferedReader(new FileReader(fileName));
		String xString  = temp.readLine();
		while(xString!=null){
			fileSize++;
			xString = temp.readLine();
		}
		BufferedReader bReader = new BufferedReader(new FileReader(fileName));	

		int [] arr = new int[maxArrSz];
		long num_runs=0;
		if(maxArrSz>=fileSize)
		{
			num_runs = 1;
		}
		else
		{
			if(fileSize%maxArrSz==0)num_runs = fileSize/maxArrSz;
			else num_runs = (fileSize/maxArrSz)+1;
		}

		int arr_sz=0;
		String iString=bReader.readLine();
		int i=0;
		while(iString!=null )
		{
			if(arr_sz>=maxArrSz)
			{
				System.out.println("arr_sz = max_sz = "+ arr_sz);
				Arrays.sort(arr);
				String temp_file = "file";
				BufferedWriter bWriter = new BufferedWriter(new FileWriter(temp_file+Integer.toString(i)+".out"));
				i++;
				for(int j=0;j<arr_sz;j++)
				{
					bWriter.write(Integer.toString(arr[j])+"\n");
					num_records++;
				}
				bWriter.close();
				arr_sz=0;	
			}
			else
			{
				System.out.println("adding "+iString);
				arr[arr_sz]= Integer.parseInt(iString);
				arr_sz++;
				iString  = bReader.readLine();
			}
		}
		if(arr_sz>0){
			Arrays.sort(arr);
			String temp_file = "file";
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(temp_file+Integer.toString(i)+".out"));
			i++;
			for(int j=0;j<arr_sz;j++)
			{
				bWriter.write(Integer.toString(arr[j])+"\n");
				num_records++;
			}
			bWriter.close();

			bReader.close();		
		}
		int num_files = i;
		//MERGE one pass
		//Create priority queue
		PriorityQueue<Pair> pQueue = new PriorityQueue<>(num_files);
		BufferedReader[] barr = new BufferedReader[num_files];
		for(i=0;i<num_files;i++)
		{
			barr[i] = new BufferedReader(new FileReader("file"+String.valueOf(i)+".out"));
		}		
		for(i=0;i<num_files;i++)
		{
			String rString = barr[i].readLine();
			if(rString!=null)
			pQueue.add(new Pair(Integer.parseInt(rString),i));
		}
		BufferedWriter bWriter = new BufferedWriter(new FileWriter("final_sorted.out"));
		while(num_records>0)
		{
			Pair ans = pQueue.poll();
			bWriter.write(Integer.toString(ans.a)+"\n");
			int file_num = ans.b;
			String next= barr[file_num].readLine();
			if(next!=null)
			{
				pQueue.add(new Pair(Integer.parseInt(next), file_num));
			}
			else {
				pQueue.add(new Pair(Integer.MAX_VALUE, -1));
			}
			num_records--;
		}
		
		bWriter.close();
	}
	
		
	
	public static void main(String[] args) throws IOException {
		ExternalSort obj = new ExternalSort();
		obj.sortAndWrite("input.in");
	}

}
