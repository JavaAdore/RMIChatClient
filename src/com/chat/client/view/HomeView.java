package com.chat.client.view;

import com.chat.common.UserDTO;

public interface HomeView {
    
    public void displayErrorMessage(String message);
    public void displayInfoMessage(String message);

    public void setCurrentPeer(UserDTO userDTO);
}
