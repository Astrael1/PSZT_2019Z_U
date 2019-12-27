import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class DecisionTree
{
    private Map<Integer, Integer> edges;
    Vector<Integer> attributes;

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

    double info(Integer nrOfAttribute, Set<Record> recordSet)
    {
        int recordSetCount = recordSet.size();
        Vector<HashSet<Record>> subsets = new Vector<>();
        for(int i = 0; i < 5; i++)
        {
            subsets.add(new HashSet<Record>());
        }
        return 0;
    }


}
