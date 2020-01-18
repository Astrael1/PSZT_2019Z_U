package decisiontree;

import inputhandling.Record;

import java.util.Set;
import java.util.Vector;

public class Node {
    boolean isLeaf;
    Integer decisionClass;
    Node ancestor;
    Integer splitAttribute;
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
}