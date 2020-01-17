package decisiontree;

import java.util.Vector;

public class Node
{
    boolean isLeaf;
    Integer decisionClass;
    Node ancestor;
    Integer splitAttribute;
    Vector<Node> children;


    public void makeLeaf(Integer decisionClass)
    {
        this.decisionClass = decisionClass;
        this.isLeaf = true;
        this.children = new Vector<>();
    }

    Node()
    {
        ancestor = null;
        isLeaf = false;
        decisionClass = -1;
        children = new Vector<>();
    }

    Node( Node other )
    {
        ancestor = other;
        isLeaf = false;
        decisionClass = -1;
        children = new Vector<>();
    }
}
