
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class TeacherGrade extends javax.swing.JFrame {

    /**
     * Creates new form TeacherGrade
     */
   static String Id;
    Connection con;
    PreparedStatement pst ;
    ResultSet rs;
    String t, c, s;
    int sem, y;

    public TeacherGrade(String Id) {
        this.Id = Id;
        initComponents();
       try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "root");
            pst = con.prepareStatement("SELECT DISTINCT semester FROM sections where ins_id = ?;");
            pst.setString(1, Id);
            rs = pst.executeQuery();
           
            DefaultComboBoxModel semesterModel = new DefaultComboBoxModel();
            while(rs.next()) {
                int semester_id = rs.getInt("semester");
                //String courseId = rs.getString("course_id");
                //String courseName = rs.getString("name");
                //model.addElement(courseId + " - " + courseName);
                semesterModel.addElement(semester_id + "");
            }
            semester.setModel(semesterModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     String Section;
     String Semester;
     String Term ;
      String Year;
    private void assignGrades() {
    try {
       Semester = (String) semester.getSelectedItem();
         Term = (String) term.getSelectedItem();
         Year = (String) year.getSelectedItem();
        String Course_id = (String) course.getSelectedItem();
       Section = (String) section.getSelectedItem(); // Assuming section is a JTextField
        
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "root");
      String query = "SELECT CONCAT(p.first_name, ' ', p.last_name) AS name, g.cms_id, g.mid_marks, g.sessionals, g.final_marks "
                     + "FROM grading g "
                     + "JOIN personal_info p ON g.cms_id = p.id "
                     + "WHERE g.semester = ? "
                     + "AND g.year = ? "
                     + "AND g.term = ? "
                     + "AND g.course_id = ? "
                     + "AND g.section_id = ?";
        
        PreparedStatement pstmt = con.prepareStatement(query);
       pstmt.setInt(1, Integer.parseInt(Semester));
        pstmt.setInt(2, Integer.parseInt(Year));
        pstmt.setString(3, Term);
       // Assuming department is already defined
        pstmt.setString(4, c);
        pstmt.setString(5, Section);
        
        ResultSet rs = pstmt.executeQuery();
        
        // Clear previous data in table
        DefaultTableModel model = (DefaultTableModel) gradingtable.getModel();
        model.setRowCount(0);
        
        // Populate jTable with new data
        while (rs.next()) {
            String name = rs.getString("name");
            String cms_id = rs.getString("cms_id");
            float mid_marks = rs.getFloat("mid_marks");
            float sessionals = rs.getFloat("sessionals");
            float final_marks = rs.getFloat("final_marks");
            model.addRow(new Object[]{name, cms_id, mid_marks, sessionals, final_marks});
        }
        
        // Update jTable on the EDT
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gradingtable.setModel(model);
            }
        });
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    
    
    private void saveGrades() {
    try {
        DefaultTableModel model = (DefaultTableModel) gradingtable.getModel();
        int rowCount = model.getRowCount();
        
        for (int i = 0; i < rowCount; i++) {
            String cms_id = (String) model.getValueAt(i, 1);
            float mid_marks = (float) model.getValueAt(i, 2);
            float sessionals = (float) model.getValueAt(i, 3);
            float final_marks = (float) model.getValueAt(i, 4);
            
            // Validate marks to ensure sum does not exceed 100
            if (mid_marks + sessionals + final_marks > 100) {
                JOptionPane.showMessageDialog(this, "Total marks cannot exceed 100 for student with CMS ID: " + cms_id, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update marks in database
            updateMarksInDatabase(cms_id, mid_marks, sessionals, final_marks);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
private void updateGrades() {
    try {
        DefaultTableModel model = (DefaultTableModel) gradingtable.getModel();
        int selectedRow = gradingtable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String cms_id = (String) model.getValueAt(selectedRow, 1);
        float mid_marks = (float) model.getValueAt(selectedRow, 2);
        float sessionals = (float) model.getValueAt(selectedRow, 3);
        float final_marks = (float) model.getValueAt(selectedRow, 4);
        
        // Validate marks to ensure sum does not exceed 100
        if (mid_marks + sessionals + final_marks > 100) {
            JOptionPane.showMessageDialog(this, "Total marks cannot exceed 100 for student with CMS ID: " + cms_id, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update marks in database
        updateMarksInDatabase(cms_id, mid_marks, sessionals, final_marks);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void updateMarksInDatabase(String cms_id, float mid_marks, float sessionals, float final_marks) {
    try {
        
        if (mid_marks > 30 || sessionals > 20 || final_marks > 50) {
    // Display error message if marks exceed the limits
    JOptionPane.showMessageDialog(null, "Error: Marks exceed the allowed limits.", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}
  float totalMarks = mid_marks + sessionals + final_marks;
  String grade = calculateGrade(totalMarks);
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "root");
  String query = "UPDATE grading SET mid_marks = ?, sessionals = ?, final_marks = ?, grade = ? WHERE cms_id = ? AND course_id = ? AND section_id = ? AND semester = ? AND term = ? AND year = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setFloat(1, mid_marks);
        pstmt.setFloat(2, sessionals);
        pstmt.setFloat(3, final_marks);
     
        pstmt.setString(4, grade);
        pstmt.setString(5, cms_id);
        pstmt.setString(6, c);
        pstmt.setString(7, Section);
        pstmt.setString(8, Semester);
        pstmt.setString(9, Term);
        pstmt.setInt(10, Integer.parseInt(Year));
        
        int rowsUpdated = pstmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Marks updated for student with CMS ID: " + cms_id);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
   private String calculateGrade(float totalMarks) {
    if (totalMarks >= 93)
        return "A";
    else if (totalMarks >= 87)
        return "A-";
    else if (totalMarks >= 82)
        return "B+";
    else if (totalMarks >= 77)
        return "B";
    else if (totalMarks >= 72)
        return "B-";
    else if (totalMarks >= 68)
        return "C+";
    else if (totalMarks >= 64)
        return "C";
    else if (totalMarks >= 60)
        return "C-";
    else
        return "F";
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        back_btn = new javax.swing.JLabel();
        back_btn1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        year = new javax.swing.JComboBox<>();
        course = new javax.swing.JComboBox<>();
        semester = new javax.swing.JComboBox<>();
        assigngrade = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        gradingtable = new javax.swing.JTable();
        save = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        section = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        term = new javax.swing.JComboBox<>();
        update = new javax.swing.JButton();
        label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1085, 700));

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));
        jPanel2.setPreferredSize(new java.awt.Dimension(1085, 48));

        jLabel1.setFont(new java.awt.Font("Dubai", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Instructor Grades");

        back_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                back_btnMouseClicked(evt);
            }
        });

        back_btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        back_btn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                back_btn1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(back_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(back_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(323, 323, 323)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(357, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(back_btn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(back_btn1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Semester:");

        jLabel6.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Section :");

        jLabel7.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText(" Course :");

        year.setBackground(new java.awt.Color(255, 255, 255));
        year.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        year.setForeground(new java.awt.Color(7, 10, 82));
        year.setMaximumRowCount(3);
        year.setToolTipText("hello");
        year.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        year.setPreferredSize(new java.awt.Dimension(128, 38));
        year.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearActionPerformed(evt);
            }
        });

        course.setBackground(new java.awt.Color(255, 255, 255));
        course.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        course.setForeground(new java.awt.Color(7, 10, 82));
        course.setMaximumRowCount(3);
        course.setToolTipText("hello");
        course.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        course.setPreferredSize(new java.awt.Dimension(128, 38));
        course.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseActionPerformed(evt);
            }
        });

        semester.setBackground(new java.awt.Color(255, 255, 255));
        semester.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        semester.setForeground(new java.awt.Color(7, 10, 82));
        semester.setMaximumRowCount(3);
        semester.setToolTipText("hello");
        semester.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        semester.setPreferredSize(new java.awt.Dimension(128, 38));
        semester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                semesterActionPerformed(evt);
            }
        });

        assigngrade.setBackground(new java.awt.Color(7, 10, 82));
        assigngrade.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        assigngrade.setForeground(new java.awt.Color(255, 255, 255));
        assigngrade.setText("Assign Grade");
        assigngrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assigngradeActionPerformed(evt);
            }
        });

        gradingtable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        gradingtable.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        gradingtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "CMS-ID", "Student Name", "Mid-Marks", "Sessional", "Final-Marks"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        gradingtable.getTableHeader().setResizingAllowed(false);
        gradingtable.getTableHeader().setReorderingAllowed(false);
        gradingtable.getTableHeader().setBackground(new java.awt.Color(255, 255, 255));
        gradingtable.getTableHeader().setForeground(new java.awt.Color(7, 10, 82));
        gradingtable.getTableHeader().setFont(new java.awt.Font("Dubai", 1, 14));
        gradingtable.setAutoscrolls(false);
        gradingtable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        gradingtable.setGridColor(new java.awt.Color(0, 0, 51));
        gradingtable.setRowHeight(40);
        gradingtable.setRowMargin(1);
        gradingtable.setSelectionBackground(new java.awt.Color(102, 102, 102));
        gradingtable.setShowGrid(false);
        jScrollPane1.setViewportView(gradingtable);
        gradingtable.getAccessibleContext().setAccessibleParent(jSeparator1);

        save.setBackground(new java.awt.Color(204, 204, 204));
        save.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        save.setForeground(new java.awt.Color(0, 0, 51));
        save.setText("Save ");
        save.setPreferredSize(new java.awt.Dimension(100, 23));
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Term:");

        section.setBackground(new java.awt.Color(255, 255, 255));
        section.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        section.setForeground(new java.awt.Color(7, 10, 82));
        section.setMaximumRowCount(3);
        section.setToolTipText("hello");
        section.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        section.setPreferredSize(new java.awt.Dimension(128, 38));
        section.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectionActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Year:");

        term.setBackground(new java.awt.Color(255, 255, 255));
        term.setFont(new java.awt.Font("Dubai", 0, 14)); // NOI18N
        term.setForeground(new java.awt.Color(7, 10, 82));
        term.setMaximumRowCount(3);
        term.setToolTipText("hello");
        term.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        term.setPreferredSize(new java.awt.Dimension(128, 38));
        term.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                termActionPerformed(evt);
            }
        });

        update.setBackground(new java.awt.Color(204, 204, 204));
        update.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        update.setForeground(new java.awt.Color(0, 0, 51));
        update.setText("Update ");
        update.setPreferredSize(new java.awt.Dimension(100, 23));
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        label.setForeground(new java.awt.Color(0, 0, 102));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1103, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(251, 251, 251))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(assigngrade, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(semester, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(term, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(course, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(375, 375, 375)
                                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1062, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(semester, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(term, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(course, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(assigngrade, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1103, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearActionPerformed
   try {
            // TODO add your handling code here:
            y = Integer.parseInt(year.getSelectedItem().toString());
           
            DefaultComboBoxModel courseModel = new DefaultComboBoxModel();
            //PreparedStatement pst = con.prepareStatement("SELECT DISTINCT sections.course_id, courses.name FROM sections INNER JOIN courses ON sections.course_id = courses.id WHERE ins_id = ?");

            pst = con.prepareStatement("SELECT DISTINCT sections.course_id, courses.name FROM sections INNER JOIN courses ON sections.course_id = courses.id WHERE sections.ins_id = ? and sections.semester = ? and sections.term = ? and sections.year = ?;");
            pst.setString(1, Id);
            pst.setInt(2, sem);
            pst.setString(3, t);
            pst.setInt(4, y);
            rs = pst.executeQuery();
            while(rs.next()){
                String courseId = rs.getString("course_id");
                String courseName = rs.getString("name");
                courseModel.addElement(courseId + ":" + courseName);
            }
            course.setModel(courseModel);
        } catch (SQLException ex) {
            Logger.getLogger(InstructorAttendence.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_yearActionPerformed

    private void courseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseActionPerformed
  try {
            // TODO add your handling code here:
            String courses = course.getSelectedItem().toString();
            String[] courses_arr = courses.split(":");
            c = courses_arr[0];

            DefaultComboBoxModel sectionModel = new DefaultComboBoxModel();
            pst = con.prepareStatement("SELECT DISTINCT id FROM sections WHERE ins_id = ? and semester = ? and term = ? and year = ? and course_id = ?;");
            pst.setString(1, Id);
            pst.setInt(2, sem);
            pst.setString(3, t);
            pst.setInt(4, y);
            pst.setString(5, c);
            rs = pst.executeQuery();
            while(rs.next()){
                String sec = rs.getString("id");
                sectionModel.addElement(sec);
            }
            section.setModel(sectionModel);
        } catch (SQLException ex) {
            Logger.getLogger(InstructorAttendence.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }//GEN-LAST:event_courseActionPerformed

    private void semesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_semesterActionPerformed
        try {
            // TODO add your handling code here:
            sem = Integer.parseInt(semester.getSelectedItem().toString());
           
            DefaultComboBoxModel termModel = new DefaultComboBoxModel();
            pst = con.prepareStatement("SELECT DISTINCT term FROM sections WHERE ins_id = ? and semester = ?;");
            pst.setString(1, Id);
            pst.setInt(2, sem);
            rs = pst.executeQuery();
            while(rs.next()){
                String term_ = rs.getString("term");
                termModel.addElement(term_);
            }
            term.setModel(termModel);
        } catch (SQLException ex) {
            Logger.getLogger(InstructorAttendence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_semesterActionPerformed

    private void assigngradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assigngradeActionPerformed
assignGrades()    ;    // Get the selected values from the combo boxes
    
  
    }//GEN-LAST:event_assigngradeActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
     saveGrades() ; 
     label.setText("Grades saved successfully.");// TODO add your handling code here:
    }//GEN-LAST:event_saveActionPerformed

    private void sectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectionActionPerformed
          s = section.getSelectedItem().toString();
        
    }//GEN-LAST:event_sectionActionPerformed

    private void back_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_back_btnMouseClicked
        new InstructorHomePage(Id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_back_btnMouseClicked

    private void termActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termActionPerformed
  try {
            // TODO add your handling code here:
            t = term.getSelectedItem().toString();
           
            DefaultComboBoxModel yearModel = new DefaultComboBoxModel();
            pst = con.prepareStatement("SELECT DISTINCT year FROM sections WHERE semester = ? and term = ?;");
            pst.setInt(1, sem);
            pst.setString(2, t);
            rs = pst.executeQuery();
            while(rs.next()){
                int y = rs.getInt("year");
                yearModel.addElement(y + "");
            }
            year.setModel(yearModel);
        } catch (SQLException ex) {
            Logger.getLogger(InstructorAttendence.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_termActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
updateGrades();  
label.setText("Grades updated successfully.");// TODO add your handling code here:
    }//GEN-LAST:event_updateActionPerformed

    private void back_btn1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_back_btn1MouseClicked
        new InstructorHomePage(Id).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_back_btn1MouseClicked
  


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
            java.util.logging.Logger.getLogger(TeacherGrade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TeacherGrade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TeacherGrade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TeacherGrade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TeacherGrade(Id).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton assigngrade;
    private javax.swing.JLabel back_btn;
    private javax.swing.JLabel back_btn1;
    private javax.swing.JComboBox<String> course;
    private javax.swing.JTable gradingtable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel label;
    private javax.swing.JButton save;
    private javax.swing.JComboBox<String> section;
    private javax.swing.JComboBox<String> semester;
    private javax.swing.JComboBox<String> term;
    private javax.swing.JButton update;
    private javax.swing.JComboBox<String> year;
    // End of variables declaration//GEN-END:variables
}
