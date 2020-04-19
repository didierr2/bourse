package fr.bbq.banque.xls;

import static fr.bbq.banque.Constants.ROWS_AND_CELLS.COL_FIRST_STOCK;
import static fr.bbq.banque.util.CellUtils.isEmpty;
import static fr.bbq.banque.util.CellUtils.writeCell;
import static fr.bbq.banque.util.CellUtils.writeNumericCell;

import org.apache.poi.ss.usermodel.Sheet;

import fr.bbq.banque.Constants;
import fr.bbq.banque.util.CellUtils;
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
		return getCellAsTextValue(todayRowIndex);
	}
	
	public String getRefPrice() {
		return getCellAsTextValue(Constants.ROWS_AND_CELLS.ROW_REF_PRICE.value);
	}		

	/**
	 * Retourne le Nième dernier prix.
	 * Ex : getLastNPrice(0) retourne le dernier prix indiqué, getLastNPrice(5) celui d'il y a une semaine
	 * @param n
	 * @return
	 */
	public String getLastNPrice(int lastN) {
		// On vérifie que le prix est possible
		int nbPricesAvailable = todayRowIndex - Constants.ROWS_AND_CELLS.ROW_REF_PRICE.value; 
		if (lastN >= nbPricesAvailable) {
			throw new IllegalStateException(String.format("Impossible de retourner le %s ième dernier prix, il y a seulement %s prix pour l'instant.", lastN, nbPricesAvailable));
		}
		return getCellAsTextValue(todayRowIndex - lastN);
	}		

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
		writeNumericCell(sheet, todayRowIndex, columnIndex, stock.getCours());
	}
	
	private String getCellAsTextValue(int rowIndex) {
		return CellUtils.getCellAsTextValue(sheet, rowIndex, columnIndex);
	}

}
