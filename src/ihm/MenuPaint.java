package ihm;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import metier.Image;

public class MenuPaint extends JMenuBar implements ActionListener
{
	private FrameApp frame;

	private JMenuItem nouveauItem;
	private JMenuItem ouvrirItem;
	private JMenuItem quitterItem;
	private JMenuItem aProposItem;

	public MenuPaint(FrameApp frame)
	{
		/* Création des composants */
		// Créer les menus
		this.frame = frame;
		JMenu fichierMenu = new JMenu("Fichier");
		JMenu aideMenu    = new JMenu("Aide");

		// Ajouter des éléments au menu "Fichier"
		this.nouveauItem = new JMenuItem("Nouveau");
		this.ouvrirItem  = new JMenuItem("Ouvrir");
		this.quitterItem = new JMenuItem("Quitter");

		// Ajouter des éléments au menu "Aide"
		this.aProposItem = new JMenuItem("À propos");

		// Ajout des items à leur menu respectif
		fichierMenu.add(nouveauItem);
		fichierMenu.add(ouvrirItem);
		fichierMenu.addSeparator();
		fichierMenu.add(quitterItem);
		aideMenu.add(aProposItem);

		/* Positionnement des composants */
		this.add(fichierMenu);
		this.add(aideMenu);

		/* Configuration du panel */
		this.setBackground(FrameApp.COUL_PRIMARY);

		/* Ecouteurs d'actions */
		this.nouveauItem.addActionListener(this);
		this.quitterItem.addActionListener(this);
		this.ouvrirItem	.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.nouveauItem == e.getSource() )
			createImage();

		if (this.ouvrirItem == e.getSource() )
			this.ouvrirImage();

		if (this.quitterItem == e.getSource() )
		{
			int response = JOptionPane.showConfirmDialog(
				this,
            	"Êtes-vous sûr de vouloir quitter ?\n" +
				"Toutes vos modifiactions d'images n'ont pas été enregistrées", 
            	"Quitter l'application",
            	JOptionPane.OK_CANCEL_OPTION
			);

			if (response == JOptionPane.YES_OPTION)
				this.frame.dispose();
		}

		if (this.aProposItem == e.getSource() ) {}
	}

	private void ouvrirImage()
	{
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionnez une image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif"));

        int resultat = fileChooser.showOpenDialog(this);
        if (resultat == JFileChooser.APPROVE_OPTION)
		{
            File fichierImage = fileChooser.getSelectedFile();
            try
			{
                // Convertir l'image sélectionnée en BufferedImage
                BufferedImage bi = ImageIO.read(fichierImage);
                if (bi != null)
				{
					this.frame.setFullImage(bi);
					this.frame.addImage    (new Image(0, 0, bi));
                    JOptionPane.showMessageDialog(this, "Image chargée avec succès !");
                }
				else
				{
                    JOptionPane.showMessageDialog(this, "Erreur : Impossible de charger l'image !");
                }
            }
			catch (IOException ex)
			{
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        }
	}

	private void createImage()
	{
		// Creation d'une nouvelle image
		BufferedImage bi = new BufferedImage(FrameApp.DEFAULT_WIDTH, FrameApp.DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = bi.createGraphics();

		// Remplissage  de l'image
		graphics.setPaint ( Color.WHITE );
		graphics.fillRect ( 0, 0, bi.getWidth(), bi.getHeight() );

		// Affectation de l'image au panelImage
		this.frame.setFullImage(bi);
		JOptionPane.showMessageDialog(this, "Nouvelle image crée !");
	}
}
