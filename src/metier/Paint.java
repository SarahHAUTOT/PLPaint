package metier;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

// import paint.Controlleur;


public class Paint 
{

	/* --------------------------------------------------- */
	/*                    VARIABLE STATIC                  */
	/* --------------------------------------------------- */

	/** Largeur par défauts. */
	public static final int DEFAULT_WIDTH  = 300;
	/** Hauteyr par défauts. */
	public static final int DEFAULT_HEIGHT = 300;


	
	/* --------------------------------------------------- */
	/*                       ATTRIBUTS                     */
	/* --------------------------------------------------- */

	/** Liste des images qui compose la grande image. */
	private ArrayList<Image> lstImages;

	/** Largeur de l'image. */
	private int width ;
	/** Hauteur de l'image. */
	private int height;
	
	/** Controller. */
	// private Controlleur ctrl;
	



	/* --------------------------------------------------------------------------------------------------- */
	/*                                             CONSTRUCTEURS                                           */
	/* --------------------------------------------------------------------------------------------------- */

	/**
	 * Constructeur par défauts (sans rien).
	 * @param ctrl
	 */
	public Paint()//Controlleur ctrl)
	{
		// this.ctrl = ctrl;
		this.lstImages = new ArrayList<>();
		
		this.width  = Paint.DEFAULT_WIDTH ;
		this.height = Paint.DEFAULT_HEIGHT;
	}
	



	/* --------------------------------------------------------------------------------------------------- */
	/*                                                IMAGES                                               */
	/* --------------------------------------------------------------------------------------------------- */

	/**
	 * Ajouter une image a la liste.
	 * @param img
	 */
	public void addImage(Image img)
	{
		if (img != null)
		{
			this.lstImages.add(img);
			
			int h = img.getImg().getHeight(); 
			int w = img.getImg().getWidth(); 
			if ( h > this.height ) this.height = h;
			if ( w > this.width  ) this.width  = w;
		}
	}

	/**
	 * Retourne la liste des images
	 * @return
	 */
    public ArrayList<Image> getImages()
	{
		return  this.lstImages;
    }

	/**
	 * Retourne une image qui est l'image combiné des autres
	 * @return
	 */
	public BufferedImage getImage()
	{
		BufferedImage imgSortie = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);

		// Parcourir chacune des pixels des images
		for (Image img : this.lstImages) 
		{
			int xStart = img.getX();
			int yStart = img.getY();

			BufferedImage imgEntre = img.getImg();

			for (int x = 0; x < imgEntre.getWidth() && x < this.width; x++)
			{
				for (int y = 0; y < imgEntre.getHeight() && y < this.height; y++)
				{
					int coul = (imgEntre.getRGB(x, y));
					// Not transparent, x and y in zone
					if((coul>>24) != 0x00 && x + xStart <= this.width && y + yStart <= this.height)
						imgSortie.setRGB(x + xStart, y + yStart, coul);
				}
			}
		}

