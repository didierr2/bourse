package fr.bbq.banque;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BourseStarter {

	// TODO Ajouter un logger log4j
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		
		if (args.length == 0 || args[0] == null) {
			System.err.println("Parametre d'execution manquant.");
			System.exit(-1);
		}
		
		String filename = args[0];
		
		// Le week end le programme ne sert a rien (marchers fermes)
		LocalDate day = LocalDate.now();
//		if (day.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SATURDAY.getValue() || day.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SUNDAY.getValue()) {
//			System.out.println("Le week-end l'analyse des cours n'est pas utile (les marchers sont fermes). Arret de l'application");
//		}
//		else {
			Workbook workbook = null;
			try (FileInputStream stream = new FileInputStream(new File(filename))) {
//			try (InputStream stream = BoursoramaStarter.class.getResourceAsStream(Constants.EXCEL_LOCATION)) {
				workbook = new XSSFWorkbook(stream);
				Sheet sheet = workbook.getSheetAt(Constants.ROWS_AND_CELLS.SHEET_DATA.value);
	
				// On ajoute la date du jour si elle n'est pas présente
				DateColumn dc = new DateColumn(sheet);
				dc.addTodayDate();
				
				
				// On parcourt toutes les colonnes
				StockColumn sc = StockColumn.first(sheet, dc.getLastRowIndex());
				while(sc.isPresent()) {
					System.out.println("\n\nurl : " + sc.getUrl());
					boolean metadata = sc.isMetaDataFilled();
					if (metadata) {
						System.out.println("isin : " + sc.getIsin());
						System.out.println("societe : " + sc.getSociete());
					}
					System.out.println("isMetaDataFilled : " + metadata);
					System.out.println("isTodayPriceFilled : " + sc.isTodayPriceFilled());
	
					// on vérifie qu'il y a les meta data
					// On vérifie que la valeur a la date du jour existe
					if (!metadata || !sc.isTodayPriceFilled()) {
						// Si ce n'est pas le cas (maj necessaire)
						// On effectue l'appel distant
						Stock stock = loadStock(sc.getUrl());
						// On enregistre les infos
						sc.update(stock);
						if (!metadata) {
							System.out.println("isin : " + sc.getIsin());
							System.out.println("societe : " + sc.getSociete());
						}
						System.out.println("update : getTodayPrice : " + sc.getTodayPrice());
					}
					sc.nextCol();
					Thread.sleep(Constants.SLEEP_INTERVAL_SECONDS * 1000);
				}
			}
			
			// on enregistre le fichier
			saveAndCloseWorkbook(workbook, filename);
//		}
	}
	

	private static void saveAndCloseWorkbook(Workbook workbook, String filename) {
		if (workbook != null) {
//			URL url = BoursoramaStarter.class.getResource(Constants.EXCEL_LOCATION);
//			try (FileOutputStream outputStream = new FileOutputStream(url.toURI().getPath())) {
			try (FileOutputStream outputStream = new FileOutputStream(filename)) {
				workbook.write(outputStream);
				workbook.close();
//				System.out.println("Fichier sauvegarde : " + url.toURI().getPath());
				System.out.println("Fichier sauvegarde : " + filename);
//			} catch (URISyntaxException | IOException e) {
			} catch (IOException e) {
				System.err.println("Erreur a l'enregistrement du fichier excel");
				e.printStackTrace();
			}
		}

	}	
	
	public static Stock loadStock(String url) {
		LogRecorder recorder = new LogRecorder(); 
		Stock stock = new Stock();
		try {
				// Call distant
				recorder.addLog("connexion a l'url... " + url);
				Document doc = Jsoup.connect(url).get();
				// Web scraping
				recorder.addLog("recuperation html ok, parse le dom...");
				Element main = doc.getElementById("main-content");
				Element header = main.getElementsByTag("header").first();
				stock.setCours(header.getElementsByClass("c-instrument--last").first().text());
				stock.setIsin(header.getElementsByClass("c-faceplate__isin").first().text());
				stock.setSociete(header.getElementsByClass("c-faceplate__company-link").first().text());
				recorder.addLog("parsing html ok : " + stock);				
//				stock.setSociete("societe");
//				stock.setIsin("isin");
//				stock.setCours("10.3");
			}
		catch (Exception e) {
			recorder.getLogs().forEach(mess -> System.err.println(mess));
			e.printStackTrace();
		}	
		return stock;
	}
	
