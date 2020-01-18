import decisiontree.DecisionTreeBuilder;
import decisiontree.Node;
import inputhandling.CustomCSVReader;
import inputhandling.Record;
import org.apache.commons.cli.*;

import java.util.List;
import java.util.Set;

public class Main
{

    private static CommandLine parseArguments(String args[]) throws ParseException {
        Options options = new Options();
        options.addOption("t", false, "test option");
        options.addOption("l", true, "program learns from given source");
        options.addOption("q", true, "program qualifies records from given source");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        return cmd;
    }
    public static void main(String args[]) throws Exception
    {
        CommandLine arguments = parseArguments(args);

        if(arguments.hasOption("t"))
        {
            System.out.println("Podano opcje \'t\'");
        }
        else
        {
            System.out.println("Nie podano opcji \'t\'");
        }

        CustomCSVReader cr = new CustomCSVReader();
        Set<Record> dataSet = cr.read(System.getProperty("user.dir") + "/src/main/resources/divorce.csv");
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
