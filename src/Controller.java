
import metier.Paint;
import ihm.PLPaint;

public class Controller 
{
    private PLPaint frame;
    private Paint    metier;

    public Controller()
    {
        this.metier = new Paint();
        this.frame  = new PLPaint(this.metier);
    }

    public static void main(String[] args)
    {
        new Controller();
    }
}
