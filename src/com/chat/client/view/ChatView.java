package com.chat.client.view;

import com.chat.common.Message;

public interface ChatView {
   
   public void displayChatMessage(Message message);

    public void displayScreen();

    public void setChatTitle(String string);
}
