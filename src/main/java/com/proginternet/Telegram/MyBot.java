package com.proginternet.Telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.lang.reflect.Field;
import java.time.LocalDate;

import com.proginternet.utils.Auth;
import com.proginternet.utils.JsonParser;
import com.proginternet.models.Activity;
import com.proginternet.models.Preference;
import com.proginternet.models.User;
import com.proginternet.models.Workspace;
import com.proginternet.models.Association;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//Telegram bot, sviluppato per il progetto di programmazione
//la scelta di usare un unico file è stata obbligata poichè
//la maggior parte dei metodi utilizza SendMessage(metodo per l'invio di messaggi)
//e questo richiede che la classe estenda TelegramLongPollingBot 

public class MyBot extends TelegramLongPollingBot {
    User us = null;
    User newUs = null;
    String actWSid;
    String prefACTid;
    boolean usernameResp = false;
    boolean passwordResp = false;
    boolean phase = false;
    Activity act = null;
    Workspace work = null;
    Preference pref = null;
    String user = "";
    int tries = 0;
    String state = "";
    ArrayList<String> value;

    @Override
    public String getBotUsername() {
        return "WorkSpaceBVbot";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String msg = update.getMessage().getText();
            if (passwordResp) {                                                 //gestione login
                passwordResp = false;
                try {
                    authenticationUsers(update.getMessage(), user);
                } catch (Exception e) {
                    errore(update.getMessage(), "Si è verificato un errore");
                }
            } else if (usernameResp) {
                user = msg;
                loginUser(update.getMessage(), "inviare la password");
            }

            if (state.startsWith("ACT")) {                  // Creazione e riempimento valori activity e aggiornamento file
                String param = "";
                try {
                    param = setValueActivity(msg, state.substring(3), act, update.getMessage(), 1);
                } catch (IllegalAccessException e) {
                    errore(update.getMessage(), "Si è verificato un errore");
                }
                if (param.equals("name") && checkID(act.getId(),act)) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(update.getMessage().getChatId()).setText("Id già utilizzato");
                    
                    param="id";
                    try { // Sending our message object to user
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (param.isEmpty()) {
                    Workspace wos = selectedObject(actWSid);
                    wos.addActivity(act);
                    try {
                        wsUpdateOnJson(wos.getActivities(), actWSid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    state = "";
                    actWSid = "";
                    act = new Activity();
                    PreLoginMenu(update.getMessage());
                } else
                    addActivity(update.getMessage(), param);
            } else if (state.startsWith("WS")) {                // Creazione e riempimento valori workspace e aggiornamento file
                String param = "";
                try {
                    param = setValueActivity(msg, state.substring(2), work, update.getMessage(), 1);
                } catch (IllegalAccessException e) {
                    errore(update.getMessage(), "Si è verificato un errore");
                }
                if (param.equals("name") && checkID(work.getId(),work)) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(update.getMessage().getChatId()).setText("Id già utilizzato");
                    
                    param="id";
                    try { // Sending our message object to user
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (param.isEmpty()) {
                    try {
                        wsUpdateOnJson(work, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    state = "";
                    work = new Workspace();
                    PreLoginMenu(update.getMessage());
                } else
                    createWS(update.getMessage(), param);
            } else if (state.startsWith("US")) {                // Creazione e riempimento valori utente e aggiornamento file
                String param = "";
                try {
                    param = setValueActivity(msg, state.substring(2), newUs, update.getMessage(), 2);

                } catch (IllegalAccessException e) {
                    errore(update.getMessage(), "Si è verificato un errore");
                }
                if (param.equals("password") && User.checkUsername(newUs.getUsername()) != null) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(update.getMessage().getChatId()).setText("Username già presente");
                    
                    param="username";
                    try { // Sending our message object to user
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (param.isEmpty()) {
                    try {
                        newUs.setPassword(Auth.generateStorngPasswordHash(newUs.getPassword()));
                        updateOnJsonUserId(newUs, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    state = "";
                    newUs = new User();
                    PreLoginMenu(update.getMessage());
                } else
                    addUser(update.getMessage(), param);
            } else if (state.startsWith("PREF")) {          // Creazione e riempimento valori preferenze e aggiornamento file
                String param = "";
                try {
                    param = setValueActivity(msg, state.substring(4), pref, update.getMessage(), 0);
                } catch (IllegalAccessException e) {
                    errore(update.getMessage(), "Si è verificato un errore");
                }
                if (param.equals("name") && checkID(pref.getId(),pref)) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(update.getMessage().getChatId()).setText("Id già utilizzato");
                    
                    param="id";
                    try { // Sending our message object to user
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (param.isEmpty()) {
                    Workspace wos = selectedObject(actWSid);
                    Activity as = selectedActivity(actWSid, prefACTid);
                    as.addPref(pref);
                    wos.updateActivities(as);
                    try {
                        wsUpdateOnJson(wos.getActivities(), actWSid); // change
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    state = "";
                    actWSid = "";
                    pref = new Preference();
                    value = new ArrayList<String>();
                    PreLoginMenu(update.getMessage());
                } else
                    addPrefe(update.getMessage(), param);
            } else if (state.startsWith("CHOICE")) {            //gestione decisione dell'utente sulla preferenza 
                Association as = new Association(us.getUsername(), actWSid, prefACTid, pref.getId(), msg);
                addAssociation(as);
                PreLoginMenu(update.getMessage());
            } else if (state.equals("RUS")) {
                if (!cancUser(msg)) {
                    errore(update.getMessage(), "Utente non esistente");
                } else
                    PreLoginMenu(update.getCallbackQuery().getMessage());
            }

            if (msg.equals("/start"))
                PreLoginMenu(update.getMessage());
            System.out.println(update.getCallbackQuery().getData());

        } else if (update.hasCallbackQuery()) {
            String cbdata = update.getCallbackQuery().getData();
            switch (cbdata) {
                case "login":
                    loginUser(update.getCallbackQuery().getMessage(), "Invia username");
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "registrazione":
                    signUp(update.getCallbackQuery().getMessage());
                    break;

                case "info":
                    infoBot(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "menu":
                    PreLoginMenu(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "viewW":
                    ViewWS(update.getCallbackQuery().getMessage());                 //visualizzazione workspace
                    deletePrevMessage(update.getCallbackQuery().getMessage());  
                    break;

                case "VWCH":                                                        //visualizzazione decisione sulla preferenza
                    viewChoice(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "CHPR":                                                        //decisione preferenza
                    userPreference(update.getCallbackQuery().getMessage());
                    break;

                case "add":
                    Creazione(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "choiceW":                                                     //associazione utente workspace
                    addWS(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "newActivity":
                    selectWS(update.getCallbackQuery().getMessage(), "THIS");
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "newPref":
                    selectWS(update.getCallbackQuery().getMessage(), "THAT");
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "newWs":
                    work = new Workspace();
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setText("Compilare i dati per la creazione di un WS");
                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    createWS(update.getCallbackQuery().getMessage(), "id");
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "newUser":
                    newUs = new User();
                    SendMessage messag = new SendMessage() // Create a message object object
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setText("Compilare i dati per la creazione di un WS");
                    try {
                        execute(messag); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    addUser(update.getCallbackQuery().getMessage(), "name");
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "remove":
                    Rimozione(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "remWs":
                    selectWS(update.getCallbackQuery().getMessage(), "RWS");
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "remActivity":
                    selectWS(update.getCallbackQuery().getMessage(), "RAC");
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "remPref":
                    selectWS(update.getCallbackQuery().getMessage(), "RPF");
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "remUser":
                    removeUS(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "mainmenu":
                    phase = true;
                    PreLoginMenu(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;
                default:
                    if (cbdata.startsWith("WS")) {                                          //gestione visualizzazione workspace, attività e preferenze
                        Workspace w = selectedObject(cbdata.substring(2));
                        if (w != null) {
                            WSDetails(update.getCallbackQuery().getMessage(), w);
                            deletePrevMessage(update.getCallbackQuery().getMessage());
                        } else
                            errore(update.getMessage(), "Si è verificato un errore");
                    }
                    if (cbdata.startsWith("ACT")) {
                        Activity a = selectedActivity(actWSid, cbdata.substring(3));
                        if (a != null) {
                            ACTDetails(update.getCallbackQuery().getMessage(), a);
                            deletePrevMessage(update.getCallbackQuery().getMessage());
                        } else
                            errore(update.getMessage(), "Si è verificato un errore");
                    }
                    if (cbdata.startsWith("PREF")) {
                        Preference p = selectedPref(actWSid, prefACTid, cbdata.substring(4));
                        if (p != null) {
                            PREFDetails(update.getCallbackQuery().getMessage(), p);
                            deletePrevMessage(update.getCallbackQuery().getMessage());
                        } else
                            errore(update.getMessage(), "Si è verificato un errore");
                    }
                    if (cbdata.startsWith("ADD")) {
                        us.addWS(cbdata.substring(3));
                        try {
                            updateOnJsonUserId(us.getWorkArray(), us.getUsername());
                        } catch (Exception e) {
                            System.out.println("Errore");
                        }
                        PreLoginMenu(update.getCallbackQuery().getMessage());
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("RWS")) {                                                 //gestione cancellazione workspace, attività e preferenze
                        removeWS(update.getCallbackQuery().getMessage());
                        PreLoginMenu(update.getCallbackQuery().getMessage());
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("RAC")) {
                        actWSid = cbdata.substring(3);
                        selectACT(update.getCallbackQuery().getMessage(), actWSid, "CAC");
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("CAC")) {
                        prefACTid = cbdata.substring(3);
                        removeACT(update.getCallbackQuery().getMessage());
                        PreLoginMenu(update.getCallbackQuery().getMessage());
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("RPF")) {
                        actWSid = cbdata.substring(3);
                        selectACT(update.getCallbackQuery().getMessage(), actWSid, "CAC");
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("CPF")) {
                        prefACTid = cbdata.substring(3);
                        selectPref(update.getCallbackQuery().getMessage(), actWSid, prefACTid);
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("RPRE")) {
                        removePref(update.getCallbackQuery().getMessage(), cbdata.substring(4));
                        PreLoginMenu(update.getCallbackQuery().getMessage());
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("THIS")) {
                        actWSid = cbdata.substring(4);
                        act = new Activity();
                        addActivity(update.getCallbackQuery().getMessage(), "id");
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("THAT")) {
                        actWSid = cbdata.substring(4);
                        selectACT(update.getCallbackQuery().getMessage(), actWSid, "THUT");
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("THUT")) {
                        prefACTid = cbdata.substring(4);
                        pref = new Preference();
                        value = new ArrayList<String>();
                        addPrefe(update.getCallbackQuery().getMessage(), "id");
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    break;
            }
        }
    }

    // menu
    // -----------------------------------------------------------------------------------------------------
    public void PreLoginMenu(Message mess) {
        clean();
        long temp = mess.getChatId();
        us = alreadyLogged(temp);

        if (us != null) {
            System.out.println(us.isAdmin());
            if (us.isAdmin() && !phase)
                MenuAdmin(mess);
            else
                MainMenu(mess);
        } else {
            SendMessage message = new SendMessage() // Create a message object object
                    .setChatId(mess.getChatId())
                    .setText("Benvenuto in WorkSpaceBV!! \nOrganizzarsi per non perdersi nulla \n");
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            List<InlineKeyboardButton> rowinfo = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText("Login").setCallbackData("login"));
            rowinfo.add(new InlineKeyboardButton().setText("Registrazione").setCallbackData("registrazione"));
            rowinfo.add(new InlineKeyboardButton().setText("Info").setCallbackData("info"));
            // Set the keyboard to the markup
            rowsInline.add(rowInline);
            rowsInline.add(rowinfo);
            // Add it to the message
            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void MainMenu(Message mess) {
        phase = false;
        System.out.println("bang");
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId())
                .setText("Bentornato in WorkSpaceBV " + us.getUsername() + "\nCosa vuoi fare?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Visualizzazione workspasce").setCallbackData("viewW"));
        rowInline.add(new InlineKeyboardButton().setText("Scelta nuovi workspace").setCallbackData("registrazione"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void MenuAdmin(Message mess) {
        System.out.println("bang");
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Salve admin " + us.getName() + "!! " + "\nCosa vuole fare?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Aggiunta").setCallbackData("add"));
        rowInline.add(new InlineKeyboardButton().setText("Rimozione").setCallbackData("remove"));
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("Associazione utente attività").setCallbackData("choiceW"));
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(new InlineKeyboardButton().setText("Main Menu").setCallbackData("mainmenu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // creazione
    // -----------------------------------------------------------------------------------------------

    public void Creazione(Message mess) {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Cosa vuoi creare o aggiungere?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("WorkSpace").setCallbackData("newWs"));
        rowInline.add(new InlineKeyboardButton().setText("Attività").setCallbackData("newActivity"));
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("Preferenza").setCallbackData("newPref"));
        rowInline2.add(new InlineKeyboardButton().setText("Utente").setCallbackData("newUser"));
        rowInline3.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void selectWS(Message mess, String selector) {                           //metodo per la selezione del ws
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Seleziona il workspace.");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson("data/Workspace.json", Workspace[].class);
        for (int i = 0; i < ws.size(); i++) {
            rowInline.add(new InlineKeyboardButton().setText(ws.get(i).getName())
                    .setCallbackData(selector + ws.get(i).getId()));
        }
        rowInline2.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void selectACT(Message mess, String id, String selector) {               //metodo per la selezione dell'attività
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Seleziona l'attività");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        ArrayList<Activity> acti = selectedObject(id).getActivities();

        for (int i = 0; i < acti.size(); i++) {
            rowInline.add(new InlineKeyboardButton().setText(acti.get(i).getName())
                    .setCallbackData(selector + acti.get(i).getId()));
        }
        rowInline2.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void selectPref(Message mess, String idws, String idac) {                //metodo per la selezione della preferenza
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Seleziona preferenza");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        ArrayList<Preference> pre = selectedActivity(idws, idac).getPref();

        for (int i = 0; i < pre.size(); i++) {
            rowInline.add(new InlineKeyboardButton().setText(pre.get(i).getName())
                    .setCallbackData("RPRE" + pre.get(i).getId()));
        }
        rowInline2.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void createWS(Message mess, String param) {                              //metodo per la richiesta di compilazione ws

        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Inserire " + param);
        if (param.equals("expiration"))
            message.setText("Inserire data di scadenza nel formato aaaa-mm-gg");
        state = "WS" + param;
        try { // Sending our message object to user
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void addActivity(Message mess, String param) {                           //metodo per la richiesta di compilazione attività
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Inserire " + param);
        state = "ACT" + param;
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void addPrefe(Message mess, String param) {                              //metodo per la richiesta di compilazione preferenza
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Inserire " + param);
        if (param.equals("valueSelection"))
            message.setText("Inserire i valori delle possibili preferenze (scrivere fine per terminare l'inserzione)");
        state = "PREF" + param;
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void addUser(Message mess, String param) {                               //metodo per la richiesta di compilazione user

        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Inserire " + param);
        if (param.equals("birthday"))
            message.setText("Inserire data di compleanno nel formato aaaa-mm-gg");
        if (param.equals("isAdmin"))
            message.setText("Inserire se l'utente è un admin(si/no)");
        state = "US" + param;
        try { // Sending our message object to user
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Rimozione elementi

    public void Rimozione(Message mess) {                                           //rimozione elementi
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Cosa vuoi rimovuere?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("WorkSpace").setCallbackData("remWs"));
        rowInline.add(new InlineKeyboardButton().setText("Attività").setCallbackData("remActivity"));
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("Preferenza").setCallbackData("remPref"));
        rowInline2.add(new InlineKeyboardButton().setText("Utente").setCallbackData("remUser"));
        rowInline3.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void removeWS(Message mess) {
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> works = parser.readOnJson("data/Workspace.json", Workspace[].class);

        for (int i = 0; i < works.size(); i++) {
            if (works.get(i).getId().equals(actWSid)) {
                works.remove(i);
                break;
            }
        }
        parser.writeOnJson("data/Workspace.json", works);
    }

    public void removeACT(Message mess) {
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> works = parser.readOnJson("data/Workspace.json", Workspace[].class);

        for (int i = 0; i < works.size(); i++) {
            if (works.get(i).getId().equals(actWSid)) {
                for (int j = 0; j < works.get(i).getActivities().size(); j++) {
                    if (works.get(i).getActivities().get(i).getId().equals(prefACTid)) {
                        works.get(i).getActivities().remove(j);
                        break;
                    }
                }
            }
        }
        parser.writeOnJson("data/Workspace.json", works);
    }

    public void removePref(Message mess, String idPref) {
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> works = parser.readOnJson("data/Workspace.json", Workspace[].class);

        for (int i = 0; i < works.size(); i++) {
            if (works.get(i).getId().equals(actWSid)) {
                for (int j = 0; j < works.get(i).getActivities().size(); j++) {
                    if (works.get(i).getActivities().get(j).getId().equals(prefACTid)) {
                        for (int j2 = 0; j2 < works.get(i).getActivities().get(j).getPref().size(); j2++) {
                            if (works.get(i).getActivities().get(j).getPref().get(j2).getId().equals(idPref)) {
                                works.get(i).getActivities().get(j).getPref().remove(j2);
                            }
                        }
                    }
                }
            }
        }
        parser.writeOnJson("data/Workspace.json", works);
    }

    public void removeUS(Message mess) {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Inserire l'username dell'utente da rimuovere");
        state = "RUS";
        try { // Sending our message object to user
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Visualizzazione workspace
    public void ViewWS(Message mess) {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Ecco i tuoi workspace. Di quale vuoi avere piu informazione?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        ArrayList<Workspace> ws = new ArrayList<Workspace>();
        ws = us.getWorkspace();
        for (int i = 0; i < ws.size(); i++) {
            rowInline.add(
                    new InlineKeyboardButton().setText(ws.get(i).getName()).setCallbackData("WS" + ws.get(i).getId()));
        }
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void WSDetails(Message mess, Workspace ws) {                             //dettagli ws
        actWSid = ws.getId();
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Informazioni relative al workspace: " + ws.getName()
                        + "\nDescrizione: " + ws.getDescrizione() + "\nScadenza: " + ws.getExpiration());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        ArrayList<Activity> act = ws.getActivities();
        if (!(act == null || act.isEmpty())) {
            for (int i = 0; i < act.size(); i++) {
                rowInline.add(new InlineKeyboardButton().setText("Activity " + (i + 1) + ": " + act.get(i).getName())
                        .setCallbackData("ACT" + act.get(i).getId()));
            }
            rowsInline.add(rowInline);
        }
        rowInline2.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void ACTDetails(Message mess, Activity act) {                            //dettagli attività
        prefACTid = act.getId();
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId())
                .setText("Informazioni relative all'attività: " + act.getName() + "\nDescrizione: "
                        + act.getDescrizione() + "\nSi svolgerà il: " + act.getWhen() + ",\nnei pressi di : "
                        + act.getWhere());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        ArrayList<Preference> pref = act.getPref();
        if (!(pref == null || pref.isEmpty())) {
            for (int i = 0; i < pref.size(); i++) {
                rowInline.add(new InlineKeyboardButton().setText("Preferenza " + (i + 1) + ": " + pref.get(i).getName())
                        .setCallbackData("PREF" + pref.get(i).getId()));
            }
            rowsInline.add(rowInline);
        }
        rowInline2.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void PREFDetails(Message mess, Preference pre) {                         //dettagli preferenza
        pref = pre;
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Informazioni relative alla preferenza: " + pre.getName()
                        + "\nDescrizione: " + pre.getDescrizione());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Visualizza scelta").setCallbackData("VWCH"));
        rowInline.add(new InlineKeyboardButton().setText("Modifica scelta").setCallbackData("CHPR"));
        rowsInline.add(rowInline);
        rowInline2.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void viewChoice(Message mess) {                                          //visualizzazione preferenza
        String s = prefChoice(actWSid, prefACTid);
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("La preferenza da lei inserita è: \n" + s);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        if (s == null) {
            message.setText("Non hai inserito alcuna preferenza!!");
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText("Inserisci la tua preferenza").setCallbackData("CHPR"));
            rowsInline.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup

        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void userPreference(Message m) {                                         //decisione preferenza
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(m.getChatId()).setText("Inserire come preferisce svolgere l'attività:");

        state = "CHOICE";
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Associazione Workspace
    public void addWS(Message mess) {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(mess.getChatId()).setText("Quale workspace vuoi aggiungere?");
        ArrayList<Workspace> ws = newWS();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        if (ws.size() == 0) {
            message.setText("Partecipi a tutti i workspace!!");
        } else {
            for (int i = 0; i < ws.size(); i++) {
                rowInline.add(new InlineKeyboardButton().setText(ws.get(i).getName())
                        .setCallbackData("ADD" + ws.get(i).getId()));
            }
            rowsInline.add(rowInline);
        }

        rowInline2.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        // Set the keyboard to the markup

        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Info------------------------------------------------------------------------------------------

    public void infoBot(Message mess) {
        EditMessageText edit = new EditMessageText().setChatId(mess.getChatId()).setMessageId(mess.getMessageId())
                .setText("Benvenuto in WorkSpaceBV!! \nOrganizzarsi per non perdersi nulla \n"+
                         "WorkSpaceBV è il bot che ti permette di gestire tutti i tuoi impegni, poichè raccoglie tutti le tue"+
                         "aree di lavoro o di interesse, gestite in attività, riguardo alle quali è possibile esprimere preferenze");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        edit.setReplyMarkup(markupInline);
        try {
            execute(edit); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Registrazione---------------------------------------------------------------------------------

    public void signUp(Message mess) {
        EditMessageText edit = new EditMessageText().setChatId(mess.getChatId()).setMessageId(mess.getMessageId())
                .setText("Benvenuto in WorkSpaceBV!! \n"
                        + "Per registrarsi o per essere inseriti nei workspace, la invitiamo a contattare gli amministratori: \n"
                        + "niccolo.bellucci2@studio.unibo.it\n" + "lorenzo.vincini@studio.unibo.it");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Torna al menu").setCallbackData("menu"));
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        edit.setReplyMarkup(markupInline);
        try {
            execute(edit); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Gestione login---------------------------------------------------------------------------------

    public void loginUser(Message message, String text) {
        if (usernameResp)
            passwordResp = true;
        usernameResp = !usernameResp;
        SendMessage Mess = new SendMessage().setChatId(message.getChatId()).setText(text);
        try {
            execute(Mess); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void authenticationUsers(Message mess, String Us) throws Exception {
        passwordResp = false;
        User user = User.checkUsername(Us);
        if (user == null)
            loginUser(mess, "Username o password non corretti \nInviare l'username");
        else if (Auth.validatePassword(mess.getText(), user.getPassword())) {
            user.setChat(mess.getChatId());
            updateOnJsonUserId(mess.getChatId(), user.getUsername());
            us = user;
            MainMenu(mess);
        } else {
            tries++;
            if (tries < 3)
                loginUser(mess, "Username o password non corretti \nInviare l'username");
            // else errore password sbagliata
        }
    }

    public User alreadyLogged(long id) {
        String filename = "data/Users.json";

        JsonParser<User> parser = new JsonParser<User>();
        ArrayList<User> users = parser.readOnJson(filename, User[].class);

        for (User user : users) {
            if (user.getChat().equals(id)) {
                System.out.println("User exist: " + user.getName());
                return user;
            }
        }
        return null;
    }

    // utils-----------------------------------------------------------------------------------------------------
    public void deletePrevMessage(Message mess) {                                   //cancellazione messaggio precedente   
        DeleteMessage del = new DeleteMessage().setChatId(mess.getChatId()).setMessageId(mess.getMessageId());
        try {
            execute(del); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public <T> void updateOnJsonUserId(T newParam, String un) throws Exception {    //update del file json degli utenti
        JsonParser<User> parser = new JsonParser<User>();
        ArrayList<User> users = parser.readOnJson("data/Users.json", User[].class);

        if (newParam instanceof User) {
            users.add((User) newParam);
            parser.writeOnJson("data/Users.json", users);
            return;
        }

        for (User user : users) {
            if (user.getUsername().equals(un)) {
                if (newParam instanceof Long) {
                    Long x = (Long) newParam;
                    user.setChat(x);
                    break;
                } else if (newParam instanceof ArrayList<?>) {
                    ArrayList<String> x = (ArrayList<String>) newParam;
                    user.setWorkArray(x);
                    break;
                }

            }
        }
        parser.writeOnJson("data/Users.json", users);
    }

    public Workspace selectedObject(String id) {                                    //selezione di un workspace dal file json
        String filename = "data/Workspace.json";

        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson(filename, Workspace[].class);
        Workspace result = new Workspace();
        for (Workspace works : ws) {
            if (works.getId().equals(id)) {
                result = works;
                break;
            }
        }
        return result;
    }

    public Activity selectedActivity(String idWS, String idACT) {                   //selezione di un attività dal file json

        String filename = "data/Workspace.json";

        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson(filename, Workspace[].class);
        Activity result = new Activity();
        for (Workspace works : ws) {
            if (works.getId().equals(idWS)) {
                for (Activity a : works.getActivities()) {
                    if (a.getId().equals(idACT)) {
                        result = a;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Preference selectedPref(String idWS, String idACT, String idPref) {      //selezione di una preferenza dal file json
        String filename = "data/Workspace.json";

        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson(filename, Workspace[].class);
        Preference result = new Preference();
        for (Workspace works : ws) {
            if (works.getId().equals(idWS)) {
                for (Activity a : works.getActivities()) {
                    if (a.getId().equals(idACT)) {
                        for (Preference p : a.getPref()) {
                            if (p.getId().equals(idPref)) {
                                result = p;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return result;
    }

    public String prefChoice(String idWS, String idACT) {                           //selezione di una decisione sulla preferenza dal file json
        String filename = "data/Associations.json";

        JsonParser<Association> parser = new JsonParser<Association>();
        ArrayList<Association> as = parser.readOnJson(filename, Association[].class);
        for (Association a : as) {
            if (a.getUser().equals(us.getUsername()) && a.getWorkspaceId().equals(idWS)
                    && a.getActivityId().equals(idACT) && a.getPreferenceId().equals(pref.getId())) {
                return a.getPreferenceAnswer();
            }
        }
        return null;
    }

    public void addAssociation(Association as) {                                    //inserimento decisione sul file json
        JsonParser<Association> parser = new JsonParser<Association>();
        ArrayList<Association> assoc = parser.readOnJson("data/Associations.json", Association[].class);
        assoc.add(as);
        parser.writeOnJson("data/Associations.json", assoc);
    }

    public ArrayList<Workspace> newWS() {                                           //metodo che ritorna i ws non associati ad un utente
        String filename = "data/Workspace.json";

        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson(filename, Workspace[].class);
        ArrayList<Workspace> usws = us.getWorkspace();

        for (int i = 0; i < usws.size(); i++) {
            for (int j = 0; j < ws.size(); j++) {

                if (ws.get(j).getId().equals(usws.get(i).getId()))
                    ws.remove(j);
            }
        }
        return ws;
    }

    public <T> String setValueActivity(String mess, String param, T obj, Message mex, int skip)     //metodo che si basa sulla reflection per creazione dei vari oggetti
            throws IllegalAccessException {
        Class fileEntityClass = (Class) obj.getClass();
        /* * Get all the attribute collections in the class */
        Field[] fs = fileEntityClass.getDeclaredFields();
        for (int i = 0; i < fs.length - skip; i++) {
            Field f = fs[i];
            if (f.getName().equals(param)) {
                f.setAccessible(true); // Set some properties to be accessible
                Object val = f.get(obj);// Get the value of this property
                System.out.println("name:" + f.getName() + "\t value = " + val);
                String type = f.getType().toString();// Get the type of this attribute
                if (type.endsWith("String")) {
                    System.out.println(f.getType() + "\t is String");
                    f.set(obj, mess); // set the value of the property
                } else if (type.endsWith("int") || type.endsWith("Integer")) {
                    System.out.println(f.getType() + "\t is int");
                    f.set(obj, Integer.parseInt(mess)); // set the value of the property
                } else if (type.endsWith("LocalDate")) {
                    Pattern p = Pattern.compile("^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                            + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                            + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                            + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");
                    Matcher m = p.matcher(mess);
                    if (m.matches()) {
                        System.out.println(f.getType() + "\t is int");
                        LocalDate ld = LocalDate.parse(mess);
                        f.set(obj, ld); // set the value of the property
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(mex.getChatId());
                        sendMessage.setText("Valore inserito nel formato sbagliato");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException x) {
                            System.err.println(x.getMessage());
                        }
                        return f.getName();
                    }
                } else if (type.endsWith("boolean")) {
                    if (mess.equalsIgnoreCase("si") || mess.equalsIgnoreCase("no")) {
                        if (mess.equalsIgnoreCase("si")) {
                            System.out.println(f.getType() + "\t is int");
                            f.set(obj, true); // set the value of the property
                        } else
                            f.set(obj, false);
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(mex.getChatId());
                        sendMessage.setText("Valore inserito nel formato sbagliato");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException x) {
                            System.err.println(x.getMessage());
                        }
                        return f.getName();
                    }
                } else if (type.endsWith("ArrayList")) {
                    if (mess.equalsIgnoreCase("fine")) {
                        f.set(obj, value);
                    } else {
                        value.add(mess);
                        return f.getName();
                    }
                } else
                    System.out.println(f.getType() + "\t");
                if (i < fs.length - (skip + 1))
                    return fs[i + 1].getName();
            }

        }
        return "";
    }

    public void errore(Message mess, String text) {                                 //metodo per la gestione degli errori
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(mess.getChatId());
        sendMessage.setText("Si è verificato un errore");
        try {
            execute(sendMessage);
        } catch (TelegramApiException x) {
            System.err.println(x.getMessage());
        }
        PreLoginMenu(mess);
    }

    public <T> void wsUpdateOnJson(T newParam, String un) throws Exception {        //update del file json sui workspace
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson("data/Workspace.json", Workspace[].class);
        if (newParam instanceof Workspace) {
            ws.add((Workspace) newParam);
            parser.writeOnJson("data/Workspace.json", ws);
            return;
        }
        for (Workspace w : ws) {
            if (w.getId().equals(un)) {
                if (newParam instanceof ArrayList<?>) {
                    ArrayList<Activity> x = (ArrayList<Activity>) newParam;
                    w.setActivities(x);
                    break;
                }

            }
        }
        parser.writeOnJson("data/Workspace.json", ws);
    }

    public boolean cancUser(String user) {                                          //metodo per la cancellazione degli utenti
        JsonParser<User> parser = new JsonParser<User>();
        ArrayList<User> users = parser.readOnJson("data/Users.json", User[].class);

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user)) {
                users.remove(i);
                parser.writeOnJson("data/Users.json", users);
                return true;
            }
        }
        return false;
    }

    public void clean() {                                                           //metodo per la pulizia delle variabili globali
        newUs = null;
        actWSid = "";
        prefACTid = "";
        usernameResp = false;
        passwordResp = false;
        act = null;
        work = null;
        pref = null;
        user = "";
        tries = 0;
        state = "";
        value = new ArrayList<String>();
    }

    public <T> boolean checkID(String id, T d){
        
        if(d instanceof Workspace){
            return selectedObject(id).getId().equals(id);
        }else if(d instanceof Activity){
            return selectedActivity(actWSid ,id).getId().equals(id);
        }else{
            return selectedPref(actWSid, prefACTid, id).getId().equals(id);
        }

    }
    

    @Override
    public String getBotToken() {
        return "1854883561:AAEyVfdWROH-FpHrUX3vrm5kHq4UcnlYc_0";
    }

}
