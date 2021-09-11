# PATonTS - Preferences AggregaTor ON Telegram and Signal
Progetto PROGRAMMAZIONE INTERNET – Informatica per il Management – Università di Bologna

Gruppo: Niccolò Bellucci-Lorenzo Vincini

PATonTS
A cura di Niccolò Bellucci e Lorenzo Vincini

Informazioni gruppo

Il gruppo è formato da Niccolò Bellucci e Lorenzo Vincini, entrambi al primo anno di frequentazione dell’università di Bologna. La suddivisione dei compiti è stata eseguita in questo modo: Niccolò si è occupato dello sviluppo dell’applicazione, mentre Lorenzo si è occupato dell’interfaccia Telegram.
Ogni decisione riguardo le specifiche del progetto è stata presa in comune, dopo adeguate discussioni, anche se si è lasciata libertà al rispettivo compagno di realizzare la propria parte al meglio delle proprie possibilità. Entrambi i componenti hanno dovuto informarsi su come svolgere il progetto correttamente, poiché per entrambi era un ambiente all’incirca nuovo: non tanto come linguaggio, quanto più le librerie (JavaFX e Telegram).

Istruzione per eseguire

Le istruzioni saranno relative ad entrambe le interfacce con cui l’utente può interagire:

# PATonTS - Preferences AggregaTor ON Telegram and Signal
Progetto PROGRAMMAZIONE INTERNET – Informatica per il Management – Università di Bologna

Gruppo: Niccolò Bellucci-Lorenzo Vincini

PATonTS
A cura di Niccolò Bellucci e Lorenzo Vincini

Informazioni gruppo

Il gruppo è formato da Niccolò Bellucci e Lorenzo Vincini, entrambi al primo anno di frequentazione dell’università di Bologna. La suddivisione dei compiti è stata eseguita in questo modo: Niccolò si è occupato dello sviluppo dell’applicazione, mentre Lorenzo si è occupato dell’interfaccia Telegram.
Ogni decisione riguardo le specifiche del progetto è stata presa in comune, dopo adeguate discussioni, anche se si è lasciata libertà al rispettivo compagno di realizzare la propria parte al meglio delle proprie possibilità. Entrambi i componenti hanno dovuto informarsi su come svolgere il progetto correttamente, poiché per entrambi era un ambiente all’incirca nuovo: non tanto come linguaggio, quanto più le librerie (JavaFX e Telegram).

Istruzione per eseguire

Le istruzioni saranno relative ad entrambe le interfacce con cui l’utente può interagire:

<h3>Telegram </h3>

Per avviare il bot, che si chiama WorkSpaceBV, bisogna inviare in chat il comando d’avvio “/start”. Appena avviato il bot sarà chiaro come comportarsi perché la conversazione è quasi completamente gestita da bottoni, quindi l’utente dovrà schiacciare il bottone relativo alla funzione ricercata, l’utilizzo della tastiera per inviare messaggi è richiesto per alcune funzioni. I menù si differenziano in base al tipo di utente, semplice o admin, ma soprattutto, per l’utilizzo dell’interfaccia Telegram, bisogna avere “l’account” registrato, qualora non lo si avesse, bisogna contattare gli sviluppatori per l’inserimento dei dati per permettere l’utilizzo di questa interfaccia.


<h3>JavaFX</h3>

Per eseguire l'applicazione bisogna inserire il file jar con la cartella "data" contenenti i file di salvataggio nella stessa cartella, aprire una finestra di terminale ed eseguire il segente comando:

```console
java --module-path [PATH LIBRERIA JAVA]/javafx-sdk-16/lib --add-modules javafx.controls,javafx.fxml -jar [PATH APPLICAZIONE]/PATonTS.jar
```

<h2>Relazione</h2>

Abbiamo creato due applicativi per la gestione delle proprie giornate: questo progetto permette di avere una visione chiara dei propri impegni/attività raggruppati per tipo, nella vita di tutti i giorni. L'applicazione java, sviluppata con javaFX, è l'applicativo principale: si ha una visione di insieme e si hanno tutte le funzionalità possibili. Le funzionalità variano a seconda del tipo di utente, semplice o admin. L'utente semplice può utilizzare tutte le feature necessarie alla corretta gestione della propria quotidianità: gestire i propri workspace, partecipare ad altri e gestire le proprie preferenze inerenti alle attività di ogni workspace. L'admin, in più, ha la possibilità di una gestione generale: creazione di workspace, attività e utenti, rimozione elementi e associazione utente workspace. Il bot di telegram, invece, sviluppato con java, semplifica l'applicazione: la conversazione tra bot e utente è gestita tramite bottoni per rendere più facile l'utilizzo di questo progetto. Questa scelta è stata fatta per rendere più user friendly un programma che potrebbe sembrare, di primo acchito, complicato. Anche per il bot vi è una divisione di funzionalità in base alla tipologia di utente e i due applicativi sono sincronizzati sullo stesso archivio.  
