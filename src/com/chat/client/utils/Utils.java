package com.chat.client.utils;

import com.chat.client.view.LoginForm;

import java.awt.Container;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

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
    
    public static String dateToTimeStirng(Date date)
    {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();        
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        return  df.format(today);

    }
}
