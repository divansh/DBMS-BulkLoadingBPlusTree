import java.awt.List;
import java.lang.reflect.Array;
import java.security.cert.X509CRLEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.security.auth.x500.X500Principal;
import javax.swing.RootPaneContainer;
import javax.swing.text.AbstractDocument.LeafElement;

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
	public void splitLeaf(Block leaf,int order )//num_ptr = order
	{
		System.out.println("inside leaf splitter");
		System.out.println("full leaf = "+ leaf.dataList.toString());
		System.out.println("root = "+mainRoot.dataList.toString());
		System.out.println("Splitting leaf");
		
		//not excess
		int total_keys = order-1;
		int l = ((total_keys+1)/2);
		
		Block child2 = new Block();
		child2.num_keys = total_keys - l;
		leaf.num_keys = l;
		child2.parent = leaf.parent;
		for(int i=leaf.num_keys;i<total_keys;i++){
			child2.dataList.add(leaf.dataList.get(i));
		}
		//CLEAR
		int median = child2.dataList.get(0);
		System.out.println("median = "+median + " leftchild without deleting = "+ leaf.dataList.toString()+" right = "+ child2.dataList.toString() );
		if(leaf.parent==null)
		{
			System.out.println("leaf is root");
			Block new_root = new Block();
			new_root.num_keys++;//==1
			new_root.dataList.add(median);
			
			new_root.ptr_arr.add( leaf);
			new_root.ptr_arr.add( child2);
			child2.parent = new_root;leaf.parent = new_root;
			mainRoot = new_root;
			System.out.println("mainroot = "+ mainRoot.dataList.toString());
		}
		else
		{
			//SORT HERE !!!!! IF NECESSARY AFTER TESTING AND REARRANGE CHILD PTRS TOO
			System.out.println("Leaf not root");
			leaf.parent.dataList.add(median);
			leaf.parent.ptr_arr.add(child2);
			leaf.parent.num_keys++;
			System.out.println("leaf parent = "+ leaf.parent.dataList.toString());
			//child2.parent = leaf.parent;  --> not needed
		}
		leaf.dataList.subList(l, total_keys).clear();
		System.out.println("leaf after clearing = "+ leaf.dataList.toString());
		splitNonLeaf(leaf.parent, order);
		return;
		
	}
	
	
	public void splitNonLeaf(Block node , int order)
	{
		if(node.num_keys<order){
			System.out.println("Found unfilled block");
			return;
		}
		//excess keys here
		Block rightChild = new Block();
		int middle = node.num_keys/2;
		for(int i=middle+1;i<node.num_keys;i++)
		{
			rightChild.dataList.add(node.dataList.get(i));			
		}
		//update ptrs
		for(int i=middle+1;i<node.num_keys;i++)
		{
			rightChild.ptr_arr.add(node.ptr_arr.get(i));
			node.ptr_arr.get(i).parent = rightChild;//UPDATE CHILDREN
		}
		
		int median= node.dataList.get(middle);
		node.num_keys =  middle;
		node.dataList.subList(middle, node.num_keys).clear();
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
			node.parent.dataList.add(median);
			node.parent.ptr_arr.add(rightChild);
			node.parent.num_keys++;
			splitNonLeaf(node.parent, order);
		}
		return;		
	}
	
	public int bsearch(ArrayList<Integer>data , int key) {
		int i = Collections.binarySearch(data, key);
		System.out.println("i = "+i);
		if(i>0)
		{
			return i+1;
		}
		i = -1*(i+1);
		return i;
		
		
	}
	public void insert(int key, int order, Block curr) {
		
		System.out.println("curr = "+ curr.dataList.toString());
//		if(root==null)
//		{
//			root.dataList.add(key);
//			root.num_keys++;
//			return;
//		}
		int index = bsearch(curr.dataList, key);
		
		if(curr.ptr_arr.size()==0) //==0??
		{
			System.out.println("LEAF = "+ curr.dataList.toString() );
			if(curr.num_keys==order-1)
			{
				System.out.println("full leaf split now = "+ curr.dataList.toString());
				splitLeaf(curr, order);
				System.out.println("split finish curr = "+ curr.dataList.toString()+ " mainroot = "+ mainRoot.dataList.toString());
				index  = bsearch(curr.parent.dataList, key);
				Block tBlock =curr.parent.ptr_arr.get(index);
				tBlock.dataList.add(key);
				tBlock.num_keys++;
				System.out.println("New right node = "+ tBlock.dataList.toString());
				if(curr.parent.num_keys==order)
				{
					System.out.println("full parent = " + curr.parent.dataList.toString()+" splitting");
					splitNonLeaf(curr.parent, order);
					
				}
				return;
			}
			else
			{
				System.out.println("leaf was not filled");
				curr.dataList.add(key);
				curr.num_keys++;
				System.out.println("new leaf = "+ curr.dataList.toString());
				return;
			}
		}
		else
		{
			System.out.println("Not leaf");
			index = bsearch(curr.dataList, key);
			System.out.println("Going down to :");
			System.out.println(curr.ptr_arr.get(index).dataList.toString());
			insert(key, order, curr.ptr_arr.get(index));
		}
	}
	
	public void printTree(ArrayList<Block>aList) {
		for(int i=0;i<aList.size();i++)
		{
			System.out.print(aList.get(i).dataList.toString());
		}
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
	
	public static void main(String[] args) {
		
		Block curr = null;
		Block dummy = new Block();
		while(true)
		{
			Scanner in = new Scanner(System.in);
			int a = in.nextInt();
			System.out.println("to insert: "+a );
			curr = mainRoot;
			dummy.insert(a, 4,curr);
			dummy.printTree(4, curr);
		}
		
	}

}
