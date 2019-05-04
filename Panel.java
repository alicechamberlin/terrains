import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class Panel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private final int PANEL_SIZE = 512;
    private BufferedImage bImage;

    public Panel(Map map)
    {
        int blockSize = PANEL_SIZE / map.getSize();
        bImage = new BufferedImage(PANEL_SIZE, PANEL_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics buffer = bImage.getGraphics();

        Block[][] blocks = map.getBlocks();
        for (int i = 0; i < blocks.length; i++)
            for (int j=0; j < blocks[i].length; j++)
            {
                Block block = blocks[i][j];
                int r = 0;
                int b = 0;
                if (block.getElevation() <= 0)
                    b += -block.getElevation();
                else
                    r += block.getElevation();
                
                buffer.setColor(new Color(r, 255, b));
                buffer.fillRect(i*blockSize, j*blockSize, blockSize, blockSize);
            }
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(bImage, 0, 0, getWidth(), getHeight(), null);
    }
}