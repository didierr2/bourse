package fr.bbq.banque;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateColumnReadTest {

	static InputStream streamNotTodayDate = null;
	static Workbook wbNotTodayDate = null;
	static Sheet sheetNotTodayDate = null;
	
	@BeforeClass
	public static void init () throws IOException{
		streamNotTodayDate = DateColumnReadTest.class.getResourceAsStream("/test_notTodayDate.xlsx");
		wbNotTodayDate = new XSSFWorkbook(streamNotTodayDate);
		sheetNotTodayDate = wbNotTodayDate.getSheetAt(Constants.ROWS_AND_CELLS.SHEET_DATA.value);		
	}

	@AfterClass
	public static void clean () {
		close(streamNotTodayDate);
		close(wbNotTodayDate);
	}

	
	@Test
	public void shouldNotHaveTodayDate () {
		DateColumn dc = new DateColumn(sheetNotTodayDate);
		Assert.assertFalse(dc.isTodayLastDate());
	}

	@Test
	public void shouldHaveLast30032020AsLastDate () {
		DateColumn dc = new DateColumn(sheetNotTodayDate);
		Assert.assertEquals("30/03/2020", dc.getLastDate());
	}

	@Test
	public void shouldHaveLastDateAsColumnIndex5 () {
		DateColumn dc = new DateColumn(sheetNotTodayDate);
		Assert.assertEquals(5, dc.getLastRowIndex());
	}

	
	
	private static void close (Closeable clo) {
		try {
			if (clo != null) {
				clo.close();
			}
		}
		catch (IOException exc) {
			// C'est clos !
		}
	}
	
}
