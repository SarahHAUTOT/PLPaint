package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PanelControl extends JPanel implements ActionListener, ChangeListener, MouseListener
{
	private static final int DEFAULT_COLOR = Color.BLUE.getRGB();
	private PLPaint frame;
	private int     action;

	private int    selectedColor;
	private JPanel panelButtons;

	private JPanel  panelOption;
	private JLabel  sliderLabel;
	private JSlider slider;

	private JButton goBack;
	private JButton eyedropper;
	private JButton bucket;
	private JButton pencil;

	private JButton brightness;
	private JButton rotation;

	private JButton horizontalFlip;
	private JButton verticalFlip;
	
	private JButton selectionRectangle;
	private JButton selectionCircle;

	private JButton removeBg;
	private JButton writeText;

	private JButton btnColor;

	private JPanel             panelOptionText;
	private JComboBox<String>  jcbFont;
	private JComboBox<Integer> jcbSize;
	private JCheckBox          cbBold;
	private JCheckBox          cbItalic;
	private JButton            btnChooseImage;
	private BufferedImage      biTexture;
	private JButton            btnValider;
	private JButton            goBackText;



	public PanelControl(PLPaint frame)
	{
		/* Création des composants */
		this.frame  = frame;
		this.action = PLPaint.ACTION_DEFAULT;
		this.selectedColor = 0;

		// Création du panel grille
		this.panelButtons = new JPanel();
		this.panelButtons.setLayout(new GridLayout(5, 2, 8, 8));

		// Création des boutons du panel grille
		try
		{
			this.eyedropper = new JButton(new ImageIcon("./src/ihm/icons/eyedropper.png"));
			this.bucket     = new JButton(new ImageIcon("./src/ihm/icons/bucket.png"));
			this.pencil     = new JButton(new ImageIcon("./src/ihm/icons/pencil.png"));
	
			this.brightness = new JButton(new ImageIcon("./src/ihm/icons/brightness.png"));
			this.rotation   = new JButton(new ImageIcon("./src/ihm/icons/rotate.png"));

			this.horizontalFlip = new JButton(new ImageIcon("./src/ihm/icons/flip-horizontal.png"));
			this.verticalFlip   = new JButton(new ImageIcon("./src/ihm/icons/flip-vertical.png"));
	
			this.selectionRectangle = new JButton(new ImageIcon("./src/ihm/icons/square.png"));
			this.selectionCircle    = new JButton(new ImageIcon("./src/ihm/icons/circle.png"));

			this.removeBg  = new JButton(new ImageIcon("./src/ihm/icons/rm-bg.png"));
			this.writeText = new JButton(new ImageIcon("./src/ihm/icons/text.png"));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.eyedropper = new JButton("eyedropper");
			this.bucket     = new JButton("Peinture");
	
			this.brightness = new JButton("Eclaircir");
			this.rotation   = new JButton("Rotation");

			this.horizontalFlip = new JButton("Tourner Horizontal");
			this.verticalFlip   = new JButton("Tourner Vertical");
	
			this.selectionRectangle = new JButton("Selection Rect");
			this.selectionCircle    = new JButton("Selection Cercle");
	
			this.removeBg  = new JButton("Enlever Fond");
			this.writeText = new JButton("Ecrire Texte");
		}

		this.goBack     = new JButton("<html>&#x21B2;</html>");
		this.goBackText = new JButton("<html>&#x21B2;</html>");

		
		this.sliderLabel = new JLabel();
		this.slider      = new JSlider(JSlider.VERTICAL, -100, 100, 0);
		
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

		this.slider      .setBackground(PLPaint.COUL_SECONDARY);


		// Création du panel option
		this.panelOption = new JPanel();
		this.panelOption.setLayout(new BorderLayout());

        // Création des composants du panel option
		JPanel panel = new JPanel();

		/* Changement de fond des comopsants */
		this             .setBackground(PLPaint.COUL_SECONDARY);
		panel            .setBackground(PLPaint.COUL_SECONDARY);
		this.panelButtons.setBackground(PLPaint.COUL_SECONDARY);
		this.panelOption .setBackground(PLPaint.COUL_SECONDARY);
		this.panelButtons.setPreferredSize(new Dimension(200, 300));
		this.panelOption .setPreferredSize(new Dimension(200, 300));

		/* Positionnement des composants */
		panel.add(this.goBack);
		panel.add(this.sliderLabel);
		this.panelOption.add(panel, BorderLayout.NORTH);
		this.panelOption.add(this.slider, BorderLayout.CENTER);
		
		this.panelOption.setVisible(false);

		/* STRUCTURE PRINCIPALE */
		this.panelButtons = new JPanel(new GridLayout(5,1,15,15));

		/* COLOR PICKER */
		this.btnColor      = new JButton();
		this.selectedColor = PanelControl.DEFAULT_COLOR;
		this.btnColor.setBackground(new Color(this.selectedColor));

		this.panelButtons.add(this.btnColor);

		/* OUTILS 1 */
		JPanel panelOutils1 = new JPanel(new GridLayout(2,2,10,10));

		panelOutils1.add(this.pencil    );
		panelOutils1.add(this.bucket    );
		panelOutils1.add(this.eyedropper);
		panelOutils1.add(this.writeText );

		this.styleButton(this.pencil    );
		this.styleButton(this.bucket    );
		this.styleButton(this.eyedropper);
		this.styleButton(this.writeText );
		
		MatteBorder topBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK);
		EmptyBorder margin = new EmptyBorder(20, 20, 0, 20);
		panelOutils1.setBorder(BorderFactory.createCompoundBorder(topBorder, margin));

		JPanel panelOutils1F = new JPanel();
		panelOutils1F.add(panelOutils1);
        this.panelButtons.add(panelOutils1F);

		/* SELECTION */
		JPanel panelSelection = new JPanel(new GridLayout(1,2,10,10));

		panelSelection.add(this.selectionRectangle);
		panelSelection.add(this.selectionCircle   );

		this.styleButton(this.selectionRectangle);
		this.styleButton(this.selectionCircle   );

		panelSelection.setBorder(BorderFactory.createCompoundBorder(topBorder, margin));

		JPanel panelSelectionF = new JPanel();
		panelSelectionF.add(panelSelection);
        this.panelButtons.add(panelSelectionF);

		/* OUTILS 2 */
		JPanel panelOutils2 = new JPanel(new GridLayout(2,2,10,10));

		panelOutils2.add(this.horizontalFlip);
		panelOutils2.add(this.verticalFlip  );
		panelOutils2.add(this.rotation      );
		panelOutils2.add(this.brightness    );

		this.styleButton(this.horizontalFlip);
		this.styleButton(this.verticalFlip  );
		this.styleButton(this.rotation      );
		this.styleButton(this.brightness    );
		
		panelOutils2.setBorder(BorderFactory.createCompoundBorder(topBorder, margin));

		JPanel panelOutils2F = new JPanel();
		panelOutils2F.add(panelOutils2);
        this.panelButtons.add(panelOutils2F);


		/* OPTION POUR LE TEXTE */

		this.panelOptionText = new JPanel();
		this.panelOptionText.setLayout(new BoxLayout(this.panelOptionText, BoxLayout.Y_AXIS));

		this.panelOptionText.add(this.goBackText);

		this.panelOptionText.add(new JLabel("Font:"));
		this.jcbFont = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		this.panelOptionText.add(this.jcbFont);

		this.panelOptionText.add(new JLabel("Size:"));
		Integer[] sizes = {8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40, 48, 56, 64};
		this.jcbSize = new JComboBox<>(sizes);
		this.jcbSize.setSelectedItem(12);
		this.panelOptionText.add(this.jcbSize);

		this.cbBold = new JCheckBox("Gras");
		this.panelOptionText.add(this.cbBold);

		this.cbItalic = new JCheckBox("Italic");
		this.panelOptionText.add(this.cbItalic);

		this.btnChooseImage = new JButton("Choisissez une image");
		this.panelOptionText.add(this.btnChooseImage);

		this.btnValider = new JButton("Ajouter");
		this.panelOptionText.add(this.btnValider);

		btnChooseImage.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "bmp", "gif"));
			int returnValue = fileChooser.showOpenDialog(panelOptionText);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				try
				{
					File selectedFile = fileChooser.getSelectedFile();
					this.biTexture = ImageIO.read(selectedFile); // Charge l'image en BufferedImage
					this.btnChooseImage.setText("Fichier choisie");
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(panelOptionText, "Failed to load the image.", "Error", JOptionPane.ERROR_MESSAGE);
					this.btnChooseImage.setText("Choisissez une image");
				}
			}
		});



		this.btnValider.addActionListener(this);

		this.panelOptionText.setVisible(false);








		/* POSITIONNEMENT */
		this.add(this.panelButtons);
		this.add(this.panelOption);
		this.add(this.panelOptionText);

		this.setBackground(PLPaint.COUL_SECONDARY);
		this.panelButtons    .setBackground(PLPaint.COUL_SECONDARY);

		panelOutils1F.setBackground(PLPaint.COUL_SECONDARY);
		panelOutils1 .setBackground(PLPaint.COUL_SECONDARY);

		panelSelectionF.setBackground(PLPaint.COUL_SECONDARY);
		panelSelection .setBackground(PLPaint.COUL_SECONDARY);

		panelOutils2F.setBackground(PLPaint.COUL_SECONDARY);
		panelOutils2 .setBackground(PLPaint.COUL_SECONDARY);


		/* Ecouteurs des boutons */
		this.goBack    .addActionListener(this);
		this.goBackText.addActionListener(this);
		this.btnColor  .addActionListener(this);

		this.eyedropper.addActionListener(this);
		this.bucket    .addActionListener(this);
		this.pencil    .addActionListener(this);

		this.brightness.addActionListener(this);
		this.rotation.addActionListener(this);

		this.horizontalFlip.addActionListener(this);
		this.verticalFlip  .addActionListener(this);

		this.selectionRectangle.addActionListener(this);
		this.selectionCircle   .addActionListener(this);

		this.removeBg .addActionListener(this);
		this.writeText.addActionListener(this);

		this.slider.addMouseListener(this);
	}

	private void styleButton(JButton button)
	{
        button.setContentAreaFilled(false);

        button.setBorder(new LineBorder(Color.BLACK, 1));

        button.setFont(new Font("Arial", Font.PLAIN, 12));

        // button.setPreferredSize(new Dimension(80,80));
		button.setPreferredSize(new Dimension(40,40));
		button.setMaximumSize  (new Dimension(40,40));


        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }




	public void setAction       (int action) { this.action = action; }
	public void setSelectedColor(int argb  )
	{
		this.selectedColor = argb;
		this.btnColor.setBackground(new Color(argb));
	}

	public int getSelectedColor() { return this.selectedColor; }
	public int getAction       () { return this.action; }
	public int getDistance     () { return this.slider.getValue(); }


	/* --------------------------------------------------------------------------------- */
	/*                              METHODE ECOUTEUR BOUTON                              */
	/* --------------------------------------------------------------------------------- */

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.frame.hideTextInput();
		this.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		if (this.btnColor == e.getSource())
		{
			Color col = JColorChooser.showDialog(null, "Choisissez une couleur", new Color(this.selectedColor));

			if (col != null) this.selectedColor = col.getRGB();
			this.btnColor.setBackground(new Color(this.selectedColor));
		}
		
		if (this.pencil == e.getSource())
		{
			// On définit l'action
			this.frame.setAction(PLPaint.ACTION_PENCIL);

			// On ecrit le mode du curseur dans le label
			this.frame.setLabelAction("Mode Crayon");
			this.frame.setCursor("./src/ihm/icons/pencil.png");
		}


		if (this.goBack == e.getSource() || this.goBackText == e.getSource())
		{
			// On réinitiale l'action
			this.frame.setAction(PLPaint.ACTION_DEFAULT);

			// On ecrit le mode du curseur dans le label
			this.frame.setLabelAction("Mode Curseur");

			// Affichage du panel d'actions
			this.panelOption.setVisible(false);
			this.panelOptionText.setVisible(false);
			this.panelButtons.setVisible(true);
			this.revalidate();
			this.repaint();
		}

		if (this.eyedropper == e.getSource())
		{
			// On désactive la séléction
			this.frame.disableSelection();

			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_EYEDROPPER;
			this.frame.setLabelAction("Mode Pipette");

			this.frame.setCursor("./src/ihm/icons/eyedropper.png");
		}

		if (this.bucket == e.getSource())
		{
			// On désactive la séléction
			this.frame.disableSelection();

			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_BUCKET;
			this.frame.setLabelAction("Mode Seau");
			this.sliderLabel.setText("Remplir la couleur");

			// Configuration du slider
			this.slider.setMinimum(0);
			this.slider.setMaximum(30);
			this.slider.setValue(10);
			this.slider.setMajorTickSpacing(5);
			this.slider.setMinorTickSpacing( 1);

			// Affichage du panel slider
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();

			this.frame.setCursor("./src/ihm/icons/bucket.png");
		}

		if (this.rotation == e.getSource())
		{
			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_ROTATION;
			this.frame.setLabelAction("Mode Rotation");
		}

		if (this.removeBg == e.getSource())
		{
			// On désactive la séléction
			this.frame.disableSelection();
			
			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_REMOVE_BG;
			this.frame.setLabelAction("Mode Effacer l'arrière-plan");
			
			// Configuration du slider
			this.sliderLabel.setText("Enlever le fond");
			this.slider.setMinimum(0);
			this.slider.setMaximum(100);
			this.slider.setValue(0);
			this.slider.setMajorTickSpacing(20);
			this.slider.setMinorTickSpacing( 5);
			
			// Affichage du panel slider
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();
		}

		if (this.writeText == e.getSource())
		{
			// On désactive la séléction
			this.frame.disableSelection();
			
			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_WRITE_TEXT;
			this.frame.setLabelAction("Mode Ecriture de texte");
			
			this.sliderLabel.setText("Ecrire du texte");
			
			this.panelOptionText.setVisible(true);
			this.panelButtons.setVisible(false);

			this.revalidate();
			this.repaint();

			this.frame.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		}

		if (this.selectionCircle == e.getSource())
		{
			// On désactive la séléction
			this.frame.disableSelection();
			
			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_SELECT_CIRCLE;
			this.frame.setLabelAction("Mode Séléction Cercle");
		}

		if (this.selectionRectangle == e.getSource())
		{
			// On désactive la séléction
			this.frame.disableSelection();
			
			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_SELECT_RECTANGLE;
			this.frame.setLabelAction("Mode Séléction Rectangle");
		}


		if (this.btnValider == e.getSource())
		{
			String  font = (String) (this.jcbFont.getSelectedItem());
			int     size = (int)    (this.jcbSize.getSelectedItem());
			
			boolean bold   = this.cbBold  .isSelected();
			boolean italic = this.cbItalic.isSelected();

			this.frame.addText(font, size, bold, italic, this.biTexture, this.selectedColor);
		}




		if (!this.frame.hasSelection()) return;

		if (this.brightness == e.getSource())
		{
			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_BRIGHTNESS;
			this.frame.setLabelAction("Mode Luminosité");
			this.sliderLabel.setText("Changer la luminosité");

			// Configuration du slider
			this.slider.setMinimum(-100);
			this.slider.setMaximum(100);
			this.slider.setValue(0);
			this.slider.setMajorTickSpacing(50);
			this.slider.setMinorTickSpacing( 20);

			// Affichage du panel slider
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();
		}
		
		if (this.horizontalFlip == e.getSource())
		{
			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_HORIZONTAL_FLIP;
			this.frame.setLabelAction("Mode Retourner (horizontalement)");

			if (this.frame.getSelectedCircle() != null)
				this.frame.flipHorizontalCircle();
			
			if (this.frame.getSelectedRectangle() != null)
				this.frame.flipHorizontalRect();
			
			if (this.frame.getSelectedImage() != null)
				this.frame.flipHorizontalImage();
		}

		if (this.verticalFlip == e.getSource())
		{
			// On renseigne l'action effectuée
			// On ecrit le mode du curseur dans le label
			this.action = PLPaint.ACTION_VERTICAL_FLIP;
			this.frame.setLabelAction("Mode Retourner (verticalement)");

			if (this.frame.getSelectedCircle() != null)
				this.frame.flipVerticalCircle();
			
			if (this.frame.getSelectedRectangle() != null)
				this.frame.flipVerticalRect();
			
			if (this.frame.getSelectedImage() != null)
				this.frame.flipVerticalImage();
		}

		this.frame.repaintImagePanel();
	}


	/* --------------------------------------------------------------------------------- */
	/*                              METHODE ECOUTEUR SLIDER                              */
	/* --------------------------------------------------------------------------------- */

	@Override
	public void stateChanged(ChangeEvent e)
	{/*
		if (!this.frame.hasSelection()) return;
		
		// Changer la valeur du cercle séléctionné
		if (this.frame.getSelectedCircle() != null)
		{
			if (this.action == PLPaint.ACTION_BRIGHTNESS)
				this.frame.setBrightnessCircle(this.slider.getValue());

			if (this.action == PLPaint.ACTION_ROTATION) {}
				// this.frame.rotate(this.frame.getSelectedCircle(), this.slider.getValue());
		}

		// Changer la valeur du rectangle séléctionné
		if (this.frame.getSelectedRectangle() != null)
		{
			if (this.action == PLPaint.ACTION_BRIGHTNESS)
				this.frame.setBrightnessRect(this.slider.getValue());
			
			if (this.action == PLPaint.ACTION_ROTATION) {}
				// this.frame.rotate(this.frame.getSelectedCircle(), this.slider.getValue());
		}

		// Changer la valeur de l'image séléctionné
		if (this.frame.getSelectedImage() != null)
		{
			if (this.action == PLPaint.ACTION_BRIGHTNESS)
				this.frame.setBrightnessImage(this.slider.getValue());
			
			if (this.action == PLPaint.ACTION_ROTATION) {}
				// this.frame.rotate(this.frame.getSelectedCircle(), this.slider.getValue());
		}

		this.frame.repaintImagePanel();*/
	}

	public void setTextOption () 
	{ 

	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		
		if (!this.frame.hasSelection()) return;
		
		// Changer la valeur du cercle séléctionné
		if (this.frame.getSelectedCircle() != null)
		{
			if (this.action == PLPaint.ACTION_BRIGHTNESS)
				this.frame.setBrightnessCircle(this.slider.getValue());

			if (this.action == PLPaint.ACTION_ROTATION) {}
				// this.frame.rotate(this.frame.getSelectedCircle(), this.slider.getValue());
		}

		// Changer la valeur du rectangle séléctionné
		if (this.frame.getSelectedRectangle() != null)
		{
			if (this.action == PLPaint.ACTION_BRIGHTNESS)
				this.frame.setBrightnessRect(this.slider.getValue());
			
			if (this.action == PLPaint.ACTION_ROTATION) {}
				// this.frame.rotate(this.frame.getSelectedCircle(), this.slider.getValue());
		}

		// Changer la valeur de l'image séléctionné
		if (this.frame.getSelectedImage() != null)
		{
			if (this.action == PLPaint.ACTION_BRIGHTNESS)
				this.frame.setBrightnessImage(this.slider.getValue());
			
			if (this.action == PLPaint.ACTION_ROTATION) {}
				// this.frame.rotate(this.frame.getSelectedCircle(), this.slider.getValue());
		}

		this.frame.repaintImagePanel();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
