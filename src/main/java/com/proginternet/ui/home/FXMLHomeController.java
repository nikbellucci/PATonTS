package com.proginternet.ui.home;

import javax.validation.constraints.Null;

import com.proginternet.models.User;
import com.proginternet.utils.Singleton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FXMLHomeController {

    @FXML private Label trialsMsg;

    private User user = null;

    @FXML
    public void initialize() {
        receiveData();
    }

    private void receiveData() {
        Singleton holder = Singleton.getInstance();
        this.user = holder.getUser();
    }
    
}