PROGETTO RELATIVO ALL'ESAME "PROGRAMMAZIONE AD OGGETTI" DATA 19/07/2019
Questo documento ha lo scopo di introdurre e spiegare il funzionamento del programma, i test effettuati, i casi particolari e le decisioni presi a tal proposito. Per una migliore comprensione delle meccaniche del programma si invita i gentili lettori di usurfruire dei diagrammi UML e dei commenti passo-passo inseriti all'interno del codice. All'interno di tale documento per rendere più chiara la spiegazione e la strategia effettuata potrebbero essere presenti riferimenti a frammenti di codice spefici. 

FUNZIONAMENTO DEL PROGRAMMA(generale): 
In generale il programma si occupa di acquisire dati attraverso un file CSV, essere in grado di riconoscerli per poter filtrare in base a degli appositi filtri e comandi mandati dall'utente e restituirni dei valori in base ai comandi richiesti

FUNZIONAMENTO DEL PROGRAMMA (passo-passo):
1) al primo avvio del programma esso controllerà se tra i suoi file è stato scaricato il file CSV e in caso contrario scaricherà tale file andandolo a ricercare all'interno dell'URL, nella console nel momento in cui la fase di scaricamento (o controllo di tale) vengono presentati due tipi di messaggi, "File Dowloaded" nel caso in cui il file non era precedentemente all'interno della directory del programma e sia stato necessario scaricarlo; "FILE ALREADY EXISTS, NO NEED TO DOWNLOAD" nel caso in cui il file sia già stato precedentemente scaricato e non sia stato necessario scaricarlo. In caso di altri tipi di messaggio si è in presenza di un errore e si consiglia di controllare il codice (provando a controllare URL dedicato)

2) Dopo aver effettuato il parsing dei dati così ricevuti crea una funzione (parseFile()) che si occupa di restituire una lista di oggetti di tipo MyClass nel quale sono state suddivise le relative informazioni relative al file CSV suddivise dal simbolo di ",".

2A) Il programma reagisce in caso di mancanza di dati inserendo dei valori di default nulli (MyDowloadAndParse riga 163-169);
2B) Il programma reagisce anche in caso di presenza di " all'interno di righe andandole a cercare ed eliminandole (evitando così problemi di compilazione) (MyDowloadAndParse riga 147-157)
2C)La parte di codice precedentemente mostrata viene utilizzata anche per ricercare eventuali "," in eccesso ed eliminarle per rendere la suddivisione delle informazioni più semplice e per rispiarmare ulteriori righe di codice o cicli per effettuare tale ricerca

3) Durante l'analisi e la suddivisione delle informazioni esse vengono progressivamente salvate all'interno della lista di oggetti di tipo MyClass finchè l'analisi delle righe del file non avrà contenuto nullo.

4) Viene chiuso il file CSV e faccio ritornare la lista di oggetti di tipo MyClass.

5)Il programma attende un comando da parte dell'utente (sfruttando il localhost assegnato da SpringBoot (di default la porta 8080)) tramite uno dei seguenti comandi:
5/data)restituisce tutti i dati ottenuti all'avvio dell'applicazione;
5/metadata) restituisce i metadati della classe MyClass;
5/filter) restituisce i dati relativi al filtro impostato o ai dati richiesti generando una mappa da far tornare in caso in cui non vengano rispettati i parametri del filtro.

FUNZIONAMENTO DEL FILTRO:
Una volta ricevute le informazioni da parte dell'utente il programma inanzitutto controlla che i parametri siano stati inviati correttamente successivamente il programma tiene conto del campo da analizzare controllando che per il relativo campo sia stato mandato il comando di controllo opportuno (generando quindi un messaggio di errore nel caso in cui si usi il comando di ">","<" per le stringhe o "in","nin" per i numeri). 
Nel caso di filtri di valori numerici vengono riportati inseme ai risultati anche il valore minimo, il massimo, la deviazione standard, la somma totale e il numero degli elementi filtrati.
Nel caso di filtri di strighe viene restituita l'unicità della stringa e il conteggio della ripetizione del relativo parametro.

MODIFICHE AL PROGETTO EFFETTUATE: 
Come precedentemente introdotto per evitare problemi di compilazione e alleggerire quanto più possibile l'esecuzione del codice (rendendolo quanto più possibile generale e non specifico per il file CSV assegnato) è stato necessario far modificare al programma alcuni valori del file CSV (solo all'interno del programma) come le " all'interno di strighe e alcune "," che si trovano all'interno delle strighe e come ultima modifica l'assegnazione di valori di default in caso di valori non presenti. 
