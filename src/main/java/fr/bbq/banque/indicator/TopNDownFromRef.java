package fr.bbq.banque.indicator;

import static fr.bbq.banque.util.Conversions.toDouble;

public class TopNDownFromRef extends AbstractTopIndicator{

	int topNumber = 0;
	
	public TopNDownFromRef(int n) {
		super(	n,
				(o1, o2) -> (int)(toDouble(o1.getValue()) - toDouble(o2.getValue()))
				);
		topNumber = n;
	}

	public void addStockIndicator(StockIndicators sind) {
		lstInd.add(sind.getDiffFromRefIndicator());
	}
	
	public Indicator buildIndicator() {
		int nbStocks = getNumberOfValues();
		double cumul = getTopSumValue();
		
		return Indicator
				.builder()
				.name(String.format("%% Top %s baissier ref (%s)", topNumber, nbStocks))
				.description("Top " + topNumber + " des actions ayant le plus baissé depuis le cours de référence")
				.value(String.valueOf(cumul / nbStocks))
				.percent(true)
				.build();
	}
	
}
