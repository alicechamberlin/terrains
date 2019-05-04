public class Map
{
    Block[][] blocks;
    public Map(int size)
    {
        blocks = new Block[size][size];
        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks[i].length; j++)
                blocks[i][j] = new Block();
        generateElevations(0, 0, blocks.length, 256);
    }

    private void generateElevations(int startX, int startY, int size, int elevationRange)
    {
        int maxChange = (int) (Math.random() * elevationRange * 2) - elevationRange;
        System.out.println("maxChange :"+maxChange);

        //Calculate coordinates of the center of the section being affected
        int centerX = startX + size / 2;

        int delta; //The difference in height change between one block and its neighbors in the section
        if (size > 1)
            delta = maxChange / (size / 2);
        else
            delta = maxChange;

        for (int nX = startX; nX < centerX; nX++)
        {
            int nY = nX + (startY - startX); // nY will be incremented with nX
            for (int i = nX; i < blocks.length - nX; i++)
            {
                for (int j = nY; j < blocks[i].length - nY; j++)
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