import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Panel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private final int PANEL_SIZE = 512;
    private BufferedImage bImage;
    private BufferedImage[] waterTextures;
    private BufferedImage[] landTextures;

    public Panel(Map map)
    {
        //Initialize basic panel information
        int blockSize = PANEL_SIZE / map.getSize();
        bImage = new BufferedImage(PANEL_SIZE, PANEL_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics buffer = bImage.getGraphics();

        //Create arrays of images to illustrate various block elevations
        String root = "file/path";
        waterTextures = new BufferedImage[3];
        for (int i=0; i<3; i++)
        {
            BufferedImage texture = null;
            try {
                File f = new File(root + "/" + "bluewater-"+i+".png");
                texture = ImageIO.read(f);
            } catch (IOException e) {
                System.out.println(e);
            }
            waterTextures[i] = texture;
        }
        landTextures = new BufferedImage[15];
        for (int i=0; i<15; i++)
        {
            BufferedImage texture = null;
            try {
                File f = new File(root + "/" + "elevation-"+i+".png");
                texture = ImageIO.read(f);
            } catch (IOException e) {
                System.out.println(e);
            }
            landTextures[i] = texture;
        }

        Block[][] blocks = map.getBlocks();

        //Determine the highest and lowest elevations on the map so that the colors can be scaled accordingly
        int maxEl = blocks[0][0].getElevation();
        int minEl = blocks[0][0].getElevation();
        for (Block[] row : blocks)
            for (Block b : row)
            {
                if (b.getElevation() > maxEl)
                    maxEl = b.getElevation();
                else if (b.getElevation() < minEl)
                    minEl = b.getElevation();
            }

        //Assign each block an icon based on its elevation
        for (int i = 0; i < blocks.length; i++)
            for (int j=0; j < blocks[i].length; j++)
            {
                Block block = blocks[i][j];
                int el = block.getElevation();

                BufferedImage texture;
                if (el < 0)
                {
                    int index = 2 - el / (minEl / 3);
                    if (index < 0)
                        index = 0;
                    texture = waterTextures[index];
                }
                else
                {
                    int index = el / (maxEl / 15);
                    if (index > 14)
                        index = 14;
                    texture = landTextures[index];
                }

                buffer.drawImage(texture, i*blockSize, j*blockSize, blockSize, blockSize, Color.BLACK, null);
            }
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(bImage, 0, 0, getWidth(), getHeight(), null);
    }
}
