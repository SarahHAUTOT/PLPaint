package paint.metier;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

public class Paint 
{
	/* --------------------------------------------------- */
	/*                    VARIABLE STATIC                  */
	/* --------------------------------------------------- */

	/** Largeur par défauts. */
	public static final int DEFAULT_WIDTH  = 500;
	/** Hauteyr par défauts. */
	public static final int DEFAULT_HEIGHT = 500;


	
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
	private Controlleur ctrl;
	



	/* --------------------------------------------------------------------------------------------------- */
	/*                                             CONSTRUCTEURS                                           */
	/* --------------------------------------------------------------------------------------------------- */

	/**
	 * Constructeur par défauts (sans rien).
	 * @param ctrl
	 */
	public Paint(Controlleur ctrl)
	{
		this.ctrl = ctrl;
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
			this.lstImages.add(img);
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

			for (int x = 0; x < imgEntre.getWidth(); x++)
			{
				for (int y = 0; y < imgEntre.getHeight(); y++)
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
		for (int i = this.lstImages.size(); i >= 0; i--)
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
		return x > img.getX() && x < img.getX() + img.getImgWidth () && 
		       y > img.getY() && y < img.getY() + img.getImgHeight() &&
			   img.getImg().getRGB(x, y) >>24 != 0x00;
	}




	/* --------------------------------------------------------------------------------------------------- */
	/*                                             METHODE BUCKET                                          */
	/* --------------------------------------------------------------------------------------------------- */


	/**
	 * Méthode de bucket a appellé.
	 * @param x
	 * @param y
	 * @param argb
	 */
	public void bucket (int x, int y, int argb)
	{
		BufferedImage ensImage = this.getImage();

		this.bucketRecursive(x, y, argb, ensImage.getRGB(x, y), ensImage);
	}

	/**
	 * Méthode récursive 
	 * @param x
	 * @param y
	 * @param argbChange
	 * @param argComp
	 * @param img
	 */
	private void bucketRecursive (int x, int y, int argbChange, int argComp, BufferedImage img)
	{
		if (img.getRGB(x, y) == argComp)
		{
			// Changer le pixel sur la première image qu'on trouvera 
			BufferedImage imgChange = this.getClickedImage(x,y).getImg();
			
			if(imgChange != null)
			{
				imgChange.setRGB(x, y, argbChange);
				
				// Verifier si on doit le faire aussi autour
				if ( x + 1 < width && y - 1 < height && img.getRGB(x + 1, y - 1) == argComp) bucketRecursive(x + 1, y - 1, argbChange, argComp, imgChange);
				if ( x + 1 < width && y + 1 < height && img.getRGB(x + 1, y + 1) == argComp) bucketRecursive(x + 1, y + 1, argbChange, argComp, imgChange);
				if ( x - 1 < width && y - 1 < height && img.getRGB(x - 1, y - 1) == argComp) bucketRecursive(x - 1, y - 1, argbChange, argComp, imgChange);
				if ( x - 1 < width && y + 1 < height && img.getRGB(x - 1, y + 1) == argComp) bucketRecursive(x - 1, y + 1, argbChange, argComp, imgChange);
			}
		}
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

				if (bi.getRGB(x, y) >>24 != 0x00)
				{
					int pixelColor = bi.getRGB(x, y) & 0xFFFFFF;
					int nouvVal = Paint.contraste(new Color(pixelColor), var);
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
			for (int y = yStart; y < yFin; y++) {
				BufferedImage bi = this.getClickedImage(x, y).getImg();

				if (bi != null)
				{
					int pixelColor = bi.getRGB(x, y) & 0xFFFFFF;
					int nouvVal = Paint.contraste(new Color(pixelColor), var);
					Color newColor = new Color(nouvVal);
					bi.setRGB(x, y, newColor.getRGB());
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
	public static int contraste (Color coul, int var)
	{
		int r = coul.getRed  ();
		int b = coul.getBlue ();
		int g = coul.getGreen();


		//Calcul
		r = (int) (r + var / 100.0 * (r - 127));
		g = (int) (g + var / 100.0 * (g - 127));
		b = (int) (b + var / 100.0 * (b - 127));

		if (r > 255) r = 255;
		if (b > 255) b = 255;
		if (g > 255) g = 255;

		if (r < 0) r = 0;
		if (b < 0) b = 0;
		if (g < 0) g = 0;

		return r * 256 * 256 + g * 256 + b;
	}
	




	/* --------------------------------------------------------------------------------------------------- */
	/*                                           METHODE LUMINOSITE                                        */
	/* --------------------------------------------------------------------------------------------------- */

	


}
