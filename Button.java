
public class Button
{
    private int minX, maxX, minY, maxY;
    private final int[] X_POINTS, Y_POINTS;
    
    public Button(int[] xPoints, int[] yPoints)
    {
        X_POINTS = xPoints;
        Y_POINTS = yPoints;
        
        minX = xPoints[0];
        maxX = xPoints[0];
        for (int x : xPoints)
            if (x < minX)
                minX = x;
            else if (x > maxX)
                maxX = x;
                
        minY = yPoints[0];
        maxY = yPoints[0];
        for (int y : yPoints)
            if (y < minY)
                minY = y;
            else if (y > maxY)
                maxY = y;
    }
    
    /**
     * @param x the horizontal location of the point
     * @param y the vertical location of the point
     * @return whether the given position is within the bounds of the button (inclusive)
     */
    public boolean contains(int x, int y)
    {
        if (minX <= x && x < maxX && minY <= y && y < maxY)
        {
            return true;
        }
        return false;
    }
    
    /**
     * @return an array with size 2 holding the values {minX, maxX}
     */
    public int[] getXBounds()
    {
        int[] coords = {minX,maxX};
        return coords;
    }
    
    /**
     * @return an array with size 2 holding the values {minY, maxY}
     */
    public int[] getYBounds()
    {
        int[] coords = {minY,maxY};
        return coords;
    }
    
    /**
     * @return the original xPoints given
     */
    public int[] getXPoints()
    {
        return X_POINTS;
    }
    
    /**
     * @return the original yPoints given
     */
    public int[] getYPoints()
    {
        return Y_POINTS;
    }
}
