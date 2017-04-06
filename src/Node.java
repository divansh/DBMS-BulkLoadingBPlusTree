import java.util.ArrayList;

import javax.xml.crypto.Data;

//http://www.sanfoundry.com/cpp-program-to-implement-b-tree/

public class Node {
	ArrayList<Node> ptr_list;
	ArrayList<Integer> Data;
	int order;
	int num_keys ;
	ArrayList<Node> parent_ptr;
	boolean isLeaf;
	public Node(int order, boolean isLeaf) {
		this.order= order;
		ptr_list = new ArrayList<>(order);
		Data = new ArrayList<>(order);
		parent_ptr  = new ArrayList<>();
		this.num_keys= 0;
		this.isLeaf = isLeaf;
	}
	
	public void  splitNode(Node node) {
		
		 
	}
	public void insert(Node root, int order, int key) {
		if(root==null)
		{
			root  = new Node(order,true);
			root.Data.add(key);
			root.num_keys++;
		}
		else
		{
			if(root.num_keys>=root.order-1) //if root full
			{
				Node newRoot = new Node(order,false);
				newRoot.ptr_list.add(root); //add pointer to root in ptr list 
				root.Data.add(key); // add new key to old root
				//root.Data.sort(arg0);  ~~~~~~~~~~~~~@!!!!!!!!!!!!!!!!! SORT HERE OR NOT CHECK
				Node child2 = new Node(order, true); //Create right child
				root.num_keys = root.Data.size()/2;
				for(int i=(root.Data.size()/2);i<root.Data.size();i++ )
				{
					child2.Data.add(root.Data.get(i));
				}
				newRoot.ptr_list.add(child2);//add right child to ptr list of new root	
				newRoot.Data.add(root.Data.get(root.order/2));
				System.out.println("New size of root: \n ptrs : "+ newRoot.ptr_list.size()+" \n keys: "+ newRoot.num_keys+" = "+ newRoot.Data.size());
			}
			else
			{
				
			}
		}
	}
	
	
	
	

}
