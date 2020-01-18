import decisiontree.DecisionTreeBuilder;
import decisiontree.Node;
import inputhandling.*;

import java.util.List;
import java.util.Set;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        CustomCSVReader cr = new CustomCSVReader();
        Set<Record> dataSet = cr.read(System.getProperty("user.dir") + "/src/main/resources/divorce.csv");
        List<Set<Record>> data = cr.splitSets(dataSet);
        Set<Record> trainSet = data.get(0);
        Set<Record> pruneSet = data.get(1);
        Set<Record> testSet = data.get(2);
        System.out.println("sizes " + trainSet.size() + " " + pruneSet.size() + " " + testSet.size());
        DecisionTreeBuilder theBuilder = new DecisionTreeBuilder();
        Node root = theBuilder.build(trainSet);
        root.printTree(0,0);
        double res = root.evaluateDataSet(testSet);
        System.out.println("Not pruned result: " + res);
        Node prunedRoot = theBuilder.pruneTree(root, pruneSet);
        double prunedRes = prunedRoot.evaluateDataSet(testSet);
        System.out.println("Pruned result: " + prunedRes);
        System.out.println("done");
    }
}
