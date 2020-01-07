import decisiontree.DecisionTreeBuilder;
import decisiontree.Node;
import inputhandling.*;

import java.util.List;
import java.util.Set;

public class Main
{
    public static void main(String args[]) throws Exception
    {
        CustomCSVReader cr = new CustomCSVReader();
        Set<Record> trainingSet = cr.read(System.getProperty("user.dir") + "/src/main/resources/divorce.csv");
        DecisionTreeBuilder theBuilder = new DecisionTreeBuilder();
        Node root = theBuilder.build(trainingSet);
        System.out.println("done");
    }
}
