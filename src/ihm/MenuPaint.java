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
	private File     savedFile;

	private JMenuItem createItem;
	private JMenuItem openItem;
	private JMenuItem saveItem;
	private JMenuItem saveAsItem;
	private JMenuItem quitterItem;
	private JMenuItem aboutItem;

	public MenuPaint(FrameApp frame)
	{
		/* Création des composants */
		// Créer les menus
		this.frame     = frame;
		this.savedFile = null;
		JMenu fichierMenu = new JMenu("Fichier");
		JMenu helpItem    = new JMenu("Aide");

		// Ajouter des éléments au menu "Fichier"
		this.createItem  = new JMenuItem("Nouveau");
		this.openItem    = new JMenuItem("Ouvrir");
		this.saveItem    = new JMenuItem("Enregistrer");
		this.saveAsItem  = new JMenuItem("Enregistrer sous...");
		this.quitterItem = new JMenuItem("Quitter");

		// Ajouter des éléments au menu "Aide"
		this.aboutItem = new JMenuItem("À propos");

		// Ajout des items à leur menu respectif
		fichierMenu.add(createItem);
		fichierMenu.add(openItem);
		fichierMenu.add(saveItem);
		fichierMenu.add(saveAsItem);
		fichierMenu.addSeparator();
		fichierMenu.add(quitterItem);
		helpItem.add(aboutItem);

		/* Positionnement des composants */
		this.add(fichierMenu);
		this.add(helpItem);

		/* Configuration du panel */
		this.setBackground(FrameApp.COUL_PRIMARY);

		/* Ecouteurs d'actions */
		this.createItem .addActionListener(this);
		this.quitterItem.addActionListener(this);
		this.openItem	.addActionListener(this);
		this.saveItem   .addActionListener(this);
		this.saveAsItem .addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int response;
		if (this.createItem == e.getSource())
		{
			if (this.frame.getFullImage() != null)
			{
				response = JOptionPane.showConfirmDialog(
					this,
					"Êtes-vous sûr de vouloir créer une nouvelle image ?\n" +
					"Tout les modifications de l'image courante seront éffacées", 
					"Ecraser l'image",
					JOptionPane.OK_CANCEL_OPTION
				);

				if (response == JOptionPane.NO_OPTION)
					return;
			}

			this.createImage();
		}

		if (this.openItem == e.getSource())
		{
			if (this.frame.getFullImage() != null)
			{
				response = JOptionPane.showConfirmDialog(
					this,
					"Êtes-vous sûr de vouloir créer une nouvelle image ?\n" +
					"Tout les modifications de l'image courante seront éffacées", 
					"Ecraser l'image",
					JOptionPane.OK_CANCEL_OPTION
				);

				if (response == JOptionPane.NO_OPTION)
					return;
			}

			this.openImage();
		}

		if (this.quitterItem == e.getSource())
		{
			response = JOptionPane.showConfirmDialog(
				this,
				"Êtes-vous sûr de vouloir quitter ?\n" +
				"Toutes vos modifiactions d'images ne seront pas enregistrées", 
				"Quitter l'application",
				JOptionPane.OK_CANCEL_OPTION
			);

			if (response == JOptionPane.YES_OPTION)
				this.frame.dispose();
		}

		if (this.saveItem == e.getSource() && this.frame.getFullImage() != null)
		{
			if (this.savedFile == null)
				this.downloadImage(new File("."));
			else
				this.saveImage();
		}

		if (this.saveAsItem == e.getSource() && this.frame.getFullImage() != null)
			this.downloadImage(new File("."));

		if (this.aboutItem == e.getSource())
		{
			// TODO : nouvelle frame avec explication des fonctionnalités
		}
	}

	private void openImage()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Sélectionnez une image");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int response = fileChooser.showOpenDialog(this);
		if (response == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			try
			{
				// Convertir l'image sélectionnée en BufferedImage
				BufferedImage bi = ImageIO.read(file);
				if (bi != null)
				{
					this.frame.setFullImage(bi);
					this.frame.addImage    (new Image(0, 0, bi));
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
		graphics.setPaint ( Color.WHITE);
		graphics.fillRect ( 0, 0, bi.getWidth(), bi.getHeight());

		// Affectation de l'image au panelImage
		this.frame.setFullImage(bi);
	}

	private void downloadImage(File baseDirectory)
	{
		JFileChooser fileChooser = new JFileChooser(); 
		fileChooser.setCurrentDirectory(baseDirectory);
		fileChooser.setDialogTitle("Sélectionnez un dossier");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File   file = fileChooser.getSelectedFile();
			String fileName = fileChooser.getSelectedFile().getName();
			if (!fileName.endsWith(".png"))
			{
				JOptionPane.showMessageDialog(this, "Erreur : L'extension de votre image doit être en .png !");
				this.downloadImage(file);
			}

			if (file.exists())
			{
				int response = JOptionPane.showConfirmDialog(
					this,
					"Ce fichier existe déjà.\n" +
					"Voulez-vous le remplacer ?", 
					"Remplacer image",
					JOptionPane.OK_CANCEL_OPTION
				);

				if (response != JOptionPane.YES_OPTION)
					this.downloadImage(fileChooser.getSelectedFile());
			}
			
            try
			{
				this.frame.disableSelection();
				ImageIO.write(this.frame.getFullImage(), "png", file);
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(this, "Erreur : Impossible d'enregistrer l'image " + e.getMessage());
				this.downloadImage(fileChooser.getSelectedFile());
			}
		}
	}

	public void saveImage()
	{
		try
		{
			this.frame.disableSelection();
			ImageIO.write(this.frame.getFullImage(), "png", this.savedFile);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(this, "Erreur : Impossible d'enregistrer l'image " + e.getMessage());
			this.downloadImage(this.savedFile);
		}
	}
}
