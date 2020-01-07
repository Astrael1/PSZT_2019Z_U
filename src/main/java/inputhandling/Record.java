package inputhandling;

public class Record
{
    private Integer[] attributes = new Integer[54];

    public Integer getAttribute(Integer index) {
        return attributes[index];
    }

    public Integer getResultClass() {
        return resultClass;
    }

    private Integer resultClass;

    Record(String[] strings)
    {
        for(int i = 0; i < 54; i++)
        {
            attributes[i] = Integer.parseInt(strings[i]);
        }
        resultClass = Integer.parseInt(strings[54]);
    }
}
