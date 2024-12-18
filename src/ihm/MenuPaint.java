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
import metier.Paint;

public class MenuPaint extends JMenuBar implements ActionListener
{
	private PLPaint frame;
	private File    savedFile;

	private JMenuItem createItem;
	private JMenuItem openItem;
	private JMenuItem importItem;
	private JMenuItem saveItem;
	private JMenuItem saveAsItem;
	private JMenuItem quitterItem;
	private JMenuItem aboutItem;

	public MenuPaint(PLPaint frame)
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
		this.importItem  = new JMenuItem("Importer");
		this.saveItem    = new JMenuItem("Enregistrer");
		this.saveAsItem  = new JMenuItem("Enregistrer sous...");
		this.quitterItem = new JMenuItem("Quitter");

		// Ajouter des éléments au menu "Aide"
		this.aboutItem = new JMenuItem("À propos");

		// Ajout des items à leur menu respectif
		fichierMenu.add(createItem);
		fichierMenu.add(openItem);
		fichierMenu.add(importItem);
		fichierMenu.addSeparator();
		fichierMenu.add(saveItem);
		fichierMenu.add(saveAsItem);
		fichierMenu.addSeparator();
		fichierMenu.add(quitterItem);
		helpItem.add(aboutItem);

		/* Positionnement des composants */
		this.add(fichierMenu);
		this.add(helpItem);

		/* Configuration du panel */
		this.setBackground(PLPaint.COUL_PRIMARY);

		/* Ecouteurs d'actions */
		this.createItem .addActionListener(this);
		this.openItem	.addActionListener(this);
		this.importItem	.addActionListener(this);
		this.saveItem   .addActionListener(this);
		this.saveAsItem .addActionListener(this);
		this.quitterItem.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Désélection
		this.frame.disableSelection();

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
					JOptionPane.YES_NO_OPTION
				);

				if (response != JOptionPane.YES_OPTION)
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
					JOptionPane.YES_NO_OPTION
				);

				if (response != JOptionPane.YES_OPTION)
					return;
			}

			this.openImage();
		}

		if (this.importItem == e.getSource())
		{
			if (this.frame.getFullImage() == null)
				this.openImage();
			else
				this.importImage();
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

		if (this.quitterItem == e.getSource())
		{
			response = JOptionPane.showConfirmDialog(
				this,
				"Êtes-vous sûr de vouloir quitter ?\n" +
				"Toutes vos modifiactions d'images ne seront pas enregistrées", 
				"Quitter l'application",
				JOptionPane.YES_NO_OPTION
			);

			if (response == JOptionPane.YES_OPTION)
				this.frame.dispose();
		}
	}

	private void openImage()
	{
		File file = this.useFileChooser("Sélectionnez une image", null);
		if (file == null) return;
		
		try
		{
			// Convertir l'image sélectionnée en BufferedImage
			BufferedImage biImport = ImageIO.read(file);
			if (biImport != null)
			{
				BufferedImage biBg = new BufferedImage(biImport.getWidth(), biImport.getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				// Remplissage de l'image de fond
				Graphics2D g2d = (Graphics2D) (biBg.getGraphics());
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, biBg.getWidth(), biBg.getHeight());

				this.frame.setFullImage(biImport);
				this.frame.addImage(new Image(0, 0, biBg));
				this.frame.addImage(new Image(0, 0, biImport));
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

	private void createImage()
	{
		// Creation d'une nouvelle image
		BufferedImage biBg = new BufferedImage(
			(int) (this.frame.getWidth() - this.frame.getWidthPanelControl() * 1.5),
			(int) (this.frame.getHeight() * 0.80),
			BufferedImage.TYPE_INT_ARGB
		);

		Graphics2D graphics = biBg.createGraphics();

		// Remplissage  de l'image
		graphics.setPaint (Color.WHITE);
		graphics.fillRect (0, 0, biBg.getWidth(), biBg.getHeight());

		// Affectation de l'image au panelImage
		this.frame.setFullImage(biBg);
		this.frame.addImage(new Image(0, 0, biBg));
	}

	private void downloadImage(File baseDirectory)
	{
		File file = this.useFileChooser("Sélectionnez un dossier", baseDirectory);
		if (file == null) return;

		String fileName  = file.getName();

		if (!fileName.endsWith(".png"))
		{
			JOptionPane.showMessageDialog(this, "Erreur : L'image enregistré doit être en .png");
			this.downloadImage(file);
		}

		if (file.exists())
		{
			int response = JOptionPane.showConfirmDialog(
				this,
				"Ce fichier existe déjà.\n" +
				"Voulez-vous le remplacer ?", 
				"Remplacer image",
				JOptionPane.YES_NO_OPTION
			);

			if (response != JOptionPane.YES_OPTION)
				this.downloadImage(file);
		}
		
		try
		{
			this.frame.disableSelection();
			ImageIO.write(this.frame.getFullImage(), "png", file);
			this.savedFile = file;
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(this, "Erreur : Impossible d'enregistrer l'image");
			if (file.exists())
				this.downloadImage(file);
			this.downloadImage(new File("."));
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
			JOptionPane.showMessageDialog(this, "Erreur : Impossible d'enregistrer l'image");
			this.downloadImage(this.savedFile);
		}
	}

	public void importImage()
	{
		File file = this.useFileChooser("Séléctionnez l'image à importer", null);
		if (file == null) return;

		try
		{
			// Convertir l'image sélectionnée en BufferedImage
			BufferedImage biImport = ImageIO.read(file);
			if (biImport != null)
			{
				// Creéation de la nouvelle fenêtre
				// de l'application
				PLPaint app = new PLPaint("Importer une nouvelle image", new Paint(), this.frame);

				// Remplissage de l'image de fond
				BufferedImage biBg = new BufferedImage(biImport.getWidth(), biImport.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = (Graphics2D) (biBg.getGraphics());
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, biBg.getWidth(), biBg.getHeight());

				app.setFullImage(biImport);
				app.addImage(new Image(0, 0, biBg));
				app.addImage(new Image(0, 0, biImport));
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

	private File useFileChooser(String dialogTitle, File baseDirectory)
	{
		JFileChooser fileChooser = new JFileChooser();
		if (baseDirectory != null)
			fileChooser.setCurrentDirectory(baseDirectory);
		fileChooser.setDialogTitle(dialogTitle);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			return fileChooser.getSelectedFile();
		}
		else
		{
			return null;
		}
	}
}
