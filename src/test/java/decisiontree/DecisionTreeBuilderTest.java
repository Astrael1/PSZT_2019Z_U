package decisiontree;

import inputhandling.CustomCSVReader;
import inputhandling.Record;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static org.junit.Assert.*;

public class DecisionTreeBuilderTest
{
    static Set<Record> testData;
    @BeforeClass
    public static void readData() throws IOException {
        CustomCSVReader reader = new CustomCSVReader();
        testData = reader.read(System.getProperty("user.dir") + "/src/test/resources/test_data.csv");

    }

    @Test
    public void splitByAttribute()
    {
        DecisionTreeBuilder testBuilder = new DecisionTreeBuilder();
        Vector<HashSet<Record>> subsets = testBuilder.splitByAttribute(0, testData);
        int sum = 0;
        for(HashSet set: subsets)
        {
            sum += set.size();
        }
        assertEquals(sum, testData.size());
    }
}