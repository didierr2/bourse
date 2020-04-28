package fr.bbq.banque;

import static fr.bbq.banque.util.CellUtils.writeCell;
import static fr.bbq.banque.util.CellUtils.writeNumericCell;
import static fr.bbq.banque.util.CellUtils.writePercentCell;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import fr.bbq.banque.Constants.ROWS_AND_CELLS;
import fr.bbq.banque.indicator.GlobalIndicator;
import fr.bbq.banque.indicator.GlobalIndicators;
import fr.bbq.banque.indicator.Indicator;
import fr.bbq.banque.indicator.StockIndicators;
import fr.bbq.banque.xls.DateColumn;
import fr.bbq.banque.xls.StockColumn;

public class StockAnalyserStarter extends AbstractWorkbookHandler {


	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		
		// Check args
		if (args.length == 0 || args[0] == null) {
			System.err.println("Parametre d'execution manquant.");
			System.exit(-1);
		}
		
		new StockAnalyserStarter().analyseStocks(args[0]);
	}
	

	public void analyseStocks(String filePath) throws FileNotFoundException, IOException {
		readWorkbook(OPEN_MODE.READ_WRITE, filePath, Constants.ROWS_AND_CELLS.SHEET_DATA.value, Constants.ROWS_AND_CELLS.SHEET_INDICATOR.value, Constants.ROWS_AND_CELLS.SHEET_SYNTHESIS.value);
	}

	@Override
	protected void processSheet(Workbook workbook, Sheet sdata, Sheet sIndicators, Sheet sSynthesis) {
		
		// Init les indicateurs globaux (cross actions)
		GlobalIndicators globalInd = new GlobalIndicators();
		
		// On récupère la date du jour
		DateColumn dc = new DateColumn(sdata);
		
		// On parcourt toutes les colonnes
		StockColumn sc = StockColumn.first(sdata, dc.getLastRowIndex());
		boolean writeHeaders = true;
		int rowIndex = ROWS_AND_CELLS.ROW_INDICATORS_FIRST.value;
		while(sc.isPresent()) {
			StockIndicators stockInd = new StockIndicators(sc);
			globalInd.addStockIndicator(stockInd);
			
			// Enregistre le nom de la societe
			writeCell(sIndicators, rowIndex, ROWS_AND_CELLS.COL_INDICATORS_HEADERS.value, stockInd.getStockName());
			
			// On enregistre dans la feuille indicateurs toutes ces infos
			int colIndex = ROWS_AND_CELLS.COL_INDICATORS_FIRST.value;
			System.out.println("Write Indicators for " + stockInd.getStockName());
			for (Indicator ind : stockInd.getIndicators()) {
				
				// Enregistre l'indicateur
				if (writeHeaders) {
					writeCell(sIndicators, ROWS_AND_CELLS.ROW_INDICATORS_HEADERS.value, colIndex, ind.getName());
				}
				writeIndicator(workbook, sIndicators, rowIndex, colIndex, ind);
//				System.out.println(String.format("   - %s = %s", ind.getName(), ind.getValue()));
				
				colIndex++;
			}
			if (writeHeaders) {
				writeCell(sIndicators, ROWS_AND_CELLS.ROW_INDICATORS_HEADERS.value, colIndex, "Date achat");
				writeCell(sIndicators, ROWS_AND_CELLS.ROW_INDICATORS_HEADERS.value, colIndex+1, "Prix de revient");
				writeCell(sIndicators, ROWS_AND_CELLS.ROW_INDICATORS_HEADERS.value, colIndex+2, "Commentaire");
			}
			writeCell(sIndicators, rowIndex, colIndex, sc.getBuyDate());
			writeCell(sIndicators, rowIndex, colIndex+1, sc.getCostPrice());
			writeCell(sIndicators, rowIndex, colIndex+2, sc.getReportComm());

			writeHeaders = false;
			rowIndex++;
			sc.nextCol();
			
		}
		
		// Traite les indicateurs globaux
		rowIndex = ROWS_AND_CELLS.ROW_INDICATORS_FIRST.value;
		System.out.println("Write global indicators");
		for (GlobalIndicator gind : globalInd.getIndicators()) {
			Indicator ind = gind.buildIndicator();
			// Enregistre l'indicateur
			writeCell(sSynthesis, rowIndex, ROWS_AND_CELLS.COL_INDICATORS_FIRST.value, ind.getName());
			writeIndicator(workbook, sSynthesis, rowIndex, ROWS_AND_CELLS.COL_INDICATORS_FIRST.value + 1, ind);
			writeCell(sSynthesis, rowIndex, ROWS_AND_CELLS.COL_INDICATORS_FIRST.value + 2, ind.getDescription());
			System.out.println(String.format("   - %s = %s", ind.getName(), ind.getValue()));
			rowIndex++;
		}
		
	}
	
	
	private void writeIndicator(Workbook workbook, Sheet sheet, int rowIndex, int colIndex, Indicator ind) {
		// Enregistre la valeur de l'indicateur
		if (ind.isPercent()) {
			writePercentCell(workbook, sheet, rowIndex, colIndex, ind.getValue());
		}
		else if (ind.isNumeric()) {
			writeNumericCell(sheet, rowIndex, colIndex, ind.getValue());
		}
		else {
			writeCell(sheet, rowIndex, colIndex, ind.getValue());
		}
	}
}
