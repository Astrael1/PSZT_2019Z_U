import decisiontree.DecisionTreeBuilder;
import decisiontree.Node;
import inputhandling.CustomCSVReader;
import inputhandling.Record;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main
{

    public static void main(String args[]) throws Exception
    {
        String csvFilePath;
        List argsList = Arrays.asList(args);
        if(argsList.contains("-l")) {
        csvFilePath = (String) argsList.get(argsList.indexOf("-l") + 1);
        csvFilePath = csvFilePath.substring(0,csvFilePath.length()-1); //on Linux I needed to remove the last character to make it work
        }
        else
        {
            csvFilePath = System.getProperty("user.dir") + "/src/main/resources/divorce.csv";
        }



        CustomCSVReader cr = new CustomCSVReader();
        Set<Record> dataSet = cr.read(csvFilePath);
        List<Set<Record>> data = cr.splitSets(dataSet);
        Set<Record> trainSet = data.get(0);
        Set<Record> pruneSet = data.get(1);
        Set<Record> testSet = data.get(2);
        DecisionTreeBuilder theBuilder = new DecisionTreeBuilder();
        Node root = theBuilder.build(trainSet);
        root.printTree(0,0);
        double res = root.evaluateDataSet(testSet);
        System.out.println("Not pruned result: " + res);
        Node prunedRoot = theBuilder.pruneTree(root, pruneSet);
        prunedRoot.printTree(0,0);
        double prunedRes = prunedRoot.evaluateDataSet(testSet);
        System.out.println("Pruned result: " + prunedRes);
    }
}
