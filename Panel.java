import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Font;

public class Panel extends JPanel implements MouseListener
{
    private static final long serialVersionUID = 1L;
    private final int PANEL_SIZE = 512;
    private BufferedImage bImage;
    private BufferedImage[] waterTextures;
    private BufferedImage[] landTextures;
    private int blockSize;
    private int x, y;
    private Button zoomInButton;
    private Button zoomOutButton;
    private Button downButton;
    private Button upButton;
    private Button rightButton;
    private Button leftButton;
    private boolean zoomInSelected;
    private boolean zoomOutSelected;
    private Button[] buttons;

    public Panel(Map map, int x, int y, int zoom, boolean in, boolean out, int seaLevel)
    {
        //Initialize basic panel information
        blockSize = PANEL_SIZE / zoom;
        this.x = x;
        this.y = y;
        bImage = new BufferedImage(PANEL_SIZE, PANEL_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics buffer = bImage.getGraphics();
        addMouseListener(this);
        
        zoomInSelected = in;
        zoomOutSelected = out;

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
        int startX = x;
        int startY = y;
        if (startX < 0)
            startX = 0;
        if (startY < 0)
            startY = 0;
        for (int i = startX; i < x+zoom && i < blocks.length; i++)
        {
            //if (i >= blocks.length)
            //    break;
            for (int j = startY; j < y+zoom && j < blocks[i].length; j++)
            {
                //if (j >= blocks[i].length)
                //    break;
                Block block = blocks[i][j];
                int el = block.getElevation();

                BufferedImage texture;
                if (el < 0)
                {
                    int index;
                    if (minEl/3 != 0)
                        index = 2 - el / (minEl / 3);
                    else
                        index = 2;
                        
                    if (index < 0)
                        index = 0;
                    texture = waterTextures[index];
                }
                else
                {
                    int index;
                    if (maxEl/15 != 0)
                        index = el / (maxEl / 15);
                    else
                        index = 0;
                    
                    if (index > 14)
                        index = 14;
                    texture = landTextures[index];
                }

                //Draw blocks on image
                buffer.drawImage(texture, (i-x)*blockSize, (j-y)*blockSize, blockSize, blockSize, Color.BLACK, null);
            
                //Draw buttons on image
                Color mainCol1 = Color.BLUE;
                Color textCol1 = Color.WHITE;
                Color mainCol2 = Color.BLUE;
                Color textCol2 = Color.WHITE;
                if (zoomInSelected)
                {
                    mainCol1 = Color.WHITE;
                    textCol1 = Color.BLUE;
                }
                if (zoomOutSelected)
                {
                    mainCol2 = Color.WHITE;
                    textCol2 = Color.BLUE;
                }
                buffer.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
                
                int zoomButtonsX = 480;
                int zoomButtonsSize = 20;
                int zoomInButtonY = 50;
                int zoomOutButtonY = 80;
                
                zoomInButton = new Button(zoomButtonsX, zoomInButtonY, zoomButtonsX+zoomButtonsSize, zoomInButtonY+zoomButtonsSize);
                zoomOutButton = new Button(zoomButtonsX, zoomOutButtonY, zoomButtonsX+zoomButtonsSize, zoomOutButtonY+zoomButtonsSize);
                
                buffer.setColor(mainCol1);
                buffer.fillRect(zoomButtonsX, zoomInButtonY, zoomButtonsSize, zoomButtonsSize);
                buffer.setColor(textCol1);
                buffer.drawString("+", zoomButtonsX+2, zoomInButtonY+18);
                
                buffer.setColor(mainCol2);
                buffer.fillRect(zoomButtonsX, zoomOutButtonY, zoomButtonsSize, zoomButtonsSize);
                buffer.setColor(textCol2);
                buffer.drawString("-", zoomButtonsX+5, zoomOutButtonY+16);
                
                buffer.setColor(Color.YELLOW);
                int[] downXPoints = {246, 256, 266};
                int[] downYPoints = {472, 489, 472};
                downButton = new Button(downXPoints[0], downYPoints[0], downXPoints[2], downYPoints[1]);
                buffer.fillPolygon(downXPoints, downYPoints, 3);
                
                int[] upXPoints = {246, 256, 266};
                int[] upYPoints = {40, 23, 40};
                upButton = new Button(upXPoints[0], upYPoints[1], upXPoints[2], upYPoints[2]);
                buffer.fillPolygon(upXPoints, upYPoints, 3);
                
                int[] leftXPoints = {40, 23, 40};
                int[] leftYPoints = {246, 256, 266};
                leftButton = new Button(leftXPoints[1], leftYPoints[0], leftXPoints[2], leftYPoints[2]);
                buffer.fillPolygon(leftXPoints, leftYPoints, 3);
                
                int[] rightXPoints = {472, 489, 472};
                int[] rightYPoints = {246, 256, 266};
                rightButton = new Button(rightXPoints[0], rightYPoints[0], rightXPoints[1], rightYPoints[2]);
                buffer.fillPolygon(rightXPoints, rightYPoints, 3);
                
                buttons = new Button[6];
                buttons[0] = zoomInButton;
                buttons[1] = zoomOutButton;
                buttons[2] = downButton;
                buttons[3] = upButton;
                buttons[4] = leftButton;
                buttons[5] = rightButton;
                
                // buffer.setColor(Color.WHITE);
                // for (Button button : buttons)
                // {
                    // int[] xPoints = {button.getXs()[0], button.getXs()[1], button.getXs()[1], button.getXs()[0]};
                    // int[] yPoints = {button.getYs()[0], button.getYs()[0], button.getYs()[1], button.getYs()[1]};
                    // buffer.drawPolygon(xPoints, yPoints, 4);
                // }
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(bImage, 0, 0, getWidth(), getHeight(), null);
    }
<<<<<<< HEAD

    public void mouseClicked(MouseEvent e)
    {
        int viewX = e.getX()/blockSize;
        int viewY = e.getY()/blockSize;
        int mapX = viewX + x;
        int mapY = viewY + y;
        if (zoomInButton.contains(e.getX(), e.getY()))
            Driver.zoomInPress();
        else if (zoomOutButton.contains(e.getX(), e.getY()))
            Driver.zoomOutPress();
        else if (downButton.contains(e.getX(), e.getY()))
            Driver.moveView(Driver.DOWN);
        else if (upButton.contains(e.getX(), e.getY()))
            Driver.moveView(Driver.UP);
        else if (leftButton.contains(e.getX(), e.getY()))
            Driver.moveView(Driver.LEFT);
        else if (rightButton.contains(e.getX(), e.getY()))
            Driver.moveView(Driver.RIGHT);
        else if (zoomInSelected)
            Driver.zoomIn(mapX, mapY, viewX, viewY);
        else if (zoomOutSelected)
            Driver.zoomOut(mapX, mapY, viewX, viewY);
    }

    public void mouseEntered(MouseEvent e)
    {
        
    }

    public void mouseExited(MouseEvent e)
    {
        
    }

    public void mousePressed(MouseEvent e)
    {
        
    }

    public void mouseReleased(MouseEvent e)
    {
        
    }
}
=======
}
>>>>>>> 6643ba9bea54aa082e5caa628bb8a92ef584ae02
