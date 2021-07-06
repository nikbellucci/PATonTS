package com.proginternet.Telegram;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.lang.reflect.Field;

import com.proginternet.utils.Auth;
import com.proginternet.utils.JsonParser;
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
    boolean usernameResp=false;
    boolean passwordResp=false;
    boolean phase=false;
    String user="";
    int tries=0;
    @Override
    public String getBotUsername() {
        return "WorkSpaceBVbot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if (update.hasMessage() && update.getMessage().hasText()) {
            
            System.out.println("fufn");
            String msg = update.getMessage().getText();
            if (passwordResp) {
                passwordResp=false;
                try {
                    authenticationUsers(update.getMessage(), user);
                } catch (Exception e) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId());
                    sendMessage.setText("Si è verificato un errore");
                    try {
                            execute(sendMessage);
                    } catch (TelegramApiException x) {
                            System.err.println(x.getMessage());
                    }
                    PreLoginMenu(update.getMessage());
                }
            }
            else if (usernameResp) {user=msg; loginUser(update.getMessage(), "inviare la password");}
            System.out.println("fufn");
            if (msg.equals("/start")) PreLoginMenu(update.getMessage());
            System.out.println(update.getCallbackQuery().getData());

        }
        else if (update.hasCallbackQuery()) {
            String cbdata=update.getCallbackQuery().getData();
            switch (cbdata) {
                case "login":
                    loginUser(update.getCallbackQuery().getMessage(),"Invia username");
                    break;
                
                case "info":
                    infoBot(update.getCallbackQuery().getMessage());
                    break;
                
                case "menu":
                    PreLoginMenu(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;

                case "mainmenu":
                    phase = true;
                    PreLoginMenu(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;
                default:
                    if (cbdata.matches("^WS?://")) {
                        //visualizzazione ws
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
        rowInline.add(new InlineKeyboardButton().setText("Scelta nuovi workspace").setCallbackData("ChoiceW"));
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
        rowInline.add(new InlineKeyboardButton().setText("WorkSpace").setCallbackData("addWs"));
        rowInline.add(new InlineKeyboardButton().setText("Attività").setCallbackData("addActivity"));
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText("Preferenza").setCallbackData("addPref"));
        rowInline2.add(new InlineKeyboardButton().setText("Utente").setCallbackData("addUser"));
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



    //Visualizzazione workspace
    public void ViewWS(Message mess){
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Ecco i tuoi workspace. Di quale vuoi avere piu informazione");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        ArrayList<Workspace> ws = new ArrayList<Workspace>();
        ws = us.getWorkspace();
        for (int i = 0; i < ws.size(); i++) {
            rowInline.add(new InlineKeyboardButton().setText(ws.get(i).getName()).setCallbackData("WS" +  ws.get(i).getName()));
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

    public void authenticationUsers(Message mess, String us) throws Exception{
        passwordResp=false;
        User user=User.checkUsername(us);
        if (user == null) loginUser(mess, "Username o password non corretti \nInviare l'username");
        else if(Auth.validatePassword(mess.getText(), user.getPassword())){
            user.setChat(mess.getChatId());
            updateOnJsonUserId("data/Users.json", mess.getChatId(), user.getUsername());
            MainMenu(mess, user);
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

    public void updateOnJsonUserId(String filename, Long newParam, String un) throws Exception{
        JsonParser<User> parser = new JsonParser<User>();
        ArrayList<User> users = parser.readOnJson(filename, User[].class);

        for (User user : users) {
            if (user.getUsername().equals(un)) {
                user.setChat(newParam);
                break;
            }
        }
        parser.writeOnJson(filename, users);
    }






    @Override
    public String getBotToken() {
        return "1854883561:AAEyVfdWROH-FpHrUX3vrm5kHq4UcnlYc_0";
    }
    

}

