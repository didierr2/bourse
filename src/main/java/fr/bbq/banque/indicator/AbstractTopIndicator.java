package fr.bbq.banque.indicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.bbq.banque.util.Conversions;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractTopIndicator implements GlobalIndicator {

	protected List<Indicator> lstInd = new ArrayList<>();
	
	// Nombre d'elements dans le top
	private final int topN;
	private final Comparator<Indicator> topComparator;

	public int getNumberOfValues() {
		return lstInd.size() > topN ? topN : lstInd.size();
	}


	public List<Indicator> getTopIndicators() {
		Collections.sort(lstInd, topComparator);
		return lstInd.size() > topN ? lstInd.subList(0, topN) : lstInd;
	}

	public double getTopSumValue() {
		double sum = 0;
		for (Indicator ind : getTopIndicators()) {
			sum += Conversions.toDouble(ind.getValue());
		}
		
		return sum;
	}
	
}
