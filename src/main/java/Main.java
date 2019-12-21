import java.util.List;

public class Main
{
    public static void main(String args[]) throws Exception
    {
        CustomCSVReader cr = new CustomCSVReader();
        List<Record> trainingSet = cr.read(System.getProperty("user.dir") + "/src/main/resources/divorce.csv");
    }
}
