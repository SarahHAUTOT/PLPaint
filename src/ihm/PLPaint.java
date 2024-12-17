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
	public static final int ACTION_CRAYON = 4;

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
	public static final Color COUL_SECONDARY = Color.decode("#729ab9"); // Couleur secondaire de l'application
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
		Rectangle rect = this.getSelectedRectangle();
		this.metier.setLuminosite(rect.x(), rect.y(), rect.xEnd(), rect.yEnd(), value);
	}

	public void setBrightnessCircle(int value)
	{
		Circle circle = this.getSelectedCircle();
		this.metier.setLuminosite(circle.xCenter(), circle.yCenter(), circle.radius(), value);
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
		Rectangle rect = this.getSelectedRectangle();
		this.metier.flipHorizontal(rect.x(), rect.y(), rect.xEnd(), rect.yEnd());
	}

	public void flipHorizontalCircle()
	{
		Circle circle = this.getSelectedCircle();
		this.metier.flipHorizontal(circle.xCenter(), circle.yCenter(), circle.radius());
	}

	public void flipVerticalImage()
	{
		this.metier.flipVertical(this.getSelectedImage());
	}

	public void flipVerticalRect()
	{
		Rectangle rect = this.getSelectedRectangle();
		this.metier.flipVertical(rect.x(), rect.y(), rect.xEnd(), rect.yEnd());
	}

	public void flipVerticalCircle()
	{
		Circle circle = this.getSelectedCircle();
		this.metier.flipVertical(circle.xCenter(), circle.yCenter(), circle.radius());
	}

	public void setCursor (Cursor cursor) { this.panelImage.setCursor(cursor); }
	public void setCursor (String cursor) { this.panelImage.setCursor(cursor); }
}
