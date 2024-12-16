package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import metier.Image;
import metier.Paint;


public class FrameApp extends JFrame
{
	// Mappage des différentes acitons possibles par l'utilisateur
	public static final int ACTION_EYEDROPPER = 1;
	public static final int ACTION_BUCKET     = 2;

	public static final int ACTION_RUBBER = 3;
	public static final int ACTION_CRAYON = 4;

	public static final int ACTION_DARKEN   = 5;
	public static final int ACTION_BRIGHTEN = 6;

	public static final int ACTION_ROTATION    = 7;
	public static final int ACTION_TURN_AROUND = 8;

	public static final int ACTION_SELECT_RECTANGLE = 9;
	public static final int ACTION_SELECT_CIRCLE    = 10;

	public static final int ACTION_REMOVE_BG  = 11;
	public static final int ACTION_WRITE_TEXT = 12;

	// Définition de la taille de la fenêtre
	public static final int DEFAULT_WIDTH  = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth () * 0.8);
	public static final int DEFAULT_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.85);
	
	public static final Color COUL_PRIMARY   = Color.decode("#E1EBF3");// Couleur primaire de l'application
	public static final Color COUL_SECONDARY = Color.decode("#729ab9");// Couleur secondaire de l'application
	public static final Color COUL_NO_BG     = Color.decode("#BFBFBF");// Couleur primaire de l'application
	
	private Paint        metier;

	private MenuPaint    menu;
	private PanelControl panelControl;
	private PanelImage   panelImage;

	public FrameApp(Paint metier)
	{
		/* Création des composants */
		this.panelControl = new PanelControl(this);
		this.panelImage   = new PanelImage  (this);
		this.menu         = new MenuPaint   (this);
		this.metier       = metier;

		/* Configuration de la frame */
		this.setLayout(new BorderLayout());
		this.setSize(FrameApp.DEFAULT_WIDTH, FrameApp.DEFAULT_HEIGHT);
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

	public int getDistance() { return this.panelControl.getDistance(); }

	public int getAction() { return this.panelControl.getAction();}

	/* ------------------------------------------------------------------------------- */
	/*                              METHODE DU METIER                                  */
	/* ------------------------------------------------------------------------------- */
	public Image getClickedImage(int x, int y)
	{
		return this.metier.getClickedImage(x, y);
	}

	public void bucket(int x, int y, int argb, int distance)
	{
		this.metier.bucket(x, y, argb, distance);
	}
}
