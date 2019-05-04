public class Block
{
    int elevation; //value between -255 and 255
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