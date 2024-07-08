package GamePage;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Buttons extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static String player;
	ImageIcon logo = new ImageIcon("logo.png");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Buttons frame = new Buttons(player);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Buttons(String name) {
		this.player = name;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(85, 10, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Choose Level");
		setResizable(false);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setIconImage(logo.getImage());
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel hard = new JLabel("");
		hard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Game(player, 75, 7).setVisible(true);
				dispose();
			}
		});
		hard.setIcon(new ImageIcon("C:\\Users\\PMLS\\Documents\\SnakeSafari\\hard.png"));
		hard.setBounds(820, 229, 220, 211);
		contentPane.add(hard);
		
		JLabel medium = new JLabel("");
		medium.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Game(player, 100, 5).setVisible(true);
				dispose();
			}
		});
		medium.setIcon(new ImageIcon("C:\\Users\\PMLS\\Documents\\SnakeSafari\\medium.png"));
		medium.setBounds(480, 229, 220, 211);
		contentPane.add(medium);
		
		JLabel easy = new JLabel("");
		easy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Game(player, 140, 3).setVisible(true);
				dispose();
			}
		});
		easy.setIcon(new ImageIcon("C:\\Users\\PMLS\\Documents\\SnakeSafari\\easy.png"));
		easy.setBounds(120, 229, 220, 211);
		contentPane.add(easy);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(0, 0, 1176, 663);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Choose The Level");
		lblNewLabel_1.setForeground(new Color(204, 235, 197));
		lblNewLabel_1.setBackground(new Color(240, 240, 240));
		lblNewLabel_1.setFont(new Font("Super Boom", Font.PLAIN, 60));
		lblNewLabel_1.setBounds(322, 68, 550, 101);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\PMLS\\Documents\\SnakeSafari\\bg.png"));
		lblNewLabel_2.setBounds(0, 0, 1176, 677);
		contentPane.add(lblNewLabel_2);
	}
}
