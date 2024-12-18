package metier;

import java.awt.image.BufferedImage;

public class Image 
{
	private int x;	
	private int y;

	private int widthOg;	
	private int heigtOg;

	private int r;


	private BufferedImage img;

	public Image (int x, int y, BufferedImage img)
	{
		this.x   = x;
		this.y   = y;

		this.img   = img;

		this.widthOg = img.getWidth();
		this.heigtOg = img.getHeight();
	}

	public int getX        () { return this.x;  }
	public int getY        () { return this.y;  }

	public BufferedImage getImg      () { return this.img;}
	public int           getImgWidth () { return this.img.getWidth();}
	public int           getImgHeight() { return this.img.getHeight();}

	public int           getImgWidthOg () { return this.widthOg;}
	public int           getImgHeightOg() { return this.heigtOg;}

	public void setX  (int x)             { this.x = x; }
	public void setY  (int y)             { this.y = y; }
	public void setImg(BufferedImage img) { this.img = img; }	

}
