package com.chat.client.view;

import com.chat.common.Message;
import com.chat.common.UserDTO;

public interface ChatView {
   
   public void displayChatMessage(Message message);

    public void displayScreen();

    public void setChatTitle(String string);

    public void userLoggedOut(UserDTO user);

    public void userLoggedIn(UserDTO user);

    public void hideForm();   
}
