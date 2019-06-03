import java.util.Random;

public class Map
{
    private Random rand;
    private Block[][] blocks;
    //private final double[] fakeRandom = {0.1, 0.7, 0.3, 0.5, 0.9, 0, 0.2, 0.8, 0.4, 0.6};
    
    public Map(int size)
    {
        rand = new Random();
        blocks = new Block[size][size];
        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks[i].length; j++)
                blocks[i][j] = new Block();
        generateElevations(0, 0, blocks.length, 256);
    }

    /**
     * Generate pseudo-random maps by adding or subtracting "pyramids" of random heights to subdivisions of the map
     */
    private void generateElevations(int startX, int startY, int size, int elevationRange)
    {
        int maxChange = (int) (rand.nextDouble() * elevationRange * 2) - elevationRange;
        
        int delta; //The difference in height change between one block and its neighbors in the section
        if (size > 1)
            delta = maxChange / (size / 2);
        else
            delta = maxChange;

        for (int n = 0; n < size/2; n++)
        {
            for (int i = startX + n; i < startX + size - n; i++)
            {
                for (int j = startY + n; j < startY + size - n; j++)
                {
                    blocks[i][j].changeElevation(delta);
                }
            }
        }
        if (size > 1)
        {
            int newSiz = size/2;
            int newRan = elevationRange/2;
            generateElevations(startX, startY, newSiz, newRan);
            generateElevations(startX+newSiz, startY, newSiz, newRan);
            generateElevations(startX, startY+newSiz, newSiz, newRan);
            generateElevations(startX+newSiz, startY+newSiz, newSiz, newRan);
        }
    }

    /**
     * @return the 2d square array of Blocks that make up the map
     */
    public Block[][] getBlocks()
    {
        return blocks;
    }
    
    /**
     * @return the length of one side of the square map in Blocks
     */
    public int getSize()
    {
        return blocks.length;
    }
}