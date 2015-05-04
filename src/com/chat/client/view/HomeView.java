package com.chat.client.view;

import com.chat.common.Message;
import com.chat.common.UserDTO;

public interface HomeView {
    
    public void displayErrorMessage(String message);
    public void displayInfoMessage(String message);
    public void setCurrentPeer(UserDTO userDTO);
    public void recieveMessage(Message message);

    public void userLoggedOut(UserDTO user);

    public void userLoggedIn(UserDTO user);

    public void cick();
}
