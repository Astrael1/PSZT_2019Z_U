package decisiontree;

import inputhandling.Record;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecisionTreeBuilder
{
    List<Pair<AttributeNode, Pair<Set<Record> , Set<Integer> > > > taskList = new LinkedList<>();

    double entrophy(Set<Double> doubles)
    {
        Double result = 0.0;

        for(double i: doubles)
        {
            result -= i * Math.log(i);
        }
        return result;
    }

    double info(Set<Record> recordSet)
    {
        Double possibility[] = new Double[2];
        possibility[0] = 0.0;
        possibility[1] = 0.0;

        for(Record record: recordSet)
        {
            possibility[record.getResultClass()]++;
        }
        possibility[0] /= recordSet.size();
        possibility[1] /= recordSet.size();

        Set<Double> possibilitySet = new HashSet<>();
        possibilitySet.add(possibility[0]);
        possibilitySet.add(possibility[1]);

        return entrophy(possibilitySet);
    }

    Vector<HashSet<Record>> splitByAttribute(Integer nrOfAttribute, Set<Record> recordSet)
    {
        // create vector of subsets
        Vector<HashSet<Record>> subsets = new Vector<>();
        for(int i = 0; i < 5; i++)
        {
            subsets.add(new HashSet<Record>());
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
        return entrophy(weights);
    }

    double gainRatio(Integer nrOfAttribute, Set<Record> recordSet)
    {
        Vector<HashSet<Record>> subsets = splitByAttribute(nrOfAttribute, recordSet);
        Integer recordSetCount = recordSet.size();

        return gain( recordSet, subsets) / splitInfo(recordSetCount, subsets);
    }

    void prepareNode(AttributeNode node, Set<Record> recordSet, Set<Integer> attributeSet)
    {
        Integer bestAttribute = 0;
        Double bestGainRatio = 0.0;
        for(Integer attributeNr: attributeSet)
        {
            double newRatio = gainRatio(attributeNr, recordSet);
            if( newRatio > bestGainRatio)
            {
                bestAttribute = attributeNr;
                bestGainRatio = newRatio;
            }
        }

        node.splitAttribute = bestAttribute;

        Set<Integer> newAttributeSet = new HashSet<>(attributeSet);
        newAttributeSet.remove(bestAttribute);

        Vector<HashSet<Record>> subsets = splitByAttribute(bestAttribute, recordSet);

        for(int i = 0; i < 5; i++)
        {
            AttributeNode nodeToAppend = new AttributeNode();

            nodeToAppend.ancestor = node;
            node.children.add(nodeToAppend);

            Pair<AttributeNode, Pair< Set<Record>, Set<Integer> > > anotherTask = new Pair<>(nodeToAppend, new Pair<>(subsets.get(i), newAttributeSet));
            this.taskList.add(anotherTask);
        }

    }

    public void build(Set<Record> recordSet)
    {
        taskList.clear();
        AttributeNode firstNode = new AttributeNode();
        Set<Integer> allAttributes = IntStream.range(0, 54).boxed().collect(Collectors.toSet());
        Pair<AttributeNode, Pair< Set<Record>, Set<Integer> > > firstTask = new Pair<>(firstNode, new Pair<>(recordSet, allAttributes));

        taskList.add(firstTask);
        while (!taskList.isEmpty())
        {
            Pair<AttributeNode, Pair< Set<Record>, Set<Integer> > > task = taskList.get(0);
            taskList.remove(0);

            prepareNode(task.getKey(), task.getValue().getKey(), task.getValue().getValue());
        }

    }


}
