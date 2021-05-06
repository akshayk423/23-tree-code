

import java.util.ArrayList;

public class Tree
{
	private Tree parent;
	private ArrayList<Integer> keys;
	private ArrayList<Tree> child;
	
	public Tree() 
	{
		keys = new ArrayList<Integer>();
		child = new ArrayList<Tree>();
	}
	
	public boolean insert(int x)
	{
		if(keys.contains(x))
			return false;
		if(this.isLeaf())
		{
			keys.add(x);
		    sortKeys();
		    split();
			return true;	    	
		}
		
		for(int i = 0; i < keys.size(); i++)
		{
			if(keys.get(i) > x)
				return child.get(i).insert(x);
		}
		return child.get(keys.size()).insert(x);
	}
	
	//DUMMY METHOD (STILL NEEDS WORK)
	public int get(int x)
	{
		if(x > size())
			return 0;
		
		if((x-size() <= 0) && (child.isEmpty()))
		{
			int index = size();
			int sizeDifference = x-size();
			while(sizeDifference < 0)
			{
				sizeDifference = sizeDifference + 1;
				index--;
			}
			return keys.get(index);	
		}
		
		int difference = 0;
		int childIndex = 0; 
		int prevSize = 0;
		for(int i = 0; i < child.size(); i++)
		{
			childIndex = i;
			difference = (x - child.get(i).size()) - prevSize -  childIndex;
			prevSize = prevSize + child.get(i).size();
			if(difference == 0)
				return keys.get(childIndex);
			if(difference < 0)
				break;
		}
		int z = 0;
		int sub = 0;
		while(z < childIndex)
		{
			sub = sub + child.get(z).size();
			z++;
		}
		return child.get(childIndex).get(x - sub - childIndex);
		
	}
	
	
	
	public int size()
	{
		// Leaf node
		if(isLeaf())
			return keys.size();
		// Node with two children
		if(child.size() == 2)
		{
			return keys.size() + child.get(0).size() + child.get(1).size();
		}
		// Node with three children
		else
			return keys.size() + child.get(0).size() + child.get(1).size() + child.get(2).size();	
	}
	
	public int size(int x)
	{
		if(searchNode(x) != null) 
			return searchNode(x).size();
		else
			return 0;
	}

	 private boolean isLeaf()
	 {
		 if(child.isEmpty())
	         return true;
		 else 
	        	 return false;
	 }
	 
	private Tree searchNode(int x)
	{
		for(int i = 0; i < keys.size(); i++)
		{
			if(keys.get(i) == x)
			{
				return this;
			}
		}
		
		if(!isLeaf())
		{
			for(int i = 0; i < keys.size(); i++)
			{
				if((x < keys.get(i)))
					return child.get(i).searchNode(x);
			}
			return child.get(keys.size()).searchNode(x);
		}
		return null;
	}
	
	private void sortKeys()
	{
		if(size() > 1)
		{
			int index = keys.size() - 1;
			int insertedKey = keys.get(index);
			int previousKey = keys.get(index - 1);
			while((insertedKey < previousKey))
			{
				keys.set(index - 1, insertedKey);
				keys.set(index, previousKey);
				index = index - 1;
				if(index == 0)
					break;
				previousKey = keys.get(index - 1);
			}
			
		}
	}
	

	private void addChild(Tree node)
	{
		child.add(node);
		node.parent = this;
	}
	
	private void removeChild(int index)
	{
		child.get(index).parent = null;
		child.remove(index);
	}
	
	
	private void sortChildren()
	{
		for(int i = 1; i< child.size(); i++)
		{
			Tree currentChild = child.get(i);
			int childNodeKey = child.get(i).keys.get(0);
			int j = i -1;
			while((j >= 0) && (child.get(j).keys.get(0) > childNodeKey))
			{
				Tree previousChild = child.get(j);
				child.set(j +1, previousChild);
				j--;
			}
			child.set(j + 1, currentChild);
		}
	}
	
	private void bubbleUp()
	{
		Tree parentNode = this.parent;
		parentNode.addChild(child.get(0));
		parentNode.addChild(child.get(1));
		parentNode.sortChildren();
		parentNode.removeChild(parent.child.indexOf(this));
		parentNode.keys.add(keys.get(0));
		parentNode.sortKeys();
		if((parentNode.keys.size() == 3))
		{
			parentNode.splitParent();
		}
		
	}
	
	
	private void split()
	{
		if(this.keys.size() == 3) 
		{
			Tree leftSubTree = new Tree();
			Tree rightSubTree = new Tree();
			leftSubTree.keys.add(this.keys.get(0));
			rightSubTree.keys.add(this.keys.get(2));
			this.addChild(leftSubTree);
			this.addChild(rightSubTree);
			this.keys.remove(2);
			this.keys.remove(0);
			if(this.parent != null)
				this.bubbleUp();
		}
	}
	
	private void splitParent()
	{
		if((keys.size() == 3) && (child.size() == 4))
		{
			Tree left = new Tree();
			Tree right = new Tree();
			left.keys.add(keys.get(0));
			right.keys.add(keys.get(2));
			keys.remove(2);
			keys.remove(0);
			left.addChild(child.get(0));
			left.addChild(child.get(1));
			right.addChild(child.get(2));
			right.addChild(child.get(3));
			child.removeAll(child);
			addChild(left);
			addChild(right);
		}
		if(parent != null)
		{
			bubbleUp();
		}
	}
	
}
