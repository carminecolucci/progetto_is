- [x] Evidenziare nomi e verbi nella specifica informale
- [x] Costruire la lista dei requisiti
- [x] Tabella del glossario dei termini
- [x] Tabelle dei requisiti funzionali/sui dati, tabella dei vincoli
- [ ] Modellazione dei casi d'uso, definizione dei casi d'uso e degli attori
- [ ] Diagramma dei casi d'uso
- [ ] Scenari
- [ ] Class Diagram
- [ ] Sequence Diagram

## Domande da chiedere
1. Come viene gestita la vendita di un farmaco? (ordine/spedizione/consegna). Differenza con l'ordine di acquisto?
	ordine con pagamento online e ritiro in farmacia.
	quando viene generato l'ordine, la scorta e l'incasso vengono aggiornati, e l'ordine assume lo stato PENDING.
	quando il cliente andrà in farmacia a ritirare, il farmacista aggiornerà lo stato dell'ordine a RITIRATO.

1. Gestione dell'incasso stampato dal report: doppio report.

1. Serve memorizzare il prezzo per il farmaco da prescrizione? SI

1. CreaOrdine e RegistraConsegnaOrdineAcquisto hanno bisogno di includere un caso d'uso extra chiamato "AggiornaScorte"?