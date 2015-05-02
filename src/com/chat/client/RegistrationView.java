package com.chat.client;

import com.chat.common.User;
import com.chat.common.UserDTO;

public interface RegistrationView {
    public void registrationSuccess(UserDTO user);

    public void displayErrorMessage(String string);
}
