package ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Cursor;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

import metier.Circle;
import metier.Image;
import metier.Point;
import metier.Rectangle;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.Stroke;
import java.awt.Dimension;

public class PanelImage extends JPanel implements MouseMotionListener, MouseListener
{
	private PLPaint      frame;
	private BufferedImage fullImage;

	private Point     startingCoord;

	private Image     selectedImage;
	private Rectangle selectedRectangle;
	private Circle    selectedCircle;

	private ArrayList<Point> pencilPoints;

	private JTextField textInput; // Champ de texte unique


	public PanelImage(PLPaint frame)
	{
		/* Création des composants */
		this.frame      = frame;
		this.fullImage  = null;
		this.startingCoord = null;
		this.pencilPoints  = new ArrayList<Point>();

		this.selectedImage     = null;
		this.selectedRectangle = null;
		this.selectedCircle    = null;

		/* Configurer du panel */
		this.setBackground(PLPaint.COUL_NO_BG);
		this.setFocusable(true);
		this.setLayout(null);

		/* Ecouteur de la souris */
		this.addMouseListener      (this);
		this.addMouseMotionListener(this);
	}

	/**
	 * Retourne vrai s'il y a selection d'un élément
	 * @return 
	 */
	public boolean hasSelection()
	{
		return this.frame.getSelectedCircle   () != null ||
			   this.frame.getSelectedImage    () != null ||
			   this.frame.getSelectedRectangle() != null;
	}

	public void setSelectedImage(Image img) { this.selectedImage = img; }

	public BufferedImage getFullImage        () { return this.fullImage; }
	public Rectangle     getSelectedRectangle() { return this.selectedRectangle; }
	public Image         getSelectedImage    () { return this.selectedImage; }
	public Circle        getSelectedCircle   () { return this.selectedCircle; }
	
	public void hideTextInput()
	{
		if (this.textInput != null) this.remove(this.textInput);
		this.revalidate();
	}

	public String getText()
	{
		if (this.textInput != null) return this.textInput.getText();

		return null;
	}

	public java.awt.Point getTextLocation()
	{
		if (this.textInput != null) return this.textInput.getLocation();
		return null;
	}

	/**
	 * Désactive la séléction d'éléments ainsi que 
	 * sa représentation graphique (rafraichissement)
	 */
	public void disableSelection()
	{
		this.selectedCircle    = null;
		this.selectedImage     = null;
		this.selectedRectangle = null;
		repaint();
	}

	public void selectScreen()
	{
		if (this.fullImage != null)
		{
			this.disableSelection();
			this.selectedRectangle = new Rectangle(0, 0, this.fullImage.getWidth(), this.fullImage.getHeight());
			this.repaint();
		}
	}

	private void outlineRect(Graphics g, Rectangle rect, Color color)
	{
		Graphics2D g2d = (Graphics2D) g;

		// Définit la couleur et l'épaisseur de la surbrillance
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(2));

		int width  = rect.xEnd() - rect.x();
		int height = rect.yEnd() - rect.y();

		// Dessine le contour en poitillé
		float[] dash = { 4F, 4F };
		Rectangle2D rectangle2d = new Rectangle2D.Float(rect.x() +1, rect.y() +1, width -1, height-1);

