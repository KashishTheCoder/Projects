
import javax.swing.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class ViewClass extends javax.swing.JFrame {

    /**
     * Creates new form Attendance
     */
    static String id;
    public ViewClass(String id) {
        this.id = id;
        initComponents();
    }

  

    // JDBC connection parameters
  private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    // Method to execute query and populate JTable
public void populateTableBasedOnSelectedItem(JTable table, JLabel totalClassesLabel, String selectedItem) throws Exception {
    String query = "SELECT * FROM class WHERE (room_no, Building, From_time, To_time) NOT IN " +
            "(SELECT room_no, Building, From_time, To_time FROM schedule WHERE day = ?)";

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.prepareStatement(query);
        statement.setString(1, selectedItem);
        resultSet = statement.executeQuery();

        // Clear existing rows in the table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // Populate JTable with the query results and count the total number of rows
        int totalClasses = 0;
        while (resultSet.next()) {
            ArrayList<Object> rowData = new ArrayList<>();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                rowData.add(resultSet.getObject(i));
            }
            model.addRow(rowData.toArray());
            totalClasses++;
        }

        // Update the label with the total number of classes
        totalClassesLabel.setText("Total Classes: " + totalClasses);
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle SQL exception
    } finally {
        // Close resources in the finally block
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}

public void populateScheduleTable(JTable scheduleTable,String dayComboBox) {
        DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
        model.setRowCount(0); // Clear existing rows

        // Get the selected day from the combo box
        String selectedDay =  dayComboBox;
        
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT s.section_id, s.course_id, s.semester, s.term, s.year, s.Day " +
               "FROM schedule s " +
               "WHERE s.Day = ? " +
               "GROUP BY s.section_id, s.Day, s.semester, s.term, s.year, s.course_id " +
               "HAVING COUNT(*) < (SELECT COUNT(*) FROM class WHERE Day = s.Day)";
;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, selectedDay); // Set the selected day as parameter
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] rowData = new Object[]{
                        resultSet.getString("section_id"),
                          resultSet.getString("course_id"),
                        resultSet.getString("semester"),
                        resultSet.getString("term"),
                        resultSet.getInt("year"),
                     
                         resultSet.getString("Day")
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }


