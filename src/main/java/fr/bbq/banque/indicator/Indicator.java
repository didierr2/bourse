package fr.bbq.banque.indicator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indicator {

	String name;
	String description;
	String value;
	@Builder.Default
	boolean numeric = true;
	@Builder.Default
	boolean percent = true;
	
}
