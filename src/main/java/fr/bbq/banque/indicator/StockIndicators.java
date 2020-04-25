package fr.bbq.banque.indicator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import fr.bbq.banque.xls.StockColumn;
import lombok.Getter;

public class StockIndicators {

	@Getter
	final StockColumn stockColumn;

	@Getter
	Indicator diffFromRefIndicator;
	@Getter
	Indicator diffFrom2WeekIndicator;
	@Getter
	Indicator diffFrom1WeekIndicator;
	@Getter
	Indicator diffFrom2DaysIndicator;

	
	public StockIndicators(StockColumn stockColumn) {
		this.stockColumn = stockColumn;
		diffFromRefIndicator(); 
		diffFrom2WeekIndicator();
		diffFrom1WeekIndicator();
		diffFrom2DaysIndicator();
	}
	
	public String getStockName() {
		return stockColumn.getSociety();
	}
	
	public List<Indicator> getIndicators() {
		List<Indicator> indicators = new ArrayList<>();
		
		indicators.add(diffFromRefIndicator);
		indicators.add(diffFrom2WeekIndicator);
		indicators.add(diffFrom1WeekIndicator);
		indicators.add(diffFrom2DaysIndicator);
		
		return indicators;
	}
	
	protected void diffFromRefIndicator() {
		diffFromRefIndicator = Indicator
				.builder()
				.name("% ref")
				.value(percentPrice(stockColumn.getRefPrice(), stockColumn.getTodayPrice()))
				.percent(true)
				.build();
	}
	
	protected void diffFrom1WeekIndicator() {
		String val = percentPrice(stockColumn.getLastNPrice(5), stockColumn.getTodayPrice());
		diffFrom1WeekIndicator = Indicator
				.builder()
				.name("% 1 week")
				.value(val)
				.percent(!val.isEmpty())
				.numeric(!val.isEmpty())
				.build();
	}
	
	protected void diffFrom2WeekIndicator() {
		String val = percentPrice(stockColumn.getLastNPrice(10), stockColumn.getTodayPrice());
		diffFrom2WeekIndicator = Indicator
				.builder()
				.name("% 2 weeks")
				.value(val)
				.percent(!val.isEmpty())
				.numeric(!val.isEmpty())
				.build();
	}	

	protected void diffFrom2DaysIndicator() {
		String val = percentPrice(stockColumn.getLastNPrice(2), stockColumn.getTodayPrice());
		diffFrom2DaysIndicator = Indicator
				.builder()
				.name("% 2 days")
				.value(val)
				.percent(!val.isEmpty())
				.numeric(!val.isEmpty())
				.build();
	}

	
	private String percentPrice(String initPrice, String actualPrice) {
		return percentPrice(initPrice, actualPrice, 2);
	}
	private String percentPrice(String initPrice, String actualPrice, int decimal) {
		String res = "";
		if (!initPrice.isEmpty() && !actualPrice.isEmpty()) {
			try {
				Double init = Double.valueOf(initPrice.replace(',', '.'));
				Double actual = Double.valueOf(actualPrice.replace(',', '.'));
				Double percent = (1D / init * actual) - 1;
				res = String.valueOf(new BigDecimal(percent).setScale(decimal, RoundingMode.HALF_EVEN).doubleValue());
			}
			catch (Exception exc) {
				System.err.println(String.format("Error calculating percent price %s of %s : %s", actualPrice, initPrice, exc.getMessage()));
			}
		}
		return res;
	}
	
}
