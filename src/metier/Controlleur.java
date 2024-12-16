package metier;
import ihm.FrameApp;

public class Controlleur 
{
    public Controlleur()
    {
        new FrameApp(this);
    }

	public static void main(String[] args)
    {
        new Controlleur();
    }
}
