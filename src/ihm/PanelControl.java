package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class PanelControl extends JPanel implements ActionListener
{
	private FrameApp frame;
	private int      action;

	private JPanel panelButtons;

	private JPanel  panelOption;
	private JLabel  sliderLabel;
	private JSlider slider;

	private JButton goBack;
	private JButton eyedropper;
	private JButton bucket;

	private JButton darken;
	private JButton brighten;

	private JButton rotation;
	private JButton turn;
	
	private JButton selectionRectangle;
	private JButton selectionCircle;

	private JButton removeBg;
	private JButton writeText;

	public PanelControl(FrameApp frame)
	{
		/* Création des composants */
		this.frame  = frame;
		this.action = 0;

		// Création du panel grille
		this.panelButtons = new JPanel();
		this.panelButtons.setLayout(new GridLayout(5, 2, 8, 8));

		// Création des boutons du panel grille
		try
		{
			this.eyedropper = new JButton(new ImageIcon("./src/ihm/icons/eyedropper.png"));
			this.bucket     = new JButton(new ImageIcon("./src/ihm/icons/bucket.png"));
	
			this.darken   = new JButton(new ImageIcon("./src/ihm/icons/darken.png"));
			this.brighten = new JButton(new ImageIcon("./src/ihm/icons/brighten.png"));
	
			this.rotation = new JButton(new ImageIcon("./src/ihm/icons/rotate.png"));
			this.turn     = new JButton(new ImageIcon("./src/ihm/icons/turn.png"));
	
			this.selectionRectangle = new JButton(new ImageIcon("./src/ihm/icons/square-dotted.png"));
			this.selectionCircle    = new JButton(new ImageIcon("./src/ihm/icons/circle-dotted.png"));

			this.removeBg  = new JButton(new ImageIcon("./src/ihm/icons/rm-bg.png"));
			this.writeText = new JButton(new ImageIcon("./src/ihm/icons/text.png"));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.eyedropper = new JButton("eyedropper");
			this.bucket     = new JButton("Peinture");
	
			this.darken   = new JButton("darken");
			this.brighten = new JButton("Eclaircir");
	
			this.rotation = new JButton("Rotation");
			this.turn     = new JButton("turn");
	
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
		this.slider      = new JSlider(JSlider.VERTICAL, 0, 100, 0);
		
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        this.slider.setMajorTickSpacing(20);
        this.slider.setMinorTickSpacing(5);

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

		this.panelButtons.add(darken);
		this.panelButtons.add(brighten);

		this.panelButtons.add(rotation);
		this.panelButtons.add(turn);

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

		this.darken  .addActionListener(this);
		this.brighten.addActionListener(this);

		this.rotation.addActionListener(this);
		this.turn    .addActionListener(this);

		this.selectionRectangle.addActionListener(this);
		this.selectionCircle   .addActionListener(this);

		this.removeBg .addActionListener(this);
		this.writeText.addActionListener(this);
	}

	public int getAction() { return this.action; }

	/* --------------------------------------------------------------------------------- */
	/*                              METHODE ECOUTEUR BOUTON                              */
	/* --------------------------------------------------------------------------------- */

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Création de Curseur personnalisé
		if (this.goBack == e.getSource())
		{
			// Affichage du panel d'actions
			this.panelOption.setVisible(false);
			this.panelButtons.setVisible(true);
			this.revalidate();
			this.repaint();
		}

		if (this.eyedropper == e.getSource())
		{
			this.action = FrameApp.ACTION_EYEDROPPER;			
		}

		if (this.bucket == e.getSource())
		{
			this.action = FrameApp.ACTION_BUCKET;
		}

		if (this.darken == e.getSource())
		{
			this.action = FrameApp.ACTION_DARKEN;

			// Affichage du panel slider
			this.sliderLabel.setText("Assombrir l'image");
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();
		}

		if (this.brighten == e.getSource())
		{
			this.action = FrameApp.ACTION_BRIGHTEN;
			
			// Affichage du panel slider
			this.sliderLabel.setText("Eclaircir l'image");
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();
		}

		if (this.rotation == e.getSource())
		{
			this.action = FrameApp.ACTION_ROTATION;
		}

		if (this.turn == e.getSource())
		{
			this.action = FrameApp.ACTION_TURN_AROUND;
		}

		if (this.selectionRectangle == e.getSource())
		{
			this.action = FrameApp.ACTION_SELECT_RECTANGLE;
		}

		if (this.selectionCircle == e.getSource())
		{
			this.action = FrameApp.ACTION_SELECT_CIRCLE;
		}

		if (this.removeBg == e.getSource())
		{
			this.action = FrameApp.ACTION_REMOVE_BG;
			
			// Affichage du panel slider
			this.sliderLabel.setText("Ecrire le fond");
			this.panelOption .setVisible(true);
			this.panelButtons.setVisible(false);
			this.revalidate();
			this.repaint();
		}

		if (this.writeText == e.getSource())
		{
			this.action = FrameApp.ACTION_WRITE_TEXT;
			
			// TODO : Affichage du panel texte
			this.panelOption .setVisible(false);
			this.panelButtons.setVisible(false);
			this.sliderLabel.setText("Ecrire le texte");
			// TODO : this.add(this.panelText);
			this.revalidate();
			this.repaint();
		}
	}

	public int getDistance() { return this.slider.getValue(); }
}
