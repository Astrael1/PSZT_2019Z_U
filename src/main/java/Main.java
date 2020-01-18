import decisiontree.DecisionTreeBuilder;
import decisiontree.Node;
import inputhandling.CustomCSVReader;
import inputhandling.Record;
import org.apache.commons.cli.*;

import java.util.Set;

public class Main
{
    private static CommandLine parseArguments(String args[]) throws ParseException {
        Options options = new Options();
        options.addOption("t", false, "test option");
        options.addOption("l", true, "program learns from given source");
        options.addOption("q", true, "program qualifies records from given source")
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
        Set<Record> trainingSet = cr.read(System.getProperty("user.dir") + "/src/main/resources/divorce.csv");
        DecisionTreeBuilder theBuilder = new DecisionTreeBuilder();
        Node root = theBuilder.build(trainingSet);
        System.out.println("done");
    }
}
