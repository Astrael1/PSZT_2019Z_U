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

    double gain( Set<Record> recordSet, Vector<HashSet<Record>> subsets)
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


}
