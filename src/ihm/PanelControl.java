package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import metier.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PanelControl extends JPanel implements ActionListener, ChangeListener
{
	private FrameApp frame;
	private int      action;

	private int    selectedColor;
	private JPanel panelButtons;

	private JPanel  panelOption;
	private JLabel  sliderLabel;
	private JSlider slider;

	private JButton goBack;
	private JButton eyedropper;
	private JButton bucket;

	private JButton brightness;
	private JButton rotation;

	private JButton horizontalTurn;
	private JButton verticalTurn;
	
	private JButton selectionRectangle;
	private JButton selectionCircle;

	private JButton removeBg;
	private JButton writeText;

	public PanelControl(FrameApp frame)
	{
		/* Création des composants */
		this.frame  = frame;
		this.action = FrameApp.ACTION_NONE;
		this.selectedColor = 0;

		// Création du panel grille
		this.panelButtons = new JPanel();
		this.panelButtons.setLayout(new GridLayout(5, 2, 8, 8));

		// Création des boutons du panel grille
		try
		{
			this.eyedropper = new JButton(new ImageIcon("./src/ihm/icons/eyedropper.png"));
			this.bucket     = new JButton(new ImageIcon("./src/ihm/icons/bucket.png"));
	
			this.brightness = new JButton(new ImageIcon("./src/ihm/icons/brightness.png"));
			this.rotation   = new JButton(new ImageIcon("./src/ihm/icons/rotate.png"));

			this.horizontalTurn = new JButton(new ImageIcon("./src/ihm/icons/turn-horizontal.png"));
			this.verticalTurn   = new JButton(new ImageIcon("./src/ihm/icons/turn-vertical.png"));
	
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

			this.horizontalTurn = new JButton("Tourner Horizontal");
			this.verticalTurn   = new JButton("Tourner Vertical");
	
			this.selectionRectangle = new JButton("Selection Rect");
			this.selectionCircle    = new JButton("Selection Cercle");
	
			this.removeBg  = new JButton("Enlever Fond");
			this.writeText = new JButton("Ecrire Texte");
		}

		// Création du panel option
		this.panelOption = new JPanel();
		this.panelOption.setLayout(new BorderLayout());

        // Création des composants du panel option
		JPanel panel = new JPanel();
		this.goBack  = new JButton("<html>&#x21B2;</html>");
		this.sliderLabel = new JLabel();
		this.slider      = new JSlider(JSlider.VERTICAL, -100, 100, 0);
		
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        this.slider.setMajorTickSpacing(20);
        this.slider.setMinorTickSpacing( 5);

		/* Changement de fond des comopsants */
		this             .setBackground(FrameApp.COUL_SECONDARY);
		panel            .setBackground(FrameApp.COUL_SECONDARY);
		this.slider      .setBackground(FrameApp.COUL_SECONDARY);
		this.panelButtons.setBackground(FrameApp.COUL_SECONDARY);
		this.panelOption .setBackground(FrameApp.COUL_SECONDARY);
		this.panelButtons.setPreferredSize(new Dimension(200, 300));
		this.panelOption .setPreferredSize(new Dimension(200, 300));

		/* Positionnement des composants */
		panel.add(this.goBack);
		panel.add(this.sliderLabel);
		this.panelOption.add(panel, BorderLayout.NORTH);
		this.panelOption.add(this.slider, BorderLayout.CENTER);
		
		this.panelButtons.add(eyedropper);
		this.panelButtons.add(bucket);

		this.panelButtons.add(brightness);
		this.panelButtons.add(rotation);

		this.panelButtons.add(horizontalTurn);
		this.panelButtons.add(verticalTurn);

		this.panelButtons.add(selectionRectangle);
		this.panelButtons.add(selectionCircle);

		this.panelButtons.add(removeBg);
		this.panelButtons.add(writeText);

		this.add(this.panelButtons);
		this.add(this.panelOption);
		this.panelOption.setVisible(false);

		/* Ecouteurs des boutons */
		this.goBack.addActionListener(this);

		this.eyedropper.addActionListener(this);
		this.bucket    .addActionListener(this);

		this.brightness.addActionListener(this);
		this.rotation.addActionListener(this);

		this.horizontalTurn.addActionListener(this);
		this.verticalTurn  .addActionListener(this);

		this.selectionRectangle.addActionListener(this);
		this.selectionCircle   .addActionListener(this);

		this.removeBg .addActionListener(this);
		this.writeText.addActionListener(this);

		this.slider.addChangeListener(this);
	}

	public void setAction       (int action) { this.action = action; }
	public void setSelectedColor(int argb  ) { this.selectedColor = argb; }

	public int getSelectedColor() { return this.selectedColor; }
	public int getAction       () { return this.action; }
	public int getDistance     () { return this.slider.getValue(); }


	/* --------------------------------------------------------------------------------- */
	/*                              METHODE ECOUTEUR BOUTON                              */
	/* --------------------------------------------------------------------------------- */

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Création de Curseur personnalisé
		if (this.goBack == e.getSource())
		{
			// On ecrit le mode du curseur dans le label
			this.frame.setLabelAction("Mode Curseur");

			// Affichage du panel d'actions
			this.panelOption.setVisible(false);
			this.panelButtons.setVisible(true);
			this.revalidate();
			this.repaint();
		}

		if (this.eyedropper == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_EYEDROPPER;
			this.frame.setLabelAction("Mode Pipette");
		}

		if (this.bucket == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_BUCKET;
			this.frame.setLabelAction("Mode Seau");
			this.sliderLabel.setText("Remplir la couleur");

			// Configuration du slider
			this.slider.setMinimum(0);
			this.slider.setValue(20);
			this.slider.setMaximum(100);

			// Affichage du panel slider
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();
		}

		if (this.brightness == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_BRIGHTNESS;
			this.frame.setLabelAction("Mode Luminosité");
			this.sliderLabel.setText("Changer la luminosité");

			// Configuration du slider
			this.slider.setMinimum(-100);
			this.slider.setValue(0);
			this.slider.setMaximum(100);

			// Affichage du panel slider
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();
		}

		if (this.rotation == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_ROTATION;
			this.frame.setLabelAction("Mode Rotation");
		}

		if (this.horizontalTurn == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_HORIZONTAL_TURN;
			this.frame.setLabelAction("Mode Retourner (horizontalement)");
		}

		if (this.verticalTurn == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_VERTICAL_TURN;
			this.frame.setLabelAction("Mode Retourner (verticalement)");
		}

		if (this.selectionRectangle == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_SELECT_RECTANGLE;
			this.frame.setLabelAction("Mode Séléction Rectangle");
		}

		if (this.selectionCircle == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_SELECT_CIRCLE;
			this.frame.setLabelAction("Mode Séléction Cercle");
		}

		if (this.removeBg == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_REMOVE_BG;
			this.frame.setLabelAction("Mode Effacer l'arrière-plan");
			
			// Configuration du slider
			this.sliderLabel.setText("Enlever le fond");
			this.slider.setMinimum(0);
			this.slider.setValue(20);
			this.slider.setMaximum(100);
			
			// Affichage du panel slider
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();
		}

		if (this.writeText == e.getSource())
		{
			// On renseigne l'action effectué
			// On ecrit le mode du curseur dans le label
			this.action = FrameApp.ACTION_WRITE_TEXT;
			this.frame.setLabelAction("Mode Ecriture de texte");
			
			// TODO : Affichage du panel texte
			this.panelOption .setVisible(false);
			this.panelButtons.setVisible(false);
			this.sliderLabel.setText("Ecrire du texte");
			// TODO : this.add(this.panelText);
			this.revalidate();
			this.repaint();
		}
	}


	/* --------------------------------------------------------------------------------- */
	/*                              METHODE ECOUTEUR SLIDER                              */
	/* --------------------------------------------------------------------------------- */

	@Override
	public void stateChanged(ChangeEvent e)
	{
		// Méthode de changement d'image uniquement appliquée sur une zone séléctionnée
		// en amont
		if (this.frame.getSelectedCircle   () == null && 
			this.frame.getSelectedRectangle() == null &&
			this.frame.getSelectedImage    () == null)
		return;
		
		if (this.action == FrameApp.ACTION_BRIGHTNESS)
		{
			if (this.frame.getSelectedCircle() != null)
				this.frame.setBrightness(this.frame.getSelectedCircle(), this.slider.getValue() / 10);

			if (this.frame.getSelectedRectangle() != null)
				this.frame.setBrightness(this.frame.getSelectedRectangle(), this.slider.getValue() / 10);
			
			if (this.frame.getSelectedImage() != null)
			{
				this.frame.setBrightness(this.frame.getSelectedImage(), this.slider.getValue() / 10);
				this.frame.repaintImagePanel();
			}
		}
	
		if (this.action == FrameApp.ACTION_HORIZONTAL_TURN)
		{
			if (this.frame.getSelectedCircle() != null) {}
				// this.frame.setBrightness(this.frame.getSelectedCircle(), this.slider.getValue());
		}

		if (this.action == FrameApp.ACTION_VERTICAL_TURN)
		{
			if (this.frame.getSelectedCircle() != null) {}
				// this.frame.setBrightness(this.frame.getSelectedCircle(), this.slider.getValue());
		}

		if (this.action == FrameApp.ACTION_ROTATION)
		{
			if (this.frame.getSelectedCircle() != null) {}
				// this.frame.setBrightness(this.frame.getSelectedCircle(), this.slider.getValue());
		}
	}
}
