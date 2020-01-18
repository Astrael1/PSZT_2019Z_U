package inputhandling;

import java.io.*;
import java.util.*;

public class CustomCSVReader
{
    private static final String delimiter = ";";
    private int trainSize;
    private int pruneSize;

    public Set<Record> read(String csvFile) throws IOException
    {
        Set<Record> result = new HashSet<>();

        File file = new File(csvFile);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line;
        String[] tempArr;
        br.readLine(); // ignore first line
        while((line = br.readLine()) != null)
        {

            tempArr = line.split(delimiter);
            Record r = new Record(tempArr);
            result.add(r);
        }
        br.close();
        setSizes(result);
        return result;
    }

    public List<Set<Record>> splitSets(Set<Record> input) {
        ArrayList<Record> inputList = new ArrayList<>(input);
        Collections.shuffle(inputList);
        List<Set<Record>> result = new LinkedList<>();
        int count = 0;
        Set<Record> trainSet = new HashSet<>();
        Set<Record> pruneSet = new HashSet<>();
        Set<Record> testSet = new HashSet<>();
        for(Record i : inputList) {
            switch(selectSet(count)) {
                case 0:
                    trainSet.add(i);
                    break;
                case 1:
                    pruneSet.add(i);
                    break;
                default:
                    testSet.add(i);
            }
            count++;
        }
        result.add(trainSet);
        result.add(pruneSet);
        result.add(testSet);
        return result;
    }

    private void setSizes(Set<Record> set) {
        int size = set.size();
        int testSize = (int) (size * 0.3);
        this.trainSize = (int) (size * 0.5);
        this.pruneSize = size - this.trainSize - testSize;
    }

    private int selectSet(int count) {
        if (count < this.trainSize) return 0;
        else if (count < this.pruneSize + this.trainSize) return 1;
        else return 2;
    }
}
