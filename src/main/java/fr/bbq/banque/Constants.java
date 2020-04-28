package fr.bbq.banque;

import java.text.SimpleDateFormat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

	public static final SimpleDateFormat SDF_EXCEL = new SimpleDateFormat("dd/MM/yyyy");
			
	
	public static final int SLEEP_INTERVAL_SECONDS = 5;
	
	public enum ROWS_AND_CELLS {
		SHEET_DATA(0),
		SHEET_INDICATOR(1),
		SHEET_SYNTHESIS(2),
		
		
		COL_FIRST_STOCK (1),
		ROW_INDEX (0), // INDICE (DOW, CAC)
		ROW_RECO (1), // RECOMMANDATION
		ROW_REPORT_COMM (2),
		ROW_BUY_DATE (3),
		ROW_COST_PRICE (4),
		ROW_URL (5),
		ROW_ISIN (6),
		ROW_SOCIETY (7),
		ROW_REF_PRICE(8),
		COL_DATE(0),
		
		// Les indicateurs
		COL_INDICATORS_HEADERS (0),
		COL_INDICATORS_FIRST(1),
		ROW_INDICATORS_HEADERS (0),
		ROW_INDICATORS_FIRST(1);
		
		
		public int value = 0;
		ROWS_AND_CELLS(int n) {
			value = n;
		}
	}
			
}
