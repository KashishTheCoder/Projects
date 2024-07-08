
import com.mysql.cj.xdevapi.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableModel;
public class AddRemoveStudent extends javax.swing.JFrame {

    /**
     * Creates new form Attendance
     */
    Connection con;
    PreparedStatement statement;
    ResultSet rs;
    String dept, term;
    int year;
    static String Id;
    public AddRemoveStudent(String Id) {
        this.Id = Id;
        initComponents();
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = con.prepareStatement("SELECT id FROM departments;");
            rs = statement.executeQuery();
            DefaultComboBoxModel deptModel = new DefaultComboBoxModel();
            while(rs.next()){
                String d = rs.getString("id");
                deptModel.addElement(d);
            }
            selecteddepartment.setModel(deptModel);
        } catch (SQLException ex) {
            Logger.getLogger(AddRemoveStudent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       }
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
 private void fetchAndPopulateData() {
    String query = "SELECT DISTINCT login.id, login.pass, personal_info.first_name, " +
            "personal_info.last_name, personal_info.CNIC, personal_info.date_of_birth, personal_info.gender, " +
            "personal_info.email, personal_info.contact, personal_info.res_address, personal_info.city, " +
            "personal_info.province, students.date_of_admission, personal_info.dept_id, students.semester, enrolled.section_id " +
            "FROM login " +
            "INNER JOIN personal_info ON login.id = personal_info.id " +
            "INNER JOIN students ON login.id = students.id " +
            "INNER JOIN enrolled ON students.id = enrolled.cms_id"+" WHERE personal_info.dept_id = ? " +
                "AND YEAR(students.date_of_admission) = ? ";

    try
         {
            statement = con.prepareStatement(query);
            statement.setString(1, dept);
            statement.setInt(2, year);

        ResultSet rs = statement.executeQuery();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("CMS Id");
        model.addColumn("Password");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("CNIC");
        model.addColumn("Date of Birth");
        model.addColumn("Gender");
        model.addColumn("Email address");
        model.addColumn("Contact");
        model.addColumn("Residential Address");
        model.addColumn("City");
        model.addColumn("Province");
        model.addColumn("Date of admission");
        model.addColumn("Department");
        model.addColumn("Semester");
        model.addColumn("Section");

        while (rs.next()) {
            Object[] row = new Object[16];
            for (int i = 0; i < 16; i++) {
                row[i] = rs.getObject(i + 1);
            }
            model.addRow(row);
        }

        addtable.setModel(model);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
 
 
 private void updateData(int rowIndex) throws Exception {
     con.setAutoCommit(false);
    try {
        String updatePersonalInfoQuery = "UPDATE personal_info SET first_name=?, last_name=?, CNIC=?, " +
                                          "date_of_birth=?, gender=?, email=?, contact=?, " +
                                          "res_address=?, city=?, province=? WHERE id=?";
        
        String updateStudentsQuery = "UPDATE students SET semester=? WHERE id=?";
        
        String updateEnrolledQuery = "UPDATE enrolled SET section_id=? WHERE cms_id=?";

        PreparedStatement updatePersonalInfoStatement = con.prepareStatement(updatePersonalInfoQuery);
        

        // Assuming that the jTable columns correspond to the database table columns in the same order
        updatePersonalInfoStatement.setString(1, (String) addtable.getValueAt(rowIndex, 2)); // First Name
        updatePersonalInfoStatement.setString(2, (String) addtable.getValueAt(rowIndex, 3)); // Last Name
        updatePersonalInfoStatement.setString(3, (String) addtable.getValueAt(rowIndex, 4)); // CNIC
        // Assuming date of birth column is at index 5
        java.sql.Date dob = (java.sql.Date) addtable.getValueAt(rowIndex, 5); // Assuming date of birth column is at index 5

        updatePersonalInfoStatement.setDate(4, dob); // Date of Birth
        updatePersonalInfoStatement.setString(5, (String) addtable.getValueAt(rowIndex, 6)); // Gender
        updatePersonalInfoStatement.setString(6, (String) addtable.getValueAt(rowIndex, 7)); // Email
        updatePersonalInfoStatement.setString(7, (String) addtable.getValueAt(rowIndex, 8)); // Contact
        updatePersonalInfoStatement.setString(8, (String) addtable.getValueAt(rowIndex, 9)); // Residential Address
        updatePersonalInfoStatement.setString(9, (String) addtable.getValueAt(rowIndex, 10)); // City
        updatePersonalInfoStatement.setString(10, (String) addtable.getValueAt(rowIndex, 11)); // Province
        updatePersonalInfoStatement.setString(11, (String) addtable.getValueAt(rowIndex, 0)); // Id
        
        int rowsUpdatedPersonalInfo = updatePersonalInfoStatement.executeUpdate();
                
                
        PreparedStatement updateStudentsStatement = con.prepareStatement(updateStudentsQuery);
        PreparedStatement updateEnrolledStatement = con.prepareStatement(updateEnrolledQuery);
        

        // Department
        updateStudentsStatement.setInt(1, Integer.parseInt(addtable.getValueAt(rowIndex, 14).toString())); // Semester
        updateStudentsStatement.setString(2, (String) addtable.getValueAt(rowIndex, 0)); // Id
         // Semester
 int rowsUpdatedStudents = updateStudentsStatement.executeUpdate();
        // Assuming date of admission column is at index 12
         // Assuming date of admission column is at index 12
        updateEnrolledStatement.setString(1, (addtable.getValueAt(rowIndex, 15).toString())); // Section
; // Date of Admission
        updateEnrolledStatement.setString(2, (String) addtable.getValueAt(rowIndex, 0)); // CMS Id

        // Execute the update queries
      
       
        int rowsUpdatedEnrolled = updateEnrolledStatement.executeUpdate();

        if (rowsUpdatedPersonalInfo > 0 && rowsUpdatedStudents > 0 && rowsUpdatedEnrolled > 0) {
            sucesslabel.setText("Data updated successfully");
            System.out.println("Data updated successfully");
        } else {
            sucesslabel.setText("Update failed. No rows affected.");
            System.out.println("Update failed. No rows affected.");
        }
        con.commit(); 
    } catch (SQLException e) {
        con.rollback();
        e.printStackTrace();
    }
    finally{
    con.setAutoCommit(true);
    }
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Update = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        back_btn = new javax.swing.JLabel();
        selectedyear = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        addtable = new javax.swing.JTable();
        create = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        selecteddepartment = new javax.swing.JComboBox<>();
        create1 = new javax.swing.JButton();
        sucesslabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Update.setBackground(new java.awt.Color(255, 255, 255));
        Update.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(7, 10, 82));
        jLabel3.setText("Batch:");

        jPanel2.setBackground(new java.awt.Color(7, 10, 82));
        jPanel2.setForeground(new java.awt.Color(7, 10, 82));

        jLabel1.setBackground(new java.awt.Color(7, 10, 82));
        jLabel1.setFont(new java.awt.Font("Dubai", 0, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("View Student");
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
                .addGap(400, 400, 400)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(back_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        selectedyear.setBackground(new java.awt.Color(255, 255, 255));
        selectedyear.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        selectedyear.setForeground(new java.awt.Color(7, 10, 82));
        selectedyear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        selectedyear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedyearActionPerformed(evt);
            }
        });

        addtable.setBackground(new java.awt.Color(255, 255, 255));
        addtable.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        addtable.setForeground(new java.awt.Color(7, 10, 82));
        addtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CMS ID", "Password", "First name", "Last name", "CNIC", "Date of birth", "Gender", "Email address", "Contact", "Residential address", "City", "Province", "Date of admission", "department", "Semester", "Section"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true, true, true, true, true, true, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        addtable.setAlignmentX(2.0F);
        addtable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        addtable.setShowVerticalLines(true);
        addtable.getTableHeader().setResizingAllowed(false);
        addtable.getTableHeader().setReorderingAllowed(false);
        addtable.getTableHeader().setBackground(new java.awt.Color(255, 255, 255));
        addtable.getTableHeader().setForeground(new java.awt.Color(7, 10, 82));
        addtable.getTableHeader().setFont(new java.awt.Font("Dubai", 1, 14));
        jScrollPane1.setViewportView(addtable);

        create.setBackground(new java.awt.Color(7, 10, 82));
        create.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        create.setForeground(new java.awt.Color(255, 255, 255));
        create.setText("Save");
        create.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(7, 10, 82));
        jLabel4.setText("Department:");

        selecteddepartment.setBackground(new java.awt.Color(255, 255, 255));
        selecteddepartment.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        selecteddepartment.setForeground(new java.awt.Color(7, 10, 82));
        selecteddepartment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        selecteddepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selecteddepartmentActionPerformed(evt);
            }
        });

        create1.setBackground(new java.awt.Color(7, 10, 82));
        create1.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        create1.setForeground(new java.awt.Color(255, 255, 255));
        create1.setText("View");
        create1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        create1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UpdateLayout = new javax.swing.GroupLayout(Update);
        Update.setLayout(UpdateLayout);
        UpdateLayout.setHorizontalGroup(
            UpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(UpdateLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(selecteddepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(selectedyear, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(create1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(204, 204, 204))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UpdateLayout.createSequentialGroup()
                .addGroup(UpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, UpdateLayout.createSequentialGroup()
                        .addGap(948, 948, 948)
                        .addComponent(sucesslabel, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 100, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UpdateLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(create, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(559, 559, 559))
        );
        UpdateLayout.setVerticalGroup(
            UpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UpdateLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(UpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selecteddepartment)
                    .addComponent(selectedyear)
                    .addComponent(create1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(create)
                .addGap(0, 0, 0)
                .addComponent(sucesslabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Update, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectedyearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedyearActionPerformed
        year = Integer.parseInt(selectedyear.getSelectedItem().toString()); // Remove the toString() conversion
    }//GEN-LAST:event_selectedyearActionPerformed

    private void createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createActionPerformed
 // Get the selected row index from the jTable
        int selectedRowIndex = addtable.getSelectedRow();
        if (selectedRowIndex != -1) { try {
            // Check if a row is selected
            updateData(selectedRowIndex); // Call the updateData method with the selected row index
            } catch (Exception ex) {
                Logger.getLogger(AddRemoveStudent.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           System.out.println("No row Selected ");
        }   
    }//GEN-LAST:event_createActionPerformed

    private void back_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_back_btnMouseClicked
        new AddStudent(Id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_back_btnMouseClicked

    private void selecteddepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selecteddepartmentActionPerformed
        // TODO add your handling code here:
        dept = selecteddepartment.getSelectedItem().toString();
        try {
            statement = con.prepareStatement("SELECT DISTINCT YEAR(students.date_of_admission) AS 'year' FROM personal_info INNER JOIN students USING (id) where personal_info.dept_id = ?;");
            statement.setString(1, dept);
            rs = statement.executeQuery();
            DefaultComboBoxModel yearModel = new DefaultComboBoxModel();
            while(rs.next()){
                int d = rs.getInt("year");
                yearModel.addElement(d);
            }
            selectedyear.setModel(yearModel);
        } catch (SQLException ex) {
            Logger.getLogger(AddRemoveStudent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_selecteddepartmentActionPerformed

    private void create1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create1ActionPerformed

        fetchAndPopulateData();
    }//GEN-LAST:event_create1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
      
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddRemoveStudent(Id).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Update;
    private javax.swing.JTable addtable;
    private javax.swing.JLabel back_btn;
    private javax.swing.JButton create;
    private javax.swing.JButton create1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox<String> selecteddepartment;
    private javax.swing.JComboBox<String> selectedyear;
    private javax.swing.JLabel sucesslabel;
    // End of variables declaration//GEN-END:variables
}
