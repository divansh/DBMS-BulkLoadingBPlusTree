import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * 
 * No duplicates allowed
 * reference:
 * https://github.com/Sayef/bplus-tree/blob/6af1557cb9e61dcd2e2cb506c08d77dfe8b00e92/BPlusTree.cpp
 * 
 * @author divansh
 *
 */


public class Block {
	public static Block mainRoot= new Block();
	int num_keys;
	Block parent;
	ArrayList<Block> ptr_arr;
	ArrayList<Integer> dataList;
	public Block() {
		
		num_keys = 0;
		parent = null;
		ptr_arr = new ArrayList<>();
		dataList = new ArrayList<>();
	}
	
	
	
	
	public Block splitLeaf(Block curr,int order )
	{
		Block leaf = curr;
		if(leaf.num_keys<=order-1)
		{
			return leaf;
		}
		int total_keys = order-1;
		int l = ((total_keys+1)/2);
		Block child2 = new Block();
		child2.num_keys = total_keys - l+1;
		leaf.num_keys = l;
		child2.parent = leaf.parent;
		for(int i=leaf.num_keys;i<total_keys+1;i++){
			child2.dataList.add(leaf.dataList.get(i));
		}

		int median = child2.dataList.get(0);
		leaf.dataList.subList(l, total_keys+1).clear();
		if(leaf.parent==null)
		{
			Block new_root = new Block();
			new_root.num_keys++;//==1
			new_root.dataList.add(median);	
			new_root.ptr_arr.add( leaf);
			new_root.ptr_arr.add( child2);
			child2.parent = new_root;
			leaf.parent = new_root;
			mainRoot = new_root;
		}
		else
		{
			leaf.parent.dataList.add(median);
			leaf.parent.ptr_arr.add(child2);
			leaf.parent.num_keys++;
			child2.parent = leaf.parent;  //--> not needed
		}
		return leaf ;
		
	}
	
	
	public void splitNonLeaf(Block node , int order)
	{
		if(node.num_keys<order){
			return;
		}
		
		Block rightChild = new Block();
		int middle = node.num_keys/2;
		rightChild.parent = node.parent;
		for(int i=middle+1;i<node.num_keys;i++)
		{
			rightChild.dataList.add(node.dataList.get(i));
			rightChild.num_keys++;
		}
		for(int i=middle+1;i<=node.num_keys;i++)
		{
			rightChild.ptr_arr.add(node.ptr_arr.get(i));
			node.ptr_arr.get(i).parent = rightChild;//UPDATE CHILDREN
		}
			
		int median= node.dataList.get(middle);
		//check
		//System.out.println("midle = "+ middle +"num keys = "+ node.num_keys);
		
		node.dataList.subList(middle, node.num_keys).clear();
		//System.out.println("ptr arr sz ### "+ node.ptr_arr.size());
		node.ptr_arr.subList(middle, node.num_keys+1).clear();
		//System.out.println("ptr arr sz ### "+ node.ptr_arr.size());

		node.num_keys =  middle;
		//System.out.println("N!!!!!!1111");
		//System.out.println(node.dataList.toString());
		if(node.parent==null)
		{
			Block new_root = new Block();
			new_root.dataList.add(median);
			new_root.ptr_arr.add(node);
			new_root.ptr_arr.add(rightChild);
			new_root.num_keys++;
			node.parent = new_root;
			rightChild.parent = new_root;
			mainRoot = new_root;
			return;
		}
		else
		{	
			//System.out.println("not root");
			//System.out.println("node = "+ node.dataList.toString());
			node.parent.dataList.add(median);
			node.parent.ptr_arr.add(rightChild);
			node.parent.num_keys++;
		}
		
		return;		
		
	}
	
	public int bsearch(ArrayList<Integer>data , int key) {
		int i = Collections.binarySearch(data, key);
		//System.out.println("i = "+i);
		if(i>0)
		{
			return i+1;
		}
		i = -1*(i+1);
		return i;
		
		
	}
	public void insert(int key, int order, Block curr) {
		
		if(curr.ptr_arr.size()==0)
		{
			if(curr.num_keys==order-1)
			{
				curr.num_keys++;
				curr.dataList.add(key);
				splitLeaf(curr, order);
				printer();
				if(curr.parent.num_keys==order)
				{
					splitNonLeaf(curr.parent, order);
				}
				return;
			}
			else
			{
				curr.dataList.add(key);
				curr.num_keys++;
				return;
			}
		}
		else
		{
			printer();
			insert(key, order, curr.ptr_arr.get(curr.ptr_arr.size()-1));
			
		}
		return;
	}
	
