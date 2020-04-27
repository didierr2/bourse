package fr.bbq.banque.indicator;

import static fr.bbq.banque.util.Conversions.toDouble;

public class Top10DownFrom1Week extends AbstractTopIndicator{

	public Top10DownFrom1Week() {
		super(	10,
				(o1, o2) -> toDouble(o1.getValue()) > toDouble(o2.getValue()) ? 1 : -1
				);
	}

	public void addStockIndicator(StockIndicators sind) {
		lstInd.add(sind.getDiffFrom1WeekIndicator());
	}
	
	public Indicator buildIndicator() {
		int nbStocks = getNumberOfValues();
		double cumul = getTopSumValue();
		
		return Indicator
				.builder()
				.name(String.format("%% Top 10 baissier 1 week (%s)", nbStocks))
				.description("Top 10 des actions ayant le plus baissé depuis 1 semaine")
				.value(String.valueOf(cumul / nbStocks))
				.percent(true)
				.build();
	}
	
}
