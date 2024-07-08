package GamePage;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Tab2 extends JPanel {
	ImageIcon head = new ImageIcon("bg2.png");
	public JTable table;
	DefaultTableModel model;
	static int high = 0;
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawRect(5, 5, 1170, 68);
		head.paintIcon(this, g, 6, 7);
		g.setColor(new Color(204, 235, 197));
		g.setFont(new Font("Razor", Font.PLAIN, 50));
		g.drawString("HIGH SCORES", 370, 60);
		repaint();
	}
	
	public Tab2() {
		setSize(1184, 661);
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("HighScores\r\n");
		scrollPane.setBounds(215, 90, 745, 525);
		add(scrollPane);
		
		table = new JTable();
		table.setBackground(new Color(178, 34, 34));
		table.setFont(new Font("Super Boom", Font.PLAIN, 25));
		table.setForeground(new Color(204, 235, 197));
		table.setRowSelectionAllowed(false);
		table.setShowGrid(true);
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setRowHeight(50);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Player", "Score"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Razor", Font.PLAIN, 30));
        header.setBackground(Color.BLACK);
        header.setForeground(new Color(204, 235, 197));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/snakesafari";
			Connection con = DriverManager.getConnection(url, "root", "root");
			String query = "select * from highscores order by score desc";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			int i = 0;
			model = (DefaultTableModel) table.getModel();
			while(rs.next() && i < 10) {
				String player = rs.getString("player");
	            int score = rs.getInt("score");
	            model.addRow(new Object[]{player, score});
	            i++;
			}
			st.close();
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		scrollPane.setViewportView(table);
		
		JButton refresh = new JButton("");
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dm = (DefaultTableModel)table.getModel();
				dm.getDataVector().removeAllElements();
				dm.fireTableDataChanged();
				
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					String url = "jdbc:mysql://localhost:3306/snakesafari";
					Connection con = DriverManager.getConnection(url, "root", "root");
					String query = "select * from highscores order by score desc";
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(query);
					//int i = 0;
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					while(rs.next()) {
						String player = rs.getString("player");
			            int score = rs.getInt("score");
			            model.addRow(new Object[]{player, score});
			           // i++;
					}
					st.close();
					con.close();
				}
				catch(Exception e1) {
					System.out.println(e1);
				}
			}
		});
		refresh.setIcon(new ImageIcon("btnRefresh.png"));
		refresh.setBounds(29, 90, 162, 57);
		add(refresh);
		
		JLabel bg = new JLabel("");
		bg.setIcon(new ImageIcon("bg.png"));
		bg.setBounds(0, 0, 1184, 650);
		add(bg);
		
		Object[] firstRow = model.getDataVector().get(0).toArray();
		high = (int)firstRow[1];
	}
}
