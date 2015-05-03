package com.chat.client;

import com.chat.common.ClientInt;
import com.chat.common.Constants;
import com.chat.common.Feedback;
import com.chat.common.Message;
import com.chat.common.SearchingCriteria;
import com.chat.common.ServerInt;

import com.chat.common.User;
import com.chat.common.UserDTO;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.server.UnicastRemoteObject;

import java.util.List;

public class ClientManagement {

    ServerInt serverInt;
    ClientInt clientInt;

    public boolean connectionIsAlreadyInitiated() {

        if (serverInt != null) {
            try {
                serverInt.ping();
                return true;
            } catch (Exception ex) {

                return false;
            }


        }

        return false;
    }


    ClientController clientController;

    ClientManagement(ClientController clientController) {
        this.clientController = clientController;
    }


    boolean tryToConnectToServer(String host, String port) {
        try {
            Registry reg = LocateRegistry.getRegistry(host, Integer.parseInt(port));
            serverInt = (ServerInt) reg.lookup(Constants.SERVICE_NAME);
            return connectionIsAlreadyInitiated();
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return false;
        }
    }

    public Feedback authenticate(String email, String password) {
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setPassword(password);
            userDTO.setClientInt(new ClientBusiness(this));
            return serverInt.login(userDTO);
        } catch (RemoteException e) {
            return new Feedback(Feedback.FAILED, "Connection Failed");
        }

    }

    public Feedback register(UserDTO user) {
        try {
            clientInt = new ClientBusiness(this);
            user.setClientInt(clientInt);
            return serverInt.register(user);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new Feedback(Feedback.FAILED, "Connection Failed");
        }
    }

    Feedback updateProfile(User user) {
        try {
            return serverInt.updateProfile(user);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new Feedback(Feedback.FAILED, "Connection Failed");
        }  
    }

    Feedback findBestMatch(User currentUser, SearchingCriteria searchingCriteria, List<String> blackList) {

        try {
            return serverInt.findBestMatch(currentUser, searchingCriteria, blackList);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new Feedback(Feedback.FAILED, "Connection Failed");
        }
    }

    Feedback sendMessageAsMail(Message message) {
        try {
            return serverInt.sendMessageAsEmail(message);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new Feedback(Feedback.FAILED, "Connection Failed");
        }
    }

    void recieveMessage(Message message) {
        clientController.recieveMessage(message);

    }
    
    void logOut(UserDTO userDTO)
    {
        try {
             serverInt.logout(userDTO); 
        
        } catch (Exception  e) {
            
        }
    }

    void userloggedOut(UserDTO user) {
        clientController.userloggedOut(user);
    }

    void userLoggedIn(UserDTO user) {
        clientController.userLoggedIn(user);
    }
}
