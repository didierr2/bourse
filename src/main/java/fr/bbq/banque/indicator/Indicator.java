package fr.bbq.banque.indicator;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Indicator {

	String name;
	String value;
	@Builder.Default
	boolean numeric = true;
	@Builder.Default
	boolean percent = true;
	
}
