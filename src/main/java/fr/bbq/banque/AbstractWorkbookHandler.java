package fr.bbq.banque;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Classe abstraite de manipulation d'un fichier excel.
 * Gère automatique l'ouverture des flux et leur fermeture.
 * Sauvegarde automatiquement le fichier avant fermeture.
 * @author Didier
 *
 */
public abstract class AbstractWorkbookHandler {

	/** Mode lecture de la fuille uniquement ou enregistrement lorsque le traitement est terminé */
	public enum OPEN_MODE {
		READ_ONLY,
		READ_WRITE;
	};
	
	/**
	 * Méthode principale de lecture d'une feuille d'un fichier excel
	 * Gère la lecture physique du fichier, la fermeture des streams et la sauvegarde du fichier si necesaire
	 * @param openMode
	 * @param workbookPath
	 * @param sheetNumber
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void readWorkbook(OPEN_MODE openMode, String workbookPath, int sheetNumber) throws FileNotFoundException, IOException {
		Workbook workbook = null;
		System.out.println("Ouverture du fichier : " + workbookPath);
		try (FileInputStream stream = new FileInputStream(new File(workbookPath))) {
			workbook = new XSSFWorkbook(stream);
			Sheet sheet = workbook.getSheetAt(sheetNumber);
			
			System.out.println("Recuperation de la feuille : " + sheet.getSheetName());
			processSheet(sheet);
			
		}
		// on enregistre le fichier
		saveAndCloseWorkbook(workbook, workbookPath, openMode);
	}
	
	/**
	 * Méthode de traitement d'une feuille excel
	 * @param sheet
	 */
	protected abstract void processSheet(Sheet sheet);
	
	/**
	 * Permet de fermer les streams du fichier excel et de sauvegarder si necessaire
	 * @param workbook
	 * @param filename
	 * @param openMode
	 */
	private void saveAndCloseWorkbook(Workbook workbook, String filename, OPEN_MODE openMode) {
		if (workbook != null) {
			try (FileOutputStream outputStream = new FileOutputStream(filename)) {
				if (openMode == OPEN_MODE.READ_WRITE) {
					workbook.write(outputStream);
					System.out.println("Fichier sauvegarde : " + filename);
				}
				workbook.close();
			} catch (IOException e) {
				if (openMode == OPEN_MODE.READ_WRITE) {
					System.err.println("Erreur a l'enregistrement du fichier excel");
				}
				else {
					System.err.println("Erreur a la fermeture du fichier excel");
				}
				e.printStackTrace();
			}
		}

	}		
	
}
