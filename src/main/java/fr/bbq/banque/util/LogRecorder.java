package fr.bbq.banque.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LogRecorder {

	@Getter
	private List<String> logs = new ArrayList<String>();
	
	public void addLog(String mess) {
		logs.add(mess);
	}
	
}
