package com.chat.client;

import com.chat.common.Constants;
import com.chat.common.Feedback;
import com.chat.common.SearchingCriteria;
import com.chat.common.ServerInt;

import com.chat.common.User;
import com.chat.common.UserDTO;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.List;

public class ClientManagement {

    ServerInt serverInt;


    public boolean connectionIsAlreadyInitiated() {
        return serverInt != null;
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
            user.setClientInt(new ClientBusiness(this));
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
            return serverInt.findBestMatch( currentUser,  searchingCriteria,blackList);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new Feedback(Feedback.FAILED, "Connection Failed");
        }
    }
}
