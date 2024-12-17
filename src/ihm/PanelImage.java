package ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.JTextField;

import metier.Circle;
import metier.Image;
import metier.Point;
import metier.Rectangle;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.Stroke;

public class PanelImage extends JPanel implements MouseMotionListener, MouseListener, KeyListener
{
	private PLPaint      frame;
	private BufferedImage fullImage;

	private Point     startingCoord;

	private Image     selectedImage;
	private Rectangle selectedRectangle;
	private Circle    selectedCircle;

	private ArrayList<Point> pencilPoints;

	private JTextField txtSaisie; // Champ de texte unique


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

		System.out.println("XCenter : " + circle.xCenter());
		System.out.println("YCenter : " + circle.yCenter());
		System.out.println("Radius  : " + circle.radius ());
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(fullImage, 0, 0, this);

		if (this.selectedRectangle != null)
			this.outlineRect(g, this.selectedRectangle, Color.RED);

		if (this.selectedCircle != null)
			this.outlineCircle(g, this.selectedCircle, Color.RED);
		
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

	/* --------------------------------------------------------------------------------- */
	/*                            METHODE ECOUTEUR SOURIS                                */
	/* --------------------------------------------------------------------------------- */

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (this.fullImage == null) return;
		
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
			return;
		}

		System.out.println( "mousseClicked : x:" + currentCoord.x() + " y:" + currentCoord.y());

		// Action de la Pipette
		if (this.frame.getAction() == PLPaint.ACTION_EYEDROPPER)
		{
			this.frame.setSelectedColor(this.fullImage.getRGB(currentCoord.x(), currentCoord.y()));
			return;
		}

		// Action du Crayon
		if (this.frame.getAction() == PLPaint.ACTION_PENCIL)
		{
			// TODO : this.draw(currentCoord.x(), currentCoord.y(), this.selectedArgb);
			this.waiting();
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
			if (this.txtSaisie != null) this.remove(this.txtSaisie);
			
			this.txtSaisie = new JTextField("Feur");
			this.txtSaisie.setBounds(currentCoord.x(), currentCoord.y(), 150, 25);
			this.setVisible(true);

			this.add(this.txtSaisie);

			return;
		}

		// Action de la séléction d'une image
		this.selectedImage = this.frame.getClickedImage(currentCoord.x(), currentCoord.y());
		this.frame.setLabelAction("Séléction Image");
	}

	public void setCursor (String fic)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		java.awt.Image image = toolkit.getImage(fic);
		Cursor c = toolkit.createCustomCursor(image , new java.awt.Point(this.getX(), this.getY()), "img");
		this.setCursor(c);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (this.fullImage == null) return;
		System.out.println( "moussePressed : x:" + e.getX() + " y:" + e.getY());

		// Séléction en dehors de l'image lors du déplacement de l'image
		// Initialisation de la coordonée de départ
		if ( this.selectedImage != null ||
			(this.startingCoord == null && 
			 e.getX() < this.fullImage.getWidth () && e.getX() > 0 &&
			 e.getY() < this.fullImage.getHeight() && e.getY() > 0))
		{
			this.startingCoord = new Point(e.getX(), e.getY());
		}
		else
		{
			return;
		}
	}

	@Override
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
		}
		
		// Création de l'image crée par le pinceau
		if (this.frame.getAction() == PLPaint.ACTION_PENCIL)
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
			for (Point p : this.pencilPoints)
				bi.setRGB(maxX - p.x(), maxY - p.y(), this.frame.getSelectedColor());
			
			Image pencilDrawing = new Image(minX, minY, bi);

			System.out.println(bi);
			System.out.println("x=" + pencilDrawing.getX() + " y=" + pencilDrawing.getY());
			this.frame.addImage(pencilDrawing);
			this.pencilPoints.clear();

			this.frame.repaintImagePanel();
		}

		System.out.println( "mouseReleased : x:" + this.startingCoord.x() + " y:" + this.startingCoord.y());
		this.startingCoord = null;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (this.fullImage == null) return;
		
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

		if (this.selectedImage != null)
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

	public void waiting()
	{
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) { e1.printStackTrace(); }
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}
