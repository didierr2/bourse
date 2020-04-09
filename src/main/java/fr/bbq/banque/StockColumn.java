package fr.bbq.banque;

import static fr.bbq.banque.CellUtils.isEmpty;
import static fr.bbq.banque.CellUtils.writeCell;
import static fr.bbq.banque.Constants.ROWS_AND_CELLS.COL_FIRST_STOCK;

import org.apache.poi.ss.usermodel.Sheet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StockColumn {

	final Sheet sheet;
	@Getter
	int columnIndex;
	final int todayRowIndex;
	
	public static StockColumn first(Sheet sh, int todayRowIndex) {
		return new StockColumn(sh, COL_FIRST_STOCK.value, todayRowIndex);
	}
	
	public String getUrl() {
		return getCellAsTextValue(Constants.ROWS_AND_CELLS.ROW_URL.value);
	}

	public String getSociete() {
		return getCellAsTextValue(Constants.ROWS_AND_CELLS.ROW_SOCIETE.value);
	}

	public String getIsin() {
		return getCellAsTextValue(Constants.ROWS_AND_CELLS.ROW_ISIN.value);
	}

	public String getTodayPrice() {
		return CellUtils.getCellAsTextValue(sheet, todayRowIndex, columnIndex);
	}	
//
//	
//	/** La date du jour n'existe pas, on l'ajoute */
//	public void addTodayDate(String todayDate) {
//		Cell dateCell = sheet.createRow(sheet.getLastRowNum() + 1).createCell(Constants.ROWS_AND_CELLS.COL_DATE.value, CellType.STRING);
//		dateCell.setCellValue(todayDate);
//	}	

	
	public boolean isPresent() {
		String url = getUrl();
		return url != null && !url.isEmpty();
	}
	
	public void nextCol() {
		columnIndex++;
	}
	
	public boolean isMetaDataFilled () {
		return !isEmpty(getSociete()) && !isEmpty(getIsin());
	}
	
	
	public boolean isTodayPriceFilled () {
		return !isEmpty(getTodayPrice());
	}
	
	
	public void update (Stock stock) {
		if (!isMetaDataFilled()) {
			writeCell(sheet, Constants.ROWS_AND_CELLS.ROW_ISIN.value, columnIndex, stock.getIsin());
			writeCell(sheet, Constants.ROWS_AND_CELLS.ROW_SOCIETE.value, columnIndex, stock.getSociete());
		}
		writeCell(sheet, todayRowIndex, columnIndex, stock.getCours());
	}
	
	private String getCellAsTextValue(int rowIndex) {
		return CellUtils.getCellAsTextValue(sheet, rowIndex, columnIndex);
	}

}
