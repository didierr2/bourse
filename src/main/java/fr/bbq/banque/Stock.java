package fr.bbq.banque;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Stock {

	String isin;
	String cours; 
	String societe;
	
	public void setCours(String c) {
		cours = c;
		if (cours != null) {
			cours = cours.replace('.', ',');
		}
	}
	
}
