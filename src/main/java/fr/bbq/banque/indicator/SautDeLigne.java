package fr.bbq.banque.indicator;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SautDeLigne implements GlobalIndicator {

	String titre = "";
	
	public void addStockIndicator(StockIndicators sind) {
	}
	
	public Indicator buildIndicator() {
		return Indicator
				.builder()
				.name(titre)
				.value("")
				.percent(false)
				.numeric(false)
				.build();
	}
}