//	public static void __old_main(String[] args) throws IOException {
//		
//		// Le week end le programme ne sert a rien (marchers fermes)
//		LocalDate day = LocalDate.now();
//		if (day.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SATURDAY.getValue() || day.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SUNDAY.getValue()) {
//			System.out.println("Le week-end l'analyse des cours n'est pas utile (les marchers sont fermes). Arret de l'application");
//		}
//		else {
//		
//			Workbook workbook = null;
//			try (FileInputStream file = new FileInputStream(new File(Constants.EXCEL_LOCATION))) {
//				workbook = new XSSFWorkbook(file);
//				Sheet sheet = workbook.getSheetAt(Constants.ROWS_AND_CELLS.SHEET_BOURSORAMA.value);
//
//				// Lit la premiere feuille
//				for (String url : Constants.URLS) {
//					LogRecorder recorder = new LogRecorder(); 
//					try {
//							Thread.sleep(Constants.SLEEP_INTERVAL_SECONDS * 1000);
//							Stock val = loadByUrl(url, sheet, recorder);
//							System.out.println("Maj OK : " + val);
//						}
//					catch (Exception e) {
//						e.printStackTrace();
//						recorder.getLogs().forEach(mess -> System.err.println(mess));
//					}
//				} 
//			}
//			
//			// on enregistre le fichier
//			if (workbook != null) {
//				try (FileOutputStream outputStream = new FileOutputStream(Constants.EXCEL_LOCATION)) {
//					workbook.write(outputStream);
//					workbook.close();
//				}
//			}
//		}
//	}		
//		
//	public static Stock loadByUrl(String url, Sheet sheet, LogRecorder recorder) throws IOException, ParseException {
//		Stock val = new Stock();
//		
//		
//		// Recup valeurs
//		recorder.addLog("connexion a l'url... " + url);
//		Document doc = Jsoup.connect(url).get();
//		recorder.addLog("recuperation html ok, parse le dom...");
//		Element main = doc.getElementById("main-content");
//		Element header = main.getElementsByTag("header").first();
//		val.setCours(header.getElementsByClass("c-instrument--last").first().text());
//		val.setIsin(header.getElementsByClass("c-faceplate__isin").first().text());
//		val.setSociete(header.getElementsByClass("c-faceplate__company-link").first().text());
//		recorder.addLog("parsing html ok : " + val);
//
//		
//		// Récupère la colonne des isin
//		Row row = sheet.getRow(Constants.ROWS_AND_CELLS.ROW_ISIN.value);
//		
//		// Trouve le code isin correspondant ou le créé
//		int i = row.getLastCellNum() - 1;
//		Cell cell = null;
//		while (cell == null && i>0) {
//			if (val.getIsin().equals(row.getCell(i).getStringCellValue())) {
//				cell = row.getCell(i);
//			}
//			i--;
//		}
//		// Créé le code isin
//		if (cell == null) {
//			recorder.addLog("le code isin n'existe pas encore, on le cree : " + val.getIsin());
//			cell = row.createCell(row.getLastCellNum());
//			cell.setCellValue(val.getIsin());
//			sheet.getRow(Constants.ROWS_AND_CELLS.ROW_SOCIETE.value).createCell(cell.getColumnIndex()).setCellValue(val.getSociete());
//		}
//		else {
//			recorder.addLog("le code isin a été trouvé en case : " + getCellLabel(cell));
//		}
//
//		// Positionne la date si elle n'existe pas
//		Row lastRow = sheet.getRow(sheet.getLastRowNum());
//		String sdfDate = Constants.SDF_EXCEL.format(new Date());
//		Cell dateCell = lastRow.getCell(Constants.ROWS_AND_CELLS.COL_DATE.value);
//		if (!sdfDate.equals(dateCell.getStringCellValue())) {
//			recorder.addLog("la date du jour " + sdfDate + " n'existe pas encore, on la créée en case : " + getCellLabel(dateCell));
//			dateCell = sheet.createRow(sheet.getLastRowNum() + 1).createCell(Constants.ROWS_AND_CELLS.COL_DATE.value, CellType.STRING);
//			dateCell.setCellValue(sdfDate);
//		}
//		else {
//			recorder.addLog("la date du jour " + sdfDate + " été trouvée en case : " + getCellLabel(dateCell));
//		}
//		
//		// Positionne la valeur sur le bon code isin et a la bonne date
//		Cell coursCell = sheet.getRow(
//				dateCell.getRowIndex())
//				.createCell(
//						cell.getColumnIndex());
//		coursCell.setCellValue(val.getCours());
//		
//
//		return val;
//	}
}
