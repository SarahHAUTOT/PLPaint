package metier;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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

	private BufferedImage fond;

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

		this.setFond();
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

			if ( h > this.height) this.height = h;
			if ( w > this.width ) this.width  = w;
		}
	}

	/**
	 * Ajouter une image mais sans une couleur donnée.
	 * @param img
	 * @param argb
	 */
	public void addImage(Image img, int argb)
	{
		BufferedImage biWithoutArgb = new BufferedImage(img.getImgWidth(), img.getImgHeight(), BufferedImage.TYPE_INT_ARGB);
		BufferedImage biWithArgb    = img.getImg();

		for (int x = 0; x < img.getImgWidth(); x++)
			for (int y = 0; y < img.getImgHeight(); y++)
				if (!Paint.sameColor(biWithArgb.getRGB(x, y) & 0xFFFFFF, argb, 30))
					biWithoutArgb.setRGB(x, y, biWithArgb.getRGB(x, y));


		img.setImg(biWithoutArgb);
		this.addImage(img);
	}


	public void setFond()
	{
		this.fond = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
	}


	/**
	 * Retire une image dans la liste.
	 * @param img
	 */
	public void removeImage(Image img)
	{
		this.lstImages.remove(img);

		// Max height
		if (img.getImgHeight() >= this.height)
			for (Image image : lstImages)
				if (!image.equals(img) && image.getImgHeight() >= this.height)
				{
					this.height = img.getImgHeight();	
					this.setFond();
				}

		// Max width
		if (img.getImgWidth() >= this.width)
			for (Image image : lstImages)
				if (!image.equals(img) && image.getImgWidth() >= this.width)
				{
					this.width = img.getImgWidth();				
					this.setFond();
				}
	}


	/**
	 * Retourne une image qui est l'image combiné des autres
	 * @return
	 */
	public BufferedImage getImage() 
	{
		BufferedImage imgSortie = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
	
		for (Image img : this.lstImages) 
		{
			int xStart = img.getX();
			int yStart = img.getY();
	
			BufferedImage imgEntre = img.getImg();
	
			for (int x = 0; x < imgEntre.getWidth(); x++) 
			{
				for (int y = 0; y < imgEntre.getHeight(); y++) 
				{
					int coul = imgEntre.getRGB(x, y);
	
					if ((coul >> 24) != 0x00) {
						int xDest = x + xStart;
						int yDest = y + yStart;
	
						if (xDest >= 0 && xDest < this.width && yDest >= 0 && yDest < this.height) 
							imgSortie.setRGB(xDest, yDest, coul);
					}
				}
			}
		}
	
		return imgSortie;
	}


	/**
	 * Retire le 
	 * @param image
	 * @param tolerance
	 */
    public void removeBackground(Image image) 
	{
		BufferedImage bi = image.getImg();

        int width  = bi.getWidth();
        int height = bi.getHeight();

        int backgroundColor = getDominantBorderColor(bi);

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) 
		{
            for (int y = 0; y < height; y++) 
			{
                int pixel = bi.getRGB(x, y);

                if (sameColor(pixel, backgroundColor, 50))
                    result.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
                else 
                    result.setRGB(x, y, pixel);
            }
        }

        image.setImg(result);
    }

	/**
	 * Trouve la couleur dominante dans une image.
	 * @param image
	 * @return
	 */
    private static int getDominantBorderColor(BufferedImage image) 
	{
        int width = image.getWidth();
        int height = image.getHeight();

        Map<Integer, Integer> colorCount = new HashMap<>();

        for (int x = 0; x < width; x++) 
		{
            addColorCount(image.getRGB(x, 0), colorCount);       
            addColorCount(image.getRGB(x, height - 1), colorCount);
        }
        for (int y = 0; y < height; y++) 
		{
            addColorCount(image.getRGB(0, y), colorCount);
            addColorCount(image.getRGB(width - 1, y), colorCount);
        }

        int dominantColorRGB = colorCount.entrySet()
		                                 .stream()
		                                 .max(Map.Entry.comparingByValue())
		                                 .get()
		                                 .getKey();

        return dominantColorRGB;
    }

	
	/**
	 * Ajoute / Incrémente la couleur 
	 * @param rgb
	 * @param colorCount
	 */
    private static void addColorCount(int rgb, Map<Integer, Integer> colorCount) 
	{
        colorCount.put(rgb, colorCount.getOrDefault(rgb, 0) + 1);
    }
	

	/**
	 * Récupérer l'image (celle-tous devant) ou elle a était cliqué (sans prendre en compte la transparence).
	 * @param x
	 * @param y
	 * @return
	 */
	public Image getClickedImage(int x, int y)
	{
		for (int i = this.lstImages.size() - 1; i >= 0; i--)
			if (imageIn(x, y, this.lstImages.get(i), true)) 
				return this.lstImages.get(i);

		return null;
	}


	public ArrayList<Image> getClickedImages(int x, int y)
	{
		ArrayList<Image> lstImages = new ArrayList<>();

		for (int i = this.lstImages.size() - 1; i >= 0; i--)
			if (imageIn(x, y, this.lstImages.get(i), true)) 
				lstImages.add(this.lstImages.get(i));

		return lstImages;
	}

	
	

	/**
	 * Récupérer l'image a des coordonées.
	 * @param x
	 * @param y
	 * @return
	 */
	public Image getImageAt(int x, int y)
	{
		for (int i = this.lstImages.size() - 1; i >= 0; i--)
			if (imageIn(x, y, this.lstImages.get(i), false)) 
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
	private boolean imageIn (int x, int y, Image img, boolean withTrans)
	{
		// System.out.println("X " + x + " | w " + img.getImgWidth () + " | start at " + img.getX() + " | x max " + (img.getX() + img.getImgWidth ()));
		// System.out.println("Y " + y + " | h " + img.getImgHeight() + " | start at " + img.getY() + " | x max " + (img.getY() + img.getImgHeight()));


		return x >= img.getX() && x < img.getX() + img.getImgWidth () && 
			   y >= img.getY() && y < img.getY() + img.getImgHeight() &&
			   !(isTrans(img.getImg().getRGB(x, y)) && withTrans);
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
	 * @param distance
	 */
	public void bucket(int x, int y, int argb, int distance) 
	{
		BufferedImage bi = this.getImage();
		Queue<Point> file = new LinkedList<Point>();
		int colorOrig = bi.getRGB(x, y) & 0xFFFFFF;

		file.add(new Point(x, y));

		while (!file.isEmpty()) 
		{
			Point p = file.remove();

			if (p.x() >= 0 && p.x() < bi.getWidth() && p.y() >= 0 && p.y() < bi.getHeight() &&
				sameColor(colorOrig, (bi.getRGB(p.x(), p.y()) & 0xFFFFFF), distance)) 
			{
				bi.setRGB(p.x(), p.y(), argb);

				Image img = this.getImageAt(p.x(), p.y());
				if (img != null) 
				{
					BufferedImage subImg = img.getImg();

					int localX = p.x() - img.getX();
					int localY = p.y() - img.getY();

					if (localX >= 0 && localX < subImg.getWidth() && localY >= 0 && localY < subImg.getHeight()) 
					{
						subImg.setRGB(localX, localY, argb);
					}
				}

				file.add(new Point(p.x() + 1, p.y()));
				file.add(new Point(p.x() - 1, p.y()));
				file.add(new Point(p.x(), p.y() - 1));
				file.add(new Point(p.x(), p.y() + 1));
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
		if (var == 0) return;

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
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 * @param var
	 */
	public void setLuminosite(int xStart, int yStart, int xEnd, int yEnd, int var) 
	{
		if (var == 0) return;

		for (int x = xStart; x < xEnd; x++) 
		{
			for (int y = yStart; y < yEnd; y++) 
			{
				ArrayList<Image> lst = this.getClickedImages(x, y);

				for (Image img : lst) {
					BufferedImage bi = img.getImg();

					int localX = x - img.getX();
					int localY = y - img.getY();

					if (localX >= 0 && localX < bi.getWidth() && localY >= 0 && localY < bi.getHeight()) 
					{
						int pixelColor = bi.getRGB(localX, localY) & 0xFFFFFF;
						int nouvVal = Paint.luminosite(new Color(pixelColor), var);
						Color newColor = new Color(nouvVal);
						bi.setRGB(localX, localY, newColor.getRGB());
					}
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
	public void setLuminosite(int xCenter, int yCenter, int radius, int var) 
	{
		if (var == 0) return;

		for (int x = xCenter - radius; x < xCenter + radius; x++) 
		{
			for (int y = yCenter - radius; y < yCenter + radius; y++) 
			{
				if (x >= 0 && x < this.width && y >= 0 && y < this.height &&
					Math.sqrt(Math.pow(x - xCenter, 2) + Math.pow(y - yCenter, 2)) <= radius) 
				{
					ArrayList<Image> lst = this.getClickedImages(x, y);
	
					for (Image img : lst) {
						BufferedImage bi = img.getImg();

						int localX = x - img.getX();
						int localY = y - img.getY();

						if (localX >= 0 && localX < bi.getWidth() && localY >= 0 && localY < bi.getHeight()) 
						{
							int pixelColor = bi.getRGB(localX, localY) & 0xFFFFFF;
							int nouvVal = Paint.luminosite(new Color(pixelColor), var);
							Color newColor = new Color(nouvVal);
							bi.setRGB(localX, localY, newColor.getRGB());
						}	
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

	/**
	 * Rotation d'un éléments.
	 * @param image
	 * @param angle
	 */
	public void rotate(Image image, int angle) 
	{
		if (angle == 0 || angle == 360) return;


		BufferedImage img = image.getImg();
	
		double angleRad = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(angleRad));
		double cos = Math.abs(Math.cos(angleRad));
	
		int oldWidth = img.getWidth();
		int oldHeight = img.getHeight();
		int newWidth = (int) Math.ceil(oldWidth * cos + oldHeight * sin);
		int newHeight = (int) Math.ceil(oldWidth * sin + oldHeight * cos);
	
		BufferedImage rotatedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

		int xCenterOld = oldWidth / 2;
		int yCenterOld = oldHeight / 2;
		int xCenterNew = newWidth / 2;
		int yCenterNew = newHeight / 2;
	
		for (int rX = 0; rX < newWidth; rX++) 
		{
			for (int rY = 0; rY < newHeight; rY++) 
			{
				double x = (rX - xCenterNew) * Math.cos(-angleRad) - (rY - yCenterNew) * Math.sin(-angleRad) + xCenterOld;
				double y = (rX - xCenterNew) * Math.sin(-angleRad) + (rY - yCenterNew) * Math.cos(-angleRad) + yCenterOld;
	
				if (x >= 0 && x < oldWidth && y >= 0 && y < oldHeight) 
				{
					int srcX = (int) Math.floor(x);
					int srcY = (int) Math.floor(y);
	
					srcX = Math.min(srcX, oldWidth - 1);
					srcY = Math.min(srcY, oldHeight - 1);
	
					rotatedImg.setRGB(rX, rY, img.getRGB(srcX, srcY));
				}
			}
		}
	
		image.setX(image.getX() + (xCenterOld - xCenterNew));
		image.setY(image.getY() + (yCenterOld - yCenterNew));
		image.setImg(rotatedImg);
	}

	
	/**
	 * Flip d'une zone rectangulaire et rotation.
	 * @param angle L'angle de rotation en degrés.
	 * @param xStart Coordonnée x du coin supérieur gauche de la zone rectangulaire.
	 * @param yStart Coordonnée y du coin supérieur gauche de la zone rectangulaire.
	 * @param xEnd Coordonnée x du coin inférieur droit de la zone rectangulaire.
	 * @param yEnd Coordonnée y du coin inférieur droit de la zone rectangulaire.
	 */
	public void rotate(int xStart, int yStart, int xEnd, int yEnd, int angle) 
	{
		if (angle == 0 || angle == 360) return;

		BufferedImage img = this.getImage();

		BufferedImage zoneImage = img.getSubimage(xStart, yStart, xEnd - xStart, yEnd - yStart);
		Image zoneImageObj = new Image(xStart, yStart, zoneImage);

		try {
			ImageIO.write(zoneImage,"png",new File ("jh.png") );
		} catch (Exception e) {
			// TODO: handle exception
		}

		this.lstImages.add(zoneImageObj);
		this.rotate(zoneImageObj, angle);
		
		
		
		for (int x = xStart; x < xEnd; x++) 
		{
			for (int y = yStart; y < yEnd; y++) 
			{
				System.out.println(xStart + " " + x + " | " + yEnd + " " + y);
				ArrayList<Image> lst = this.getClickedImages(x, y);
				for (Image imgInZone : lst) 
				{
					// Si l'image est totalement couvert dans la zone on l'enlève de la liste
					if (imgInZone.getX() >= xStart && imgInZone.getX() + imgInZone.getImgWidth () <= xEnd &&
					    imgInZone.getY() >= yStart && imgInZone.getY() + imgInZone.getImgHeight() <= yEnd) 
					{
						this.lstImages.remove(imgInZone);
					}else {
						BufferedImage bi = imgInZone.getImg();
						int localX = x - imgInZone.getX();
						int localY = y - imgInZone.getY();
						if (localX >= 0 && localX < bi.getWidth() && localY >= 0 && localY < bi.getHeight()) 
						{
							bi.setRGB(localX, localY, 0);  
						}
					}
				}
			}
		}
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
	* Flip horizontal d'une zone rectangulaire.
	* @param xStart
	* @param yStart
	* @param xEnd
	* @param yEnd
	*/
	public void flipHorizontal(int xStart, int yStart, int xEnd, int yEnd) 
	{
		for (int x = xStart; x < xStart + (xEnd - xStart) / 2; x++) 
		{
			for (int y = yStart; y < yEnd; y++) 
			{
				Image img = getClickedImage(x, y);
				Image imgMirror = getClickedImage(xEnd - (x - xStart) - 1, y);

				if (img != null && imgMirror != null) 
				{
					BufferedImage bi = img.getImg();
					BufferedImage biMirror = imgMirror.getImg();

					int localX1 = x - img.getX();
					int localX2 = xEnd - (x - xStart) - 1 - imgMirror.getX();
					int localY = y - img.getY();

					if (localX1 >= 0 && localX1 < bi.getWidth() &&
						localX2 >= 0 && localX2 < biMirror.getWidth() &&
						localY >= 0 && localY < bi.getHeight()) 
					{
						int temp = bi.getRGB(localX1, localY);
						bi.setRGB(localX1, localY, biMirror.getRGB(localX2, localY));
						biMirror.setRGB(localX2, localY, temp);
					}
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
		for (int x = xCenter - radius; x < xCenter; x++) 
		{
			for (int y = yCenter - radius; y <= yCenter + radius; y++) 
			{
				if (Math.sqrt(Math.pow(x - xCenter, 2) + Math.pow(y - yCenter, 2)) <= radius) 
				{
					Image img = getClickedImage(x, y);
					Image imgMirror = getClickedImage(xCenter + (xCenter - x), y);

					if (img != null && imgMirror != null) 
					{
						BufferedImage bi = img.getImg();
						BufferedImage biMirror = imgMirror.getImg();

						int localX1 = x - img.getX();
						int localX2 = xCenter + (xCenter - x) - imgMirror.getX();
						int localY = y - img.getY();

						if (localX1 >= 0 && localX1 < bi.getWidth() &&
							localX2 >= 0 && localX2 < biMirror.getWidth() &&
							localY >= 0 && localY < bi.getHeight()) 
						{
							int temp = bi.getRGB(localX1, localY);
							bi.setRGB(localX1, localY, biMirror.getRGB(localX2, localY));
							biMirror.setRGB(localX2, localY, temp);
						}
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
	 * Flip vertical d'une zone rectangulaire.
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 */
	public void flipVertical(int xStart, int yStart, int xEnd, int yEnd) 
	{
		for (int x = xStart; x < xEnd; x++) 
		{
			for (int y = yStart; y < yStart + (yEnd - yStart) / 2; y++) 
			{
				Image img = getClickedImage(x, y);
				Image imgMirror = getClickedImage(x, yEnd - (y - yStart) - 1);

				if (img != null && imgMirror != null) 
				{
					BufferedImage bi = img.getImg();
					BufferedImage biMirror = imgMirror.getImg();

					int localX = x - img.getX();
					int localY1 = y - img.getY();
					int localY2 = yEnd - (y - yStart) - 1 - imgMirror.getY();

					if (localX >= 0 && localX < bi.getWidth() &&
						localY1 >= 0 && localY1 < bi.getHeight() &&
						localY2 >= 0 && localY2 < biMirror.getHeight()) 
					{
						int temp = bi.getRGB(localX, localY1);
						bi.setRGB(localX, localY1, biMirror.getRGB(localX, localY2));
						biMirror.setRGB(localX, localY2, temp);
					}
				}
			}
		}
	}

	/**
	 * Flip vertical d'une zone circulaire.
	 * @param xCenter
	 * @param yCenter
	 * @param radius
	 */
	public void flipVertical(int xCenter, int yCenter, int radius) 
	{
		for (int x = xCenter - radius; x <= xCenter + radius; x++) 
		{
			for (int y = yCenter - radius; y < yCenter; y++) 
			{
				if (Math.sqrt(Math.pow(x - xCenter, 2) + Math.pow(y - yCenter, 2)) <= radius) 
				{
					Image img = getClickedImage(x, y);
					Image imgMirror = getClickedImage(x, yCenter + (yCenter - y));

					if (img != null && imgMirror != null) 
					{
						BufferedImage bi = img.getImg();
						BufferedImage biMirror = imgMirror.getImg();

						int localX = x - img.getX();
						int localY1 = y - img.getY();
						int localY2 = yCenter + (yCenter - y) - imgMirror.getY();

						if (localX >= 0 && localX < bi.getWidth() &&
							localY1 >= 0 && localY1 < bi.getHeight() &&
							localY2 >= 0 && localY2 < biMirror.getHeight()) 
						{
							int temp = bi.getRGB(localX, localY1);
							bi.setRGB(localX, localY1, biMirror.getRGB(localX, localY2));
							biMirror.setRGB(localX, localY2, temp);
						}
					}
				}
			}
		}
	}





	/* --------------------------------------------------------------------------------------------------- */
	/*                                             METHODE TEXTE                                           */
	/* --------------------------------------------------------------------------------------------------- */
	
	/**
	 * Ajoute du texte.
	 * @param text
	 * @param bold
	 * @param italic
	 * @param police
	 * @param size
	 */
	public void addText (String text, boolean bold, boolean italic, String police, int size, int argb)
	{
		BufferedImage bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2 = bi.createGraphics();
		
		int style = Font.PLAIN;
		if (bold  ) style = style | Font.BOLD;
		if (italic) style = style | Font.ITALIC;

		g2.setColor(new Color(argb));
		g2.setFont(new Font(police ,style, size));
		g2.drawString(text, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.addImage(new Image(0, 0, bi));
	}	



	/* --------------------------------------------------------------------------------------------------- */
	/*                                            METHODE TEXTURE                                          */
	/* --------------------------------------------------------------------------------------------------- */
	

	/**
	 * Remplace les pixels non transparent par une texture.
	 * @param biTexture
	 * @param img
	 */
	public void addTexture(BufferedImage biTexture, Image img)
	{
		BufferedImage bi = img.getImg();
		for (int x = 0; x < bi.getWidth(); x++)
			for (int y = 0; y < bi.getHeight(); y++)
				if (!Paint.isTrans(bi.getRGB(x, y)))
					bi.setRGB(x, y, biTexture.getRGB(x % biTexture.getWidth(), y % biTexture.getHeight()));
	}






	/* --------------------------------------------------------------------------------------------------- */
	/*                                              MAIN DE TEST                                           */
	/* --------------------------------------------------------------------------------------------------- */


	public static void main(String[] args) {
		Paint p = new Paint();

		try {
			Image img = new Image(60,40, ImageIO.read(new File("src/metier/test.png")));
			p.addImage(img);
			// p.bucket(0,0,Color.RED.getRGB(), 30);

			// p.flipVertical(50,50,500,350);
			// p.setLuminosite(0,0,600,300,-50);
			// p.setLuminosite(img, 50);
			// p.setLuminosite(img, -50);
			// p.setLuminosite(img, -50);

			p.setLuminosite(80,120,300,450,80);

			ImageIO.write(p.getImage(),"png",new File ("fin.png") );

		} catch (Exception e) {
			e.printStackTrace();
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
}
