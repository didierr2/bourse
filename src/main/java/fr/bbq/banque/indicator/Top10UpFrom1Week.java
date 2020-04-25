package fr.bbq.banque.indicator;

import static fr.bbq.banque.util.Conversions.toDouble;

public class Top10UpFrom1Week extends AbstractTopIndicator{

	public Top10UpFrom1Week() {
		super(	10,
				(o1, o2) -> (int)(toDouble(o2.getValue()) - toDouble(o1.getValue()))
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
				.name(String.format("%% Top 10 haussier 1 week (%s)", nbStocks))
				.description("Top 10 des actions ayant le plus monté depuis 1 semaine")
				.value(String.valueOf(cumul / nbStocks))
				.percent(true)
				.build();
	}
	
}
