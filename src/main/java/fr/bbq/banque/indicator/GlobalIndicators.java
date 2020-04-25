package fr.bbq.banque.indicator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class GlobalIndicators {

	@Getter
	List<GlobalIndicator> indicators = new ArrayList<>();
	
	public GlobalIndicators() {
		
		// Init les indicateurs globaux
		indicators.add(new SautDeLigne("Indicateurs : "));
		indicators.add(new AllFromRef());
		indicators.add(new AllRecommendedFromRef());
		indicators.add(new AllDownFromRef());
		indicators.add(new AllFrom2Week());
		indicators.add(new AllFrom1Week());

		indicators.add(new SautDeLigne());
		indicators.add(new SautDeLigne("Top 10"));
		indicators.add(new TopNDownFromRef(10));
		indicators.add(new Top10DownFrom1Week());
		indicators.add(new Top10UpFrom1Week());

		indicators.add(new SautDeLigne());
		indicators.add(new SautDeLigne("Short Top"));
		indicators.add(new TopNDownFromRef(5));
		indicators.add(new TopNDownFromRef(2));
	}
	
	public void addStockIndicator(StockIndicators sind) {
		indicators.forEach(gi -> gi.addStockIndicator(sind));
	}
	
}
