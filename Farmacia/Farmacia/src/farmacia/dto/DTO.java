package farmacia.dto;
/**
 * Classe del package DTO non presente nel modello BCED, utilizzata per lo scambio di informazioni dai livelli inferiori
 * verso i superiori.
 */
public class DTO {

	/**
	 * Campo 1 del DTO.
	 */
	private String campo1;

	/**
	 * Campo 2 del DTO.
	 */
	private String campo2;

	/**
	 * Campo 3 del DTO.
	 */
	private String campo3;

	/**
	 * Campo 4 del DTO.
	 */
	private String campo4;

	/**
	 * Campo 5 del DTO.
	 */
	private String campo5;

	/**
	 * Campo 6 del DTO.
	 */
	private String campo6;

	/**
	 * Campo 7 del DTO.
	 */
	private String campo7;

	/**
	 * Campo 8 del DTO.
	 */
	private String campo8;

	/**
	 * Costruttore di default di MyDto, crea un'entità vuota.
	 */
	public DTO() {}

	/**
	 * Costruttore di MyDto che popola i suoi campi con i parametri forniti.
	 * @param campo1 campo 1 del DTO.
	 * @param campo2 campo 2 del DTO.
	 * @param campo3 campo 3 del DTO.
	 * @param campo4 campo 4 del DTO.
	 * @param campo5 campo 5 del DTO.
	 * @param campo6 campo 6 del DTO.
	 * @param campo7 campo 7 del DTO.
	 * @param campo8 campo 8 del DTO.
	 */
	public DTO(String campo1, String campo2, String campo3, String campo4, String campo5, String campo6,
				 String campo7, String campo8) {
		super();
		this.campo1 = campo1;
		this.campo2 = campo2;
		this.campo3 = campo3;
		this.campo4 = campo4;
		this.campo5 = campo5;
		this.campo6 = campo6;
		this.campo7 = campo7;
		this.campo8 = campo8;
	}

	/**
	 * Getter del campo 1 del DTO.
	 * @return il campo 1 del DTO.
	 */
	public String getCampo1() {
		return campo1;
	}

	/**
	 * Setter del campo 1 del DTO.
	 * @param campo1 il nuovo campo 1 del DTO.
	 */
	public void setCampo1(String campo1) {
		this.campo1 = campo1;
	}

	/**
	 * Getter del campo 2 del DTO.
	 * @return il campo 2 del DTO.
	 */
	public String getCampo2() {
		return campo2;
	}

	/**
	 * Setter del campo 2 del DTO.
	 * @param campo2 il nuovo campo 2 del DTO.
	 */
	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}

	/**
	 * Getter del campo 3 del DTO.
	 * @return il campo 3 del DTO.
	 */
	public String getCampo3() {
		return campo3;
	}

	/**
	 * Setter del campo 3 del DTO.
	 * @param campo3 il nuovo campo 3 del DTO.
	 */
	public void setCampo3(String campo3) {
		this.campo3 = campo3;
	}

	/**
	 * Getter del campo 4 del DTO.
	 * @return il campo 4 del DTO.
	 */
	public String getCampo4() {
		return campo4;
	}

	/**
	 * Setter del campo 4 del DTO.
	 * @param campo4 il nuovo campo 4 del DTO.
	 */
	public void setCampo4(String campo4) {
		this.campo4 = campo4;
	}

	/**
	 * Getter del campo 5 del DTO.
	 * @return il campo 5 del DTO.
	 */
	public String getCampo5() {
		return campo5;
	}

	/**
	 * Setter del campo 5 del DTO.
	 * @param campo5 il nuovo campo 5 del DTO.
	 */
	public void setCampo5(String campo5) {
		this.campo5 = campo5;
	}

	/**
	 * Getter del campo 6 del DTO.
	 * @return il campo 6 del DTO.
	 */
	public String getCampo6() {
		return campo6;
	}

	/**
	 * Setter del campo 6 del DTO.
	 * @param campo6 il nuovo campo 6 del DTO.
	 */
	public void setCampo6(String campo6) {
		this.campo6 = campo6;
	}

	/**
	 * Getter del campo 7 del DTO.
	 * @return il campo 7 del DTO.
	 */
	public String getCampo7() {
		return campo7;
	}

	/**
	 * Setter del campo 7 del DTO.
	 * @param campo7 il nuovo campo 7 del DTO.
	 */
	public void setCampo7(String campo7) {
		this.campo7 = campo7;
	}

	/**
	 * Getter del campo 8 del DTO.
	 * @return il campo 8 del DTO.
	 */
	public String getCampo8() {
		return campo8;
	}

	/**
	 * Setter del campo 8 del DTO.
	 * @param campo8 il nuovo campo 8 del DTO.
	 */
	public void setCampo8(String campo8) {
		this.campo8 = campo8;
	}

	/**
	 * Override del metodo <code>toString</code> per visualizzare il contenuto del DTO.
	 * @return una stringa formattata per visualizzare il contenuto del DTO.
	 */
	@Override
	public String toString() {
		return "MyDTO [campo1=" + campo1 + ", campo2=" + campo2 + ", campo3=" + campo3 + ", campo4="+ campo4 + ", campo5="
				+ campo5 + ", campo6="+ campo6 + ", campo7="+ campo7 +"]";
	}
}
