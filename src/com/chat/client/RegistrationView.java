package com.chat.client;

import com.chat.common.User;

public interface RegistrationView {
    public void registrationSuccess(User user);

    public void displayErrorMessage(String string);
}
