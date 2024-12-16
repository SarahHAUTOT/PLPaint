package ihm;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import metier.Image;
import metier.Point;

public class PanelImage extends JPanel implements MouseListener
{
	private FrameApp      frame;
	private BufferedImage fullImage;
	private Point         lastCoord;
	private Image         selectedImage;
	private int           selectedArgb;

	public PanelImage(FrameApp frame)
	{
		/* Création des composants */
		this.frame      = frame;
		this.fullImage  = null;
		this.lastCoord  = null;
		this.selectedImage = null;
		this.selectedArgb  = 0;

		/* Configurer du panel */
		this.setBackground(FrameApp.COUL_NO_BG);

		/* Ecouteur de la souris */
		this.addMouseListener(this);
	}
	
	private void showOutline(Image image)
	{
		// TODO : contour de l'image en surbrillance
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(fullImage, 0, 0, this);
	}

	/* --------------------------------------------------------------------------------- */
	/*                              METHODE DE LA FRAME                                  */
	/* --------------------------------------------------------------------------------- */

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
		System.out.println("mouseClicked");
		if (this.fullImage == null) return;
		
		// Initialisation de la coordonée 
		Point currentCoord = null;
		if (e.getX() < this.fullImage.getWidth () && e.getX() > this.fullImage.getWidth () &&
			e.getY() < this.fullImage.getHeight() && e.getY() > this.fullImage.getHeight())
		{
			currentCoord = new Point(e.getX(), e.getY());
		}

		// Action de la Pipette
		if (this.frame.getAction() == FrameApp.ACTION_EYEDROPPER && currentCoord != null)
		{
			this.selectedArgb = this.fullImage.getRGB(currentCoord.x(), currentCoord.y());
			this.lastCoord = null;
			return;
		}

		// Action du Crayon
		if (this.frame.getAction() == FrameApp.ACTION_CRAYON && currentCoord != null)
		{
			// TODO : this.draw(currentCoord.x(), currentCoord.y(), this.selectedArgb);
			this.lastCoord = null;
			return;
		}

		// Actiond du remplissage
		if (this.frame.getAction() == FrameApp.ACTION_BUCKET && currentCoord != null)
		{
			this.frame.bucket(
				currentCoord.x(),
				currentCoord.y(),
				this.fullImage.getRGB(currentCoord.x(), currentCoord.y()),
				this.frame.getDistance()
			);
			
			this.lastCoord = null;
			return;
		}

		// Action de la séléction d'une image
		if (this.frame.getAction() == FrameApp.ACTION_BUCKET && currentCoord != null)
		{
			this.selectedImage = this.frame.getClickedImage(currentCoord.x(), currentCoord.y());
			this.showOutline(this.selectedImage);
			this.lastCoord = null;
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		System.out.println("mousePressed");
		if (this.fullImage == null) return;
		// Initialisation de la coordonée 
		if (e.getX() < this.fullImage.getWidth () && e.getX() > this.fullImage.getWidth () &&
			e.getY() < this.fullImage.getHeight() && e.getY() > this.fullImage.getHeight())
			this.lastCoord = new Point(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		System.out.println("mouseReleased");
		if (this.fullImage == null) return;
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
