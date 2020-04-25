package fr.bbq.banque.indicator;

public interface GlobalIndicator {

	void addStockIndicator(StockIndicators sind);
	
	Indicator buildIndicator();
	
}
