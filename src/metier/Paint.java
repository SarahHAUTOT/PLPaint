package metier;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

// import paint.Controlleur;


public class Paint 
{

	/* --------------------------------------------------- */
	/*                    VARIABLE STATIC                  */
	/* --------------------------------------------------- */

	/** Largeur par défauts. */
	public static final int DEFAULT_WIDTH  = 1000;
	/** Hauteyr par défauts. */
	public static final int DEFAULT_HEIGHT = 600;


	
	/* --------------------------------------------------- */
	/*                       ATTRIBUTS                     */
	/* --------------------------------------------------- */

	/** Liste des images qui compose la grande image. */
	private ArrayList<Image> lstImages;

	private ArrayList<ArrayList<Image>> lstHistorique;

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
	public Paint()
	{
		this.reset();
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
			if ( h == this.height || w == this.width) this.resizeBackground();
		}

		this.save();
	}



	/**
	 * Reset la liste
	 */
	public void reset ()
	{
		this.lstImages = new ArrayList<>();
		this.lstHistorique = new ArrayList<>();
		
		this.width  = Paint.DEFAULT_WIDTH ;
		this.height = Paint.DEFAULT_HEIGHT;
	}


	/**
	 * Retire une image dans la liste.
	 * @param img
	 */
	public void removeImage(Image img)
	{
		this.lstImages.remove(img);

		this.height = 0;
		this.width  = 0;
	
		// On prend pa le fond
		for (int i = 1; i < this.lstImages.size(); i++) 
		{
			Image image = this.lstImages.get(i);

			if (image.getImgHeight() > this.height)
				this.height = image.getImgHeight();
			if (image.getImgWidth() > this.width)
				this.width = image.getImgWidth();
		}

		if (this.height == 0) this.height = DEFAULT_HEIGHT;
		if (this.width  == 0) this.width  = DEFAULT_WIDTH;

		this.resizeBackground();
	}
	


	public void resizeBackground()
	{
		BufferedImage biBg = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
				
		Graphics2D g2d = (Graphics2D) (biBg.getGraphics());
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, biBg.getWidth(), biBg.getHeight());

		this.lstImages.get(0).setImg(biBg);
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
	
					if (!isTrans(coul)) {
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


	
	public BufferedImage getImageWithoutBackground() 
	{
		BufferedImage imgSortie = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
	
		for (int i = 1; i < this.lstImages.size(); i++) 
		{
			Image img = this.lstImages.get(i);
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


	public void merge(Image img1, Image img2) 
	{
		int minX = Math.min(img1.getX(), img2.getX());
		int minY = Math.min(img1.getY(), img2.getY());
		int maxX = Math.max(img1.getX() + img1.getImgWidth(), img2.getX() + img2.getImgWidth());
		int maxY = Math.max(img1.getY() + img1.getImgHeight(), img2.getY() + img2.getImgHeight());

		int mergedWidth = maxX - minX;
		int mergedHeight = maxY - minY;

		BufferedImage mergedImg = new BufferedImage(mergedWidth, mergedHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics g = mergedImg.getGraphics();

		g.drawImage(img1.getImg(), img1.getX() - minX, img1.getY() - minY, null);
		g.drawImage(img2.getImg(), img2.getX() - minX, img2.getY() - minY, null);

		g.dispose();

		img1.setImg(mergedImg);
		img1.setX(minX);
		img1.setY(minY);
}






	/* --------------------------------------------------------------------------------------------------- */
	/*                                           METHODE BACKGROUND                                        */
	/* --------------------------------------------------------------------------------------------------- */



	/**
	 * Retire le 
	 * @param image
	 * @param tolerance
	 */
    public void removeBackground(Image image, int color, int val) 
	{
		BufferedImage bi = image.getImgOg();

        int width  = bi.getWidth();
        int height = bi.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) 
		{
            for (int y = 0; y < height; y++) 
			{
                int pixel = bi.getRGB(x, y) ;

                if (sameColor(pixel, color, val))
                    result.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
                else 
                    result.setRGB(x, y, pixel);
            }
        }

        image.setImg(result);

		try {
			
			ImageIO.write(bi,"png",new File ("image.png") );
			ImageIO.write(result,"png",new File ("fond.png") );
		} catch (Exception e) {
			// TODO: handle exception
		}
    }


	public void removeBackground (Image img, int val)
	{
		this.removeBackground(img, Paint.getDominantBorderColor(img.getImg()), val);
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
		for (int i = this.lstImages.size() - 1; i >= 1; i--)
			if (imageIn(x, y, this.lstImages.get(i), true)) 
				return this.lstImages.get(i);

		return null;
	}


	public ArrayList<Image> getClickedImages(int x, int y)
	{
		ArrayList<Image> lstImages = new ArrayList<>();

		for (int i = this.lstImages.size() - 1; i >= 1; i--)
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
		for (int i = this.lstImages.size() - 1; i >= 1; i--)
			if (imageIn(x, y, this.lstImages.get(i), true)) 
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
		return x >= img.getX() && x < img.getX() + img.getImgWidth () && 
			   y >= img.getY() && y < img.getY() + img.getImgHeight() &&
			   !(isTrans(img.getImg().getRGB(x- img.getX(), y - img.getY())) && withTrans);
	}

	public static boolean isTrans(int rgb)
	{
		return ((rgb >>24 )& 0xff) == 0;
	}

	public void save ()
	{
		ArrayList<Image> historique= new ArrayList<>();
		for (Image image : lstImages)
		{
			image.save();
			historique.add(new Image(image));
		}
		
		this.lstHistorique.add(historique);
	}

	public boolean goBack()
	{
		int lastInd = this.lstHistorique.size() - 1;
	
		if (lastInd <= 0) {
			return false;
		}
	
		ArrayList<Image> previousState = this.lstHistorique.get(lastInd - 1);
	
		this.lstImages = new ArrayList<>(previousState);
		this.lstHistorique.remove(lastInd);
	
		return true;
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
		int width = bi.getWidth();
		int height = bi.getHeight();

		int[][] pixels = new int[width][height];
		boolean[][] visited = new boolean[width][height];

		for (int i = 0; i < width; i++) 
		{
			for (int j = 0; j < height; j++) 
			{
				pixels[i][j] = bi.getRGB(i, j) & 0xFFFFFF;
			}
		}

		int colorOrig = pixels[x][y];
		ArrayDeque<Point> file = new ArrayDeque<>();
		file.add(new Point(x, y));

		int minX = x, minY = y, maxX = x, maxY = y;

		boolean aServie = false;
		while (!file.isEmpty()) 
		{
			Point p = file.removeFirst();
			int px = p.x();
			int py = p.y();

			if (px >= 0 && px < width && py >= 0 && py < height && !visited[px][py] &&
				sameColor(colorOrig, pixels[px][py], distance)) 
			{
				visited[px][py] = true;
				pixels[px][py] = argb;

				minX = Math.min(minX, px);
				minY = Math.min(minY, py);
				maxX = Math.max(maxX, px);
				maxY = Math.max(maxY, py);

				file.add(new Point(px + 1, py));
				file.add(new Point(px - 1, py));
				file.add(new Point(px, py + 1));
				file.add(new Point(px, py - 1));

				// Si il y a une image a ce point la, on changer la couleur a ce pixel
				Image img = this.getImageAt(px, py);
				if (img != null) 
				{
					BufferedImage biImg = img.getImg();
					biImg.setRGB(px - img.getX(), py - img.getY(), argb);
					visited[px][py] = false;
				} else {
					aServie = true;
				}
			}
		}


		

		if (aServie)
		{
			int zoneWidth  = maxX - minX + 1;
			int zoneHeight = maxY - minY + 1;

			BufferedImage backgroundImg = new BufferedImage(zoneWidth, zoneHeight, BufferedImage.TYPE_INT_ARGB);


			for (int i = minX; i <= maxX; i++) 
			{
				for (int j = minY; j <= maxY; j++) 
				{
					if (visited[i][j]) 
					{
						backgroundImg.setRGB(i - minX, j - minY, argb);
						aServie = true;
					}
				}
			}

			Image newBackgroundImage = new Image(minX, minY, backgroundImg); 
			this.lstImages.add(1,newBackgroundImage);
		}

		this.save();
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
	public void setLuminosite(Image image, int var) 
	{
		if (var == 0) return;
	
		BufferedImage biOg = image.getImgOg();

		BufferedImage bi = new BufferedImage(biOg.getWidth(), biOg.getHeight(), BufferedImage.TYPE_INT_ARGB);
	
		for (int x = 0; x < biOg.getWidth(); x++) 
		{
			for (int y = 0; y < biOg.getHeight(); y++) 
			{
				int pixelColor = biOg.getRGB(x, y);
	
				if (!Paint.isTrans(pixelColor)) 
				{
					int nouvVal = Paint.luminosite(new Color(pixelColor & 0xFFFFFF), var);
					int alpha = (pixelColor >> 24) & 0xFF;  // Extraire l'alpha du pixel original
					Color newColor = new Color(nouvVal >> 16 & 0xFF, nouvVal >> 8 & 0xFF, nouvVal & 0xFF, alpha); // Conserver l'alpha
					bi.setRGB(x, y, newColor.getRGB());
				}
			}
		}
	
		image.setImg(bi);
	}
	


	/**
	 * Changer la luminosité d'une zone rectangulaire.
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 * @param var
	 */
	public void setLuminosite(Rectangle rect, int var) 
	{
		Image img = this.trim(rect);
		if (img == null) return;

		this.lstImages.add(img);
		this.setLuminosite(img, var);
	}

	/**
	 * Changer la luminosité d'une zone circulaire.
	 * @param xCenter
	 * @param yCenter
	 * @param radius
	 * @param var
	 */
	public void setLuminosite(Circle cerc, int var) 
	{
		Image img = this.trim(cerc);
		if (img == null) return;

		this.lstImages.add(img);
		this.setLuminosite(img, var);
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
		if (angle == 0 || angle == 360) 
		{
			image.setImg(image.getImgOg());
			return;
		}
	
		BufferedImage img = image.getImgOg();
	
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
	
		// Remplir l'image tournée
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
	
		image.setImg(rotatedImg);
	
		try 
		{
			ImageIO.write(image.getImg(), "png", new File("simple.png"));
			ImageIO.write(image.getImgOg(), "png", new File("og.png"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}
	
	
	

	
	/**
	 * Flip d'une zone rectangulaire et rotation.
	 * @param angle L'angle de rotation en degrés.
	 * @param xStart Coordonnée x du coin supérieur gauche de la zone rectangulaire.
	 * @param yStart Coordonnée y du coin supérieur gauche de la zone rectangulaire.
	 * @param xEnd Coordonnée x du coin inférieur droit de la zone rectangulaire.
	 * @param yEnd Coordonnée y du coin inférieur droit de la zone rectangulaire.
	 */
	public void rotate(Rectangle rect, int angle) 
	{
		if (angle == 0 || angle == 360) return;

		Image zoneImage = this.trim(rect);
		if (zoneImage == null) return;

		this.lstImages.add(zoneImage);
		this.rotate(zoneImage, angle);
	}


	
	
	
	/**
	 * Rotation d'une zone circulaire.
	 * @param xCentre Coordonnée x du centre du cercle.
	 * @param yCentre Coordonnée y du centre du cercle.
	 * @param radius Rayon du cercle.
	 * @param angle L'angle de rotation en degrés.
	 */

	public void rotate(Circle cerc, int angle) 
	{

		if (angle == 0 || angle == 360) return;

		Image zoneImageObj = this.trim(cerc);
		if (zoneImageObj == null) return;

		this.lstImages.add(zoneImageObj);
		rotate(zoneImageObj, angle);

		this.lstImages.add(zoneImageObj);
	}


	




		





	/* --------------------------------------------------------------------------------------------------- */
	/*                                            METHODE INVERSER                                         */
	/* --------------------------------------------------------------------------------------------------- */
	
	/**
	 * Flip Vertical d'une image.
	 * @param img
	 */
	public void flipVertical(Image img) 
	{
		this.save();

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

		this.save();
	}
	
	
	
	
	/**
	* Flip Vertical d'une zone rectangulaire.
	* @param xStart
	* @param yStart
	* @param xEnd
	* @param yEnd
	*/
	public void flipVertical(Rectangle rect) 
	{
		Image img = this.trim(rect);
		if (img == null) return;

		this.lstImages.add(img);
		this.flipVertical(img);
	}

	public void flipVertical(Circle cerc) 
	{
		Image img = this.trim(cerc);
		if (img == null) return;

		this.lstImages.add(img);
		this.flipVertical(img);
	}

	



	/**
	 * Flip Horizontal d'une image.
	 * @param img
	 */
	public void flipHorizontal(Image img) 
	{
		this.save();

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
	 * Flip Horizontal d'une zone rectangulaire.
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 */
	public void flipHorizontal(Rectangle rect) 
	{
		Image img = this.trim(rect);
		if (img == null) return;

		this.lstImages.add(img);
		this.flipHorizontal(img);
	}

	

	/**
	 * Flip Horizontal d'une zone circulaire.
	 * @param xCenter
	 * @param yCenter
	 * @param radius
	 */
	public void flipHorizontal(Circle cerc) 
	{
		Image img = this.trim(cerc);
		if (img == null) return;

		this.lstImages.add(img);
		this.flipHorizontal(img);
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
	
	 public Image addText(String text, boolean bold, boolean italic, String police, int size, int argb, int x, int y)
	{
		int style = Font.PLAIN;
		if (bold) style |= Font.BOLD;
		if (italic) style |= Font.ITALIC;
		
		Font font = new Font(police, style, size);
		
		BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = dummyImage.createGraphics();
		g2d.setFont(font);
		FontMetrics metrics = g2d.getFontMetrics();
		
		int textWidth  = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();
		
		BufferedImage bi = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_INT_ARGB);
		
		g2d = bi.createGraphics();
		g2d.setColor(new Color(argb));
		g2d.setFont(font);
		g2d.drawString(text, 0, metrics.getAscent()); 
		
		g2d.dispose();
		
		Image imgText = new Image(x, y, bi);
		this.addImage(imgText);
		this.save();
		
		return imgText;
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


		this.save();
	}




	/* --------------------------------------------------------------------------------------------------- */
	/*                                            METHODE trim                                           */
	/* --------------------------------------------------------------------------------------------------- */

	public Image trim (Rectangle rect)
	{
		if (this.lstImages.size() < 1) return null;

		int xStart = rect.x();
		int yStart = rect.y();

		int xEnd = rect.xEnd();
		int yEnd = rect.yEnd();

		
		BufferedImage img = this.getImageWithoutBackground();

		BufferedImage zoneImage = img.getSubimage(xStart, yStart, xEnd - xStart, yEnd - yStart);
		Image zoneImageObj = new Image(xStart, yStart, zoneImage);


		boolean isTransparent = true;
		for (int x = 0; x < zoneImage.getWidth(); x++) {
			for (int y = 0; y < zoneImage.getHeight(); y++) 
			{
				int pixel = zoneImage.getRGB(x, y);
				if (!isTrans(pixel)) 
				{ 
					isTransparent = false;
					break;
				}
			}
			if (!isTransparent) break;
		}

		if (isTransparent) return null;

		for (int x = xStart; x < xEnd; x++) 
		{
			for (int y = yStart; y < yEnd; y++) 
			{
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
							bi.setRGB(localX, localY, 0x00000000);  
						}
					}
				}
			}
		}

		return zoneImageObj;
	}


	public Image trim (Circle cerc)
	{
		if (this.lstImages.size() < 1) return null;

		int xCentre = cerc.xCenter();
		int yCentre = cerc.yCenter();
		int radius  = cerc.radius();
		
		BufferedImage img = this.getImageWithoutBackground();

		// Calcul des limites de la zone circulaire
		int xStart = Math.max(xCentre - radius, 0);
		int yStart = Math.max(yCentre - radius, 0);
		int width = Math.min(radius * 2, img.getWidth() - xStart);
		int height = Math.min(radius * 2, img.getHeight() - yStart);

		// Créer une image pour contenir la zone circulaire avant rotation
		BufferedImage zoneImage = img.getSubimage(xStart, yStart, width, height);
		Image zoneImageObj = new Image(xCentre - radius, yCentre - radius, zoneImage);

		for (int x = xCentre - radius; x < xCentre + radius; x++) 
		{
			for (int y = yCentre - radius; y < yCentre + radius; y++) 
			{
				if (Math.pow(x - xCentre, 2) + Math.pow(y - yCentre, 2) <= Math.pow(radius, 2)) 
				{
					ArrayList<Image> lst = this.getClickedImages(x, y);
					for (Image imgInZone : lst) {
						// Si l'image est totalement couverte dans la zone, on l'enlève de la liste
						if (imgInZone.getX() >= xStart && imgInZone.getX() + imgInZone.getImgWidth () <= xStart + width &&
							imgInZone.getY() >= yStart && imgInZone.getY() + imgInZone.getImgHeight() <= yStart + height) {
							this.lstImages.remove(imgInZone);
						} else {
							BufferedImage bi = imgInZone.getImg();
							int localX = x - imgInZone.getX();
							int localY = y - imgInZone.getY();
							if (localX >= 0 && localX < bi.getWidth() && localY >= 0 && localY < bi.getHeight()) 
								bi.setRGB(localX, localY, 0x00000000);  // Effacer le pixel
						}
					}
				} else { 
					zoneImage.setRGB(x - zoneImageObj.getX(), y - zoneImageObj.getY(), 0x00000000);
				}
			}
		}

		boolean isTransparent = true;
		for (int x = 0; x < zoneImage.getWidth(); x++) {
			for (int y = 0; y < zoneImage.getHeight(); y++) 
			{
				int pixel = zoneImage.getRGB(x, y);
				if (!isTrans(pixel)) 
				{ 
					isTransparent = false;
					break;
				}
			}
			if (!isTransparent) break;
		}

		if (isTransparent) return null;

		return zoneImageObj;
	}



	/* --------------------------------------------------------------------------------------------------- */
	/*                                              MAIN DE TEST                                           */
	/* --------------------------------------------------------------------------------------------------- */


	public static void main(String[] args) {
		Paint p = new Paint();

		try {
			Image img = new Image(0,0, ImageIO.read(new File("src/metier/test.png")));
			p.addImage(img);

			p.removeBackground(img, img.getImg().getRGB(0, 0), 20);
			// p.bucket(0,0,Color.RED.getRGB(), 30);

			// p.flipVertical(50,50,500,350);
			// p.setLuminosite(0,0,600,300,-50);
			// p.setLuminosite(img, 50);
			// p.setLuminosite(img, -50);
			// p.setLuminosite(img, -50);

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
