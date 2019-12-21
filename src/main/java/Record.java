public class Record
{
    public Integer[] atttributes = new Integer[54];
    public Integer resultClass;

    Record(String[] strings)
    {
        for(int i = 0; i < 54; i++)
        {
            atttributes[i] = Integer.parseInt(strings[i]);
        }
        resultClass = Integer.parseInt(strings[54]);
    }
}
