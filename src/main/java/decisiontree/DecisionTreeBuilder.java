package decisiontree;

import inputhandling.Record;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecisionTreeBuilder
{
    private List<Task> taskList = new LinkedList<>();

    private Integer mostAccurateDecision;

    double entropy(Set<Double> doubles)
    {
        double result = 0.0;

        for(double i: doubles)
        {
            result -= i == 0.0 ? 0.0 :  i * Math.log(i);
        }
        return result;
    }

    double info(Set<Record> recordSet)
    {
        if(recordSet.isEmpty())
            return 0.0;
        Double[] probability = new Double[2];
        probability[0] = 0.0;
        probability[1] = 0.0;

        for(Record record: recordSet)
        {
            probability[record.getResultClass()]++;
        }
        probability[0] /= recordSet.size();
        probability[1] /= recordSet.size();

        Set<Double> probabilitySet = new HashSet<>();
        probabilitySet.add(probability[0]);
        probabilitySet.add(probability[1]);

        return entropy(probabilitySet);
    }

    Vector<HashSet<Record>> splitByAttribute(Integer nrOfAttribute, Set<Record> recordSet)
    {
        // create vector of subsets
        Vector<HashSet<Record>> subsets = new Vector<>();
        for(int i = 0; i < 5; i++)
        {
            subsets.add(new HashSet<>());
        }

        // distribute records according to value of attribute 'nrOfAttribute'
        for(Record record: recordSet)
        {
            subsets.get(record.getAttribute(nrOfAttribute)).add(record);
        }

        return subsets;
    }

    double info( Integer recordSetCount, Vector<HashSet<Record>> subsets)
    {
        double result = 0.0;

        // calculate the result
        for(int i = 0; i <= 4; i++)
        {
            double tmp = ((double) subsets.get(i).size())/recordSetCount;
            tmp *= info(subsets.get(i));
            result += tmp;
        }

        return result;
    }

    double gain(Set<Record> recordSet, Vector<HashSet<Record>> subsets)
    {
        return info(recordSet) - info(recordSet.size(), subsets);
    }

    double splitInfo( Integer recordSetCount, Vector<HashSet<Record>> subsets)
    {
        Set<Double> weights = new HashSet<>();

        for(Set set: subsets)
        {
            weights.add(((double)set.size())/recordSetCount);
        }
        return entropy(weights);
    }

    double gainRatio(Integer nrOfAttribute, Set<Record> recordSet)
    {
        Vector<HashSet<Record>> subsets = splitByAttribute(nrOfAttribute, recordSet);
        Integer recordSetCount = recordSet.size();

        return gain( recordSet, subsets) / splitInfo(recordSetCount, subsets);
    }

    void prepareNode(Node nodeToProcess, Set<Record> recordSet, Set<Integer> attributeSet)
    {
        if(recordSet.isEmpty())
        {
            nodeToProcess.makeLeaf(mostAccurateDecision);
            return;
        }

        Integer[] classOccurrences = new Integer[2];
        classOccurrences[0] = 0;
        classOccurrences[1] = 0;
        for (Record record: recordSet)
        {
            Integer decisionClass = record.getResultClass();
            classOccurrences[decisionClass]++;
        }

        if(classOccurrences[0] == 0 || classOccurrences[1] == 0)
        {
            nodeToProcess.makeLeaf(classOccurrences[0] != 0 ? 0 : 1);
            return;
        }

        this.mostAccurateDecision = classOccurrences[0] > classOccurrences[1] ? 0 : 1;

        Integer bestAttribute = 0;
        double bestGainRatio = 0.0;
        for(Integer attributeNr: attributeSet)
        {
            double newRatio = gainRatio(attributeNr, recordSet);
            if( newRatio > bestGainRatio)
            {
                bestAttribute = attributeNr;
                bestGainRatio = newRatio;
            }
        }

        nodeToProcess.splitAttribute = bestAttribute;

        Set<Integer> newAttributeSet = new HashSet<>(attributeSet);
        newAttributeSet.remove(bestAttribute);

        Vector<HashSet<Record>> subsets = splitByAttribute(bestAttribute, recordSet);

        for(int i = 0; i < 5; i++)
        {
            Node nodeToAppend = new Node( nodeToProcess );

            nodeToProcess.children.add(nodeToAppend);

            Task anotherTask = new Task(nodeToAppend, subsets.get(i), newAttributeSet);
            this.taskList.add(anotherTask);
        }

    }

    public Node build(Set<Record> recordSet)
    {
        taskList.clear();
        Node firstNode = new Node();
        Set<Integer> allAttributes = IntStream.range(0, 54).boxed().collect(Collectors.toSet());
        Task firstTask = new Task(firstNode, recordSet, allAttributes);

        taskList.add(firstTask);
        while (!taskList.isEmpty())
        {
            Task task = taskList.get(0);
            taskList.remove(0);

            prepareNode(task.getNode(), task.getRecordSet(), task.getAttributeSet());
        }
        return firstNode;
    }

    private Node tryToPrune(Node node, Set<Record> pruneSet) throws CloneNotSupportedException {
        Node root = node.getRoot();
        Node newNode = (Node) node.clone();
        newNode.prune(pruneSet);
        Node ancestor = node.ancestor;
        int childIndex = ancestor.getChildIndex(node);

        double oldRes = root.evaluateDataSet(pruneSet);

        ancestor.children.set(childIndex, newNode);
        double newRes = root.evaluateDataSet(pruneSet);
        if (newRes < oldRes) {
            ancestor.children.set(childIndex, node);
        }
        return root;
    }

    public Node pruneTree(Node node, Set<Record> pruneSet) throws CloneNotSupportedException {
        if(node.areChildrenLeaves() && !node.isLeaf) {
            node = tryToPrune(node, pruneSet);
            if(node.ancestor != null) {
                pruneTree(node.ancestor, pruneSet);
            }
        }
        else {
            for(Node i : node.children) {
                if(!i.isLeaf && i.areChildrenLeaves()) {
                    pruneTree(i, pruneSet);
                }
            }
        }
        return node;
    }
}
