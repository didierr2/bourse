package fr.bbq.banque;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import fr.bbq.banque.util.LogRecorder;
import fr.bbq.banque.xls.DateColumn;
import fr.bbq.banque.xls.Stock;
import fr.bbq.banque.xls.StockColumn;

public class StockImportStarter extends AbstractWorkbookHandler {

	
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		
		// Check args
		if (args.length == 0 || args[0] == null) {
			System.err.println("Parametre d'execution manquant.");
			System.exit(-1);
		}
		
		// On réalise l'import des cours
		new StockImportStarter().importStocks(args[0]);
		
		// On enchaine avec l'analyse des cours
		new StockAnalyserStarter().analyseStocks(args[0]);
	}
	

	public void importStocks(String filePath) throws FileNotFoundException, IOException {
		readWorkbook(OPEN_MODE.READ_WRITE, filePath, Constants.ROWS_AND_CELLS.SHEET_DATA.value);
	}

	@Override
	protected void processSheet(Workbook workbook, Sheet data) {
		
		// On ajoute la date du jour si elle n'est pas présente
		DateColumn dc = new DateColumn(data);
		dc.addTodayDate();
		
		
		// On parcourt toutes les colonnes
		StockColumn sc = StockColumn.first(data, dc.getLastRowIndex());
		while(sc.isPresent()) {
			System.out.println("\n\nurl : " + sc.getUrl());
			boolean metadata = sc.isMetaDataFilled();
			if (metadata) {
				System.out.println("isin : " + sc.getIsin());
				System.out.println("societe : " + sc.getSociety());
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
					System.out.println("societe : " + sc.getSociety());
				}
				System.out.println("update : getTodayPrice : " + sc.getTodayPrice());
			}
			sc.nextCol();
			
			sleep();
		}
	}

	
	private Stock loadStock(String url) {
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
			}
		catch (Exception e) {
			recorder.getLogs().forEach(mess -> System.err.println(mess));
			e.printStackTrace();
		}	
		return stock;
	}
	
	
	private void sleep() {

		// L'attente est aléatoirement calculée entre 1x et 2x SLEEP_INTERVAL_SECONDS
		try {
			Thread.sleep(((int)(Math.random() * Constants.SLEEP_INTERVAL_SECONDS) + Constants.SLEEP_INTERVAL_SECONDS) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
