package fr.bbq.banque;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DateColumnUpdateTest {

	InputStream streamNotTodayDate = null;
	Workbook wbNotTodayDate = null;
	Sheet sheetNotTodayDate = null;
	
	@Before
	public void before () throws IOException{
		streamNotTodayDate = DateColumnReadTest.class.getResourceAsStream("/test_notTodayDate.xlsx");
		wbNotTodayDate = new XSSFWorkbook(streamNotTodayDate);
		sheetNotTodayDate = wbNotTodayDate.getSheetAt(Constants.ROWS_AND_CELLS.SHEET_DATA.value);		
	}

	@After
	public void clean () {
		close(streamNotTodayDate);
		close(wbNotTodayDate);
	}

	@Test
	public void shouldDataBeUpdated () {
		DateColumn dc = new DateColumn(sheetNotTodayDate);
		int lastIndex = dc.getLastRowIndex();
		dc.addTodayDate();
		DateColumn dcToday = new DateColumn(sheetNotTodayDate);

		Assert.assertEquals(lastIndex + 1, dc.getLastRowIndex());
		Assert.assertEquals(dc.getTodayDate(), dc.getLastDate());
		Assert.assertTrue(dc.isTodayLastDate());
	}
	
	@Test
	public void shouldSetTodayDate () {
		DateColumn dc = new DateColumn(sheetNotTodayDate);
		int lastIndex = dc.getLastRowIndex();
		String todayDate = dc.getTodayDate();
		dc.addTodayDate();
		DateColumn dcToday = new DateColumn(sheetNotTodayDate);

		Assert.assertEquals(lastIndex + 1, dcToday.getLastRowIndex());
		Assert.assertEquals(todayDate, dcToday.getLastDate());
	}

	@Test
	public void shouldNotSetTodayDateTwice () {
		DateColumn dc = new DateColumn(sheetNotTodayDate);
		dc.addTodayDate();
		DateColumn dcToday = new DateColumn(sheetNotTodayDate);
		dcToday.addTodayDate();
		DateColumn dcTodayBis = new DateColumn(sheetNotTodayDate);

		Assert.assertEquals(dcToday.getLastRowIndex(), dcTodayBis.getLastRowIndex());
		Assert.assertEquals(dcToday.getLastDate(), dcTodayBis.getLastDate());
	}

	
	
	
	private void close (Closeable clo) {
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
