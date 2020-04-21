package fr.bbq.banque.indicator;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AllFromRef extends GlobalIndicator {

	double nbStocks = 0d;
	double cumulPercent = 0d;
	
	public void addStockIndicator(StockIndicators sind) {
		Double val = toDouble(sind.getDiffFromRefIndicator().getValue());
		if (val != null) {
			nbStocks++;
			cumulPercent += val.doubleValue();
		}
	}
	
	public Indicator getIndicator() {
		return Indicator
				.builder()
				.name("% moyen ref")
				.value(String.valueOf(cumulPercent / nbStocks))
				.percent(true)
				.build();
	}
	
	private Double toDouble(String value) {
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
