package GamePage;
import javax.swing.*;
import java.awt.*;

public class Tab3 extends JPanel{
	ImageIcon head = new ImageIcon("bg2.png");
	ImageIcon icon = new ImageIcon("help.jpg");
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0,1184, 661);
		g.setColor(Color.WHITE);
		g.drawRect(5, 5, 1170, 68);
		head.paintIcon(this, g, 6, 7);
		icon.paintIcon(this, g, 6, 80);
		g.setColor(new Color(204, 235, 197));
		g.setFont(new Font("Razor", Font.PLAIN, 50));
		g.drawString("INSTRUCTIONS", 360, 60);
	}
}
