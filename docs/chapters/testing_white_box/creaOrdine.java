public String creaOrdine(Map<Integer, Integer> farmaciQuantita) throws OrderCreationFailedException {
	if (farmaciQuantita.isEmpty())
		throw new OrderCreationFailedException("Ordine vuoto");

	EntityCatalogo catalogo = EntityCatalogo.getInstance();
	if (!catalogo.checkScorte(farmaciQuantita)) {
		throw new OrderCreationFailedException("Ordine non creato per mancanza scorte");
	}
	EntityOrdine ordine = new EntityOrdine(this.getId());
	try {
		EntityOrdineAcquisto ordineAcquisto = new EntityOrdineAcquisto();
		for (Map.Entry<Integer, Integer> entry : farmaciQuantita.entrySet()) {
			int id = entry.getKey();
			int quantita = entry.getValue();
			EntityFarmaco farmaco = catalogo.cercaFarmacoById(id);
			ordine.aggiungiOrdineFarmaco(farmaco, quantita);
			int scorteResidue = catalogo.decrementaScorte(id, quantita);
			if (scorteResidue == 0) {
				ordineAcquisto.aggiungiOrdineAcquistoFarmaco(farmaco, EntityOrdineAcquisto.QUANTITA_ORDINE_DEFAULT);
			}
		}
		if (!ordineAcquisto.getQuantitaFarmaci().isEmpty()) {
			ordineAcquisto.salvaInDB();
		}
		ordine.salvaInDB();
		storicoOrdini.add(ordine);
		return ordine.getId();
	} catch (FarmacoNotFoundException e) {
		throw new OrderCreationFailedException("Errore creazione ordine, farmaco non trovato");
	} catch (DBException e) {
		throw new OrderCreationFailedException(e.getMessage());
	}
}