		Stroke dashedStroke = new BasicStroke(2F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1F, dash, 4F);
		g2d.fill(dashedStroke.createStrokedShape(rectangle2d));
	}

	private void outlineCircle(Graphics g, Circle circle, Color color)
	{
		Graphics2D g2d = (Graphics2D) g;

		// Définit la couleur et l'épaisseur de la surbrillance
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(2));

		// Dessine le contour
		float[] dash = { 8F, 8F };
		Ellipse2D ellpise2d = new Ellipse2D.Float(circle.xCenter() - circle.radius(), circle.yCenter() - circle.radius(), circle.radius() * 2, circle.radius() * 2);

		Stroke dashedStroke = new BasicStroke(3F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 2F, dash, 6F);
		g2d.fill(dashedStroke.createStrokedShape(ellpise2d));
	}

	public void addPencilDrawing()
	{
		int minX, minY;
		minX = minY = Integer.MAX_VALUE;

		int maxX, maxY;
		maxY = maxX = Integer.MIN_VALUE;

		for (Point p : this.pencilPoints)
		{
			minX = (minX > p.x()) ? p.x(): minX;
			minY = (minY > p.y()) ? p.y(): minY;
			maxX = (maxX < p.x()) ? p.x(): maxX;
			maxY = (maxY < p.y()) ? p.y(): maxY;
		}

		BufferedImage bi = new BufferedImage(maxX - minX +1, maxY - minY +1, BufferedImage.TYPE_INT_ARGB);
		
		// Couleur du trait du dessin
		Graphics2D g2d = bi.createGraphics();
		g2d.setPaint(new Color(this.frame.getSelectedColor()));
		Stroke stroke = new BasicStroke(2f);
		g2d.setStroke(stroke);
		
		for (int i = 1; i < this.pencilPoints.size(); i++)
		{
			Point lastCoord = this.pencilPoints.get(i -1);
			Point coord     = this.pencilPoints.get(i);
			g2d.drawLine(
				lastCoord.x() - minX,
				lastCoord.y() - minY, 
            	coord.x() - minX,
				coord.y() - minY
			);
		}

		Image pencilDrawing = new Image(minX, minY, bi);

		this.frame.addImage(pencilDrawing);
		this.frame.selectLastImage();
		this.pencilPoints.clear();
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(fullImage, 0, 0, this);

		// Affichage de la séléction d'un rectangle
		if (this.selectedRectangle != null)
			this.outlineRect(g, this.selectedRectangle, Color.RED);

		// Affichage de la séléction d'un cercle
		if (this.selectedCircle != null)
			this.outlineCircle(g, this.selectedCircle, Color.RED);
		
		// Affichage de la séléction d'une image (rectangle)
		if (this.selectedImage != null)
			this.outlineRect(g,
				new Rectangle(
					this.selectedImage.getX(),
					this.selectedImage.getY(), 
					this.selectedImage.getImgWidth () + this.selectedImage.getX(),
					this.selectedImage.getImgHeight() + this.selectedImage.getY()
				),
				Color.YELLOW
			);
		
		// Affichage du tracé du trait
		if (this.pencilPoints.size() > 2)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint(new Color(this.frame.getSelectedColor()));
			Stroke stroke = new BasicStroke(2f);
			g2d.setStroke(stroke);
			
			for (int i = 1; i < this.pencilPoints.size(); i++)
			{
				Point lastCoord = this.pencilPoints.get(i -1);
				Point coord     = this.pencilPoints.get(i);
				g2d.drawLine(
					lastCoord.x(),
					lastCoord.y(), 
					coord.x(),
					coord.y()
				);
			}
		}
	}

	/* --------------------------------------------------------------------------------- */
	/*                              METHODE DE LA FRAME                                  */
	/* --------------------------------------------------------------------------------- */

	/**
	 * Cette méthode change l'image complète du panel Image
	 * et rafraichi la page.
	 * @param bi image posé sur le panel
	 */
	public void setFullImage(BufferedImage bi)
	{
		// Affectation de la nouvelle image
		this.fullImage = bi;
		this.repaint();
	}

	
    public Dimension getPreferredSize() 
	{
		if (this.fullImage != null) return new Dimension(this.fullImage.getWidth(), this.fullImage.getHeight());
        return super.getPreferredSize(); // Dimensions de l'image
    }

	/* --------------------------------------------------------------------------------- */
	/*                            METHODE ECOUTEUR SOURIS                                */
	/* --------------------------------------------------------------------------------- */

	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.hideTextInput();
		if (this.fullImage == null) return;
		
		// Si c'est une action qui necessite un élément selectionné on empeche la desselection 
		if (this.frame.getAction() == PLPaint.ACTION_ROTATION || this.frame.getAction() == PLPaint.ACTION_BRIGHTNESS)
			return;
		
		// On remet à null tout les éléments séléctionnés
		this.disableSelection();

		this.startingCoord = null;

		// Initialisation de la coordonée cliquée
		Point currentCoord = null;
		if (e.getX() < this.fullImage.getWidth () && e.getX() > 0 &&
			e.getY() < this.fullImage.getHeight() && e.getY() > 0)
		{
			currentCoord = new Point(e.getX(), e.getY());
		}
		else
		{
			this.frame.defaultAction();
			return;
		}

		// Actiond du remplissage
		if (this.frame.getAction() == PLPaint.ACTION_BUCKET)
		{
			this.frame.bucket(
				currentCoord.x(),
				currentCoord.y(),
				this.frame.getSelectedColor(),
				this.frame.getDistance()
			);

			this.frame.repaintImagePanel();
			return;
		}

		// Action du texte
		if (this.frame.getAction() == PLPaint.ACTION_WRITE_TEXT)
		{
			if (this.textInput != null) this.remove(this.textInput);
			
			this.textInput = new JTextField("");
			this.textInput.setBounds(currentCoord.x(), currentCoord.y() - 25, 150, 25);
			this.setVisible(true);

			this.add(this.textInput);

			return;
		}

		// Action de la Pipette
		if (this.frame.getAction() == PLPaint.ACTION_EYEDROPPER)
		{
			this.frame.setSelectedColor(this.fullImage.getRGB(currentCoord.x(), currentCoord.y()));
			if (this.frame.isChildren())
				this.frame.selectLastImage();
			
			this.frame.defaultAction();
			return;
		}

		// Action de la séléction d'une image
		if (this.frame.getAction() == PLPaint.ACTION_DEFAULT)
		{
			this.frame.defaultAction();
			this.selectedImage = this.frame.getClickedImage(currentCoord.x(), currentCoord.y());
			if (this.selectedImage != null)
				this.frame.setLabelAction("Séléction Image");
		}

		// Action de rognage, pinceau, et retourner
		if (this.frame.getAction() == PLPaint.ACTION_TRIM_SURFACE    ||
			this.frame.getAction() == PLPaint.ACTION_PENCIL          ||
			this.frame.getAction() == PLPaint.ACTION_VERTICAL_FLIP   ||
			this.frame.getAction() == PLPaint.ACTION_HORIZONTAL_FLIP ||
			this.frame.getAction() == PLPaint.ACTION_SELECT_CIRCLE   ||
			this.frame.getAction() == PLPaint.ACTION_SELECT_RECTANGLE)
		{
			this.frame.defaultAction();
			return;
		}
	}

	public void setCursor(String fic) 
	{
		try 
		{
			// Chargement de l'image comme ressource
			BufferedImage image = ImageIO.read(getClass().getResource(fic));
			
			// Création d'un curseur personnalisé
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(image, new java.awt.Point(0, 30), "customCursor");
			this.setCursor(c);
		} 
		catch (IOException | NullPointerException e) 
		{
			e.printStackTrace();
		}
	}



	public void mousePressed(MouseEvent e)
	{
		if (this.fullImage == null) return;

		// Séléction en dehors de l'image autorisé lors du déplacement d'une l'image
		// Initialisation de la coordonée de départ
		boolean clickInFullImage = this.startingCoord == null &&
			e.getX() < this.fullImage.getWidth () && e.getX() > 0 &&
			e.getY() < this.fullImage.getHeight() && e.getY() > 0;
		
		if (clickInFullImage || this.selectedImage != null)
		{
			this.startingCoord = new Point(e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if (this.fullImage == null || this.startingCoord == null) return;
		
		int x, y;
		x = (e.getX() > this.fullImage.getWidth ()) ? this.fullImage.getWidth () : e.getX();
		y = (e.getY() > this.fullImage.getHeight()) ? this.fullImage.getHeight() : e.getY();
		y = (e.getY() < 0) ? 0 : y;
		x = (e.getX() < 0) ? 0 : x;

		// Déplacement de l'image : Rafraichissement de l'image
		if (this.selectedImage != null)
		{
			this.frame.repaintImagePanel();
			this.frame.save();
		}
		
		// Création de l'image crée par le pinceau
		if (this.frame.getAction() == PLPaint.ACTION_PENCIL)
		{
			this.pencilPoints.add(new Point(x,y));
			this.addPencilDrawing();
			this.frame.repaintImagePanel();
		}

		this.startingCoord = null;
	}

	public void mouseDragged(MouseEvent e)
	{
		if (this.fullImage == null || this.startingCoord == null) return;
		
		// Initialisation de la coordonée au clic maintenu
		// Elle est limitée à la taille du panel
		int x, y;
		x = (e.getX() > this.fullImage.getWidth ()) ? this.fullImage.getWidth () : e.getX();
		y = (e.getY() > this.fullImage.getHeight()) ? this.fullImage.getHeight() : e.getY();
		y = (e.getY() < 0) ? 0 : y;
		x = (e.getX() < 0) ? 0 : x;
		
		Point currentCoord = new Point(x, y);

		if (this.frame.getAction() == PLPaint.ACTION_PENCIL)
		{
			if (!this.pencilPoints.isEmpty())
			{
				Point lastCoord = this.pencilPoints.get(this.pencilPoints.size() -1);	
				if (!lastCoord.equals(currentCoord)) 
					this.pencilPoints.add(currentCoord);
			}

			this.pencilPoints.add(currentCoord);
			this.disableSelection();
		}

		if (!this.frame.isChildren() && this.selectedImage != null)
		{
			this.selectedImage.setX(x - this.selectedImage.getImgWidth () /2);
			this.selectedImage.setY(y - this.selectedImage.getImgHeight() /2);
		}

		if (this.frame.getAction() == PLPaint.ACTION_SELECT_CIRCLE)
		{
			int radius1 = Math.abs((currentCoord.x() - this.startingCoord.x()) / 2);
			int radius2 = Math.abs((currentCoord.y() - this.startingCoord.y()) / 2);

			int radius = radius1 < radius2 ? radius1 : radius2;
			this.selectedCircle = new Circle(
				this.startingCoord.x() + radius,
				this.startingCoord.y() + radius,
				radius
			);

			this.selectedImage = null; // On remet l'image séléctionnée à null
		}

		
		if (this.frame.getAction() == PLPaint.ACTION_SELECT_RECTANGLE)
		{
			this.selectedRectangle = new Rectangle
			(
				this.startingCoord.x(),
				this.startingCoord.y(),
				currentCoord.x(),
				currentCoord.y()
			);

			this.selectedImage = null; // On remet l'image séléctionnée à null
		}
		
		repaint(); // Rafraichir pour voir la séléction graphique du rectangle
	}

	public void mouseMoved  (MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited (MouseEvent e) {}
}
