package frogger;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import UI.PlaceholderTextField;

public class ScoreMenu extends JPanel  {


	private BufferedImage map;
	private Font font;
	private Timer t;
	private ArrayList<DataBaseObject> db;
	private JFrame frame;
	private boolean win;
	

	
	
	public ScoreMenu(boolean win,ArrayList<DataBaseObject> db){
		this.win = win;
		this.db = db;

		loadMap();
		

		t=new Timer(10, new GameCycle());
		t.start();
		
	}

	public void RunMenu() {
     	frame= new JFrame("Ido Ben Result"); 		
 		frame.add(this);	
 		frame.setSize(Globals.WIDTH,Globals.HEIGHT);
       	frame.setResizable(false);
       	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		frame.setVisible(true);
	}

	public void drawPaper(Graphics2D g2d) {
		g2d.drawRect(Globals.WIDTH/2-200,0,500,500); 
        g2d.setColor(Color.white);
        g2d.fillRect(Globals.WIDTH/2-200,0,550,550);  
	}

	public void render(Graphics g){
		
		Graphics2D g2d= (Graphics2D)g;
		font= new Font("Century Gothic",Font.BOLD,40);
		g2d.setFont(font);
		g2d.drawImage(map, 0, 0, null);
		
		//ArrayList<DataBaseObject> db = dataBase.showDataBase();
			
		drawPaper(g2d);
		g2d.setColor(Color.blue);
		font= new Font(Font.MONOSPACED,Font.BOLD,40);
		g2d.setFont(font);
		g2d.drawString("Name:          Score:",  Globals.WIDTH/2-200,50);
		g2d.setColor(Color.yellow);
		font= new Font(Font.MONOSPACED,Font.BOLD,60);
		g2d.setFont(font);
		if(win) {
			g2d.drawString("You Won!",  Globals.WIDTH/2-120,600);

			}
		else {
			g2d.drawString("You Lose!",  Globals.WIDTH/2-120,600);

		}
				
		for (int i = 0; i < db.size(); i++) {
		g2d.setColor(Color.red);
		font= new Font(Font.SANS_SERIF,Font.BOLD,40);
		g2d.setFont(font);
					
		g2d.drawString(db.get(i).getName(),  Globals.WIDTH/2-195,100+i*50);
				 
				 
		g2d.setColor(Color.green);
		font= new Font(Font.MONOSPACED,Font.BOLD,40);
		g2d.setFont(font);
		g2d.drawString(String.valueOf(db.get(i).getScore()),  Globals.WIDTH/2+200,100+i*50);

					
				 
			}
			

			 

		}
	
	
	public void loadMap(){
		try {
		
				map= ImageIO.read(getClass().getResourceAsStream("img/IdoBenFroggerBack.png"));
		

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	 private class GameCycle implements ActionListener {
	        
	        public void actionPerformed(ActionEvent e) {
	            doGameCycle();
	        }
	    }

	    private void doGameCycle() {
	    
	    	
	    	repaint();
	    }
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		render(g);
		
	
		
	}



}
