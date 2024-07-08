/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author PMLS
 */
//import static ChangePassword.Id;
import java.security.SecureRandom;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;


import java.util.logging.Level;
import java.util.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;


public class AddInstructor extends javax.swing.JFrame {

    /**
     * Creates new form AddStudent
     */
    static String id;
    public AddInstructor(String Id) {
        this.id = Id;
        initComponents();
        generatePassword();
        addEmailMouseListener();

    }
    private final String SPECIAL_CHARACTERS = "!@#";
    private final SecureRandom RANDOM = new SecureRandom();

    public String generatePassword() {
        StringBuilder password = new StringBuilder();

        // Generate 6 random alphanumeric characters
        for (int i = 0; i < 6; i++) {
            char character = (char) (RANDOM.nextInt(26) + 'a');
            if (RANDOM.nextBoolean()) {
                character = Character.toUpperCase(character);
            }
            password.append(character);
        }

        // Insert 2 random special characters at random positions
        for (int i = 0; i < 2; i++) {
            int position = RANDOM.nextInt(password.length());
            char specialChar = SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length()));
            password.insert(position, specialChar);
        }
        pass.setText(password.toString());
        return password.toString();
    }

    public String generateEmail(String firstName, String lastName) {

        String domain = "iba-suk.edu.pk"; // Your desired domain name
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domain;
        return email;
    }

    private void addEmailMouseListener() {
        email.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String firstName = firstname.getText().replace(" ", "");
                String lastName = lastname.getText().replace(" ", "");
                String generatedEmail = generateEmail(firstName, lastName);
                email.setText(generatedEmail);
            }
        });
    }

    public String[] getStudentData() throws Exception {
        String CMSId = cmsid.getText();
        String password = pass.getText();
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        String CNIC = cnic.getText();
        String dateOfBirth = formatDateOfBirth(dateOfBirthChooser.getDate());
        String gender = jRadioButton1.isSelected() ? "Male" : "Female";
        String Email = email.getText();
        String contact_no = contact.getText();
        String Address = address.getText();
        String City = city.getText();
        String Province = (String) province.getSelectedItem();
        String Department = (String) department.getSelectedItem();
        String dateOfJoined = formatDateOfBirth(dateOfAdmissionChooser.getDate());
        successlabel.setText("");
        
    
        
        
        

        String[] studentData = {CMSId, password, Department, firstName, lastName, gender, CNIC, dateOfBirth, Email, contact_no, Address, City, Province, dateOfJoined};
        return studentData;
    }

    private static boolean checkExistingValues(String id, String email, String contact, String CNIC) throws Exception {
        String query = "SELECT COUNT(*) FROM Personal_Info WHERE id = ? OR email = ? OR contact = ? OR CNIC = ?";
        String URL = "jdbc:mysql://localhost:3306/cms";
        String USERNAME = "root";
        String PASSWORD = "root";

        try (Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pst1 = con1.prepareStatement(query)) {

            pst1.setString(1, id);
            pst1.setString(2, email);
            pst1.setString(3, contact);
            pst1.setString(4, CNIC);

            try (ResultSet resultSet = pst1.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw e; // Re-throw the exception to be handled by the caller
        }
    }

    public void clearFields() {
        cmsid.setText("");
        generatePassword();
        firstname.setText("");
        lastname.setText("");
        cnic.setText("");
        dateOfBirthChooser.setDate(null);
        jRadioButton1.setSelected(false);

        email.setText("");
        contact.setText("");
        address.setText("");
        city.setText("");
        province.setSelectedIndex(0); // Assuming 0 index corresponds to the default value
        department.setSelectedIndex(0);
        dateOfAdmissionChooser.setDate(null);
        successlabel.setText("");
    }

    // Method to adjust term combo box based on admission year
    private String formatDateOfBirth(java.util.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    Connection con;
    java.sql.PreparedStatement pst;
    ResultSet rs;
    String Id;

    public boolean addStudent(String[] studentData) {
        String URL = "jdbc:mysql://localhost:3306/cms";
        String USERNAME = "root";
        String PASSWORD = "root";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean flag= false;
        try {
            
            
    Calendar cal = Calendar.getInstance();
    cal.setTime(dateOfAdmissionChooser.getDate());
    int year = cal.get(Calendar.YEAR);
    if (year < 2023) {
        JOptionPane.showMessageDialog(null, "Year should be greater than or equal to 2023 for date of joining.", "Error", JOptionPane.ERROR_MESSAGE);
        return flag; // Or handle the error according to your application logic
    }
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            con.setAutoCommit(false); // Disable auto-commit to manage transactions

            // Insert into Personal_Info table
            String personalInfoQuery = "INSERT INTO Personal_Info (id, dept_id, first_name, last_name, gender, CNIC, date_of_birth, email, contact, res_address, city, province) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(personalInfoQuery);
            pst.setString(1, studentData[0]); // CMSId
            pst.setString(2, studentData[2]); // Department ID
            pst.setString(3, studentData[3]); // FirstName
            pst.setString(4, studentData[4]); // LastName
            pst.setString(5, studentData[5]); // Gender
            pst.setString(6, studentData[6]); // CNIC
            pst.setString(7, studentData[7]); // DateOfBirth
            pst.setString(8, studentData[8]); // Email
            pst.setString(9, studentData[9]); // Contact
            pst.setString(10, studentData[10]); // Address
            pst.setString(11, studentData[11]); // City
            pst.setString(12, studentData[12]); // Province
            pst.executeUpdate();
            System.out.println("Data successfully added in Personal_Info");

            // Insert into Login table
            String insertLoginQuery = "INSERT INTO Login (id, pass) VALUES (?, ?)";
            pst = con.prepareStatement(insertLoginQuery);
            pst.setString(1, studentData[0]); // CMSId
            pst.setString(2, studentData[1]); // Password
            pst.executeUpdate();
            System.out.println("Data successfully added in Login");

            // Insert into Istructor table
            String studentQuery = "INSERT INTO Instructors (id,date_joined) VALUES (?, ?)";
            pst = con.prepareStatement(studentQuery);
            pst.setString(1, studentData[0]); // INS Id
            pst.setString(2, studentData[13]); // DateOfJoined
            pst.executeUpdate();
            System.out.println("Data successfully added in Students");

            con.commit();
            flag = true;
            System.out.println("Transaction committed successfully");
      
            clearFields();
           
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("Transaction rolled back successfully");
                }
            } catch (SQLException ex) {
                System.out.println("Failed to rollback transaction: " + ex.getMessage());
            }
            e.printStackTrace();
            successlabel.setText("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            successlabel.setText("Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.setAutoCommit(true); // Restore auto-commit mode
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    return flag ;}
        
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        back_btn = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        firstname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmsid = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        city = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lastname = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cnic = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        contact = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        department = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        address = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        province = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        clear = new javax.swing.JButton();
        back4 = new javax.swing.JButton();
        back5 = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        dateOfBirthChooser = new com.toedter.calendar.JDateChooser();
        dateOfAdmissionChooser = new com.toedter.calendar.JDateChooser();
        successlabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(7, 10, 82));
        jPanel2.setForeground(new java.awt.Color(7, 10, 82));

        jLabel1.setBackground(new java.awt.Color(7, 10, 82));
        jLabel1.setFont(new java.awt.Font("Dubai", 0, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Add Instructor");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        back_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        back_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                back_btnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(back_btn)
                .addGap(406, 406, 406)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(back_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(7, 10, 82));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("INS ID:");

        firstname.setBackground(new java.awt.Color(204, 204, 204));
        firstname.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        firstname.setForeground(new java.awt.Color(7, 10, 82));
        firstname.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        firstname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(7, 10, 82));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("City:");

        cmsid.setBackground(new java.awt.Color(204, 204, 204));
        cmsid.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        cmsid.setForeground(new java.awt.Color(7, 10, 82));
        cmsid.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        cmsid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        cmsid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmsidMouseClicked(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(7, 10, 82));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("First Name :");

        city.setBackground(new java.awt.Color(204, 204, 204));
        city.setFont(new java.awt.Font("Dubai", 0, 12)); // NOI18N
        city.setForeground(new java.awt.Color(7, 10, 82));
        city.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        city.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(7, 10, 82));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Last Name :");

        lastname.setBackground(new java.awt.Color(204, 204, 204));
        lastname.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        lastname.setForeground(new java.awt.Color(7, 10, 82));
        lastname.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        lastname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(7, 10, 82));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("CNIC:");

        cnic.setBackground(new java.awt.Color(204, 204, 204));
        cnic.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        cnic.setForeground(new java.awt.Color(7, 10, 82));
        cnic.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        cnic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(7, 10, 82));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Gender:");

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(7, 10, 82));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Date of birth:");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Male");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(female);
        female.setText("Female");

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(7, 10, 82));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Email address:");

        contact.setBackground(new java.awt.Color(204, 204, 204));
        contact.setFont(new java.awt.Font("Dubai", 0, 12)); // NOI18N
        contact.setForeground(new java.awt.Color(7, 10, 82));
        contact.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        contact.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(7, 10, 82));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Contact:");

        email.setBackground(new java.awt.Color(204, 204, 204));
        email.setFont(new java.awt.Font("Dubai", 0, 12)); // NOI18N
        email.setForeground(new java.awt.Color(7, 10, 82));
        email.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        email.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(7, 10, 82));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Date of joined:");

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(7, 10, 82));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Department:");

        department.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Department", "BSCS", "BBA", "BEEE", "BECSE", "B.Ed" }));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(7, 10, 82));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("Address:");

        address.setBackground(new java.awt.Color(204, 204, 204));
        address.setFont(new java.awt.Font("Dubai", 0, 12)); // NOI18N
        address.setForeground(new java.awt.Color(7, 10, 82));
        address.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        address.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(7, 10, 82));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("Province");

        province.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Province", "Sindh", "Punjab", "Balochistan", "Khyber Pakhtunkwa", "Gilgit Baltistan" }));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(7, 10, 82));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("Password:");

        clear.setBackground(new java.awt.Color(7, 10, 82));
        clear.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        clear.setForeground(new java.awt.Color(255, 255, 255));
        clear.setText("Clear");
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        back4.setBackground(new java.awt.Color(7, 10, 82));
        back4.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        back4.setForeground(new java.awt.Color(255, 255, 255));
        back4.setText("Save");
        back4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                back4ActionPerformed(evt);
            }
        });

        back5.setBackground(new java.awt.Color(7, 10, 82));
        back5.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        back5.setForeground(new java.awt.Color(255, 255, 255));
        back5.setText("View");
        back5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                back5ActionPerformed(evt);
            }
        });

        pass.setEditable(false);
        pass.setText("jPasswordField1");
        pass.setEchoChar('\u25cf');
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });

        dateOfBirthChooser.setDateFormatString("yyyy-MM-dd");
        dateOfBirthChooser.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N

        dateOfAdmissionChooser.setDateFormatString("yyyy-MM-dd");

        successlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        successlabel.setForeground(new java.awt.Color(0, 0, 153));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(192, 192, 192)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(jRadioButton1)
                                .addGap(27, 27, 27)
                                .addComponent(female))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(contact)
                                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cnic, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(firstname, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmsid, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(dateOfBirthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel19))
                        .addGap(57, 57, 57)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(province, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateOfAdmissionChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(back5, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(433, 433, 433)
                .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(back4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158)
                .addComponent(successlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel21)
                            .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmsid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(province, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateOfAdmissionChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(firstname, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cnic, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(dateOfBirthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jRadioButton1)
                            .addComponent(female))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(contact, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 76, Short.MAX_VALUE)
                        .addComponent(successlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(clear)
                            .addComponent(back4)
                            .addComponent(back5))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void back_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_back_btnMouseClicked
        new AdminHomePage(id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_back_btnMouseClicked

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        clearFields();
    }//GEN-LAST:event_clearActionPerformed

    private void back4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_back4ActionPerformed
        String[] data;
        try {
            data = getStudentData();
        boolean success = addStudent(data);
        if (success) {
            successlabel.setText("Data Saved Successfully");
        } else {
            // If data is not saved successfully, you might want to display an error message
            JOptionPane.showMessageDialog(this, "Error: Data could not be saved.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        } catch (Exception ex) {

            Logger.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_back4ActionPerformed

    private void back5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_back5ActionPerformed
        // TODO add your handling code here:
        new ViewInstructor(Id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_back5ActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passActionPerformed

    private void cmsidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmsidMouseClicked
        successlabel.setText("");
    }//GEN-LAST:event_cmsidMouseClicked
    
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
            java.util.logging.Logger.getLogger(AddStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddInstructor(id).setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField address;
    private javax.swing.JButton back4;
    private javax.swing.JButton back5;
    private javax.swing.JLabel back_btn;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JTextField city;
    private javax.swing.JButton clear;
    private javax.swing.JTextField cmsid;
    private javax.swing.JTextField cnic;
    private javax.swing.JTextField contact;
    private com.toedter.calendar.JDateChooser dateOfAdmissionChooser;
    private com.toedter.calendar.JDateChooser dateOfBirthChooser;
    private javax.swing.JComboBox<String> department;
    private javax.swing.JTextField email;
    private javax.swing.JRadioButton female;
    private javax.swing.JTextField firstname;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JTextField lastname;
    private javax.swing.JPasswordField pass;
    private javax.swing.JComboBox<String> province;
    private javax.swing.JLabel successlabel;
    // End of variables declaration//GEN-END:variables

}
