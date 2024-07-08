package CoverPage;

import GamePage.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.sql.*;

public class LoginPage extends JFrame {
	public String player;
	private JPanel contentPane;
	private JTextField uName;
	ImageIcon bg = new ImageIcon("bg.png");
	ImageIcon btnPlay = new ImageIcon("play.png");
	ImageIcon snakepic = new ImageIcon("snake.png");
	ImageIcon btnBack = new ImageIcon("back.png");
	ImageIcon logo = new ImageIcon("logo.png");
	int preScore;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage frame = new LoginPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public LoginPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(85, 10, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Login Page");
		setResizable(false);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setIconImage(logo.getImage());
				
		JLabel head = new JLabel("WELCOME TO SNAKE SAFARI", JLabel.CENTER);
		head.setForeground(new Color(204, 235, 197));
		head.setFont(new Font("Razor", Font.PLAIN, 53));
		head.setBounds(0, 0, 1184, 81);
		
		JLabel snake = new JLabel(snakepic);
		snake.setBounds(0, 72, 562, 589);
		
		
		JButton back = new JButton(btnBack);
		back.setBounds(618, 439, 166, 60);
		
		JButton play = new JButton(btnPlay);
		play.setBounds(817, 439, 166, 60);
		play.setEnabled(false);
		
		JLabel name = new JLabel("Enter Your Name: ");
		name.setForeground(new Color(204, 235, 197));
		name.setFont(new Font("Hero archer free version", Font.PLAIN, 42));
		name.setBounds(608, 272, 438, 70);
		
		
		uName = new JTextField();
		uName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == e.VK_BACK_SPACE) {
					if(uName.getText().isEmpty()) {
						play.setEnabled(false);
					}
					for(int i = 0; i < uName.getText().length(); i++) {
						if(uName.getText().isEmpty()) {
							play.setEnabled(false);
						}
						if((int)uName.getText().charAt(i) != 32) {
							play.setEnabled(true);
							break;
						}
						else
							play.setEnabled(false);
					}
				}
				else if(e.getKeyCode() == e.VK_SPACE) {
					for(int i = 0; i < uName.getText().length(); i++) {
						if(uName.getText().isEmpty()) {
							play.setEnabled(false);
						}
						if((int)uName.getText().charAt(i) != 32) {
							play.setEnabled(true);
							break;
						}
						else
							play.setEnabled(false);
					}
				}
				else
					play.setEnabled(true);
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == e.VK_ENTER) {
					if(play.isEnabled()) {
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							String url = "jdbc:mysql://localhost:3306/snakesafari";
							Connection con = DriverManager.getConnection(url, "root", "root");
							player = uName.getText();
							String query = "select * from HighScores where player = ?;";
							PreparedStatement ps = con.prepareStatement(query);
							ps.setString(1, player);
							ResultSet rs = ps.executeQuery();
							if(rs.next()) {
								preScore = rs.getInt("score");
								if(preScore > 0) {
									String query2 = "update HighScores set score = ? where player = ?;";
									PreparedStatement ps2 = con.prepareStatement(query2);
									ps2.setInt(1, preScore);
									ps2.setString(2, player);
									ps2.executeUpdate();
									ps2.close();
								}
							}
							else {
								String query3 = "insert into HighScores (player, score) values(?, ?);";
								PreparedStatement ps3 = con.prepareStatement(query3);
								ps3.setString(1, player);
								ps3.setInt(2, 0);
								ps3.executeUpdate();
								ps3.close();
							}
							ps.close();
							con.close();
						}
						catch(Exception e2) {
							System.out.println(e2);	
						}
						dispose();
						new Buttons(player).setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(null, "Please Enter your name!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		uName.setBackground(new Color(192, 192, 192));
		uName.setFont(new Font("Century", Font.PLAIN, 38));
		uName.setBounds(618, 353, 365, 60);
		uName.setColumns(10);
		
		JLabel bg2 = new JLabel(bg);
		bg2.setBounds(0, 0, 1184, 661);
		
		
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CoverPage().setVisible(true);
				dispose();
			}
		});
		
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/snakesafari";
				Connection con = DriverManager.getConnection(url, "root", "root");
				player = uName.getText();
				String query = "select * from HighScores where player = ?;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, player);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					preScore = rs.getInt("score");
					if(preScore > 0) {
						String query2 = "update HighScores set score = ? where player = ?;";
						PreparedStatement ps2 = con.prepareStatement(query2);
						ps2.setInt(1, preScore);
						ps2.setString(2, player);
						ps2.executeUpdate();
						ps2.close();
					}
				}
				else {
					String query3 = "insert into HighScores (player, score) values(?, ?);";
					PreparedStatement ps3 = con.prepareStatement(query3);
					ps3.setString(1, player);
					ps3.setInt(2, 0);
					ps3.executeUpdate();
					ps3.close();
				}
				ps.close();
				con.close();
			}
			catch(Exception e2) {
				System.out.println(e2);	
			}
			dispose();
			new Buttons(player).setVisible(true);
		}
	});
		
		contentPane.add(head);
		contentPane.add(snake);
		contentPane.add(back);
		contentPane.add(play);
		contentPane.add(name);
		contentPane.add(uName);
		contentPane.add(bg2);
	}
}
