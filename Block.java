public class Block
{
    int elevation;
    public Block()
    {
        elevation = 0;
    }
    public int getElevation()
    {
        return elevation;
    }
    public void setElevation(int el)
    {
        elevation = el;
    }
    public void changeElevation(int dEl)
    {
        elevation += dEl;
    }
}