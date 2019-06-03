import javax.swing.JFrame;
import java.awt.Container;

public class Driver
{
    private static JFrame frame = new JFrame("Terrains");
    private static Map map = new Map(32);
    private static int x;
    private static int y;
    private static int waterLevel;
    private static int zoom;
    private static boolean zoomInSelected;
    private static boolean zoomOutSelected;
    
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String RIGHT = "right";
    public static final String LEFT = "left";
    
    public static void main(String[] args)
    {
        //For some reason I need to set the size to (529,552) in order for it to actually be (512,512)
        frame.setSize(529,552);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        x = 0;
        y = 0;
        zoom = 32;
        waterLevel = 0;
        zoomInSelected = false;
        zoomOutSelected = false;
        updateFrame();
    }

    public static void updateFrame()
    {
        Container panel = new Panel(map, x, y, zoom, zoomInSelected, zoomOutSelected, waterLevel);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static void zoomIn(int mapX, int mapY, int viewX, int viewY)
    {
        zoomInSelected = false;
        x = mapX - viewX/2;
        y = mapY - viewY/2;
        zoom /= 2;
        updateFrame();
    }
    
    public static void zoomOut(int mapX, int mapY, int viewX, int viewY)
    {
        zoomOutSelected = false;
        if (zoom*2 <= 32)
        {
            x = mapX - viewX*2;
            y = mapY - viewY*2;
            zoom *= 2;
            if (x < 0)
                x = 0;
            else if (x + zoom >= 32)
                x = 32 - zoom;
            if (y < 0)
                y = 0;
            else if (y + zoom >= 32)
                y = 32 - zoom;
        }
        updateFrame();
    }
    
    public static void moveView(String direction)
    {
        if (direction == UP && y-1 >= 0)
            y--;
        else if (direction == DOWN && y+1+zoom < 32)
            y++;
        else if (direction == LEFT && x-1 >= 0)
            x--;
        else if (direction == RIGHT && x+1+zoom < 32)
            x++;
        updateFrame();
    }
    
    public static void increaseWaterLevel()
    {
        waterLevel++;
        updateFrame();
    }
    
    public static void decreaseWaterLevel()
    {
        waterLevel--;
        updateFrame();
    }
    
    public static void zoomInPress()
    {
        zoomInSelected = !zoomInSelected;
        zoomOutSelected = false;
        updateFrame();
    }
    
    public static void zoomOutPress()
    {
        zoomOutSelected = !zoomOutSelected;
        zoomInSelected = false;
        updateFrame();
    }
}