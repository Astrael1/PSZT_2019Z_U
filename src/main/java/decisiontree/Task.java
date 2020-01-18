package decisiontree;

import inputhandling.Record;

import java.util.Set;

public class Task {
    private Node node;
    private Set<Record> recordSet;
    private Set<Integer> attributeSet;

    Task() {}

    Task(Node node, Set<Record> recordSet, Set<Integer> attributeSet) {
        this.node = node;
        this.recordSet = recordSet;
        this.attributeSet = attributeSet;
    }

    public Node getNode() {
        return node;
    }

    public Set<Record> getRecordSet() {
        return recordSet;
    }

    public Set<Integer> getAttributeSet() {
        return attributeSet;
    }

}
