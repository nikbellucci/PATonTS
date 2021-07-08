package com.proginternet.Telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.Properties;
import java.lang.Math;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.proginternet.utils.Auth;
import com.proginternet.utils.JsonParser;
import com.proginternet.models.Activity;
import com.proginternet.models.User;
import com.proginternet.models.Workspace;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {
    User us=null;
    User newUs=null;
    String actWSid;
    boolean usernameResp=false;
    boolean passwordResp=false;
    boolean phase=false;
    Activity act=null;
    Workspace work=null;
    String user="";
    int tries=0;
    String state="";
    @Override
    public String getBotUsername() {
        return "WorkSpaceBVbot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if (update.hasMessage() && update.getMessage().hasText()) {
            
            String msg = update.getMessage().getText();
            if (passwordResp) {
                passwordResp=false;
                try {
                    authenticationUsers(update.getMessage(), user);
                } catch (Exception e) {
                    errore(update.getMessage());
                }
            }
            else if (usernameResp) {user=msg; loginUser(update.getMessage(), "inviare la password");}

            if (state.startsWith("ACT")) {
                String param="";
                try {
                    param=setValueActivity(msg, state.substring(3), act, update.getMessage());
                } catch (IllegalAccessException e) {
                    errore(update.getMessage());
                }
                if(param.isEmpty()) {
                    Workspace wos = selectedObject(actWSid);
                    wos.addActivity(act);
                    try {
                        wsUpdateOnJson(wos.getActivities(), actWSid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    state="";
                    actWSid="";
                    act=new Activity();
                    PreLoginMenu(update.getMessage());
                }
                else addActivity(update.getMessage(), param);
            }
            else if (state.startsWith("WS")) {
                String param="";
                try {
                    param=setValueActivity(msg, state.substring(2), work, update.getMessage());
                } catch (IllegalAccessException e) {
                    errore(update.getMessage());
                }
                if(param.isEmpty()) {
                    try {
                        wsUpdateOnJson(work, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    state="";
                    work=new Workspace();
                    PreLoginMenu(update.getMessage());
                }
                else createWS(update.getMessage(), param);
            } else if (state.startsWith("US")) {
                String param="";
                try {
                    param=setValueActivity(msg, state.substring(2), newUs, update.getMessage());
                } catch (IllegalAccessException e) {
                    errore(update.getMessage());
                }
                if(param.isEmpty()) {
                    try {
                        newUs.setPassword(Auth.generateStorngPasswordHash(newUs.getPassword()));
                        updateOnJsonUserId(newUs, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    state="";
                    newUs=new User();
                    PreLoginMenu(update.getMessage());
                }
                else addUser(update.getMessage(), param);
            }

            if (msg.equals("/start")) PreLoginMenu(update.getMessage());
            System.out.println(update.getCallbackQuery().getData());

        }
        else if (update.hasCallbackQuery()) {
            String cbdata=update.getCallbackQuery().getData();
            switch (cbdata) {
                case "login":
                    loginUser(update.getCallbackQuery().getMessage(),"Invia username");
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
                    ViewWS(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "add":
                    Creazione(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "choiceW":
                    addWS(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;
                
                case "newActivity":
                    selectWS(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "newWs":
                    work=new Workspace();
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
                    newUs=new User();
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

                case "mainmenu":
                    phase = true;
                    PreLoginMenu(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;
                default:
                    if (cbdata.startsWith("WS")) {
                        WSDetails(update.getCallbackQuery().getMessage(), selectedObject(cbdata.substring(2)));
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("ADD")) {
                        us.addWS(cbdata.substring(3));
                        try {
                            updateOnJsonUserId(us.getWorkArray(), us.getUsername());
                        } 
                        catch (Exception e) {
                            System.out.println("Errore");
                        }
                        PreLoginMenu(update.getCallbackQuery().getMessage());
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    if (cbdata.startsWith("THIS")) {
                        actWSid=cbdata.substring(4);
                        act=new Activity();
                        addActivity(update.getCallbackQuery().getMessage(), "id");
                        deletePrevMessage(update.getCallbackQuery().getMessage());
                    }
                    break;
            }
        }
    }
    



    //menu -----------------------------------------------------------------------------------------------------
    public void PreLoginMenu(Message mess){
        
        long temp=mess.getChatId();
        us=alreadyLogged(temp);
        
        if (us!=null) {
            System.out.println(us.isAdmin());
            if(us.isAdmin()&&!phase) MenuAdmin(mess);
            else MainMenu(mess);
        }
        else {
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

    public void MainMenu(Message mess){
        phase=false;
        System.out.println("bang");
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Bentornato in WorkSpaceBV "+ us.getUsername()+ "\nCosa vuoi fare?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Visualizzazione workspasce").setCallbackData("viewW"));
        rowInline.add(new InlineKeyboardButton().setText("Scelta nuovi workspace").setCallbackData("choiceW"));
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

    public void MenuAdmin(Message mess){
        System.out.println("bang");
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Salve admin "+us.getName()+"!! " + "\nCosa vuole fare?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Creazione/Aggiunta").setCallbackData("add"));
        rowInline.add(new InlineKeyboardButton().setText("Modifica/Rimozione").setCallbackData("changes"));
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("Main Menu").setCallbackData("mainmenu"));
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


    //creazione -----------------------------------------------------------------------------------------------

    public void Creazione(Message mess){
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Cosa vuoi creare o aggiungere?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("WorkSpace").setCallbackData("newWs"));
        rowInline.add(new InlineKeyboardButton().setText("Attività").setCallbackData("newActivity"));
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("Preferenza").setCallbackData("newPref"));
        rowInline2.add(new InlineKeyboardButton().setText("Utente").setCallbackData("newUser"));
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
    
    public void createWS(Message mess, String param){
        
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Inserire "+ param);
        if(param.equals("expiration")) message.setText("Inserire data di scadenza nel formato aaaa-mm-gg");
        state="WS"+ param;
        try { // Sending our message object to user
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void selectWS(Message mess){
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("A quale workspace vuoi aggiungere l'attività?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson("data/Workspace.json", Workspace[].class);
        for (int i = 0; i < ws.size(); i++) {
            rowInline.add(new InlineKeyboardButton().setText(ws.get(i).getName()).setCallbackData("THIS" +  ws.get(i).getId()));
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

    public void addActivity(Message mess, String param){
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Inserire "+ param);
        state="ACT"+ param;
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void addUser(Message mess, String param){
        
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Inserire "+ param);
        if(param.equals("birthday")) message.setText("Inserire data di compleanno nel formato aaaa-mm-gg");
        if(param.equals("isAdmin")) message.setText("Inserire se l'utente è un admin(si/no)");
        state="US"+ param;
        try { // Sending our message object to user
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Visualizzazione workspace
    public void ViewWS(Message mess){
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Ecco i tuoi workspace. Di quale vuoi avere piu informazione?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        ArrayList<Workspace> ws = new ArrayList<Workspace>();
        ws = us.getWorkspace();
        for (int i = 0; i < ws.size(); i++) {
            rowInline.add(new InlineKeyboardButton().setText(ws.get(i).getName()).setCallbackData("WS" +  ws.get(i).getId()));
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

    public void WSDetails(Message mess, Workspace ws){
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Informazioni relative al workspace: "+ ws.getName()+
                     "\nDescrizione: "+ ws.getDescrizione()+
                     "\nScadenza: "+ ws.getExpiration());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        ArrayList<Activity> act = ws.getActivities();
        if(!(act==null||act.isEmpty())){
            for (int i = 0; i < act.size(); i++) {
                rowInline.add(new InlineKeyboardButton().setText(act.get(i).getName()).setCallbackData("ACT" +  act.get(i).getName()));
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

    //Aggiunta Workspace
    public void addWS(Message mess){
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Quale workspace vuoi aggiungere?");
        ArrayList<Workspace> ws = newWS();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        if(ws.size()==0){ message.setText("Partecipi a tutti i workspace!!");}
        else{
            for (int i = 0; i < ws.size(); i++) {
                rowInline.add(new InlineKeyboardButton().setText(ws.get(i).getName()).setCallbackData("ADD" +  ws.get(i).getId()));
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



//Info------------------------------------------------------------------------------------------

    public void infoBot(Message mess){
        EditMessageText edit = new EditMessageText()
            .setChatId(mess.getChatId())
            .setMessageId(mess.getMessageId())
            .setText("Benvenuto in WorkSpaceBV!! \nOrganizzarsi per non perdersi nulla \n");
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

//Registrazione---------------------------------------------------------------------------------

    public void signUp(Message mess){
        EditMessageText edit = new EditMessageText()
            .setChatId(mess.getChatId())
            .setMessageId(mess.getMessageId())
            .setText("Benvenuto in WorkSpaceBV!! \n"+
                    "Per registrarsi la invitiamo a contattare gli amministratori: \n"+
                    "niccolo.bellucci2@studio.unibo.it\n"+
                    "lorenzo.vincini@studio.unibo.it");
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

//Gestione login---------------------------------------------------------------------------------

    public void loginUser(Message message, String text){
        if(usernameResp) passwordResp=true;
        usernameResp=!usernameResp;
        SendMessage Mess = new SendMessage()
            .setChatId(message.getChatId())
            .setText(text);
            try {
                execute(Mess); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    public void authenticationUsers(Message mess, String Us) throws Exception{
        passwordResp=false;
        User user=User.checkUsername(Us);
        if (user == null) loginUser(mess, "Username o password non corretti \nInviare l'username");
        else if(Auth.validatePassword(mess.getText(), user.getPassword())){
            user.setChat(mess.getChatId());
            updateOnJsonUserId(mess.getChatId(), user.getUsername());
            us=user;
            MainMenu(mess);
        }
        else {
            tries++;
            if (tries<3) loginUser(mess, "Username o password non corretti \nInviare l'username");
            //else errore password sbagliata
        }
    }

    public User alreadyLogged(long id){
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



    //utils-----------------------------------------------------------------------------------------------------
    public void deletePrevMessage(Message mess){
        DeleteMessage del = new DeleteMessage()
            .setChatId(mess.getChatId())
            .setMessageId(mess.getMessageId());
        try {
            execute(del); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        
    }

    public <T> void updateOnJsonUserId(T newParam, String un) throws Exception{
        JsonParser<User> parser = new JsonParser<User>();
        ArrayList<User> users = parser.readOnJson("data/Users.json", User[].class);

        if(newParam instanceof User){
            users.add((User)newParam);
            parser.writeOnJson("data/Users.json", users);
            return;
        }

        for (User user : users) {
            if (user.getUsername().equals(un)) {
                if (newParam instanceof Long) {
                    Long x=(Long)newParam;
                    user.setChat(x);
                    break;
                }
                else if (newParam instanceof ArrayList<?>) {
                    ArrayList<String> x = (ArrayList<String>) newParam;
                    user.setWorkArray(x);
                    break;
                }
                
            }
        }
        parser.writeOnJson("data/Users.json", users);
    }

    public Workspace selectedObject(String id){
        String filename = "data/Workspace.json";

        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson(filename, Workspace[].class);
        Workspace result = null;
        for (Workspace works : ws) {
            if (works.getId().equals(id)) {
                result=works;
                break;
            }
        }
        return result;
    }

    public ArrayList<Workspace> newWS(){
        String filename = "data/Workspace.json";

        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson(filename, Workspace[].class);
        ArrayList<Workspace> usws=us.getWorkspace();


        for (int i = 0; i < usws.size(); i++) {
            for (int j = 0; j < ws.size(); j++) {
                
                if(ws.get(j).getId().equals(usws.get(i).getId())) ws.remove(j);
            }
        }
        return ws;
    }

    public <T> String setValueActivity(String mess, String param, T obj, Message mex) throws IllegalAccessException {
        Class fileEntityClass = (Class) obj.getClass();
	    /* * Get all the attribute collections in the class */
        Field[] fs = fileEntityClass.getDeclaredFields();
        for(int i = 0;i<fs.length-1;i++){
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
                    Pattern p = Pattern.compile(
                        "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$" 
                        + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                        + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$" 
                        + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");
                    Matcher m = p.matcher(mess);
                    if(m.matches()) {
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
                    if(mess.equalsIgnoreCase("si")||mess.equalsIgnoreCase("no")){
                        if (mess.equalsIgnoreCase("si")) {
                            System.out.println(f.getType() + "\t is int");
	    	                f.set(obj, true); // set the value of the property
                        }
                        else f.set(obj, false);
                    }else {
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
                }
                else System.out.println(f.getType() + "\t");
                if(i<fs.length-2) return fs[i+1].getName();
            }

	    }
        return "";
    }

    public void errore(Message mess){
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

    public <T> void wsUpdateOnJson(T newParam, String un) throws Exception{
        JsonParser<Workspace> parser = new JsonParser<Workspace>();
        ArrayList<Workspace> ws = parser.readOnJson("data/Workspace.json", Workspace[].class);
        if(newParam instanceof Workspace) {
            ws.add((Workspace)newParam);
            parser.writeOnJson("data/Workspace.json", ws);
            return;
        }
        for (Workspace w : ws) {
            if (w.getId().equals(un)) {
                // if (newParam instanceof Long) {
                //     Long x=(Long)newParam;
                //     user.setChat(x);
                //     break;
                // }
                if (newParam instanceof ArrayList<?>) {
                    ArrayList<Activity> x = (ArrayList<Activity>) newParam;
                    w.setActivities(x);
                    break;
                }
                
                
            }
        }
        parser.writeOnJson("data/Workspace.json", ws);
    }

    @Override
    public String getBotToken() {
        return "1854883561:AAEyVfdWROH-FpHrUX3vrm5kHq4UcnlYc_0";
    }
    

}

