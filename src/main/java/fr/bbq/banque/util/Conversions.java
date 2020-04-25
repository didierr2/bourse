package fr.bbq.banque.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Conversions {

	public static Double toDouble(String value) {
		Double res = null;
		try {
			res = Double.valueOf(value);
		}
		catch (Exception exc) {
			System.err.println(String.format("Error calculating percent of %s : %s", value, exc.getMessage()));
			res = null;
		}
		return res;
	}
	
}
