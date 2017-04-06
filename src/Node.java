import java.util.ArrayList;

import javax.xml.crypto.Data;

//http://www.sanfoundry.com/cpp-program-to-implement-b-tree/

public class Node {
	ArrayList<Node> ptr_list;
	ArrayList<Integer> Data;
	int num_pointer;
	ArrayList<Node> parent_ptr;
	public Node(int num_pointer) {
		this.num_pointer= num_pointer;
		ptr_list = new ArrayList<>(num_pointer);
		Data = new ArrayList<>(num_pointer-1);
		for(int i=0;i<num_pointer;i++)
		{
			ptr_list.set(i, null);
		}
		parent_ptr  = new ArrayList<>(num_pointer-1);
		for(int i=0;i<num_pointer-1;i++)
		{
			parent_ptr.set(i, null);
		}
	}
	
	public void  splitNode(Node node) {
		 Node newNode = new Node(node.num_pointer);
		 
	}
	
	
	

}
