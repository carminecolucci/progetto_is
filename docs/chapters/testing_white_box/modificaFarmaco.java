public void modificaFarmaco(int id, float prezzo, boolean prescrizione, String nome, int scorte) throws FarmacoNotFoundException {
    try {
        FarmacoDAO.aggiornaFarmacoDB(id, prezzo, prescrizione, nome, scorte);
        for (EntityFarmaco farmaco : farmaci) {
            if (farmaco.getId() == id) {
                farmaco.setPrezzo(prezzo);
                farmaco.setPrescrizione(prescrizione);
                farmaco.setNome(nome);
                farmaco.setScorte(scorte);
                break;
            }
        }
    } catch (DBException e) {
        throw new FarmacoNotFoundException(e.getMessage());
    }
}