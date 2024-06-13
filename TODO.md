- [x] Evidenziare nomi e verbi nella specifica informale
- [x] Costruire la lista dei requisiti
- [x] Tabella del glossario dei termini
- [x] Tabelle dei requisiti funzionali/sui dati, tabella dei vincoli
- [x] Modellazione dei casi d'uso, definizione dei casi d'uso e degli attori
- [x] Diagramma dei casi d'uso
- [x] Scenari
- [ ] Class Diagram
- [ ] Sequence Diagram

### Domande fatte a ricevimento
1. Come viene gestita la vendita di un farmaco? (ordine/spedizione/consegna). Differenza con l'ordine di acquisto?

	Ordine con pagamento online e ritiro in farmacia.
	Quando viene generato l'ordine, la scorta e l'incasso vengono aggiornati, e l'ordine assume lo stato PENDING.
	Quando il cliente andrà in farmacia a ritirare, il farmacista aggiornerà lo stato dell'ordine a RITIRATO.

1. Gestione dell'incasso stampato dal report: doppio report.

1. Serve memorizzare il prezzo per il farmaco da prescrizione? Sì

1. CreaOrdine e RegistraConsegnaOrdineAcquisto hanno bisogno di includere un caso d'uso extra chiamato "AggiornaScorte"?

	No, nel diagramma dei casi d'uso non vanno scritti tutti i dettagli. È utile descrivere gli scenari per identificare tutti i casi d'uso.

1. Farmacista e Direttore sono utenti del sistema che si registrano come utenti normali. Come rappresentarlo nello use case?
	Gerarchia.
### Domande da chiedere

1. Validazione dei dati di input in ogni caso d'uso? 

1. Il direttore e il farmacista devono registrarsi come il cliente? O sono già stabiliti, e devono solo fare il login?

1. CercaProdotto può essere fatta dal Cliente o dal Farmacista. Consideriamo come attore primario l'Utente?

