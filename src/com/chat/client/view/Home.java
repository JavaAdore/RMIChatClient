
package com.chat.client.view;

import com.chat.client.ClientController;
import com.chat.client.utils.Utils;
import com.chat.common.Hobies;
import com.chat.common.Message;
import com.chat.common.SearchingCriteria;
import com.chat.common.User;

import com.chat.common.UserDTO;

import com.neovisionaries.i18n.*;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author orcl
 */
public class Home extends javax.swing.JFrame implements HomeView {

    /** Creates new form Home */
    ClientController clientController;
    UserDTO currentUser;
    List<String> blackList = new ArrayList();
    SearchingCriteria searchingCriteria;

    UserDTO currentPeer;

    Map<String, ChatView> openChat = new HashMap();

    public Home(ClientController clientController, UserDTO currentUser) {
        initComponents();
        this.clientController = clientController;
        this.clientController.setHomeView(this);
        this.currentUser = currentUser;
        initializeUpdateProfileModels();
        initiaizeSearchModels();
        setRendrers();
        jTabbedPane.setTitleAt(0,"Search Peers" );
        jTabbedPane.setTitleAt(1,"Update my profile" );
        resultPanel.setVisible(false);
        setLocationRelativeTo(null);
        setTitle("Dare2Date");
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                logout();
                super.windowClosing(e);
            }


        });
        enableItemDependingOnCurrentUserRole(currentUser);
        setVisible(true);

    }


    private void enableItemDependingOnCurrentUserRole(UserDTO userDTO) {

        if (userDTO.getSubscriptionType().equals(User.SubscriptionType.BLIND)) {
            for (Component c : basePanel.getComponents()) {
                c.setEnabled(false);
            }

            for (Component c : advancedPanel.getComponents()) {
                c.setEnabled(false);
            }


            SwingUtilities.updateComponentTreeUI(basePanel);
            SwingUtilities.updateComponentTreeUI(advancedPanel);
        } else if (userDTO.getSubscriptionType().equals(User.SubscriptionType.SELECTION)) {
            for (Component c : advancedPanel.getComponents()) {
                c.setEnabled(false);
            }
            SwingUtilities.updateComponentTreeUI(advancedPanel);

        }

    }

    private void logout() {
        logout(currentUser);
    }

    private void setRendrers() {
        countriesComboBox.setRenderer(new CountryRendrer());
        searchCountriesComboBox.setRenderer(new CountryRendrer());
    }

    private void initializeCountriesModel() {
        countriesComboBox.setModel(new DefaultComboBoxModel(CountryCode.values()));
        if (currentUser.getCountry() != null) {
            countriesComboBox.getModel().setSelectedItem(CountryCode.getByCode(Integer.parseInt(currentUser.getCountry())));
        } else {
            countriesComboBox.getModel().setSelectedItem(null);
        }
    }

    private void initializeYearOfBirthModel() {
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int maximumAllowedYear = currentYear - 8;
        int minimumAllowedYear = currentYear - 118;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel();
        spinnerModel.setStepSize(1);
        spinnerModel.setMaximum(maximumAllowedYear);
        spinnerModel.setMinimum(minimumAllowedYear);
        if (currentUser.getBirthYear() != null &&
            (currentUser.getBirthYear() >= minimumAllowedYear || currentUser.getBirthYear() <= maximumAllowedYear)) {
            spinnerModel.setValue(currentUser.getBirthYear());
        }
        ageSpinner.setModel(spinnerModel);
    }

    private void initializeHobbiesModels() {
        hobbiesComboBox.setModel(new DefaultComboBoxModel(Hobies.values()));
        DefaultListModel<Hobies> hobbiesModel = new DefaultListModel<Hobies>();
        if (currentUser.getHobbies() != null) {
            for (Hobies hobby : currentUser.getHobbies()) {
                hobbiesModel.addElement(hobby);
            }
        }
        hobbiesList.setModel(hobbiesModel);
    }

    private void inttializeKeywordsModels() {
        DefaultListModel<String> keywordsModel = new DefaultListModel<String>();
        if (currentUser.getKeywords() != null) {
            for (String keyword : currentUser.getKeywords()) {
                keywordsModel.addElement(keyword);
            }
        }

        keywordsList.setModel(keywordsModel);
    }

    private void loadUserNameAndPassword() {
        userName.setText(currentUser.getUserName());
        password.setText(currentUser.getPassword());
    }

    private void initializeGenderModel() {
        ComboBoxModel genderModel = new DefaultComboBoxModel(User.Gender.values());
        if (currentUser.getGender() != null) {
            genderModel.setSelectedItem(currentUser.getGender());
        } else {
            genderModel.setSelectedItem(User.Gender.MALE);
        }
        genderComboBox.setModel(genderModel);
    }

    private void initializeUpdateProfileModels() {

        loadUserNameAndPassword();

        initializeCountriesModel();

        initializeYearOfBirthModel();

        initializeHobbiesModels();

        inttializeKeywordsModels();

        initializeGenderModel();

    }

    private void initializeSearchCountriesModel() {
        searchCountriesComboBox.setModel(new DefaultComboBoxModel(CountryCode.values()));
        searchCountriesComboBox.getModel().setSelectedItem(null);
    }

    private void initializeSearchHobbiesModels() {

        seachHobbiesComboBox.setModel(new DefaultComboBoxModel(Hobies.values()));
        searchCurrentHobbiesList.setModel(new DefaultListModel<Hobies>());

    }

    private void initializeSearchKeywordsModels() {
        DefaultListModel<String> searchKeywordsModel = new DefaultListModel<String>();
        searchKeywordsList.setModel(searchKeywordsModel);
    }


    private void initializeSearchGenderModel() {
        seachGenderComboBox.setModel(new DefaultComboBoxModel(User.Gender.values()));
        ((DefaultComboBoxModel) seachGenderComboBox.getModel()).insertElementAt(null, 0);
        seachGenderComboBox.getModel().setSelectedItem(null);
    }

    private void initializeMinAndMaxSearchAgeModels() {

        SpinnerNumberModel minimumAgeModel = new SpinnerNumberModel();
        minimumAgeModel.setMaximum(100);
        minimumAgeModel.setMinimum(8);
        minimumAgeModel.setValue(8);
        minAgeSpinner.setModel(minimumAgeModel);


        SpinnerNumberModel maximumAgeModel = new SpinnerNumberModel();
        maximumAgeModel.setMaximum(100);
        maximumAgeModel.setMinimum(8);
        maximumAgeModel.setValue(100);
        maxAgeSpinner.setModel(maximumAgeModel);

    }

    private void initiaizeSearchModels() {
        initializeSearchCountriesModel();

        initializeSearchHobbiesModels();

        initializeSearchKeywordsModels();

        initializeSearchGenderModel();

        initializeMinAndMaxSearchAgeModels();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        resultPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        peerEmail = new javax.swing.JTextField();
        peerUserName = new javax.swing.JTextField();
        peerGender = new javax.swing.JTextField();
        peerCountry = new javax.swing.JTextField();
        peerBirithYear = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        peerHobbies = new javax.swing.JList();
        jScrollPane6 = new javax.swing.JScrollPane();
        peerKeywords = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        basePanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        searchCountriesComboBox = new javax.swing.JComboBox();
        seachGenderComboBox = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        minAgeSpinner = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        maxAgeSpinner = new javax.swing.JSpinner();
        jLabel17 = new javax.swing.JLabel();
        advancedPanel = new javax.swing.JPanel();
        removeSearchKeyword = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        searchKeywordsList = new javax.swing.JList();
        addSearchKeyWordButton = new javax.swing.JButton();
        searchNewKeyword = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        searchCurrentHobbiesList = new javax.swing.JList();
        removeSearchHobby = new javax.swing.JButton();
        addSeachHobby = new javax.swing.JButton();
        seachHobbiesComboBox = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        userName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        countriesComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        keywordsList = new javax.swing.JList();
        addKeyWordButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        ageSpinner = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        hobbiesComboBox = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hobbiesList = new javax.swing.JList();
        removeKeyword = new javax.swing.JButton();
        addHobby = new javax.swing.JButton();
        removeHobby = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        newKeyword = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        genderComboBox = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane.setBackground(new java.awt.Color(255, 204, 204));

        jPanel1.setBackground(new java.awt.Color(255, 204, 255));

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        resultPanel.setBackground(new java.awt.Color(204, 204, 255));

        jButton3.setText("Connect");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("Next Result");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel18.setText("User Name");

        jLabel19.setText("Email");

        jLabel20.setText("Gender");

        jLabel21.setText("Country");

        jLabel22.setText("Year of Birith");

        jLabel23.setText("Hobbies");

        jLabel24.setText("Keywords");

        peerHobbies.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(peerHobbies);

        peerKeywords.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(peerKeywords);

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane6)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resultPanelLayout.createSequentialGroup()
                        .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(resultPanelLayout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(42, 42, 42)
                                .addComponent(jScrollPane5))
                            .addGroup(resultPanelLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(peerBirithYear))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, resultPanelLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(40, 40, 40)
                                .addComponent(peerCountry))
                            .addGroup(resultPanelLayout.createSequentialGroup()
                                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel20))
                                .addGap(44, 44, 44)
                                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(peerGender)
                                    .addComponent(peerEmail)))
                            .addGroup(resultPanelLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(peerUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38))))
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resultPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(peerUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(peerEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(peerGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(peerCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(peerBirithYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel23))
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resultPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel24)
                        .addGap(130, 130, 130)))
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton5)))
        );

        jButton2.setText("Logout");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setText("Country");

        searchCountriesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        seachGenderComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setText("Gender");

        jLabel15.setText("Age");

        jLabel16.setText("Min Age");

        jLabel17.setText("Max Age");

        javax.swing.GroupLayout basePanelLayout = new javax.swing.GroupLayout(basePanel);
        basePanel.setLayout(basePanelLayout);
        basePanelLayout.setHorizontalGroup(
            basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, basePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addGroup(basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(basePanelLayout.createSequentialGroup()
                        .addGroup(basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, basePanelLayout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, basePanelLayout.createSequentialGroup()
                                .addComponent(minAgeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(maxAgeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2))
                    .addGroup(basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(seachGenderComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchCountriesComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        basePanelLayout.setVerticalGroup(
            basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, basePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchCountriesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(seachGenderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(minAgeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxAgeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        removeSearchKeyword.setText("-");
        removeSearchKeyword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSearchKeywordActionPerformed(evt);
            }
        });

        searchKeywordsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(searchKeywordsList);

        addSearchKeyWordButton.setText("+");
        addSearchKeyWordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSearchKeyWordButtonActionPerformed(evt);
            }
        });

        jLabel13.setText("Keywords");

        jLabel14.setText("Current Hobbies");

        searchCurrentHobbiesList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(searchCurrentHobbiesList);

        removeSearchHobby.setText("-");
        removeSearchHobby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSearchHobbyActionPerformed(evt);
            }
        });

        addSeachHobby.setText("+");
        addSeachHobby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSeachHobbyActionPerformed(evt);
            }
        });

        seachHobbiesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel12.setText("Hobbies");

        javax.swing.GroupLayout advancedPanelLayout = new javax.swing.GroupLayout(advancedPanel);
        advancedPanel.setLayout(advancedPanelLayout);
        advancedPanelLayout.setHorizontalGroup(
            advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, advancedPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(advancedPanelLayout.createSequentialGroup()
                        .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(searchNewKeyword)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addSearchKeyWordButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeSearchKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(advancedPanelLayout.createSequentialGroup()
                        .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane4)
                            .addComponent(seachHobbiesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(removeSearchHobby, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addSeachHobby, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        advancedPanelLayout.setVerticalGroup(
            advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(advancedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(advancedPanelLayout.createSequentialGroup()
                        .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(seachHobbiesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(advancedPanelLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel14))
                            .addGroup(advancedPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)
                        .addGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchNewKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(addSearchKeyWordButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(advancedPanelLayout.createSequentialGroup()
                        .addComponent(addSeachHobby)
                        .addGap(18, 18, 18)
                        .addComponent(removeSearchHobby)
                        .addGap(66, 66, 66)
                        .addComponent(removeSearchKeyword)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(searchButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(advancedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(basePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(233, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(25, 25, 25)
                        .addComponent(basePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(advancedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(resultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(264, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("tab1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 204, 255));

        jLabel1.setText("User Name");

        jLabel2.setText("Country");

        countriesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Keywords");

        keywordsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(keywordsList);

        addKeyWordButton.setText("+");
        addKeyWordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addKeyWordButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Year or Birith");

        jButton1.setText("Update Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Password");

        password.setText("jPasswordField1");

        jLabel6.setText("Hobbies");

        hobbiesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Current Hobbies");

        hobbiesList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(hobbiesList);

        removeKeyword.setText("-");
        removeKeyword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeKeywordActionPerformed(evt);
            }
        });

        addHobby.setText("+");
        addHobby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addHobbyActionPerformed(evt);
            }
        });

        removeHobby.setText("-");
        removeHobby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeHobbyActionPerformed(evt);
            }
        });

        jLabel8.setText("Keywords");

        jLabel9.setText("Gender");

        genderComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton4.setText("Logout");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(hobbiesComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(countriesComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                    .addComponent(userName)
                                    .addComponent(jScrollPane2)
                                    .addComponent(jScrollPane1)
                                    .addComponent(newKeyword)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ageSpinner)
                                    .addComponent(genderComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addKeyWordButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeKeyword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addHobby, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeHobby, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(487, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton4)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton4)
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countriesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(genderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ageSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(hobbiesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel7))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(jLabel3))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(newKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)
                                    .addComponent(addKeyWordButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(addHobby)
                        .addGap(32, 32, 32)
                        .addComponent(removeHobby)
                        .addGap(52, 52, 52)
                        .addComponent(removeKeyword)))
                .addGap(39, 39, 39)
                .addComponent(jButton1)
                .addContainerGap(307, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("tab2", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 863, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        if (isFormHasBeenValidated()) {
            fillCurrentUserInfo();
            clientController.updateProfile(currentUser);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    public void fillCurrentUserInfo() {
        currentUser.setUserName(userName.getText());
        currentUser.setPassword(password.getText());
        currentUser.setGender((User.Gender) genderComboBox.getModel().getSelectedItem());
        if (countriesComboBox.getModel().getSelectedItem() != null) {
            currentUser.setCountry(((CountryCode) countriesComboBox.getModel().getSelectedItem()).getNumeric() + "");
        }
        currentUser.setHobbies((List<Hobies>) Collections.list(((DefaultListModel) hobbiesList.getModel()).elements()));
        currentUser.setKeywords((List<String>) Collections.list(((DefaultListModel) keywordsList.getModel()).elements()));
    }

    public boolean isFormHasBeenValidated() {
        return userName.getText() != null && userName.getText().length() > 0 && password.getText() != null &&
               password.getText().length() > 0;
    }

    private void addHobbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addHobbyActionPerformed
        // TODO add your handling code here:


        if (Utils.isModelInclude(hobbiesList.getModel(), hobbiesComboBox.getModel().getSelectedItem()) == false) {
            ((DefaultListModel) hobbiesList.getModel()).addElement(hobbiesComboBox.getModel().getSelectedItem());
        }
            
    }//GEN-LAST:event_addHobbyActionPerformed

    private void addKeyWordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addKeyWordButtonActionPerformed
        if (validateAddingNewKeyword()) {
            if (Utils.isModelInclude(keywordsList.getModel(), newKeyword.getText()) == false) {
                ((DefaultListModel) keywordsList.getModel()).addElement(newKeyword.getText());
            }
        }
          
          
    }//GEN-LAST:event_addKeyWordButtonActionPerformed

    private void addSeachHobbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSeachHobbyActionPerformed
        // TODO add your handling code here:
        if (Utils.isModelInclude(searchCurrentHobbiesList.getModel(),
                                 seachHobbiesComboBox.getModel().getSelectedItem()) == false) {
            ((DefaultListModel) searchCurrentHobbiesList.getModel()).addElement(seachHobbiesComboBox.getModel().getSelectedItem());
        }
        
    }//GEN-LAST:event_addSeachHobbyActionPerformed

    private void addSearchKeyWordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSearchKeyWordButtonActionPerformed
        // TODO add your handling code here:

        if (validateAddingNewSearchKeyword()) {
            if (Utils.isModelInclude(searchKeywordsList.getModel(), searchNewKeyword.getText()) == false) {
                ((DefaultListModel) searchKeywordsList.getModel()).addElement(searchNewKeyword.getText());
            }
        }
        
    }//GEN-LAST:event_addSearchKeyWordButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:

        constructSearchingCriteria();
        clientController.findBestMatch(currentUser, searchingCriteria, blackList);
      
     
    }//GEN-LAST:event_searchButtonActionPerformed
    
    
    private void constructSearchingCriteria() {
        blackList.clear();
        searchingCriteria = new SearchingCriteria();
        if (searchCountriesComboBox.getModel().getSelectedItem() != null) {
            searchingCriteria.setCountry(((CountryCode) searchCountriesComboBox.getModel().getSelectedItem()).getNumeric() +
                                         "");
        }

        if (seachGenderComboBox.getModel().getSelectedItem() != null) {
            searchingCriteria.setGender((User.Gender) seachGenderComboBox.getModel().getSelectedItem());
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        searchingCriteria.setMinAge(currentYear - (Integer) maxAgeSpinner.getModel().getValue());

        searchingCriteria.setMaxAge(currentYear - (Integer) minAgeSpinner.getModel().getValue());

        searchingCriteria.setHobbies(Collections.list(((DefaultListModel) hobbiesList.getModel()).elements()));

        searchingCriteria.setKeywords(Collections.list(((DefaultListModel) searchKeywordsList.getModel()).elements()));

    }

    private ChatView createOrLoadChatView(UserDTO currentPeer) {
        ChatView chatView = openChat.get(currentPeer.getEmail());
        if (chatView == null) {
            chatView = new ChatForm(clientController, currentUser, currentPeer);
            chatView.setChatTitle(String.format("Conversation with : %s (%s)", currentPeer.getUserName(),
                                                currentPeer.getEmail()));
            openChat.put(currentPeer.getEmail(), chatView);
        }
        chatView.displayScreen();
        return chatView;
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        ChatView chatView = createOrLoadChatView(currentPeer);
       
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        blackList.add(currentPeer.getEmail());
        clientController.findBestMatch(currentUser, searchingCriteria, blackList);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void removeHobbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeHobbyActionPerformed
        // TODO add your handling code here:
        List selectedObjects = hobbiesList.getSelectedValuesList();
        if (selectedObjects != null) {
            for (Object obj : selectedObjects)
                ((DefaultListModel) hobbiesList.getModel()).removeElement(obj);
        }
    }//GEN-LAST:event_removeHobbyActionPerformed

    private void removeKeywordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeKeywordActionPerformed
        // TODO add your handling code here:
        List selectedObjects = keywordsList.getSelectedValuesList();
        if (selectedObjects != null) {
            for (Object obj : selectedObjects)
                ((DefaultListModel) keywordsList.getModel()).removeElement(obj);
        }
    }//GEN-LAST:event_removeKeywordActionPerformed

    private void removeSearchHobbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSearchHobbyActionPerformed
        // TODO add your handling code here:

        List selectedObjects = searchCurrentHobbiesList.getSelectedValuesList();
        if (selectedObjects != null) {
            for (Object obj : selectedObjects)
                ((DefaultListModel) searchCurrentHobbiesList.getModel()).removeElement(obj);
        }
        
        
    }//GEN-LAST:event_removeSearchHobbyActionPerformed

    private void removeSearchKeywordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSearchKeywordActionPerformed
        // TODO add your handling code here:
        List selectedObjects = searchKeywordsList.getSelectedValuesList();
        if (selectedObjects != null) {
            for (Object obj : selectedObjects)
                ((DefaultListModel) searchKeywordsList.getModel()).removeElement(obj);
        }
    }//GEN-LAST:event_removeSearchKeywordActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        logout(currentUser);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        logout(currentUser);
    }//GEN-LAST:event_jButton2ActionPerformed

    private boolean validateAddingNewSearchKeyword() {
        return searchNewKeyword.getText() != null && searchNewKeyword.getText().trim().length() > 0;

    }

    public boolean validateAddingNewKeyword() {
        return newKeyword.getText() != null && newKeyword.getText().trim().length() > 0;
    }

    private void loadCurrentPeerData(UserDTO currentPeer) {
        Utils.fillTextFieldWith(peerUserName, currentPeer.getUserName());
        Utils.fillTextFieldWith(peerEmail, currentPeer.getEmail());
        Utils.fillTextFieldWith(peerGender,
                                currentPeer.getGender() != null ? currentPeer.getGender().toString() : null);
        Utils.fillTextFieldWith(peerCountry,
                                currentPeer.getCountry() != null ?
                                CountryCode.getByCode(Integer.parseInt(currentPeer.getCountry())).getName() : null);
        Utils.fillTextFieldWith(peerBirithYear,
                                currentPeer.getBirthYear() != null ? currentPeer.getBirthYear() + "" : null);
        peerHobbies.setModel(new DefaultListModel());
        if (currentPeer.getHobbies() != null) {
            for (Hobies hobby : currentPeer.getHobbies()) {
                ((DefaultListModel) peerHobbies.getModel()).addElement(hobby.getValue());
            }
        }

        peerKeywords.setModel(new DefaultListModel());
        if (currentPeer.getKeywords() != null) {
            for (String keyWord : currentPeer.getKeywords()) {
                ((DefaultListModel) peerKeywords.getModel()).addElement(keyWord);
            }
        }
        resultPanel.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addHobby;
    private javax.swing.JButton addKeyWordButton;
    private javax.swing.JButton addSeachHobby;
    private javax.swing.JButton addSearchKeyWordButton;
    private javax.swing.JPanel advancedPanel;
    private javax.swing.JSpinner ageSpinner;
    private javax.swing.JPanel basePanel;
    private javax.swing.JComboBox countriesComboBox;
    private javax.swing.JComboBox genderComboBox;
    private javax.swing.JComboBox hobbiesComboBox;
    private javax.swing.JList hobbiesList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JList keywordsList;
    private javax.swing.JSpinner maxAgeSpinner;
    private javax.swing.JSpinner minAgeSpinner;
    private javax.swing.JTextField newKeyword;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField peerBirithYear;
    private javax.swing.JTextField peerCountry;
    private javax.swing.JTextField peerEmail;
    private javax.swing.JTextField peerGender;
    private javax.swing.JList peerHobbies;
    private javax.swing.JList peerKeywords;
    private javax.swing.JTextField peerUserName;
    private javax.swing.JButton removeHobby;
    private javax.swing.JButton removeKeyword;
    private javax.swing.JButton removeSearchHobby;
    private javax.swing.JButton removeSearchKeyword;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JComboBox seachGenderComboBox;
    private javax.swing.JComboBox seachHobbiesComboBox;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox searchCountriesComboBox;
    private javax.swing.JList searchCurrentHobbiesList;
    private javax.swing.JList searchKeywordsList;
    private javax.swing.JTextField searchNewKeyword;
    private javax.swing.JTextField userName;
    // End of variables declaration//GEN-END:variables

    @Override
    public void displayErrorMessage(String message) {
        // TODO Implement this method
        JOptionPane.showMessageDialog(this, message, "Failed", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void displayInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void setCurrentPeer(UserDTO currentPeer) {
        this.currentPeer = currentPeer;
        loadCurrentPeerData(currentPeer);
        SwingUtilities.updateComponentTreeUI(resultPanel);
    }

    @Override
    public void recieveMessage(Message message) {
        createOrLoadChatView(message.getSenderDTO()).displayChatMessage(message);
    }

    private void logout(UserDTO userDTO) {
        clientController.logOut(userDTO);
        synchronized (openChat) {
            Iterator<ChatView> chatViewIterator = openChat.values().iterator();
            while (chatViewIterator.hasNext()) {
                ChatView currentChatView = chatViewIterator.next();
                ;
                currentChatView.hideForm();
            }
            Utils.hideAndShow(this, new LoginForm(clientController));
        }
    }

    @Override
    public void userLoggedOut(UserDTO user) {
        synchronized (openChat) {
            Iterator<ChatView> chatViewIterator = openChat.values().iterator();
            while (chatViewIterator.hasNext()) {
                ChatView currentChatView = chatViewIterator.next();
                ;
                currentChatView.userLoggedOut(user);
            }
        }
    }

    @Override
    public void userLoggedIn(UserDTO user) {
        synchronized (openChat) {
            Iterator<ChatView> chatViewIterator = openChat.values().iterator();
            while (chatViewIterator.hasNext()) {
                ChatView currentChatView = chatViewIterator.next();
                ;
                currentChatView.userLoggedIn(user);
            }
        }
    }
}
