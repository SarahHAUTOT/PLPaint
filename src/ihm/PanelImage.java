package ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import metier.Circle;
import metier.Image;
import metier.Point;
import metier.Rectangle;

public class PanelImage extends JPanel implements MouseMotionListener, MouseListener, KeyListener
{
	private FrameApp      frame;
	private BufferedImage fullImage;

	private Point     startingCoord;

	private Image     selectedImage;
	private Rectangle selectedRectangle;
	private Circle    selectedCircle;

	public PanelImage(FrameApp frame)
	{
		/* Création des composants */
		this.frame      = frame;
		this.fullImage  = null;
		this.startingCoord = null;

		this.selectedImage     = null;
		this.selectedRectangle = null;
		this.selectedCircle    = null;

		/* Configurer du panel */
		this.setBackground(FrameApp.COUL_NO_BG);

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

	public BufferedImage getFullImage        () { return this.fullImage; }
	public Rectangle     getSelectedRectangle() { return this.selectedRectangle; }
	public Image         getSelectedImage    () { return this.selectedImage; }
	public Circle        getSelectedCircle   () { return this.selectedCircle; }
	
	/**
	 * Désactive la séléction ainsi que 
	 * sa représentation graphique (rafraichissement)
	 */
	public void disableSelection()
	{
		this.selectedCircle    = null;
		this.selectedImage     = null;
		this.selectedRectangle = null;
		repaint();
	}

	private void outlineRect(Graphics g, Rectangle rect)
	{
        Graphics2D g2d = (Graphics2D) g;

        // Définit la couleur et l'épaisseur de la surbrillance
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));

        int width  = rect.xEnd() - rect.x();
        int height = rect.yEnd() - rect.y();

        // Dessine le contour
        g2d.drawRect(rect.x(), rect.y(), width, height);
    }

	private void outlineCirle(Graphics g, Circle cirle)
	{
        Graphics2D g2d = (Graphics2D) g;

        // Définit la couleur et l'épaisseur de la surbrillance
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));

		int height, width;
        width = height = cirle.radius();

        // Dessine le contour
        // g2d.drawCircle()
    }
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(fullImage, 0, 0, this);

		if (this.selectedRectangle != null)
			this.outlineRect(g, this.selectedRectangle);

		if (this.selectedCircle != null)
			this.outlineCirle(g, this.selectedCircle);
		
		if (this.selectedImage != null)
			this.outlineRect(g,
				new Rectangle(
					this.selectedImage.getX(),
					this.selectedImage.getY(), 
					this.selectedImage.getImgWidth () + this.selectedImage.getX(),
					this.selectedImage.getImgHeight() + this.selectedImage.getY()
				)
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
		this.paintComponent(getGraphics());
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
		if (this.frame.getAction() == FrameApp.ACTION_EYEDROPPER)
		{
			this.frame.setSelectedColor(this.fullImage.getRGB(currentCoord.x(), currentCoord.y()));
			this.startingCoord = null;
		}

		// Action du Crayon
		if (this.frame.getAction() == FrameApp.ACTION_CRAYON)
		{
			// TODO : this.draw(currentCoord.x(), currentCoord.y(), this.selectedArgb);
			this.startingCoord = null;
		}

		// Actiond du remplissage
		if (this.frame.getAction() == FrameApp.ACTION_BUCKET)
		{
			this.frame.bucket(
				currentCoord.x(),
				currentCoord.y(),
				4789456,
				this.frame.getDistance()
			);

			this.frame.repaintImagePanel();
			this.startingCoord = null;
		}

		// Action de la séléction d'une image
		if (currentCoord != null)
		{
			this.selectedImage = this.frame.getClickedImage(currentCoord.x(), currentCoord.y());

			this.selectedRectangle = null;
			this.selectedCircle = null;

			repaint(); // Rafraichir pour voir la séléction graphique de l'image
		}

		this.frame.setLabelAction("Mode Curseur");
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (this.fullImage == null) return;

		// Initialisation de la coordonée de départ
		if (this.startingCoord == null && 
			e.getX() < this.fullImage.getWidth () && e.getX() > 0 &&
			e.getY() < this.fullImage.getHeight() && e.getY() > 0)
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
		
		System.out.println( "mouseReleased : x:" + this.startingCoord.x() + " y:" + this.startingCoord.y());
		this.startingCoord = null;
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (this.fullImage == null) return;
		
		// Initialisation de la coordonée cliquée
		// qui forme le rectangle de séléction
		int x, y;
		x = (e.getX() > this.fullImage.getWidth ()) ? this.fullImage.getWidth () : e.getX();
		y = (e.getY() > this.fullImage.getHeight()) ? this.fullImage.getHeight() : e.getY();
		
		Point currentCoord = new Point(x, y);

		if (this.frame.getAction() == FrameApp.ACTION_SELECT_CIRCLE)
		{
			this.selectedCircle = new Circle(
				currentCoord.x() - this.startingCoord.x(),
				currentCoord.y() - this.startingCoord.y(),
				this.startingCoord.x() - currentCoord.x()
			);
			
			System.out.println("cercle : x=" + selectedCircle.xCenter() + " y=" + selectedCircle.yCenter());
		}

		this.selectedImage = null; // On remet l'image séléctionnée à null
		this.selectedRectangle = new Rectangle
		(
			this.startingCoord.x(),
			this.startingCoord.y(),
			currentCoord.x(),
			currentCoord.y()
		);

		repaint(); // Rafraichir pour voir la séléction graphique du rectangle
	}

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
