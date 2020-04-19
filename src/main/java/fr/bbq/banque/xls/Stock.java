package fr.bbq.banque;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Stock {

	String isin;
	String cours; 
	String societe;
}
