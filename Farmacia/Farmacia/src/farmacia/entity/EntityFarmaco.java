package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.exceptions.DBException;

public class EntityFarmaco {
    private int id;
    private float prezzo;
    private boolean prescrizione;
    private String nome;
    private int scorte;

    public EntityFarmaco() { }

    public EntityFarmaco(FarmacoDAO farmacoDAO) {
        this.id = farmacoDAO.getId();
        this.prezzo = farmacoDAO.getPrezzo();
        this.prescrizione = farmacoDAO.isPrescrizione();
        this.nome = farmacoDAO.getNome();
        this.scorte = farmacoDAO.getScorte();
    }

    public EntityFarmaco(float prezzo, boolean prescrizione, String nome, int scorte) {
        this.prezzo = prezzo;
        this.prescrizione = prescrizione;
        this.nome = nome;
        this.scorte = scorte;
    }

    public void salvaInDB() throws DBException {
        FarmacoDAO farmaco = new FarmacoDAO(this.prezzo, this.prescrizione, this.nome, this.scorte);
        farmaco.createFarmaco();
        this.id = farmaco.getId();
    }

    public void eliminaDaDB() throws DBException {
        FarmacoDAO farmaco = new FarmacoDAO(this.id);
        farmaco.deleteFarmaco();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public boolean isPrescrizione() {
        return prescrizione;
    }

    public void setPrescrizione(boolean prescrizione) {
        this.prescrizione = prescrizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getScorte() {
        return scorte;
    }

    public void setScorte(int scorte) {
        this.scorte = scorte;
    }
}
