package farmacia.util;

public enum TipoUtente {
	CLIENTE,
	FARMACISTA,
	DIRETTORE;

	public static TipoUtente fromInt(int x) {
		return TipoUtente.values()[x];
	}
}
