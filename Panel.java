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
    private int blockSize;
    private int x, y;
    private boolean zoomInSelected;
    private boolean zoomOutSelected;
    private Button[] buttons;
    private BufferedImage[] waterTextures, landTextures;
    private final String ROOT = "file/path";

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
        waterTextures = getWaterTextures();
        landTextures = getLandTextures();

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
            for (int j = startY; j < y+zoom && j < blocks[i].length; j++)
            {
                Block block = blocks[i][j];
                BufferedImage texture = this.getTexture(block, maxEl, minEl, seaLevel);

                //Draw block on image
                buffer.drawImage(texture, (i-x)*blockSize, (j-y)*blockSize, blockSize, blockSize, Color.BLACK, null);
            }
        }
        
        //Create and draw buttons
        buttons = new Button[8];
        buttons[0] = createZoomInButton();
        buttons[1] = createZoomOutButton();
        buttons[2] = createDownButton();
        buttons[3] = createUpButton();
        buttons[4] = createLeftButton();
        buttons[5] = createRightButton();
        buttons[6] = createWaterUpButton();
        buttons[7] = createWaterDownButton();
        
        //Set up colors for highlighting of zoom in and out buttons
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
        
        //Draw zoom in button
        buffer.setColor(mainCol1);
        buffer.fillPolygon(buttons[0].getXPoints(), buttons[0].getYPoints(), 4);
        buffer.setColor(textCol1);
        buffer.drawString("+", buttons[0].getXPoints()[0]+2, buttons[0].getYPoints()[0]+18);
        
        //Draw zoom out button
        buffer.setColor(mainCol2);
        buffer.fillPolygon(buttons[1].getXPoints(), buttons[1].getYPoints(), 4);
        buffer.setColor(textCol2);
        buffer.drawString("-",  buttons[1].getXPoints()[0]+5, buttons[1].getYPoints()[0]+16);
        
        //Draw direction buttons
        buffer.setColor(Color.YELLOW);
        for (int n = 2; n < 6; n++)
            buffer.fillPolygon(buttons[n].getXPoints(), buttons[n].getYPoints(), 3);
        
        //Draw sea level change buttons
        buffer.setColor(Color.RED);
        for (int n = 6; n < 8; n++)
            buffer.fillPolygon(buttons[n].getXPoints(), buttons[n].getYPoints(), 3);
        
        
        //Draw the areas in which you can click for a button to be pressed
        // buffer.setColor(Color.WHITE);
        // for (Button button : buttons)
        // {
            // int[] xPoints = {button.getXBounds()[0], button.getXBounds()[1], button.getXBounds()[1], button.getXBounds()[0]};
            // int[] yPoints = {button.getYBounds()[0], button.getYBounds()[0], button.getYBounds()[1], button.getYBounds()[1]};
            // buffer.drawPolygon(xPoints, yPoints, 4);
        // }
    }

    /**
     * Draws bImage on the panel. Called automatically.
     */
    public void paintComponent(Graphics g)
    {
        g.drawImage(bImage, 0, 0, getWidth(), getHeight(), null);
    }

    /**
     * Checks if the user has clicked a button or zoomed in/out and calls the appropriate method
     */
    public void mouseClicked(MouseEvent e)
    {
        System.out.println("clicked at ("+e.getX()+", "+e.getY()+")");
        
        int viewX = e.getX()/blockSize;
        int viewY = e.getY()/blockSize;
        int mapX = viewX + x;
        int mapY = viewY + y;
        //I could create a for loop that traverses buttons and calls
        //.contains(e.getX(), e.getY()) on each one, but I can't store 
        //the method that it calls as a data field of the button, so I 
        //can't do different things for each button
        if (buttons[0].contains(e.getX(), e.getY()))
            Driver.zoomInPress();
        else if (buttons[1].contains(e.getX(), e.getY()))
            Driver.zoomOutPress();
        else if (buttons[2].contains(e.getX(), e.getY()))
            Driver.moveView(Driver.DOWN);
        else if (buttons[3].contains(e.getX(), e.getY()))
            Driver.moveView(Driver.UP);
        else if (buttons[4].contains(e.getX(), e.getY()))
            Driver.moveView(Driver.LEFT);
        else if (buttons[5].contains(e.getX(), e.getY()))
            Driver.moveView(Driver.RIGHT);
        else if (buttons[6].contains(e.getX(), e.getY()))
            Driver.increaseWaterLevel();
        else if (buttons[7].contains(e.getX(), e.getY()))
            Driver.decreaseWaterLevel();
        else if (zoomInSelected)
            Driver.zoomIn(mapX, mapY, viewX, viewY);
        else if (zoomOutSelected)
            Driver.zoomOut(mapX, mapY, viewX, viewY);
    }

    /**
     * No action is performed
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * No action is performed
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * No action is performed
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * No action is performed
     */
    public void mouseReleased(MouseEvent e) {}
    
    private BufferedImage[] getWaterTextures()
    {
        BufferedImage[] waterTextures = new BufferedImage[3];
        for (int i=0; i<3; i++)
        {
            BufferedImage texture = null;
            try {
                File f = new File(ROOT + "/" + "bluewater-"+i+".png");
                texture = ImageIO.read(f);
            } catch (IOException e) {
                System.out.println(e);
            }
            waterTextures[i] = texture;
        }
        return waterTextures;
    }
    
    private BufferedImage[] getLandTextures()
    {
        BufferedImage[] landTextures = new BufferedImage[15];
        for (int i=0; i<15; i++)
        {
            BufferedImage texture = null;
            try {
                File f = new File(ROOT + "/" + "elevation-"+i+".png");
                texture = ImageIO.read(f);
            } catch (IOException e) {
                System.out.println(e);
            }
            landTextures[i] = texture;
        }
        return landTextures;
    }
    
    private BufferedImage getTexture(Block block, int maxEl, int minEl, int seaLevel)
    {
        int el = block.getElevation();
        BufferedImage texture;
        int index;
        int increment;
        if (el <= seaLevel)
        {
            //Get water texture
            increment = (minEl-seaLevel)/waterTextures.length;
            if (increment != 0)
                index = waterTextures.length-1 - (el-seaLevel) / increment;
            else
                index = 0;
                
            if (index < 0)
                index = 0;
            texture = waterTextures[index];
        }
        else
        {
            //Get land texture
            increment = (maxEl-seaLevel)/landTextures.length;
            if (increment != 0)
                index = (el-seaLevel) / increment;
            else
                index = landTextures.length - 1;
            if (index >= landTextures.length)
                index = landTextures.length - 1;
            texture = landTextures[index];
        }
        return texture;
    }
    
    private Button createUpButton()
    {
        int[] xPoints = {246, 256, 266};
        int[] yPoints = {40, 23, 40};
        
        return new Button(xPoints, yPoints);
    }
    
    private Button createDownButton()
    {
        int[] xPoints = {246, 256, 266};
        int[] yPoints = {472, 489, 472};
        
        return new Button(xPoints, yPoints);
    }
    
    private Button createLeftButton()
    {
        int[] xPoints = {40, 23, 40};
        int[] yPoints = {246, 256, 266};
        
        return new Button(xPoints, yPoints);
    }
    
    private Button createRightButton()
    {
        int[] xPoints = {472, 489, 472};
        int[] yPoints = {246, 256, 266};
        
        return new Button(xPoints, yPoints);
    }
    
    private Button createZoomInButton()
    {
        int[] xPoints = {480, 480, 500, 500};
        int[] yPoints = {50, 70, 70, 50};
        
        return new Button(xPoints, yPoints);
    }
    
    private Button createZoomOutButton()
    {
        int[] xPoints = {480, 480, 500, 500};
        int[] yPoints = {80, 100, 100, 80};
        
        return new Button(xPoints, yPoints);
    }
    
    private Button createWaterUpButton()
    {
        int[] xPoints = {480, 490, 500};
        int[] yPoints = {150, 130, 150};
        
        return new Button(xPoints, yPoints);
    }
    
    private Button createWaterDownButton()
    {
        int[] xPoints = {480, 490, 500};
        int[] yPoints = {170, 190, 170};
        
        return new Button(xPoints, yPoints);
    }
}