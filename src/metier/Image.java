package metier;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Image 
{
	private int x;	
	private int y;

	private int xOg;	
	private int yOg;


	private BufferedImage img;
	private BufferedImage imgOg;

	public Image (int x, int y, BufferedImage img)
	{
		this.x   = this.xOg = x;
		this.y   = this.yOg = y;

		this.img   = img;
		this.imgOg = Image.copy(img);
	}

	public int getX        () { return this.x;  }
	public int getY        () { return this.y;  }

	public int getXOg      () { return this.x;  }
	public int getYOg      () { return this.y;  }

	public BufferedImage getImg      () { return this.img;}
	public int           getImgWidth () { return this.img.getWidth();}
	public int           getImgHeight() { return this.img.getHeight();}
	
	public BufferedImage getImgOg      () { return this.imgOg;}
	public int           getImgOgWidth () { return this.imgOg.getWidth();}
	public int           getImgOgHeight() { return this.imgOg.getHeight();}

	public void setX  (int x)             { this.x = x; }
	public void setY  (int y)             { this.y = y; }
	public void setImg(BufferedImage img) { this.img = img; }	

	public void maj () 
	{ 
		this.imgOg = Image.copy(this.img);
		this.xOg = this.x;
		this.yOg = this.y;
	}


	public static BufferedImage copy(BufferedImage source)
	{
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}
}
