public class Map
{
    Block[][] blocks;
    public Map(int size)
    {
        blocks = new Block[size][size];
        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks[i].length; j++)
                blocks[i][j] = new Block();
        generateElevations(blocks);
    }

    private void generateElevations(Block[][] section)
    {
        
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