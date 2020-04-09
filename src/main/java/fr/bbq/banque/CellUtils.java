package fr.bbq.banque;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CellUtils {

	public static String getCellAsTextValue(Sheet sheet, int rowIndex, int colIndex) {
		String val = "";
		try {
			Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
			if (cell != null) {
				switch (cell.getCellType()) {
				case BOOLEAN:
					val = String.valueOf(cell.getBooleanCellValue());
					break;
				case STRING:
					val = cell.getStringCellValue();
					break;
				case BLANK:
					val = "";
					break;
				case NUMERIC:
					val = String.valueOf(cell.getNumericCellValue());
					break;
				default:
					System.err.println("Read error " + getCellLabel(rowIndex, colIndex) + ", type = " + cell.getCellType().name());
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("Error recuperation cellule " + getCellLabel(rowIndex, colIndex) + " : " + e.getMessage());
		}
		return val;
	}

	public static String getCellLabel(int rowIndex, int colIndex) {
		return (char) ('A' + colIndex) + "" + (rowIndex + 1);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	public static void writeCell (Sheet sheet, int rowIndex, int colIndex, String val) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex, CellType.STRING);
		}
		cell.setCellValue(val);
//		System.out.println("WRITE : " + getCellLabel(rowIndex, colIndex) + " = " + val);
	}
	
	
}
