package fr.bbq.banque;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Valeur {

	String isin;
	String cours; 
	String societe;
	
}
