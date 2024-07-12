package farmacia.util;

import farmacia.dto.DTO;

import java.util.Map;

public class Utility {
	public static boolean contains(Map<DTO, Integer> quantitaFarmaci, String nomeFarmaco, int quantita) {
		for (Map.Entry<DTO, Integer> entry: quantitaFarmaci.entrySet()) {
			if (entry.getKey().get("nome").equals(nomeFarmaco) && entry.getValue() == quantita) {
				return true;
			}
		}
		return false;
	}
}
