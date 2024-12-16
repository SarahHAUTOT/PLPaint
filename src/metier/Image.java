package paint.metier;

import java.awt.image.BufferedImage;

public class Image 
{
	private int x;	
	private int y;

	private BufferedImage img;

	public int getX        () { return this.x;  }
	public int getY        () { return this.y;  }

	public BufferedImage getImg      () { return this.img;}
	public int           getImgWidth () { return this.img.getWidth();}
	public int           getImgHeight() { return this.img.getHeight();}


	public void setX  (int x)             { this.x = x; }
	public void setY  (int y)             { this.y = y; }
	public void setImg(BufferedImage img) { this.img = img; }	
}
