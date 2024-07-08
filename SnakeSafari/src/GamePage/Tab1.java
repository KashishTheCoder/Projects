package GamePage;
import java.util.Random;
import javax.swing.*;
import java.awt.event.*;

import CoverPage.*;
import java.awt.*;
import java.sql.*;

public class Tab1 extends JPanel implements ActionListener, KeyListener{
	private boolean left = false;
	private boolean right = true;
	private boolean up = false;
	private boolean down = false;
	private boolean gameover = false;
	private int XFood,YFood;
	private int length = 3;
	private int XLength[] = new int[1170];
	private int YLength[] = new int[570];
	public static int newScore = 0;
	int preScore;
	Timer time;
	private int delay;
	private int add_score;
	String name;
	Random rand = new Random();
	JLabel btnPlay;
	JLabel btnPause;
	
	private ImageIcon head = new ImageIcon("bg2.png");
	private ImageIcon leftMouth = new ImageIcon("FaceLeft.png");
	private ImageIcon rightMouth = new ImageIcon("FaceRight.png");
	private ImageIcon downMouth = new ImageIcon("FaceDown.png");
	private ImageIcon upMouth = new ImageIcon("FaceUp.png");
	private ImageIcon food = new ImageIcon("Food.png");
	private ImageIcon body = new ImageIcon("body.png");
	private ImageIcon playBtn = new ImageIcon("PlayBtn.png");
	private ImageIcon pauseBtn = new ImageIcon("PauseBtn.png");
	
