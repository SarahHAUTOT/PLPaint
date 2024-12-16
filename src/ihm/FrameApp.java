package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import metier.Controlleur;


public class FrameApp extends JFrame
{
	public static int width  = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth () * 0.8);
	public static int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.85);
	
	public static final Color COUL_PRIMARY   = Color.decode("#E1EBF3"); // Couleur primaire de l'application
	public static final Color COUL_SECONDARY = Color.decode("#729ab9"); // Couleur secondaire de l'application
	public static final Color COUL_NO_BG     = Color.decode("#BFBFBF"); // Couleur primaire de l'application
	
	private Controlleur  ctrl;

	private MenuPaint    menu;
	private PanelControl panelControl;
	private PanelImage   panelImage;

	public FrameApp(Controlleur ctrl)
	{
		/* Création des composants */
		this.panelControl = new PanelControl(this);
		this.panelImage   = new PanelImage  (this);
		this.menu         = new MenuPaint   (this);
		this.ctrl         = ctrl;

		/* Configuration de la frame */
		this.setLayout(new BorderLayout());
		this.setSize(FrameApp.width, FrameApp.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Positionnement des composants */
		this.setJMenuBar(menu);
		this.add(panelControl, BorderLayout.EAST);
		this.add(panelImage, BorderLayout.CENTER);
		this.setVisible(true);
		this.setResizable(false);
	}

	/* --------------------------------------------------------------------------------- */
	/*                              METHODE DES PANNELS                                  */
	/* --------------------------------------------------------------------------------- */

	public void setFullImage(BufferedImage bi)
	{
		this.panelImage.setFullImage(bi);
		this.repaint();
	}

	/* --------------------------------------------------------------------------------- */
	/*                             METHODE DU CONTROLEUR                                 */
	/* --------------------------------------------------------------------------------- */
}
