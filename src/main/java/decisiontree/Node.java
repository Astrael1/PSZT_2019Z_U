package decisiontree;

import inputhandling.Record;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Node implements Cloneable {
    boolean isLeaf;
    int decisionClass;
    Node ancestor;
    int splitAttribute;
    Vector<Node> children;


    Node() {
        ancestor = null;
        isLeaf = false;
        decisionClass = -1;
        children = new Vector<>();
    }

    Node(Node other) {
        ancestor = other;
        isLeaf = false;
        decisionClass = -1;
        children = new Vector<>();
    }

    public void makeLeaf(Integer decisionClass) {
        this.decisionClass = decisionClass;
        this.isLeaf = true;
        this.children = new Vector<>();
    }

    public int classify(Record record) {
        if (this.isLeaf) {
            return decisionClass;
        } else {
            int a = record.getAttribute(this.splitAttribute);
            Node child = this.children.get(a);
            return child.classify(record);
        }
    }

    public boolean evaluate(Record record) {
        return classify(record) == record.getResultClass();
    }

    public double evaluateDataSet(Set<Record> dataSet) {
        double positiveCount = 0.0;
        for (Record i : dataSet) {
            if (evaluate(i)) {
                positiveCount += 1;
            }
        }
        return positiveCount / dataSet.size();
    }

    public void printTree(int attrV, int lvl) {
        for(int i = 0; i < lvl; i++) {
            System.out.print("  ");
        }
        if(this.isLeaf) {
            System.out.println("(" + attrV + ")C:" + decisionClass);
        }
        else {
            System.out.println("(" + attrV + ")S:" + splitAttribute);
            for(int i = 0; i < children.size(); i++) {
                children.get(i).printTree(i,lvl+1);
            }
        }
    }

    public int determineClass(Set<Record> data) {
        int count0 = 0;
        int count1 = 0;
        for(Record i : data) {
            if(i.getResultClass() == 1) {
                count1++;
            }
            else {
                count0++;
            }
        }
        return count0 > count1 ? 0 : 1;
    }

    public Node getRoot() {
        if(this.ancestor == null) {
            return this;
        }
        else {
            return this.ancestor.getRoot();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        Node result = (Node)super.clone();
        result.ancestor = this.ancestor;
        result.decisionClass = this.decisionClass;
        result.isLeaf = this.isLeaf;
        result.children = new Vector<>();
        if(!this.isLeaf) {
            for (int i = 0; i < 5; i++) {
                result.children.insertElementAt((Node) this.children.elementAt(i).clone(), i);
            }
        }
        return result;
    }
    public boolean areChildrenLeaves() {
        for(int i = 0; i < 5; i++) {
            if(!children.get(i).isLeaf) {
                return false;
            }
        }
        return true;
    }

    public void prune(Set<Record> dataSet) {
        this.makeLeaf(this.determineClass(getRecordsAtNode(dataSet)));
    }

    public int getChildIndex(Node node) {
        for(int i = 0; i < 5; i++) {
            if(node == children.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public Set<Record> getRecordsAtNode(Set<Record> input) {
        if(this.ancestor == null) {
            return input;
        }
        else {
            HashSet<Record> subset = new HashSet<>();
            for(Record i : input) {
                if(i.getAttribute(this.ancestor.splitAttribute) == this.ancestor.getChildIndex(this)) {
                    subset.add(i);
                }
            }
            return this.ancestor.getRecordsAtNode(subset);
        }
    }
}