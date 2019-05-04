import java.util.Random;

public class Map
{
    Random rand;
    Block[][] blocks;
    public Map(int size)
    {
        rand = new Random();
        blocks = new Block[size][size];
        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks[i].length; j++)
                blocks[i][j] = new Block();
        generateElevations(0, 0, blocks.length, 256);
    }

    private void generateElevations(int startX, int startY, int size, int elevationRange)
    {
        int maxChange = (int) (rand.nextDouble() * elevationRange * 2) - elevationRange;
        System.out.println(startX+", "+startY+" "+size);
        System.out.println("maxChange :"+maxChange);


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

    public Block[][] getBlocks()
    {
        return blocks;
    }
    public int getSize()
    {
        return blocks.length;
    }
}