package farmacia.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe del package DTO non presente nel modello BCED, utilizzata per lo scambio di informazioni dai livelli inferiori
 * verso i superiori.
 */
public class DTO {
	private Map<String, Object> data;

	/**
	 * Costruttore di default di Dto, crea un'entità vuota.
	 */
	public DTO() {
		data = new HashMap<>();
	}

	public Object get(String key) {
		return data.get(key);
	}

	public void set(String key, Object value) {
		data.put(key, value);
	}
}