public void setScheduleFromTables(JTable scheduleTable, JTable timetableTable) {
    DefaultTableModel scheduleModel = (DefaultTableModel) scheduleTable.getModel();
    DefaultTableModel timetableModel = (DefaultTableModel) timetableTable.getModel();

    // Get selected row indices from both tables
    int selectedRow1 = scheduleTable.getSelectedRow();
    int selectedRow2 = timetableTable.getSelectedRow();

    // Check if both rows are selected
    if (selectedRow1 == -1 || selectedRow2 == -1) {
        JOptionPane.showMessageDialog(null, "Please select rows from both tables.", "Selection Error", JOptionPane.ERROR_MESSAGE);

        System.out.println("Please select rows from both tables.");
        return;
    }

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
        // Get data from the schedule table
        String sectionId = (String) scheduleModel.getValueAt(selectedRow1, 0);
        String courseId = (String) scheduleModel.getValueAt(selectedRow1, 1);
        String semester = (String) scheduleModel.getValueAt(selectedRow1, 2);
        String term = (String) scheduleModel.getValueAt(selectedRow1, 3);
        int year = (int) scheduleModel.getValueAt(selectedRow1, 4);
        String day = (String) scheduleModel.getValueAt(selectedRow1, 5);

        // Get data from the timetable table
        String roomNo = (String) timetableModel.getValueAt(selectedRow2, 0);
        String building = (String) timetableModel.getValueAt(selectedRow2, 1);
        Time fromTime = (Time) timetableModel.getValueAt(selectedRow2, 2);
        Time toTime = (Time) timetableModel.getValueAt(selectedRow2, 3);

        // Merge data from both tables
        String sql = "INSERT INTO schedule (section_id, course_id, semester, term, year,  Room_No,Building, From_time, To_time,day) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, sectionId);
        statement.setString(2, courseId);
        statement.setString(3, semester);
        statement.setString(4, term);
        statement.setInt(5, year);
      
        statement.setString(6, roomNo);
        statement.setString(7, building);
        statement.setTime(8, fromTime);
        statement.setTime(9, toTime);
          statement.setString(10, day);

        statement.executeUpdate();
        
Totalclasses.setText("Schedule set succesfully!");
        System.out.println("Data inserted successfully into the schedule table.");

    } catch (SQLException e) {
     Totalclasses.setText("Some Erorr!");
        e.printStackTrace();
        // Handle exceptions
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

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        back_btn = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        scheduletable = new javax.swing.JTable();
        addRow = new javax.swing.JButton();
        create = new javax.swing.JButton();
        selectday = new javax.swing.JComboBox<>();
        Totalclasses = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        timetable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(7, 10, 82));
        jPanel2.setForeground(new java.awt.Color(7, 10, 82));

        jLabel1.setBackground(new java.awt.Color(7, 10, 82));
        jLabel1.setFont(new java.awt.Font("Dubai", 0, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("View Class Status");
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
                .addGap(345, 345, 345)
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

        scheduletable.setBackground(new java.awt.Color(255, 255, 255));
        scheduletable.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        scheduletable.setForeground(new java.awt.Color(7, 10, 82));
        scheduletable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Section_id", "course_id", "semester", "Term", "year", "Day"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scheduletable.setAlignmentX(2.0F);
        scheduletable.getTableHeader().setResizingAllowed(false);
        scheduletable.getTableHeader().setReorderingAllowed(false);
        scheduletable.getTableHeader().setBackground(new java.awt.Color(255, 255, 255));
        scheduletable.getTableHeader().setForeground(new java.awt.Color(7, 10, 82));
        scheduletable.getTableHeader().setFont(new java.awt.Font("Dubai", 1, 14));
        jScrollPane1.setViewportView(scheduletable);

        addRow.setBackground(new java.awt.Color(7, 10, 82));
        addRow.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        addRow.setForeground(new java.awt.Color(255, 255, 255));
        addRow.setText("Add Row");
        addRow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowActionPerformed(evt);
            }
        });

        create.setBackground(new java.awt.Color(7, 10, 82));
        create.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        create.setForeground(new java.awt.Color(255, 255, 255));
        create.setText("Set Schedule");
        create.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });

        selectday.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "friday" }));
        selectday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectdayActionPerformed(evt);
            }
        });

        Totalclasses.setForeground(new java.awt.Color(0, 0, 153));

        timetable.setBackground(new java.awt.Color(255, 255, 255));
        timetable.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        timetable.setForeground(new java.awt.Color(7, 10, 82));
        timetable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Room no", "Building", "From time", "To time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        timetable.setAlignmentX(2.0F);
        timetable.getTableHeader().setResizingAllowed(false);
        timetable.getTableHeader().setReorderingAllowed(false);
        scheduletable.getTableHeader().setBackground(new java.awt.Color(255, 255, 255));
        scheduletable.getTableHeader().setForeground(new java.awt.Color(7, 10, 82));
        scheduletable.getTableHeader().setFont(new java.awt.Font("Dubai", 1, 14));
        jScrollPane2.setViewportView(timetable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Totalclasses, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addRow))
                        .addGap(90, 90, 90)
                        .addComponent(create, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(416, 416, 416)
                .addComponent(selectday, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(selectday, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(Totalclasses, javax.swing.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addRow)
                    .addComponent(create))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createActionPerformed
        setScheduleFromTables(scheduletable,timetable);
    }//GEN-LAST:event_createActionPerformed

    private void addRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowActionPerformed
//        DefaultTableModel model = (DefaultTableModel)scheduletable.getModel();
//        model.addRow(new Object[]{});
    }//GEN-LAST:event_addRowActionPerformed

    private void back_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_back_btnMouseClicked
        new AdminHomePage(id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_back_btnMouseClicked

    private void selectdayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectdayActionPerformed
        String selectedItem = (String) selectday.getSelectedItem();

        try {
            populateTableBasedOnSelectedItem(timetable,Totalclasses, selectedItem);
        } catch (Exception ex) {
            Logger.getLogger(ViewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
         try {
                    // Call the populateScheduleTable method when the button is clicked
                  populateScheduleTable(scheduletable, selectday.getSelectedItem().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Handle exceptions
                }
    }//GEN-LAST:event_selectdayActionPerformed

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
                new ViewClass(id).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Totalclasses;
    private javax.swing.JButton addRow;
    private javax.swing.JLabel back_btn;
    private javax.swing.JButton create;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable scheduletable;
    private javax.swing.JComboBox<String> selectday;
    private javax.swing.JTable timetable;
    // End of variables declaration//GEN-END:variables
}
