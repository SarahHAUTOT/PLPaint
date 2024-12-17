public class Tombe 
{

	
	/** 
	 * Rotation d'une section de l'image spécifiée par un rectangle.
	 * @param xStart  Coordonnée X de départ.
	 * @param yStart  Coordonnée Y de départ.
	 * @param xEnd    Coordonnée X de fin.
	 * @param yEnd    Coordonnée Y de fin.
	 * @param angle   Angle de rotation en degrés.
	 */
	/*
	public void retourner(int xStart, int yStart, int xEnd, int yEnd, int angle) 
	{
		double angleRad = Math.toRadians(angle);

		// Dimensions de la section originale
		int oldWidth  = xEnd - xStart - 1;
		int oldHeight = yEnd - yStart - 1;

		// Calcul des nouvelles dimensions (en utilisant les formules trigonométriques)
		int newWidth = (int) Math.ceil(Math.abs(oldWidth * Math.cos(angleRad)) + Math.abs(oldHeight * Math.sin(angleRad)));
		int newHeight = (int) Math.ceil(Math.abs(oldWidth * Math.sin(angleRad)) + Math.abs(oldHeight * Math.cos(angleRad)));

		// Centre de la section originale
		int xCenterOld = xStart + oldWidth / 2;
		int yCenterOld = yStart + oldHeight / 2;

		// Nouvelle image temporaire pour stocker les résultats
		int[][] tempImage = new int[newWidth][newHeight];

		// Rotation des pixels
		for (int rX = 0; rX < newWidth; rX++) {
			for (int rY = 0; rY < newHeight; rY++) {
				// Calcul des coordonnées inverses pour retrouver le pixel source
				double x = (rX - newWidth / 2) * Math.cos(-angleRad) - (rY - newHeight / 2) * Math.sin(-angleRad) + xCenterOld;
				double y = (rX - newWidth / 2) * Math.sin(-angleRad) + (rY - newHeight / 2) * Math.cos(-angleRad) + yCenterOld;

				// Vérification que les coordonnées source sont dans les limites de la section originale
				int srcX = (int) Math.round(x);
				int srcY = (int) Math.round(y);

				if (srcX >= xStart && srcX < xEnd && srcY >= yStart && srcY < yEnd) {
					// Récupérer la couleur du pixel source
					Image imageSrc = getClickedImage(srcX, srcY);
					if (imageSrc != null) {
						int color = imageSrc.getImg().getRGB(srcX, srcY);
						tempImage[rX][rY] = color;
					}
				} else {
					// Si en dehors des limites, on met un pixel transparent
					tempImage[rX][rY] = 0x00000000; // Transparent
				}
			}
		}

		// Appliquer les pixels temporaires dans la nouvelle section
		for (int rX = 0; rX < newWidth; rX++) {
			for (int rY = 0; rY < newHeight; rY++) {
				int destX = xCenterOld - newWidth / 2 + rX;
				int destY = yCenterOld - newHeight / 2 + rY;

				// Vérification des limites de l'image principale
				if (destX >= 0 && destX < this.width && destY >= 0 && destY < this.height) {
					Image imageDest = getClickedImage(destX, destY);
					if (imageDest != null) {
						imageDest.getImg().setRGB(destX, destY, tempImage[rX][rY]);
					}
				}
			}
		}
	}*/
}
