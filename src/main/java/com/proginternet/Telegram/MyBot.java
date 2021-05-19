package com.proginternet.Telegram;

import java.util.ArrayList;
import java.util.List;

import com.proginternet.utils.*;
import com.proginternet.models.User;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    boolean usernameResp;
    boolean passwordResp;
    @Override
    public String getBotUsername() {
        return "WorkSpaceBVbot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if (update.hasMessage() && update.getMessage().hasText()) {
            String user="";
            String msg = update.getMessage().getText();
            if (usernameResp) {user=msg; usernameResp=false;}
            if (passwordResp) authenticationUsers(update.getMessage(), user);
            if (msg.equals("/start")) MainMenu(update.getMessage());
            if (update.hasCallbackQuery()) {
                switch (update.getCallbackQuery().getData()) {
                    case "login":
                        loginUsername(update.getMessage(),"Invia username");
                        break;
                    
                    default:
        
                        break;
                    }
            }
            
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
    }

    public void MainMenu(Message mess){
        System.out.println("MyBot here");
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


    public void loginUsername(Message message, String text){
        SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText(text);
        usernameResp=true;
    }

    public void loginPassword(Message message){
        SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Inviare la password");
        passwordResp=true;
    }

    public void authenticationUsers(Message mess, String us) throws Exception{
        passwordResp=false;
        User user=User.checkUsername(us);
        if (user == null) loginUsername(mess, "Username o password non corretti");
        else if(Auth.validatePassword(mess.getText(), user.getPassword())){
            user.setChat(mess.getChatId());
            //postlogin
        }
        else loginUsername(mess, "Username o password non corretti");
    }

    @Override
    public String getBotToken() {
        return "1854883561:AAEyVfdWROH-FpHrUX3vrm5kHq4UcnlYc_0";
    }
    

}

