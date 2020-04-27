package fr.bbq.banque.indicator;

import lombok.NoArgsConstructor;
import static fr.bbq.banque.util.Conversions.toDouble;

@NoArgsConstructor
public class AllFromRef implements GlobalIndicator {

	int nbStocks = 0;
	double cumulPercent = 0d;
	
	public void addStockIndicator(StockIndicators sind) {
		Double val = toDouble(sind.getDiffFromRefIndicator().getValue());
		if (val != null) {
			nbStocks++;
			cumulPercent += val.doubleValue();
		}
	}
	
	public Indicator buildIndicator() {
		return Indicator
				.builder()
				.name(String.format("%% moyen ref (%s)", nbStocks))
				.description("Pourcentage moyen entre le cours actuel et le cours de ref (Ex : -40% si le cours est passé de 10 (ref) à 6 (actuel))")
				.value(String.valueOf(cumulPercent / nbStocks))
				.percent(true)
				.build();
	}
}
