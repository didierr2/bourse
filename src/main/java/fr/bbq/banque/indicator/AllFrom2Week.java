package fr.bbq.banque.indicator;

import lombok.NoArgsConstructor;
import static fr.bbq.banque.util.Conversions.toDouble;

@NoArgsConstructor
public class AllFrom2Week implements GlobalIndicator {

	int nbStocks = 0;
	double cumulPercent = 0d;
	
	public void addStockIndicator(StockIndicators sind) {
		Double val = toDouble(sind.getDiffFrom2WeekIndicator().getValue());
		if (val != null) {
			nbStocks++;
			cumulPercent += val.doubleValue();
		}
	}
	
	public Indicator buildIndicator() {
		return Indicator
				.builder()
				.name(String.format("%% moyen 2 weeks (%s)", nbStocks))
				.description("Pourcentage moyen entre le cours actuel et le cours d'il y a 2 semaines (Ex : -10% si le cours est passé de 10 (il y a 2 semaines) à 9 (actuel))")
				.value(String.valueOf(cumulPercent / nbStocks))
				.percent(true)
				.build();
	}
}
