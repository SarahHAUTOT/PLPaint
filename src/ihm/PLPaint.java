package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.Cursor;

import metier.Circle;
import metier.Image;
import metier.Paint;
import metier.Rectangle;


public class PLPaint extends JFrame
{
	// Mappage des différentes actions disponibles par l'utilisateur
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

	public static final int ACTION_REMOVE_BG    = 11;
	public static final int ACTION_TRIM_SURFACE = 12;
	public static final int ACTION_WRITE_TEXT   = 13;

	// Controle avec les touches clavier

	// Définition de la taille de la fenêtre
	public static final int DEFAULT_WIDTH  = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth () * 0.8);
	public static final int DEFAULT_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.85);
	
	public static final Color PRIMARY_COLOR   = Color.decode("#9289f2"); // Couleur secondaire de l'application
	public static final Color SECONDARY_COLOR = Color.decode("#ddd6fc"); // Couleur secondaire de l'application
	public static final Color BASE_COLOR     = Color.decode("#f5f5ff"); // Couleur de base de l'application
	public static final Color COUL_NO_BG     = Color.decode("#BFBFBF"); // Couleur de fond par défault
	
	private PLPaint parent;
	private PLPaint children;

	private Paint        metier;
	private MenuPaint    menu;
	private PanelControl panelControl;
	private PanelImage   panelImage;
	private JLabel       lblAction;

	public PLPaint(String title, Paint metier, PLPaint parent)
	{
		/* Création des composants */
		this.parent       = parent;
		this.children     = null;
		this.panelControl = new PanelControl(this, parent != null);
		this.panelImage   = new PanelImage  (this);
		this.menu         = new MenuPaint   (this);
		this.metier       = metier;
		this.lblAction    = new JLabel("Mode Curseur");

		/* Configuration de la frame */
		this.setLayout(new BorderLayout());
		this.setSize(PLPaint.DEFAULT_WIDTH, PLPaint.DEFAULT_HEIGHT);
		this.setFocusable(true);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		try 
		{
			URL resource = getClass().getResource("/ihm/icons/logo.png");
			BufferedImage img = ImageIO.read(resource);
			java.awt.Image icon = img.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
			this.setTitle(title);
			this.setIconImage(icon);
		} catch (IOException e) {
			e.printStackTrace();
		}


		JPanel panelLbl = new JPanel();
		panelLbl.setBackground(PLPaint.BASE_COLOR);
		panelLbl.add(this.lblAction);

		/* Positionnement des composants */
		if (parent == null)
			this.setJMenuBar(menu);

		JScrollPane sp = new JScrollPane(this.panelImage);
		configureScrollPaneSensitivity(sp);

		this.add(panelControl, BorderLayout.EAST);
		this.add(sp, BorderLayout.CENTER);
		this.add(panelLbl, BorderLayout.SOUTH);

		/* Ecouteur du clavier */

		this.setVisible(true);
	}

	public void dispose()
	{
		if (this.parent != null)
			this.parent.setFocusable(true);
		else
			if (this.getFullImage() != null)
			{
				int response = JOptionPane.showConfirmDialog(
					this,
					"Êtes-vous sûr de vouloir quitter ?\n" +
					"Toutes vos modifiactions d'images ne seront pas enregistrées", 
					"Quitter l'application",
					JOptionPane.YES_NO_OPTION
				);
	
				if (response != JOptionPane.YES_OPTION)
					return;
			}
		
		if (this.children != null) this.children.dispose();
		super.dispose();
	}

	private void configureScrollPaneSensitivity(JScrollPane scrollPane)
	{
		JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
		JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();

		verticalScrollBar  .setUnitIncrement(16);  
		horizontalScrollBar.setUnitIncrement(16);

		verticalScrollBar  .setBlockIncrement(100);   
		horizontalScrollBar.setBlockIncrement(100); 
	}

	public void setChildren(PLPaint plpaint) { this.children = plpaint; }

	public void setLabelAction(String str) { this.lblAction.setText(str); }
	public void repaintImagePanel       () { this.setFullImage(this.getImage()); }

	public void addImgToParent()
	{
		BufferedImage biImport = this.metier.getImageWithoutBackground();
		
		// Vérifier la taille de l'image de fond du parent
		/*
		// Changement de l'image de fond
		// Si l'image importée est plus grande
		Image imgBg = this.frame.getImages().get(0);
		if (this.frame.getFullImage().getWidth() < biImport.getWidth())
		{
			biBg = new BufferedImage(biImport.getWidth(), imgBg.getImgHeight(), BufferedImage.TYPE_INT_ARGB);
			
			// Remplissage  de l'image
			Graphics2D graphics = biBg.createGraphics();
			graphics.setPaint (Color.WHITE);
			graphics.fillRect (0, 0, biBg.getWidth(), biBg.getHeight());
			imgBg.setImg(biBg);
		}

		if (this.frame.getFullImage().getHeight() < biImport.getHeight())
		{
			biBg = new BufferedImage(imgBg.getImgWidth(), biImport.getHeight(), BufferedImage.TYPE_INT_ARGB);

			// Remplissage  de l'image
			Graphics2D graphics = biBg.createGraphics();
			graphics.setPaint (Color.WHITE);
			graphics.fillRect (0, 0, biBg.getWidth(), biBg.getHeight());
			imgBg.setImg(biBg);
		}
		 */

		System.out.println(biImport.getWidth() + " : " + biImport.getHeight());
		this.parent.addImage(new Image(0, 0, biImport)); // TODO Prendre les coordonées courante du panelImage

		this.parent.selectLastImage();			
		this.parent.repaintImagePanel();
		this.parent.setVisible(true);
		this.dispose();
	}

	/**
	 * Définit l'action et le curseur de base
	 */
	public void defaultAction()
	{
		this.setAction(PLPaint.ACTION_DEFAULT);
		this.setLabelAction("Mode Curseur");
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/* --------------------------------------------------------------------------------- */
	/*                              METHODE ECOUTEUR CLAVIER                             */
	/* --------------------------------------------------------------------------------- */


	public void keyTyped   (KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	/* --------------------------------------------------------------------------------- */
	/*                              METHODE DES PANNELS                                  */
	/* --------------------------------------------------------------------------------- */

	// Panel de l'image
    public void selectLastImage()
	{
		int lengthLst = this.getImages().size();
		this.disableSelection();
		this.panelImage.setSelectedImage(this.getImages().get(lengthLst -1));
	}

	public void setFullImage(BufferedImage bi)
	{
		this.panelImage.setFullImage(bi);
		this.repaint();
	}

	public void hideTextInput() { this.panelImage.hideTextInput(); }

	public void setCursor (Cursor cursor) { this.panelImage.setCursor(cursor); }
	public void setCursor (String cursor) { this.panelImage.setCursor(cursor); }

	public void          disableSelection    () { this.panelImage.disableSelection(); }
	public BufferedImage getFullImage        () { return this.panelImage.getFullImage(); }
	public Circle        getSelectedCircle   () { return this.panelImage.getSelectedCircle(); }
	public Rectangle     getSelectedRectangle() { return this.panelImage.getSelectedRectangle(); }
	public Image         getSelectedImage    () { return this.panelImage.getSelectedImage(); }
	public boolean       hasSelection        () { return this.panelImage.hasSelection(); }

	// Panel de contrôle
	public void setSelectedColor(int argb  ) { this.panelControl.setSelectedColor(argb); }
	public void setAction       (int action) { this.panelControl.setAction(action); }

	public int getWidthPanelControl () { return this.panelControl.getWidth (); }
	public int getHeightPanelControl() { return this.panelControl.getHeight(); }

	public int getSelectedColor() { return this.panelControl.getSelectedColor(); }
	public int getDistance     () { return this.panelControl.getDistance(); }
	public int getAction       () { return this.panelControl.getAction  (); }

	/* ------------------------------------------------------------------------------- */
	/*                              METHODE DU METIER                                  */
	/* ------------------------------------------------------------------------------- */
	// Gestion de l'historique
	public void ctrlZ()
	{
		this.metier.goBack();
		this.panelImage.disableSelection();
		try {
			wait(500);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		this.repaintImagePanel();
	}

	// Gestion des images
	public void reset()
	{
		this.metier.reset();
		this.panelImage.disableSelection();
	}

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

	public void removeImage()
	{
		Image img = this.getSelectedImage();

		if (img == null & this.getSelectedRectangle() != null) img = this.metier.trim(this.getSelectedRectangle());
		if (img == null & this.getSelectedCircle   () != null) img = this.metier.trim(this.getSelectedCircle   ());

		if (img != null)
		{
			this.metier.removeImage(img);
			this.disableSelection();
			this.repaintImagePanel();

			this.save();
		}
	}

	// Méthode de changement de luminosité
	public void setBrightnessImage(int value)
	{
		this.metier.setLuminosite(this.getSelectedImage(), value);
	}

	public void setBrightnessRect(int value)
	{
		this.metier.setLuminosite(this.getSelectedRectangle(), value);
		this.selectLastImage();
	}

	public void setBrightnessCircle(int value)
	{
		this.metier.setLuminosite(this.getSelectedCircle(), value);
		this.selectLastImage();
	}

	// Methode Seau
	public void bucket(int x, int y, int argb, int distance)
	{
		this.metier.bucket(x, y, argb, distance);
	}

	// Methode Effacer arrière plan
	public void removeBgImage(int distance)
	{
		this.metier.removeBackground(
			this.getSelectedImage(),
			this.getSelectedColor(),
			distance
		);

		this.repaintImagePanel();
	}

	public void removeBgScreen(int argb, int distance)
	{
		this.metier.removeBackground(
			new Image(0, 0, this.getFullImage()),
			this.getSelectedColor(),
			distance
		);

		this.repaintImagePanel();
	}

	// Methode Rogner
	public void trimRect()
	{
		Image img = this.metier.trim(this.getSelectedRectangle());
		this.disableSelection();
		this.defaultAction();
		if (img == null) return;

		this.addImage(img);
		this.selectLastImage();
	}

	public void trimCircle()
	{
		Image img = this.metier.trim(this.getSelectedCircle());
		this.disableSelection();
		this.defaultAction();
		if (img == null) return;

		this.addImage(img);
		this.selectLastImage();
	}

	public void selectScreen() 
	{
		this.panelImage.selectScreen();
	}


	// Methode retourner (horizontal)
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

	// Methode retourner (vertical)
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

	// Methode rotation
	public void rotateCircle(int angle)
	{
		this.metier.rotate(this.getSelectedCircle(), angle);
		this.selectLastImage();
	}

	public void rotateImage(int angle)
	{
		this.metier.rotate(this.getSelectedImage(), angle);
		this.selectLastImage();
	}

	public void rotateRect(int angle)
	{
		this.metier.rotate(this.getSelectedRectangle(), angle);
		this.selectLastImage();
	}

	public void save() {this.metier.save();}

	public void addText(String font, int size, boolean bold, boolean italic, BufferedImage texture, int rgb)
	{
		String text = this.panelImage.getText();

		if (text != null)
		{
			java.awt.Point p = this.panelImage.getTextLocation();

			Image img = this.metier.addText(text, bold, italic, font, size, rgb, (int) p.getX(), (int) p.getY());

			if (texture != null)
				this.metier.addTexture(texture, img);
				
			this.repaintImagePanel();
		}
	}
}
