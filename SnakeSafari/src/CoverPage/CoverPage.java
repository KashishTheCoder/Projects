package CoverPage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CoverPage extends JFrame {

	private JPanel contentPane; 
	private ImageIcon btnStart = new ImageIcon("start.jpg");
	private ImageIcon labelSnake = new ImageIcon("snake.png");
	private ImageIcon bg = new ImageIcon("bg.png");
	private ImageIcon logo = new ImageIcon("logo.png");
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CoverPage frame = new CoverPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public CoverPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setBounds(85, 10, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Snake Safari");
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		setIconImage(logo.getImage());
				
		JButton btnPlay = new JButton(btnStart);
		btnPlay.setBackground(new Color(0, 0, 0));
		btnPlay.setFont(new Font("Razor", Font.PLAIN, 34));
		btnPlay.setBounds(701, 467, 164, 58);
		contentPane.add(btnPlay);
		
		JLabel snake = new JLabel(labelSnake);
		snake.setBounds(0, 0, 469, 661);
		contentPane.add(snake);
		
		JLabel l1 = new JLabel("SNAKE");
		l1.setFont(new Font("Razor", Font.PLAIN, 120));
		l1.setForeground(new Color(204, 235, 197));
		l1.setBounds(546, 133, 519, 119);
		contentPane.add(l1);
		
		JLabel l2 = new JLabel("SAFARI");
		l2.setForeground(new Color(204, 235, 197));
		l2.setFont(new Font("Razor", Font.PLAIN, 120));
		l2.setBounds(546, 266, 580, 119);
		contentPane.add(l2);
		
		JLabel bg2 = new JLabel(bg);
		bg2.setBounds(0, 0, 1184, 661);
		contentPane.add(bg2);
		
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginPage().setVisible(true);
				dispose();
			}
		});
	}
}
