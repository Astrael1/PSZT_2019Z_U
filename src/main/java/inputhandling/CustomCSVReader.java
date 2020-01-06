package inputhandling;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CustomCSVReader
{
    private static final String delimiter = ";";
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
        return result;
    }
}
