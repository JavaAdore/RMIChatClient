package com.chat.client.view;

import com.chat.common.User;
import com.chat.common.UserDTO;

public interface LoginView {
    public void loginSuccessed(UserDTO user);

    public void displayErrorMessage(String string);
}
