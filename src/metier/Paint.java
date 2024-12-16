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
		new Image(0, 0, new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB));
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
			p.setLuminosite(150,150, 50, -100);

			ImageIO.write(p.getImage(),"png",new File ("fin.png") );

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
