package ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelControl extends JPanel implements ActionListener
{
	private FrameApp frame;
	private char lastAction;

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
		this.frame = frame;
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

		// Création du panel grille
		JPanel panelGrid = new JPanel();
		panelGrid.setLayout(new GridLayout(5, 2, 8, 8));

		/* Configuration du panel */
		this     .setBackground(FrameApp.COUL_SECONDARY);
		panelGrid.setBackground(FrameApp.COUL_SECONDARY);
		panelGrid.setPreferredSize(new Dimension(150, 300));

		/* Positionnement des composants */
		panelGrid.add(eyedropper);
		panelGrid.add(bucket);
		panelGrid.add(darken);
		panelGrid.add(brighten);
		panelGrid.add(rotation);
		panelGrid.add(turn);
		panelGrid.add(selectionRectangle);
		panelGrid.add(selectionCircle);
		panelGrid.add(removeBg);
		panelGrid.add(writeText);

		this.add(panelGrid);

		/* Ecouteurs des boutons */
		this.eyedropper.addActionListener(this);
		this.bucket    .addActionListener(this);

		this.darken  .addActionListener(this);
		this.brighten.addActionListener(this);

		this.rotation.addActionListener(this);
		this.turn    .addActionListener(this);

		this.selectionRectangle.addActionListener(this);
		this.selectionCircle   .addActionListener(this);

		this.removeBg.addActionListener(this);
		this.writeText.addActionListener(this);
	}

	/* --------------------------------------------------------------------------------- */
	/*                              METHODE ECOUTEUR BOUTON                              */
	/* --------------------------------------------------------------------------------- */

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.eyedropper == e.getSource())
			// this.frame.methode();

		if (this.bucket == e.getSource())
			// this.frame.methode();

		if (this.darken == e.getSource())
			// this.frame.methode();

		if (this.brighten == e.getSource())
			// this.frame.methode();

		if (this.rotation == e.getSource())
			// this.frame.methode();

		if (this.turn == e.getSource())
			// this.frame.methode();

		if (this.selectionRectangle == e.getSource())
			// this.frame.methode();

		if (this.selectionCircle == e.getSource())
			// this.frame.methode();

		if (this.removeBg == e.getSource())
			// this.frame.methode();

		if (this.writeText == e.getSource()) {}
			// this.frame.methode();
	}

	
	/* --------------------------------------------------------------------------------- */
	/*                              METHODE DE LA FRAME                                  */
	/* --------------------------------------------------------------------------------- */

}
