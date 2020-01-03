package decisiontree;

import java.util.Vector;

public class AttributeNode extends Node
{
    public Vector<Node> children;
    public Integer splitAttribute;
}
