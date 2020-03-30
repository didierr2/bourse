package fr.bbq.banque;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
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

public class BanqueStarter {

	public static void main(String[] args) throws IOException {
		Workbook workbook = null;
		try (FileInputStream file = new FileInputStream(new File(Constants.EXCEL_LOCATION))) {
			workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheetAt(Constants.ROWS_AND_CELLS.SHEET_BOURSORAMA.value);

			try {
				// Lit la premiere feuille
				for (String url : Constants.URLS) {
					Thread.sleep(Constants.SLEEP_INTERVAL_SECONDS * 1000);
					Valeur val = loadByUrl(url, sheet);
					System.out.println("Maj OK : " + val);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
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
		
	public static Valeur loadByUrl(String url, Sheet sheet) throws IOException, ParseException {
		Valeur val = new Valeur();
		
		
		// Recup valeurs
		Document doc = Jsoup.connect(url).get();
		Element main = doc.getElementById("main-content");
		Element header = main.getElementsByTag("header").first();
		val.setCours(header.getElementsByClass("c-instrument--last").first().text());
		val.setIsin(header.getElementsByClass("c-faceplate__isin").first().text());
		val.setSociete(header.getElementsByClass("c-faceplate__company-link").first().text());

		
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
			cell = row.createCell(row.getLastCellNum());
			cell.setCellValue(val.getIsin());
			sheet.getRow(Constants.ROWS_AND_CELLS.ROW_SOCIETE.value).createCell(cell.getColumnIndex()).setCellValue(val.getSociete());
		}

		// Positionne la date si elle n'existe pas
		Row lastRow = sheet.getRow(sheet.getLastRowNum());
		String sdfDate = Constants.SDF_EXCEL.format(new Date());
		Cell dateCell = lastRow.getCell(Constants.ROWS_AND_CELLS.CELL_DATE.value);
		if (!sdfDate.equals(dateCell.getStringCellValue())) {
			dateCell = sheet.createRow(sheet.getLastRowNum() + 1).createCell(Constants.ROWS_AND_CELLS.CELL_DATE.value, CellType.STRING);
			dateCell.setCellValue(sdfDate);
		}
		
		// Positionne la valeur sur le bon code isin et a la bonne date
		Cell coursCell = sheet.getRow(
				dateCell.getRowIndex())
				.createCell(
						cell.getColumnIndex());
		coursCell.setCellValue(val.getCours());

		return val;
	}
}