		return imgSortie;
	}

	/**
	 * Récupérer l'image (celle-tous devant) ou elle a était cliqué.
	 * @param x
	 * @param y
	 * @return
	 */
	public Image getClickedImage(int x, int y)
	{
		for (int i = this.lstImages.size() - 1; i >= 0; i--)
			if (imageIn(x, y, this.lstImages.get(i))) 
				return this.lstImages.get(i);

		return null;
	}

	/**
	 * Set la largeur.
	 * @param width
	 */
	public void setWidth (int width ) {this.width  = width; }

	/**
	 * Set la hauteur.
	 * @param height
	 */
	public void setHeight(int height) {this.height = height;}

	/**
	 * Méthode privé, permet de savoir si une image est concerné par 
	 * @param x
	 * @param y
	 * @param img
	 * @return
	 */
	private boolean imageIn (int x, int y, Image img)
	{
		return x >= img.getX() && x < img.getX() + img.getImgWidth () && 
		       y >= img.getY() && y < img.getY() + img.getImgHeight() &&
			   !isTrans(img.getImg().getRGB(x, y));
	}

	public static boolean isTrans(int rgb)
	{
		return ((rgb >>24 )& 0xff) == 0;
	}




	/* --------------------------------------------------------------------------------------------------- */
	/*                                             METHODE BUCKET                                          */
	/* --------------------------------------------------------------------------------------------------- */


	/**
	 * Méthode de bucket à appeler.
	 * @param x
	 * @param y
	 * @param argb
	 */
	public void bucket(int x, int y, int argb, int distance) 
	{
		BufferedImage bi = this.getImage();
		
		Queue<Point> file = new LinkedList<Point>();
		int colorOrig;

		colorOrig = bi.getRGB( x, y ) & 0xFFFFFF;

		file.add ( new Point ( x, y ) );

		while ( ! file.isEmpty() )
		{
			Point p = file.remove();

			if ( p.x() >= 0 && p.x() < bi.getWidth  () && p.y() >= 0 && p.y() < bi.getHeight () &&
			    //  colorOrig == ( bi.getRGB(p.x(), p.y() ) & 0xFFFFFF)
				sameColor(colorOrig, ( bi.getRGB(p.x(), p.y() ) & 0xFFFFFF), distance)
			   )
			{
				bi.setRGB ( p.x(), p.y(), argb );
				
				if (this.getClickedImage(p.x(), p.y()) != null)
					this.getClickedImage(p.x(), p.y()).getImg().setRGB(p.x(), p.y(), argb);

				file.add ( new Point ( p.x()+1, p.y()   ) );
				file.add ( new Point ( p.x()-1, p.y()   ) );
				file.add ( new Point ( p.x()  , p.y()-1 ) );
				file.add ( new Point ( p.x()  , p.y()+1 ) );
			}
		}
	}

	
	public static boolean sameColor (int c1, int c2, int dist)
	{
		int b1 = c1%256;
		c1     = c1/256;
		int g1 = c1%256;
		c1     = c1/256;
		int r1 = c1%256;

		int b2 = c2%256;
		c2     = c2/256;
		int g2 = c2%256;
		c2     = c2/256;
		int r2 = c2%256;


		int distance = (int) (Math.sqrt(Math.pow(r1 - r2, 2.0) + Math.pow(g1 - g2, 2.0) + Math.pow(b1 - b2, 2.0)));

		return distance < dist;
	}






	/* --------------------------------------------------------------------------------------------------- */
	/*                                           METHODE LUMINOSITE                                        */
	/* --------------------------------------------------------------------------------------------------- */

	/**
	 * Changer la luminosité de toute une image.
	 * @param image
	 * @param var
	 */
	public void setLuminosite (Image image, int var)
	{
		BufferedImage bi = image.getImg();

		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {

				int pixelColor = bi.getRGB(x, y) & 0xFFFFFF;

				if (!Paint.isTrans(bi.getRGB(x, y)))
				{
					int nouvVal = Paint.luminosite(new Color(pixelColor), var);
					Color newColor = new Color(nouvVal);
					bi.setRGB(x, y, newColor.getRGB());
				}
			}
		}
	}


	/**
	 * Changer la luminosité d'une zone rectangulaire.
	 * @param xSart
	 * @param yStart
	 * @param xFin
	 * @param yFin
	 * @param var
	 */
	public void setLuminosite (int xSart, int yStart, int xFin, int yFin, int var)
	{
		for (int x = xSart; x < xFin; x++) {
			for (int y = yStart; y < yFin; y++) 
			{
				Image img = this.getClickedImage(x, y);

				if (img != null)
				{
					BufferedImage bi = img.getImg();

					int pixelColor = bi.getRGB(x, y) & 0xFFFFFF;
					int nouvVal = Paint.luminosite(new Color(pixelColor), var);
					Color newColor = new Color(nouvVal);
					bi.setRGB(x, y, newColor.getRGB());
				}
			}
		}
	}

	/**
	 * Changer la luminosité d'une zone circulaire.
	 * @param xCenter
	 * @param yCenter
	 * @param radius
	 * @param var
	 */
	public void setLuminosite(int xCenter, int yCenter, int radius, int var) {

		for (int x = xCenter - radius; x < xCenter + radius; x++) {
			for (int y = yCenter - radius; y < yCenter + radius; y++) 
			{
				if (x > 0 && x < this.width && y > 0 && y < this.height && 
				    radius >= Math.sqrt( Math.pow( x - xCenter, 2 ) + Math.pow( y - yCenter, 2 ) ) 
				   )
				{
					Image img = this.getClickedImage(x, y);
					if (img != null) {
						BufferedImage bi = img.getImg();
	
						int pixelColor = bi.getRGB(x, y) & 0xFFFFFF;
						int nouvVal = Paint.luminosite(new Color(pixelColor), var); 
						Color newColor = new Color(nouvVal);
						bi.setRGB(x, y, newColor.getRGB()); 
					}
				}
			}
		}
	}
	

	/**
	 * Calcule d'une couleur pour envoyer la couleur contrasté.
	 * @param coul
	 * @param var
	 * @return
	 */
	public static int luminosite (Color coul, int var)
	{
		
		int r = coul.getRed  () + var;
		int b = coul.getBlue () + var;
		int g = coul.getGreen() + var;

		if (r > 255) r = 255;
		if (b > 255) b = 255;
		if (g > 255) g = 255;

		if (r < 0) r = 0;
		if (g < 0) g = 0;
		if (b < 0) b = 0;


		return r * 256 * 256 + g * 256 + b;
	}






	/* --------------------------------------------------------------------------------------------------- */
	/*                                            METHODE ROTATIONS                                        */
	/* --------------------------------------------------------------------------------------------------- */

	
	public void retourner(Image image, int angle) 
	{
		BufferedImage img = image.getImg();

		double angleRad = Math.toRadians(angle);
		double sin = Math.sin(angleRad);
		double cos = Math.cos(angleRad);
	
		int nH = (int) Math.floor(img.getWidth() * Math.abs(sin) + img.getHeight() * Math.abs(cos));
		int nW = (int) Math.floor(img.getWidth() * Math.abs(cos) + img.getHeight() * Math.abs(sin));
	
		BufferedImage rotatedImg = new BufferedImage(nW, nH, BufferedImage.TYPE_INT_ARGB);
	
		int xO = width / 2;
		int yO = height / 2;
	
		for (int rX = 0; rX < nW; rX++) 
		{
			for (int rY = 0; rY < nH; rY++) 
			{
				double x = (rX - xO) * cos + (rY - yO) * sin + xO;
				double y = -(rX - xO) * sin + (rY - yO) * cos + yO;
	
				if (x >= 0 && x < nW && y >= 0 && y < nH) 
				{
					int x1 = (int) Math.floor(x);
					int x2 = x1 + 1;
					int y1 = (int) Math.floor(y);
					int y2 = y1 + 1;
	
					double dx = x - x1;
					double dy = y - y1;
	
					double wHG = (1 - dx) * (1 - dy);
					double wBG = dx * (1 - dy);      
					double wHD = (1 - dx) * dy;      
					double wBD = dx * dy;            
	
					x1 = Math.max(0, Math.min(x1, width - 1));
					x2 = Math.max(0, Math.min(x2, width - 1));
					y1 = Math.max(0, Math.min(y1, height - 1));
					y2 = Math.max(0, Math.min(y2, height - 1));
	
					Color colHG = new Color(img.getRGB(x1, y1));
					Color colBG = new Color(img.getRGB(x2, y1));
					Color colHD = new Color(img.getRGB(x1, y2));
					Color colBD = new Color(img.getRGB(x2, y2));
	
					int r = (int) (wHG * colHG.getRed()   + wBG * colBG.getRed()   +
								   wHD * colHD.getRed()   + wBD * colBD.getRed());
					int g = (int) (wHG * colHG.getGreen() + wBG * colBG.getGreen() +
								   wHD * colHD.getGreen() + wBD * colBD.getGreen());
					int b = (int) (wHG * colHG.getBlue()  + wBG * colBG.getBlue()  +
								   wHD * colHD.getBlue()  + wBD * colBD.getBlue());
	
					rotatedImg.setRGB(rX, rY, new Color(r, g, b).getRGB());
				}
			}
		}
	
		image.setImg(rotatedImg);
	}






	/* --------------------------------------------------------------------------------------------------- */
	/*                                            METHODE INVERSER                                         */
	/* --------------------------------------------------------------------------------------------------- */
	
	/**
	 * Flip horizontal d'une image.
	 * @param img
	 */
	public void flipHorizontal(Image img) 
	{
		BufferedImage bi = img.getImg();

		int width  = img.getImgWidth();
		int height = img.getImgHeight();
	
		for (int x = 0; x < width / 2; x++) 
		{
			for (int y = 0; y < height; y++) 
			{
				int temp = bi.getRGB(x, y);
	
				bi.setRGB(x, y, bi.getRGB(width - x - 1, y));
				bi.setRGB(width - x - 1, y, temp);
			}
		}
	}
	
	/**
	 * Flip horizontal d'une zone rectanculaire.
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 */
	public void flipHorizontal(int xStart, int yStart, int xEnd, int yEnd) 
	{
		for (int x = xStart; x < xEnd / 2; x++) 
		{
			for (int y = yStart; y < yEnd; y++) 
			{
				Image img = getClickedImage(x, y);

				if (img != null)
				{
					BufferedImage bi = img.getImg();
					int temp = bi.getRGB(x, y);
		
					bi.setRGB(x, y, bi.getRGB(xEnd - x - 1, y));
					bi.setRGB(xEnd - x - 1, y, temp);
				}
			}
		}
	}
	
	/**
	 * Flip horizontal d'une zone circulaire.
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 */
	public void flipHorizontal(int xCenter, int yCenter, int radius) 
	{
		for (int x = xCenter - radius; x < xCenter + radius; x++) {
			for (int y = yCenter - radius; y < yCenter + radius; y++) 
			{
				if (x > 0 && x < this.width && y > 0 && y < this.height && 
				    radius >= Math.sqrt( Math.pow( x - xCenter, 2 ) + Math.pow( y - yCenter, 2 ) ) 
				   )
				{
					Image img = this.getClickedImage(x, y);
					if (img != null) {
						BufferedImage bi = img.getImg();
	
						int pixelColor = bi.getRGB(x, y) & 0xFFFFFF;
						int nouvVal = Paint.luminosite(new Color(pixelColor), var); 
						Color newColor = new Color(nouvVal);
						bi.setRGB(x, y, newColor.getRGB()); 
					}
				}
			}
		}
	}



	/**
	 * Flip vertical d'une image.
	 * @param img
	 */
	public void flipVertical(Image img) 
	{
		BufferedImage bi = img.getImg();

		int width  = img.getImgWidth();
		int height = img.getImgHeight();
	
		for (int x = 0; x < width; x++) 
		{
			for (int y = 0; y < height / 2; y++) 
			{
				int temp = bi.getRGB(x, y);
	
				bi.setRGB(x, y, bi.getRGB(x, height - y - 1));
				bi.setRGB(x, height - y - 1, temp);
			}
		}
	}
	
	/**
	 * Flip horizontal d'une zone rectanculaire.
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 */
	public void flipVertical(int xStart, int yStart, int xEnd, int yEnd) 
	{
		for (int x = xStart; x < xEnd; x++) 
		{
			for (int y = yStart; y < yEnd / 2; y++) 
			{
				Image img = getClickedImage(x, y);

				if (img != null)
				{
					BufferedImage bi = img.getImg();
					int temp = bi.getRGB(x, y);
		
					bi.setRGB(x, y, bi.getRGB(x, yEnd - y - 1));
					bi.setRGB(x, yEnd - y - 1, temp);
				}
			}
		}
	}
	







	/* --------------------------------------------------------------------------------------------------- */
	/*                                              MAIN DE TEST                                           */
	/* --------------------------------------------------------------------------------------------------- */


	public static void main(String[] args) {
		Paint p = new Paint();

		try {
			Image img = new Image(0,0, ImageIO.read(new File("src/metier/test.png")));
			p.addImage(img);
			
			// p.bucket(0, 0, Color.RED.getRGB(), 80);

			// // p.setLuminosite(img, -200);
			// p.setLuminosite(0,0,100,100, 50, -100);
			// p.setLuminosite(200,200,100,100, 50, -100);
			// p.setLuminosite(150,150, 50, -100);
			// p.retourner(img, 30);

			p.flipVertical(0, 100, img.getImgWidth() / 2, 300);

			ImageIO.write(p.getImage(),"png",new File ("fin.png") );

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
