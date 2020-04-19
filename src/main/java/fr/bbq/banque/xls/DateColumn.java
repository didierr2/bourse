package fr.bbq.banque.xls;

import static fr.bbq.banque.Constants.SDF_EXCEL;
import static fr.bbq.banque.util.CellUtils.getCellAsTextValue;
import static fr.bbq.banque.util.CellUtils.isEmpty;
import static fr.bbq.banque.util.CellUtils.writeCell;

import java.util.Date;

import org.apache.poi.ss.usermodel.Sheet;

import fr.bbq.banque.Constants;
import lombok.Getter;

public class DateColumn {

	final Sheet sheet;
	@Getter
	int lastRowIndex;
	@Getter
	String lastDate = null;
	@Getter
	boolean isTodayLastDate = false;

	public DateColumn(Sheet sh) {
		this.sheet = sh;

		// Récupère l'index et la date de la derniere cellul
		lastRowIndex = sheet.getLastRowNum() + 1;
		do {
			lastRowIndex--;
			lastDate = getCellAsTextValue(sheet, lastRowIndex, Constants.ROWS_AND_CELLS.COL_DATE.value);
		} while (isEmpty(lastDate) && lastRowIndex >= 0);

		// Indique si c'est la date du jour
		isTodayLastDate = getTodayDate().equals(lastDate);
	}

	/** La date du jour */
	public String getTodayDate() {
		return SDF_EXCEL.format(new Date());
	}

	/** La date du jour n'existe pas, on l'ajoute */
	public void addTodayDate() {
		if (!isTodayLastDate) {
			writeCell(sheet, lastRowIndex + 1, Constants.ROWS_AND_CELLS.COL_DATE.value, getTodayDate());
			isTodayLastDate = true;
			lastDate = getTodayDate();
			lastRowIndex++;
		}
	}

}
