package fr.bbq.banque;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

	public final static List<String> URLS = Arrays.asList(
			"https://www.boursorama.com/cours/1rPRNO/",
			"https://www.boursorama.com/cours/1rPGLE/");
	public final static String EXCEL_LOCATION = "C:\\Users\\Didier\\eclipse-workspace\\ws_2020-03\\banque\\src\\main\\resources\\banque.xlsx";
	
	public static final SimpleDateFormat SDF_EXCEL = new SimpleDateFormat("dd/MM/yyyy");
			
	public static final int SLEEP_INTERVAL_SECONDS = 1;
	
	enum ROWS_AND_CELLS {
		SHEET_BOURSORAMA(0),
		SHEET_BOURSIER(1),
		ROW_ISIN (0),
		ROW_SOCIETE (1),
		CELL_DATE(0);
		
		
		int value = 0;
		ROWS_AND_CELLS(int n) {
			value = n;
			
		}
	}
			
}
