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
		ROW_URL (2),
		ROW_ISIN (3),
		ROW_SOCIETY (4),
		ROW_REF_PRICE(5),
		COL_DATE(0),
		
		// Les indicateurs
		COL_INDICATORS_SOCIETY (0),
		COL_INDICATORS_FIRST(1),
		ROW_INDICATORS_HEADERS (0),
		ROW_INDICATORS_FIRST(1);
		
		
		public int value = 0;
		ROWS_AND_CELLS(int n) {
			value = n;
		}
	}
			
}
