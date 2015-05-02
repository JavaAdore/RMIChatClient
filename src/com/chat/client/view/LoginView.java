package com.chat.client.view;

import com.chat.common.User;

public interface LoginView {
    public void loginSuccessed(User user);

    public void displayErrorMessage(String string);
}