	private int foodX[] = {0, 27, 54, 81, 108, 135, 162, 189, 216, 243, 270, 297,
			324, 351, 378, 405, 432, 459, 486, 513, 540, 567, 594, 621, 648, 675, 702, 729,
			756, 783, 810, 837, 864, 891, 918, 945, 972, 999, 1026, 1053, 1080, 1107};
	private int foodY[] = {81, 108, 135, 162, 189, 216, 243, 270, 297,
			324, 351, 378, 405, 432, 459, 486, 513, 540, 567};
	
	
	
	
	public void actionPerformed(ActionEvent e1) {
		for(int i = length - 1; i > 0; i--) {
			XLength[i] = XLength[i - 1];
			YLength[i] = YLength[i - 1];
		}
		if(left) {
			XLength[0] -= 18;
		}
		if(right) {
			XLength[0] += 18;
		}
		if(up) {
			YLength[0] -= 18;
		}
		if(down) {
			YLength[0] += 18;
		}
		if(XLength[0] > 1125)
			XLength[0] = 0;
		if(XLength[0] < 0)
			XLength[0] = 1125;
		if(YLength[0] > 598)
			YLength[0] = 65;
		if(YLength[0] < 65)
			YLength[0] = 598;
		collide();
		bodyCollision();
		repaint();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/snakesafari";
			Connection con = DriverManager.getConnection(url, "root", "root");
			String query = "select * from HighScores where player = ?;";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				preScore = rs.getInt("score");
				if(newScore > preScore) {
					String query2 = "update HighScores set score = ? where player = ?";
					PreparedStatement ps2 = con.prepareStatement(query2);
					ps2.setInt(1, newScore);
					ps2.setString(2, name);
					ps2.executeUpdate();
					ps2.close();
				}
			}
			else {
				String query3 = "insert into HighScores (player, score) values(?, ?);";
				PreparedStatement ps3 = con.prepareStatement(query3);
				ps3.setString(1, name);
				ps3.setInt(2, newScore);
				ps3.executeUpdate();
				ps3.close();
			}
			ps.close();
			con.close();
		}
		catch(Exception e2) {
			System.out.println(e2);
		}
	}
	
	public void paint(Graphics g) {
		Tab2 t = new Tab2();
		int high = t.high;
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawRect(5, 5, 1170, 68);
		g.drawRect(5, 80, 1170, 550);
		g.setColor(Color.BLACK);
		g.fillRect(6, 81, 1169, 549);
		
		if(left) {
			leftMouth.paintIcon(this, g, XLength[0], YLength[0]);
		}
		if(right) {
			rightMouth.paintIcon(this, g, XLength[0], YLength[0]);
		}
		if(up) {
			upMouth.paintIcon(this, g, XLength[0], YLength[0]);
		}
		if(down) {
			downMouth.paintIcon(this, g, XLength[0], YLength[0]);
		}
		for(int i = 1; i < length; i++) {
			body.paintIcon(this, g, XLength[i], YLength[i]);
		}
		if(gameover) {
			g.setColor(new Color(204, 235, 197));
			g.setFont(new Font("Razor", Font.PLAIN, 100));
			g.drawString("Game Over", 250, 300);
			g.setFont(new Font("Super Boom", Font.PLAIN, 30));
			g.drawString("Press SPACE to REPLAY", 400, 375);
			g.setFont(new Font("Goxaqo Personal Use", Font.PLAIN, 50));
			g.drawString("YOU SCORED: " + newScore, 500, 410);
			time.stop();
		}
		food.paintIcon(this, g, XFood, YFood);
		g.setColor(new Color(204, 235, 197));
		g.setFont(new Font("Super Boom", Font.PLAIN, 20));
		g.drawString("Score: " + newScore, 950, 70);
		g.drawString("Your Best: " + preScore, 950, 40);
		g.drawString("Highest Score: " + high, 500, 28);
		g.setColor(new Color(204, 235, 197));
		g.setFont(new Font("Razor", Font.PLAIN, 40));
		g.drawString(name, 480, 60);
		g.dispose();
	}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_LEFT && !(right)) {
			left = true;
			right = up = down = false;
		}
		if(e.getKeyCode() == e.VK_RIGHT && !(left)) {
			right = true;
			left = up = down = false;
		}
		if(e.getKeyCode() == e.VK_UP && !(down)) {
			up = true;
			left = right = down = false;
		}
		if(e.getKeyCode() == e.VK_DOWN && !(up)) {
			down = true;
			right = up = left = false;
		}
		if(e.getKeyCode() == e.VK_SPACE && gameover) {
			replay();
		}
	}
	public void keyReleased(KeyEvent e) {}
	
	public Tab1(String name, int delay, int add_score){
		newScore = 0;
		this.add_score = add_score;
		this.delay = delay;
		setSize(1184, 661);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
		time = new Timer(delay, this);
		time.start();
		newFood();
		XLength[0] = 100;
		XLength[1] = 82;
		XLength[2] = 64;
		this.name = name;
		YLength[0] = 100;
		YLength[1] = 100;
		YLength[2] = 100;
		
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		JLabel btnBack = new JLabel("");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Buttons(name).setVisible(true);
				// How to dispose this page here??
				SwingUtilities.getWindowAncestor(Tab1.this).dispose();
			}
		});
		btnBack.setIcon(new ImageIcon("C:\\Users\\PMLS\\Documents\\SnakeSafari\\BackBtn.png"));
		btnBack.setBounds(8, 5, 89, 68);
		add(btnBack);
		
		btnPause = new JLabel(pauseBtn);
		btnPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				time.stop();
				btnPause.setEnabled(false);
				btnPlay.setEnabled(true);
			}
		});
		btnPause.setBounds(82, 5, 89, 68);
		add(btnPause);
		
		btnPlay = new JLabel(playBtn);
		btnPlay.setEnabled(false);
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				time.start();
				btnPause.setEnabled(true);
				btnPlay.setEnabled(false);
			}
		});
		btnPlay.setBounds(150, 5, 82, 68);
		add(btnPlay);
		
		JLabel bg = new JLabel(head);
		bg.setBounds(10, 5, 1164, 68);
		add(bg);
	}
	private void bodyCollision() {
		for(int i = 1; i < length; i++) {
			if(XLength[0] == XLength[i] && YLength[0] == YLength[i]) {
				gameover = true;
			}
		}
	}
	private void collide() {
		if((Math.abs(XLength[0] - XFood) <= 10) && (Math.abs(YLength[0] - YFood) <= 10)){
			newFood();
			newScore += add_score;
			length++;
			if(add_score == 3) {
				if(newScore >= 30 && newScore < 50) 
					delay = 120;
				else if (newScore >= 50 && newScore < 70) 
					delay = 100;
				else if (newScore >= 70 && newScore < 85) 
					delay = 80;
				else if(newScore >= 85 && newScore < 100) 
					delay = 75;
				else if(newScore >= 100 && newScore < 120)
					delay = 60;
				else if(newScore >= 120 && newScore < 140)
					delay = 55;
				else if(newScore >= 140 && newScore < 160)
					delay = 50;
				else if(newScore >= 160)
					delay = 45;
			}
			else if(add_score == 5) {
				if(newScore >= 40 && newScore < 55) 
					this.delay = 90;
				else if(newScore >= 55 && newScore < 80)
					delay = 80;
				else if(newScore >= 80 && newScore < 100)
					delay = 75;
				else if(newScore >= 100 && newScore < 120)
					delay = 60;
				else if(newScore >= 120 && newScore < 140)
					delay = 55;
				else if(newScore >= 140 && newScore < 170)
					delay = 50;
				else if(newScore >= 170)
					delay = 45;
			}
			else if(add_score == 7) {
				if(newScore >= 50 && newScore < 75)
					delay = 70;
				else if(newScore >= 75 && newScore < 100)
					delay = 65;
				else if(newScore >= 100 && newScore < 125)
					delay = 60;
				else if(newScore >= 125 && newScore < 150)
					delay = 55;
				else if(newScore >= 150 && newScore < 180)
					delay = 50;
				else if(newScore >= 180)
					delay = 45;
			}
			time.setDelay(delay);
		}
	}
	private void newFood() {
		XFood = foodX[rand.nextInt(41)];
		YFood = foodY[rand.nextInt(19)];
	}
	private void replay() {
		length = 3;
		newScore = 0;
		gameover = false;
		right = true;
		left = up = down = false;
		time.start();
		
		XLength[0] = 100;
		XLength[1] = 82;
		XLength[2] = 64;
		
		YLength[0] = 100;
		YLength[1] = 100;
		YLength[2] = 100;
	}
}