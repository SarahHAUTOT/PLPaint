package ihm;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PanelImage extends JPanel implements MouseListener
{
	private FrameApp      frame;
	private BufferedImage fullImage;

	public PanelImage(FrameApp frame)
	{
		/* Création des composants */
		this.frame      = frame;
		this.fullImage  = null;

		/* Configurer du panel */
		this.setBackground(FrameApp.COUL_NO_BG);

		/* Ecouteur de la souris */
		this.addMouseListener(this);
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
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
