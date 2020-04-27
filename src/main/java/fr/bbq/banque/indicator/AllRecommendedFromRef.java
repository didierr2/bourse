package fr.bbq.banque.indicator;

import lombok.NoArgsConstructor;
import static fr.bbq.banque.util.Conversions.toDouble;

@NoArgsConstructor
public class AllRecommendedFromRef implements GlobalIndicator {

	int nbStocks = 0;
	double cumulPercent = 0d;
	
	public void addStockIndicator(StockIndicators sind) {
		// Seulement les actions recommandées
		if (sind.getStockColumn().isRecommended()) {
			Double val = toDouble(sind.getDiffFromRefIndicator().getValue());
			if (val != null) {
				nbStocks++;
				cumulPercent += val.doubleValue();
			}
		}
	}
	
	public Indicator buildIndicator() {
		return Indicator
				.builder()
				.name(String.format("%% moyen reco ref (%s)", nbStocks))
				.description("Pourcentage moyen entre le cours actuel et le cours de ref pour les actions ayant une recommandation")
				.value(String.valueOf(cumulPercent / nbStocks))
				.percent(true)
				.build();
	}
}
