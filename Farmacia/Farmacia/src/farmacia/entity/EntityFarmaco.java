package farmacia.entity;

import farmacia.database.FarmacoDAO;
import farmacia.exceptions.DBException;

public class EntityFarmaco {
    private int id;
    private float prezzo;
    private boolean prescrizione;
    private String nome;
    private int scorta;

    public EntityFarmaco() {}

    public EntityFarmaco(FarmacoDAO farmacoDAO) {
        this.id = farmacoDAO.getId();
        this.prezzo = farmacoDAO.getPrezzo();
        this.prescrizione = farmacoDAO.isPrescrizione();
        this.nome = farmacoDAO.getNome();
        this.scorta = farmacoDAO.getScorta();
    }

    public EntityFarmaco(float prezzo, boolean prescrizione, String nome, int scorta) {
        this.prezzo = prezzo;
        this.prescrizione = prescrizione;
        this.nome = nome;
        this.scorta = scorta;
    }

    public void salvaInDB() throws DBException {
        FarmacoDAO nuovoFarmaco = new FarmacoDAO();
        nuovoFarmaco.createFarmaco(this.prezzo, this.prescrizione, this.nome, this.scorta);
        this.id = nuovoFarmaco.getId();
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

    public int getScorta() {
        return scorta;
    }

    public void setScorta(int scorta) {
        this.scorta = scorta;
    }
}
