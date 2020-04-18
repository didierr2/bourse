package fr.bbq.banque;

import java.text.SimpleDateFormat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

	public static final SimpleDateFormat SDF_EXCEL = new SimpleDateFormat("dd/MM/yyyy");
			
	
	public static final int SLEEP_INTERVAL_SECONDS = 5;
	
	enum ROWS_AND_CELLS {
		SHEET_DATA(0),
		COL_FIRST_STOCK (1),
		ROW_URL (2),
		ROW_ISIN (3),
		ROW_SOCIETE (4),
		COL_DATE(0);
		
		
		int value = 0;
		ROWS_AND_CELLS(int n) {
			value = n;
			
		}
	}
			
}
