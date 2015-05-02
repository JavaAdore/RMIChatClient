package com.chat.client.utils;

import com.chat.client.view.LoginForm;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

public class Utils {

    public static boolean isAPortNumber(String str) {
        try {
            Integer number = new Integer(str);

            return number >= 1024 && number < 60000;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void displayErrorMessage(Container container, String message) {
        JOptionPane.showMessageDialog(container, message);
    }

    public static void hideAndShow(JFrame toHide, JFrame toShow) {

        toHide.setVisible(false);
        toShow.setVisible(true);
    }

    public static boolean isModelInclude(ListModel listModel, Object obj) {

        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public static void fillTextFieldWith(JTextField textField, String value) {
        if(textField!=null)
        {
            textField.setText(value!=null?value:"");
        }
    }
}
