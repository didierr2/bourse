package fr.bbq.banque.indicator;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class GlobalIndicator {

	public abstract void addStockIndicator(StockIndicators sind);
	
	public abstract Indicator getIndicator();
	
	
}
