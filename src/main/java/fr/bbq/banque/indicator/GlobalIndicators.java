package fr.bbq.banque.indicator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class GlobalIndicators {

	@Getter
	List<GlobalIndicator> indicators = new ArrayList<>();
	
	public GlobalIndicators() {
		
		// Init les indicateurs globaux
		indicators.add(new AllFromRef());
	}
	
	public void addStockIndicator(StockIndicators sind) {
		indicators.forEach(gi -> gi.addStockIndicator(sind));
	}
	
}
