package farmacia;

public enum TipoOrdine {
	ORDINE_CLIENTE,
	ORDINE_ACQUISTO;

	public static TipoOrdine fromInt(int x) {
		return TipoOrdine.values()[x];
	}
}
