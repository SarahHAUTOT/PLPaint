
import metier.Paint;
import ihm.FrameApp;

public class Controller 
{
    private FrameApp frame;
    private Paint    metier;

    public Controller()
    {
        this.metier = new Paint();
        this.frame  = new FrameApp(this.metier);
    }

	public static void main(String[] args)
    {
        new Controller();
    }
}
