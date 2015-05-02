package com.chat.client;

import com.chat.common.ClientInt;
import com.chat.common.Constants;
import com.chat.common.Feedback;
import com.chat.common.Message;
import com.chat.common.ServerInt;
import com.chat.common.UserDTO;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientBusiness extends UnicastRemoteObject implements ClientInt {

    ClientManagement clientManagement;

    public ClientBusiness(ClientManagement clientManagement) throws RemoteException{
        this.clientManagement = clientManagement;
    }


    @Override
    public Feedback recieveMessage(Message message) throws RemoteException {
        // TODO Implement this method
        return null;
    }

    @Override
    public void logout(UserDTO userDTO) throws RemoteException {
        // TODO Implement this method
    }

    @Override
    public void userLoggedIn(UserDTO user) throws RemoteException {
        // TODO Implement this method
    }

    @Override
    public void userLoggedOut(UserDTO user) throws RemoteException {
        // TODO Implement this method
    }
}
