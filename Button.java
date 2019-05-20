
public class Button
{
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    public Button(int x1, int  y1, int x2, int y2)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    /**
     * @param x the horizontal location of the point
     * @param y the vertical location of the point
     * @return whether the given position is within the bounds of the button (inclusive)
     */
    public boolean contains(int x, int y)
    {
        if (x1 <= x && x < x2 && y1 <= y && y < y2)
        {
            return true;
        }
        return false;
    }
    
    /**
     * @return an array with size 2 holding the values {x1, x2}
     */
    public int[] getXs()
    {
        int[] coords = {x1,x2};
        return coords;
    }
    
    /**
     * @return an array with size 2 holding the values {y1, y2}
     */
    public int[] getYs()
    {
        int[] coords = {y1,y2};
        return coords;
    }
}
