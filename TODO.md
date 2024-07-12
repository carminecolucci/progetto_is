- [x] Evidenziare nomi e verbi nella specifica informale
- [x] Costruire la lista dei requisiti
- [x] Tabella del glossario dei termini
- [x] Tabelle dei requisiti funzionali/sui dati, tabella dei vincoli
- [x] Modellazione dei casi d'uso, definizione dei casi d'uso e degli attori
- [x] Diagramma dei casi d'uso
- [x] Scenari
- [x] Class Diagram di analisi
- [x] Sequence Diagram di analisi
- [x] Tabella di copertura dei requisiti funzionali
- [ ] Piano di test funzionale
- [ ] Class Diagram di progettazione
- [ ] Sequence Diagram di progettazione
- [ ] Implementazione Java, diagramma di deployment, documentazione javadoc
- [ ] Testing strutturale
- [ ] Testing funzionale

### Domande fatte a ricevimento
1. Come viene gestita la vendita di un farmaco? (ordine/spedizione/consegna). Differenza con l'ordine di acquisto?

	Ordine con pagamento online e ritiro in farmacia.
	Quando viene generato l'ordine, la scorte e l'incasso vengono aggiornati, e l'ordine assume lo stato PENDING.
	Quando il cliente andrà in farmacia a ritirare, il farmacista aggiornerà lo stato dell'ordine a RITIRATO.

1. Gestione dell'incasso stampato dal report: doppio report.

1. Serve memorizzare il prezzo per il farmaco da prescrizione? Sì

1. CreaOrdine e RegistraConsegnaOrdineAcquisto hanno bisogno di includere un caso d'uso extra chiamato "AggiornaScorte"?

	No, nel diagramma dei casi d'uso non vanno scritti tutti i dettagli. È utile descrivere gli scenari per identificare tutti i casi d'uso.

1. Farmacista e Direttore sono utenti del sistema che si registrano come utenti normali. Come rappresentarlo nello use case?
	Gerarchia.

1. CercaFarmaco può essere fatta dal Cliente o dal Farmacista. Consideriamo come attore primario l'Utente? Risolto su vpp con la gerarchia

1. Validazione dei dati di input in ogni caso d'uso? Superfluo in fase di analisi

1. Il direttore e il farmacista devono registrarsi come il cliente? O sono già stabiliti, e devono solo fare il login? Solo login

### Domande da chiedere

1. Nel testing delle funzionalità del controller, si possono usare funzionalità del controller (già testate) per settare le precondizioni del test? Ex. per testare visualizzaStoricoOrdini abbiamo usato prima creaOrdine.

1. Nella progettazione della test suite con il metodo del category-partitioning-testing dobbiamo includere solo i test di validazione dell'input? Come facciamo il formalismo tabellare dell'esempio del cinema a gestire le diverse precondizioni? (volendo ad esempio testare il fatto che non ci si può loggare se non si è registrati o che non ci si può registrare se si è già registrati).

1. I test della domanda sopracitata dobbiamo inserirli anche se a livello GUI rendiamo impossibile finire nella maggioranza delle casistiche di errore? Ad esempio potremmo pensare ad un test che verifica che il controller ci restituisca un errore quando cerchiamo di eliminare un farmaco che non esiste, ma noi a livello GUI permettiamo al farmacista di eliminare solo farmaci cliccando sull'elenco del catalogo.

1. Gestione delle eccezioni nei CFG

1. Domanda classe di equivalenza (booleano in input, esempio ModificaFarmaco)

