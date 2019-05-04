import javax.swing.JFrame;

public class Driver
{
    private static JFrame frame = new JFrame("Terrains");
    private static Map map = new Map(16);
    public static void main(String[] args)
    {
        frame.setSize(512,512);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateFrame();
    }

    public static void updateFrame()
    {
        frame.setContentPane(new Panel(map));
        frame.setVisible(true);
    }
}