package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Cursor;

import metier.Circle;
import metier.Image;
import metier.Paint;
import metier.Rectangle;


public class PLPaint extends JFrame
{
	// Mappage des différentes acitons possibles par l'utilisateur
	public static final int ACTION_DEFAULT = 0;
	public static final int ACTION_EYEDROPPER = 1;
	public static final int ACTION_BUCKET     = 2;

	public static final int ACTION_RUBBER = 3;
	public static final int ACTION_PENCIL = 4;

	public static final int ACTION_BRIGHTNESS = 5;
	public static final int ACTION_ROTATION   = 6;

	public static final int ACTION_HORIZONTAL_FLIP = 7;
	public static final int ACTION_VERTICAL_FLIP   = 8;

	public static final int ACTION_SELECT_RECTANGLE = 9;
	public static final int ACTION_SELECT_CIRCLE    = 10;

	public static final int ACTION_REMOVE_BG  = 11;
	public static final int ACTION_WRITE_TEXT = 12;

	// Définition de la taille de la fenêtre
	public static final int DEFAULT_WIDTH  = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth () * 0.8);
	public static final int DEFAULT_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.85);
	
	public static final Color COUL_PRIMARY   = Color.decode("#E1EBF3"); // Couleur primaire de l'application
	public static final Color COUL_SECONDARY = Color.decode("#F9F6EE"); // Couleur secondaire de l'application
	public static final Color COUL_NO_BG     = Color.decode("#BFBFBF"); // Couleur de fond par défault
	
	private Paint        metier;
	private MenuPaint    menu;
	private PanelControl panelControl;
	private PanelImage   panelImage;
	private JLabel       lblAction;

	public PLPaint(Paint metier)
	{
		/* Création des composants */
		this.panelControl = new PanelControl(this);
		this.panelImage   = new PanelImage  (this);
		this.menu         = new MenuPaint   (this);
		this.metier       = metier;
		this.lblAction    = new JLabel("Mode Curseur");

		/* Configuration de la frame */
		this.setLayout(new BorderLayout());
		this.setSize(PLPaint.DEFAULT_WIDTH, PLPaint.DEFAULT_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Positionnement des composants */
		this.setJMenuBar(menu);
		this.add(panelControl, BorderLayout.EAST);
		this.add(panelImage, BorderLayout.CENTER);
		this.add(this.lblAction, BorderLayout.SOUTH);

		this.setVisible(true);
		this.setResizable(true);

		java.awt.Image icon = Toolkit.getDefaultToolkit().getImage("./src/ihm/icons/logo.png");  
		this.setTitle("PLPaint");
		this.setIconImage(icon);
	}

	public void setLabelAction(String str) { this.lblAction.setText(str); }
	public void repaintImagePanel       () { this.setFullImage(this.getImage()); }
	
    public void selectLastImage()
	{
		int lengthLst = this.getImages().size();
		this.panelImage.setSelectedImage(this.getImages().get(lengthLst -1));
	}


	/* --------------------------------------------------------------------------------- */
	/*                              METHODE DES PANNELS                                  */
	/* --------------------------------------------------------------------------------- */

	// Panel de l'image
	public void setFullImage(BufferedImage bi)
	{
		this.panelImage.setFullImage(bi);
		this.repaint();
	}

	public void          disableSelection    () { this.panelImage.disableSelection(); }
	public BufferedImage getFullImage        () { return this.panelImage.getFullImage(); }
	public Circle        getSelectedCircle   () { return this.panelImage.getSelectedCircle(); }
	public Rectangle     getSelectedRectangle() { return this.panelImage.getSelectedRectangle(); }
	public Image         getSelectedImage    () { return this.panelImage.getSelectedImage(); }
	public boolean       hasSelection        () { return this.panelImage.hasSelection(); }


	// Panel de contrôle
	public void setSelectedColor(int argb  ) { this.panelControl.setSelectedColor(argb); }
	public void setAction       (int action) { this.panelControl.setAction(action); }

	public int getSelectedColor() { return this.panelControl.getSelectedColor(); }
	public int getDistance     () { return this.panelControl.getDistance(); }
	public int getAction       () { return this.panelControl.getAction  (); }

	/* ------------------------------------------------------------------------------- */
	/*                              METHODE DU METIER                                  */
	/* ------------------------------------------------------------------------------- */
	// Gestion des images
	public Image getClickedImage(int x, int y)
	{
		return this.metier.getClickedImage(x, y);
	}

	public ArrayList<Image> getImages()
	{
		return this.metier.getImages();
	}
	
	public BufferedImage getImage()
	{
		return this.metier.getImage();
	}

	public void addImage(Image img)
	{
		this.metier.addImage(img);
	}

	// Méthode de changement de luminosité
	public void setBrightnessImage(int value)
	{
		this.metier.setLuminosite(this.getSelectedImage(), value);
	}

	public void setBrightnessRect(int value)
	{
		this.metier.setLuminosite(this.getSelectedRectangle(), value);
	}

	public void setBrightnessCircle(int value)
	{
		this.metier.setLuminosite(this.getSelectedCircle(), value);
	}

	// Methode Seau
	public void bucket(int x, int y, int argb, int distance)
	{
		this.metier.bucket(x, y, argb, distance);
	}

	// Methode retourner
	public void flipHorizontalImage()
	{
		this.metier.flipHorizontal(this.getSelectedImage());
	}

	public void flipHorizontalRect()
	{
		this.metier.flipHorizontal(this.getSelectedRectangle());
	}

	public void flipHorizontalCircle()
	{
		this.metier.flipHorizontal(this.getSelectedCircle());
	}

	public void flipVerticalImage()
	{
		this.metier.flipVertical(this.getSelectedImage());
	}

	public void flipVerticalRect()
	{
		this.metier.flipVertical(this.getSelectedRectangle());
	}

	public void flipVerticalCircle()
	{
		this.metier.flipVertical(this.getSelectedCircle());
	}

	public void setCursor (Cursor cursor) { this.panelImage.setCursor(cursor); }
	public void setCursor (String cursor) { this.panelImage.setCursor(cursor); }


	public void setTextOption () { this.panelControl.setTextOption(); }
}