	public void myinsert(int key , int order, Block curr) {
		if(mainRoot.dataList.isEmpty())
		{
			mainRoot.dataList.add(key);
			mainRoot.num_keys++;
			return;
		}
		curr = mainRoot;
		
		while(curr.ptr_arr.size()!=0)
		{
			curr = curr.ptr_arr.get(curr.num_keys);
		}
		//if(curr.dataList.get(num_keys-1)==key)return;
		curr.dataList.add(key);
		curr.num_keys++;
		if(curr.num_keys>=order)
		{
			Block xBlock = splitLeaf(curr, order);
			curr = xBlock.parent;
			while(curr!=null && curr.num_keys>=order )
			{
				splitNonLeaf(curr, order);
				curr = curr.parent;
			}

		}
		
		
	}
	
	
	public void printer() {
		ArrayList<Block> aList = new ArrayList<>();
		aList.add(mainRoot);
		System.out.println("TREE=====================TREE");
		printTree(aList);
		System.out.println("TREE=====================TREE");
	}
	public void printTree(ArrayList<Block>aList) {
		if(aList.size()==0)return;
		for(int i=0;i<aList.size();i++)
		{
			System.out.print(aList.get(i).dataList.toString());
//			if(aList.get(i).parent!=null){
//			System.out.print(" ((p = "+ aList.get(i).parent.dataList.toString()+ "))" );
//			}
		}
		System.out.println();
		ArrayList<Block> xList = new ArrayList<>();
		for(int i=0;i<aList.size();i++)
		{
			for(int j=0;j<aList.get(i).ptr_arr.size();j++)
			{
				xList.add(aList.get(i).ptr_arr.get(j));
			}
		}
		aList.clear();
		printTree(xList);	
	}	
	
	public Block  insertBottomUp (String filename, int order) throws IOException {
			BufferedReader bReader = new BufferedReader(new FileReader(filename));
			ArrayList<Block> aList = new ArrayList<>();
			Block curr = new Block();
			String line = bReader.readLine();
			while(line!=null)
			{
				if(curr.dataList.size()==order-1)
				{
					aList.add(curr);
					curr = new Block();
				}
				curr.dataList.add(Integer.parseInt(line));
				line = bReader.readLine();
			}
			if(curr.dataList.size()>0)
			{
				aList.add(curr);
			}
			bReader.close();
//			System.out.println("!!@#$%$##@@@");
//			for(int i=0;i<aList.size();i++)System.out.print(aList.get(i).dataList.toString()+"  ");
			Block root =  builder(aList, order).get(0);
			return root;
	}
	
	public ArrayList<Block> builder(ArrayList<Block>aList, int order)
	{
		if(aList.size()==1)
		{
			return aList;
		}
		ArrayList<Block> nList = new ArrayList<>();
		Block curr = new Block();
		for(int i=0;i<aList.size();i++)
		{
			if(curr.dataList.size()==order-1)
			{
				nList.add(curr);
				curr = new Block();
			}
		
				curr.dataList.add(aList.get(i).dataList.get(0));
				curr.ptr_arr.add(aList.get(i));
		}
		if(curr.dataList.size()>0)
		{
			nList.add(curr);
		}
		return builder(nList, order);
	}
	
	public void printBottomUp(Block root) {
		ArrayList<Block> aList = new ArrayList<>();
		aList.add(root);
		System.out.println("TREE=====================TREE");
		printTree(aList);
		System.out.println("TREE=====================TREE");
	}
	
	public static void main(String[] args) throws IOException {
		
//		Block curr = null;
		Block dummy = new Block();
//		while(true)
//		{
//			Scanner in = new Scanner(System.in);
//			int a = in.nextInt();
//			curr = mainRoot;
//			dummy.myinsert(a, 4,curr);
//			dummy.printer();
//		}		
		dummy.printBottomUp(dummy.insertBottomUp("final_sorted.out", 4));
	}
}