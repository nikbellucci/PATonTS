package com.proginternet.Telegram;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import com.proginternet.utils.*;
import com.proginternet.models.User;

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

    boolean usernameResp=false;
    boolean passwordResp=false;
    int tries=0;
    @Override
    public String getBotUsername() {
        return "WorkSpaceBVbot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if (update.hasMessage() && update.getMessage().hasText()) {
            String user="";
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
                    MainMenu(update.getMessage());
                }
            }
            else if (usernameResp) {user=msg; loginUser(update.getMessage(), "inviare la password");}
            System.out.println("fufn");
            if (msg.equals("/start")) MainMenu(update.getMessage());
            System.out.println(update.getCallbackQuery().getData());
            
            
            // String chatId = update.getMessage().getChatId().toString();
            // SendMessage sendMessage = new SendMessage();
            // sendMessage.setChatId(chatId);
            // sendMessage.setText("");
            // try {
            //     execute(sendMessage);
            // } catch (TelegramApiException e) {
            //     System.err.println(e.getMessage());
            // }
        }
        else if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case "login":
                    loginUser(update.getCallbackQuery().getMessage(),"Invia username");
                    break;
                
                case "info":
                    infoBot(update.getCallbackQuery().getMessage());
                    break;
                
                case "menu":
                    MainMenu(update.getCallbackQuery().getMessage());
                    deletePrevMessage(update.getCallbackQuery().getMessage());
                    break;
                
                default:
    
                    break;
            }
        }
    }



    //menu pre login----------------------------------------------------------------------------------------------
    public void MainMenu(Message mess){
        System.out.println("MyBot here");
        if (alreadyLogged(mess.getChatId())!=null) {
            //postlogin
        }
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(mess.getChatId())
            .setText("Benvenuto in WorkSpaceBV!! \nOrganizzarsi per non perdersi nulla \n");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowinfo = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Login").setCallbackData("login"));
        rowInline.add(new InlineKeyboardButton().setText("Registrati").setCallbackData("registrazione"));
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

    // public void loginPassword(Message message){
    //     EditMessageText Mess = new EditMessageText()
    //         .setChatId(message.getChatId())
    //         .setMessageId(message.getMessageId())
    //         .setText("Inviare la password");
    //     passwordResp=true;
    // }

    public void authenticationUsers(Message mess, String us) throws Exception{
        passwordResp=false;
        User user=User.checkUsername(us);
        if (user == null) loginUser(mess, "Username o password non corretti \nInviare l'username");
        else if(Auth.validatePassword(mess.getText(), user.getPassword())){
            user.setChat(mess.getChatId());
            //postlogin
        }
        else tries++;
        if (tries<3) loginUser(mess, "Username o password non corretti \nInviare l'username");
        //else errore password sbagliata
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


//Info------------------------------------------------------------------------------------------

    public void infoBot(Message mess){
        EditMessageText edit = new EditMessageText();
        edit.setChatId(mess.getChatId())
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



    @Override
    public String getBotToken() {
        return "1854883561:AAEyVfdWROH-FpHrUX3vrm5kHq4UcnlYc_0";
    }
    

}

