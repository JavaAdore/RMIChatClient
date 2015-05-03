package com.chat.client.view;

import com.chat.common.UserDTO;

import com.neovisionaries.i18n.CountryCode;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CountryRendrer extends JLabel implements ListCellRenderer<CountryCode> {


    @Override
    public Component getListCellRendererComponent(JList<? extends CountryCode> list, CountryCode country, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        if (country != null) {

            setText(country.getName());
        }

        return this;
    }

}


