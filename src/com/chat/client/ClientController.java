package com.chat.client;

import com.chat.client.view.HomeView;
import com.chat.client.view.LoginView;
import com.chat.common.ClientInt;
import com.chat.common.Feedback;
import com.chat.common.SearchingCriteria;
import com.chat.common.User;
import com.chat.common.UserDTO;

import java.util.List;

public class ClientController {
    
    LoginView loginView;
    HomeView homeView;
    RegistrationView registrationView;
    ClientManagement clientManagement;
    {
        clientManagement = new ClientManagement(this);   
    }
    
    
    
    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    public HomeView getHomeView() {
        return homeView;
    }

    public void setRegistrationView(RegistrationView registrationView) {
        this.registrationView = registrationView;
    }

    public RegistrationView getRegistrationView() {
        return registrationView;
    }


   public  boolean tryToConnectToServer(String host, String port) {
       return  clientManagement.tryToConnectToServer( host,  port);
    }

    public void authenticate(String email, String password) {
        Feedback authenticationResult = clientManagement.authenticate(email, password);
        if (authenticationResult.isSuccess()) {
            loginView.loginSuccessed((User) authenticationResult.getObject());
        } else {
            loginView.displayErrorMessage(authenticationResult.getMessage());
        }

    }

    public void register(UserDTO user) {
        Feedback registrationResult = clientManagement.register(user);
        if(registrationResult.isSuccess())
        {
            registrationView.registrationSuccess(user);
        }else
        {
            registrationView.displayErrorMessage(registrationResult.getMessage());
        
        }
    }

    public boolean alreadyConnectedToServer() {
        return clientManagement.connectionIsAlreadyInitiated();
    }

    public void updateProfile(User currentUser) {
        Feedback feedBack = clientManagement.updateProfile( currentUser);
        if(feedBack.isSuccess())
        {
            homeView.displayInfoMessage(feedBack.getMessage());
        }else
        {
            homeView.displayErrorMessage(feedBack.getMessage());
        }
    }

    public void findBestMatch(User currentUser, SearchingCriteria searchingCriteria, List<String> blackList) {
        
        Feedback feedback = clientManagement.findBestMatch( currentUser,  searchingCriteria,  blackList);
        if(feedback.isSuccess())
        {
            homeView.setCurrentPeer((UserDTO)feedback.getObject());
        }else
        {
            homeView.displayErrorMessage(feedback.getMessage());    
        }
        
    }
}


