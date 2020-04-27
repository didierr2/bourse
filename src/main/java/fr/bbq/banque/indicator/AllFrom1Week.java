package fr.bbq.banque.indicator;

import lombok.NoArgsConstructor;
import static fr.bbq.banque.util.Conversions.toDouble;

@NoArgsConstructor
public class AllFrom1Week implements GlobalIndicator {

	int nbStocks = 0;
	double cumulPercent = 0d;
	
	public void addStockIndicator(StockIndicators sind) {
		Double val = toDouble(sind.getDiffFrom1WeekIndicator().getValue());
		if (val != null) {
			nbStocks++;
			cumulPercent += val.doubleValue();
		}
	}
	
	public Indicator buildIndicator() {
		return Indicator
				.builder()
				.name(String.format("%% moyen 1 week (%s)", nbStocks))
				.description("Pourcentage moyen entre le cours actuel et le cours d'il y a une semaine (Ex : -10% si le cours est passé de 10 (il y a une semaine) à 9 (actuel))")
				.value(String.valueOf(cumulPercent / nbStocks))
				.percent(true)
				.build();
	}
}
