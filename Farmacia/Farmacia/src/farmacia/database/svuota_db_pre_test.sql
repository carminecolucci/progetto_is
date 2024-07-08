# L'esecuzione di questo script presuppone che sulle tabelle ordini_acquisto_farmaci e ordini_farmaci siano state settate le politiche di reazione ON_CASCADE per le FK
# riferite alle chiavi di ordini e ordini_acquisto e che sia stata disabilitata l'opzione di SAFE UPDATE del DB (Edit > Preferences > SQL Editor > Other)
DELETE from ordini;
DELETE from ordini_acquisto;
DELETE from farmaci;