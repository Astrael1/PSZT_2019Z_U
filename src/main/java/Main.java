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
        DecisionTreeBuilder theBuilder = new DecisionTreeBuilder();
        Node root = theBuilder.build(data.get(0));
        System.out.println("done");
    }
}
