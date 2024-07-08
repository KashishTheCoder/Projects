/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author PMLS
 */
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;


public class ManageSection extends javax.swing.JFrame {

    /**
     * Creates new form ManageSection
     */
    static String id;
    public ManageSection(String id) {
        this.id = id;
        initComponents();
        model = new DefaultTableModel();
    }
     // Initialize database connection
    
   
    Connection con;
    Statement stmt;
    ResultSet rs;
    
   
   
    DefaultTableModel model = new DefaultTableModel();
   
       
       
           
    
     private void loadDataFromDatabase() {
          model.setColumnIdentifiers(new Object[]{"section", "Course ID", "Instructor ID"});
        String department = (String) departmentcombo.getSelectedItem();
        String year = (String) yearcombo.getSelectedItem();
        String term = (String) termcombo.getSelectedItem();
        String semester = (String) semestercombo.getSelectedItem();
        System.out.println(term);
        System.out.println(year);
        System.out.println(semester);

       try {
    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "root");
    stmt = con.createStatement();
    String query = "SELECT course_id, ins_id, id FROM sections WHERE semester = ? AND term = ? AND year = ? AND dept_id = ?";
    PreparedStatement pstmt = con.prepareStatement(query);
    pstmt.setString(1, semester);
    pstmt.setString(2, term);
    pstmt.setString(3, year);
    pstmt.setString(4, department);
    rs = pstmt.executeQuery();

    // Clear previous data in table
    model.setRowCount(0);
System.out.println(rs);
    // Populate jTable with new data
    while (rs.next()) {
        String course_id = rs.getString("course_id");
        String ins_id = rs.getString("ins_id");
        String id = rs.getString("id");
        System.out.println("course_id: " + course_id + ", ins_id: " + ins_id + ", id: " + id); // Debug print statement
        model.addRow(new Object[]{id, course_id, ins_id});
    }
    
    
     String courseQuery = "SELECT DISTINCT course_id FROM sections WHERE semester = ? AND term = ? AND year = ? AND dept_id = ?";
        PreparedStatement coursePstmt = con.prepareStatement(courseQuery);
        coursePstmt.setString(1, semester);
        coursePstmt.setString(2, term);
        coursePstmt.setString(3, year);
        coursePstmt.setString(4, department);
        ResultSet courseRs = coursePstmt.executeQuery();
        
        // Populate course combo box
        coursecombo.removeAllItems();
        while (courseRs.next()) {
            String course_id = courseRs.getString("course_id");
            coursecombo.addItem(course_id);
        }
        
        String instructorQuery = "SELECT DISTINCT ins_id FROM sections WHERE semester = ? AND term = ? AND year = ? AND dept_id = ?";
        PreparedStatement instructorPstmt = con.prepareStatement(instructorQuery);
        instructorPstmt.setString(1, semester);
        instructorPstmt.setString(2, term);
        instructorPstmt.setString(3, year);
        instructorPstmt.setString(4, department);
        ResultSet instructorRs = instructorPstmt.executeQuery();
        
        // Populate instructor combo box
        instructorcombo.removeAllItems();
        while (instructorRs.next()) {
            String ins_id = instructorRs.getString("ins_id");
            instructorcombo.addItem(ins_id);
        }

    // Update jTable on the EDT
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            sectiontable.setModel(model);
        }
    });
} catch (SQLException e) {
    e.printStackTrace();
}
     }
     private void saveDataToDatabase() {
    try {
        String section_id = section.getText();
         if (section_id.isEmpty()) {
            erorrlabel.setText("Section field cannot be empty.");
            return; // Exit the method
        }
        String course_id = (String) coursecombo.getSelectedItem();
        String semester = (String) semestercombo.getSelectedItem();
        String term = (String) termcombo.getSelectedItem();
        String year = (String) yearcombo.getSelectedItem();
        String department = (String) departmentcombo.getSelectedItem();
        String ins_id = (String) instructorcombo.getSelectedItem();
        
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "root");
        String query = "INSERT INTO sections (id, course_id, semester, term, year, dept_id, ins_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, (section_id));
        pstmt.setString(2, course_id);
        pstmt.setString(3, semester);
        pstmt.setString(4, term);
        pstmt.setInt(5, Integer.parseInt(year));
        pstmt.setString(6, department);
        pstmt.setString(7, ins_id);
        
        int rowsInserted = pstmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new row has been inserted into the sections table.");
            erorrlabel.setText("Added Succesfully");
        }
    } catch (SQLException e) {
       if (e instanceof SQLIntegrityConstraintViolationException) {
            // This exception is thrown when a duplicate entry violation occurs
            erorrlabel.setText("Duplicate entry detected. Please check your input.");
        } else {
            e.printStackTrace();
        }
    }
}




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        back_btn = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        departmentcombo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        yearcombo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        termcombo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        semestercombo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        sectiontable = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        section = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        coursecombo = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        instructorcombo = new javax.swing.JComboBox<>();
        back1 = new javax.swing.JButton();
        show = new javax.swing.JButton();
        erorrlabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setForeground(new java.awt.Color(255, 255, 255));
        panel.setPreferredSize(new java.awt.Dimension(1270, 640));

        jPanel2.setBackground(new java.awt.Color(7, 10, 82));
        jPanel2.setForeground(new java.awt.Color(7, 10, 82));

        jLabel1.setBackground(new java.awt.Color(7, 10, 82));
        jLabel1.setFont(new java.awt.Font("Dubai", 0, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Manage Sections");
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
        jLabel4.setText("Department:");

        departmentcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BSCS", "BBA", "BECSE", "BEEE", "B.Ed" }));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(7, 10, 82));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Year:");

        yearcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2023", "2024" }));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(7, 10, 82));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Term:");

        termcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Spring", "Fall" }));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(7, 10, 82));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Semester:");

        semestercombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));

        sectiontable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Section", "Course id", "Instructor Id"
            }
        ));
        jScrollPane1.setViewportView(sectiontable);

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(7, 10, 82));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Select Section:");

        section.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectionActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(7, 10, 82));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Select Course:");

        coursecombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "C++" }));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(7, 10, 82));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Select Instructor:");

        instructorcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "C++" }));

        back1.setBackground(new java.awt.Color(7, 10, 82));
        back1.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        back1.setForeground(new java.awt.Color(255, 255, 255));
        back1.setText("Save");
        back1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                back1ActionPerformed(evt);
            }
        });

        show.setBackground(new java.awt.Color(7, 10, 82));
        show.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        show.setForeground(new java.awt.Color(255, 255, 255));
        show.setText("Show");
        show.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        show.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showActionPerformed(evt);
            }
        });

        erorrlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        erorrlabel.setForeground(new java.awt.Color(0, 0, 102));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(show, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel4)
                        .addGap(29, 29, 29)
                        .addComponent(departmentcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(yearcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(termcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(semestercombo, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addGap(150, 150, 150)
                                .addComponent(erorrlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(301, 301, 301))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelLayout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addGap(38, 38, 38)
                                                .addComponent(coursecombo, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelLayout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(instructorcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelLayout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(34, 34, 34)
                                                .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(185, 185, 185))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                        .addComponent(back1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(329, 329, 329)))))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(60, 60, 60))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(departmentcombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(yearcombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(termcombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(semestercombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(show, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(coursecombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(instructorcombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addComponent(back1)
                        .addGap(18, 18, 18)
                        .addComponent(erorrlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void back_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_back_btnMouseClicked
        new AdminHomePage(id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_back_btnMouseClicked

    private void sectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sectionActionPerformed

    private void back1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_back1ActionPerformed
saveDataToDatabase();
 loadDataFromDatabase(); 

// TODO add your handling code here:
    }//GEN-LAST:event_back1ActionPerformed

    private void showActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActionPerformed

        loadDataFromDatabase();        // TODO add your handling code here:
    }//GEN-LAST:event_showActionPerformed

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
            java.util.logging.Logger.getLogger(ManageSection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageSection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageSection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageSection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageSection(id).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back1;
    private javax.swing.JLabel back_btn;
    private javax.swing.JComboBox<String> coursecombo;
    private javax.swing.JComboBox<String> departmentcombo;
    private javax.swing.JLabel erorrlabel;
    private javax.swing.JComboBox<String> instructorcombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField section;
    private javax.swing.JTable sectiontable;
    private javax.swing.JComboBox<String> semestercombo;
    private javax.swing.JButton show;
    private javax.swing.JComboBox<String> termcombo;
    private javax.swing.JComboBox<String> yearcombo;
    // End of variables declaration//GEN-END:variables
}
