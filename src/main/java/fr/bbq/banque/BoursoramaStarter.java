package fr.bbq.banque;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BoursoramaStarter {

	public static void main(String[] args) throws IOException {
		
		// Le week end le programme ne sert a rien (marchers fermes)
		LocalDate day = LocalDate.now();
		if (day.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SATURDAY.getValue() || day.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SUNDAY.getValue()) {
			System.out.println("Le week-end l'analyse des cours n'est pas utile (les marchers sont fermes). Arret de l'application");
		}
		else {
		
			Workbook workbook = null;
			try (FileInputStream file = new FileInputStream(new File(Constants.EXCEL_LOCATION))) {
				workbook = new XSSFWorkbook(file);
				Sheet sheet = workbook.getSheetAt(Constants.ROWS_AND_CELLS.SHEET_BOURSORAMA.value);

				// Lit la premiere feuille
				for (String url : Constants.URLS) {
					LogRecorder recorder = new LogRecorder(); 
					try {
							Thread.sleep(Constants.SLEEP_INTERVAL_SECONDS * 1000);
							Valeur val = loadByUrl(url, sheet, recorder);
							System.out.println("Maj OK : " + val);
						}
					catch (Exception e) {
						e.printStackTrace();
						recorder.getLogs().forEach(mess -> System.err.println(mess));
					}
				} 
			}
			
			// on enregistre le fichier
			if (workbook != null) {
				try (FileOutputStream outputStream = new FileOutputStream(Constants.EXCEL_LOCATION)) {
					workbook.write(outputStream);
					workbook.close();
				}
			}
		}
	}		
		
	public static Valeur loadByUrl(String url, Sheet sheet, LogRecorder recorder) throws IOException, ParseException {
		Valeur val = new Valeur();
		
		
		// Recup valeurs
		recorder.addLog("connexion a l'url... " + url);
		Document doc = Jsoup.connect(url).get();
		recorder.addLog("recuperation html ok, parse le dom...");
		Element main = doc.getElementById("main-content");
		Element header = main.getElementsByTag("header").first();
		val.setCours(header.getElementsByClass("c-instrument--last").first().text());
		val.setIsin(header.getElementsByClass("c-faceplate__isin").first().text());
		val.setSociete(header.getElementsByClass("c-faceplate__company-link").first().text());
		recorder.addLog("parsing html ok : " + val);

		
		// Récupère la colonne des isin
		Row row = sheet.getRow(Constants.ROWS_AND_CELLS.ROW_ISIN.value);
		
		// Trouve le code isin correspondant ou le créé
		int i = row.getLastCellNum() - 1;
		Cell cell = null;
		while (cell == null && i>0) {
			if (val.getIsin().equals(row.getCell(i).getStringCellValue())) {
				cell = row.getCell(i);
			}
			i--;
		}
		// Créé le code isin
		if (cell == null) {
			recorder.addLog("le code isin n'existe pas encore, on le cree : " + val.getIsin());
			cell = row.createCell(row.getLastCellNum());
			cell.setCellValue(val.getIsin());
			sheet.getRow(Constants.ROWS_AND_CELLS.ROW_SOCIETE.value).createCell(cell.getColumnIndex()).setCellValue(val.getSociete());
		}
		else {
			recorder.addLog("le code isin a été trouvé en case : " + getCellLabel(cell));
		}

		// Positionne la date si elle n'existe pas
		Row lastRow = sheet.getRow(sheet.getLastRowNum());
		String sdfDate = Constants.SDF_EXCEL.format(new Date());
		Cell dateCell = lastRow.getCell(Constants.ROWS_AND_CELLS.CELL_DATE.value);
		if (!sdfDate.equals(dateCell.getStringCellValue())) {
			recorder.addLog("la date du jour " + sdfDate + " n'existe pas encore, on la créée en case : " + getCellLabel(dateCell));
			dateCell = sheet.createRow(sheet.getLastRowNum() + 1).createCell(Constants.ROWS_AND_CELLS.CELL_DATE.value, CellType.STRING);
			dateCell.setCellValue(sdfDate);
		}
		else {
			recorder.addLog("la date du jour " + sdfDate + " été trouvée en case : " + getCellLabel(dateCell));
		}
		
		// Positionne la valeur sur le bon code isin et a la bonne date
		Cell coursCell = sheet.getRow(
				dateCell.getRowIndex())
				.createCell(
						cell.getColumnIndex());
		coursCell.setCellValue(val.getCours());
		

		return val;
	}
	
	/**
	 * Retourne le label de la cellule : A1, C4, etc. 
	 */
	private static String getCellLabel (Cell cell) {
		return (char)('A' + cell.getColumnIndex()) + "" + (cell.getRowIndex() + 1);
	}
}
