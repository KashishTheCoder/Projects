package GamePage;
import javax.swing.*;
import javax.swing.event.*;

import CoverPage.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;

public class Game extends JFrame{
	private ImageIcon logo = new ImageIcon("logo.png");
	public String name;
	public int delay, add_score;	
	
	
	public Game(String name, int delay, int add_score){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(85, 10, 1200, 700);
		setResizable(false);
		setTitle("Snake Safari");
		setIconImage(logo.getImage());
		this.name = name;
		
		JTabbedPane myMenu = new JTabbedPane();
		myMenu.setBackground(Color.BLACK);
		myMenu.setForeground(Color.RED);
		
		Tab1 t1 = new Tab1(name, delay, add_score);
		Tab2 t2 = new Tab2();
		Tab3 t3 = new Tab3();
		myMenu.add("Play", t1);
		myMenu.add("Score", t2);
		myMenu.add("Help", t3);
		add(myMenu);
		setVisible(true);
		myMenu.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int index = myMenu.getSelectedIndex();
				if (index == 0 || index == 1 || index == 2) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    	e.consume();
                    	t1.requestFocusInWindow();
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    	e.consume();
                    	t1.requestFocusInWindow();
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    	e.consume();
                    	t1.requestFocusInWindow();
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    	e.consume();
                    	t1.requestFocusInWindow();
                    }
				}
			}
		});
	}
}