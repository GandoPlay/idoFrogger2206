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

public class ClientMenu extends JPanel implements MouseMotionListener,MouseListener,KeyListener {
	public enum STATE{
		MENU,
		MULTIPLAYER,
		SCORE	
	};
	private STATE state =  STATE.MENU;
	private Rectangle playButton;
	private Rectangle quitButton;
	private BufferedImage map;
	private Font font;
	private PlaceholderTextField usernameTextArea;
	private PlaceholderTextField ipArea;
	private PlaceholderTextField portArea;
	private Timer t;
	private boolean remove=false;
	private Rectangle submitButton;
	private Rectangle backButton;
	private String Ip;
	private int port;
	private String name;
	private JFrame frame;
	private PlaceholderTextField chooseColor;
	private String color;
	private boolean show = true;
	
	
	public ClientMenu(){
		createTextfield();
		CreateIP();
		EnterPort();
		loadMap();
		 
		playButton= new Rectangle(400, 250, 170, 60);
		quitButton= new Rectangle(400, 450, 170, 60);
		submitButton= new Rectangle(Globals.WIDTH / 2 - usernameTextArea.getWidth() / 2+60, 350, 170, 60);
		backButton= new Rectangle(submitButton.x, 450, 170, 60);
		t=new Timer(10, new GameCycle());
		t.start();
		
	}
	public boolean isFrameVisible() {
		System.out.println(show);
		return show;
	}
	public void RunMenu() {
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
     	frame= new JFrame("Ido Ben Menu"); 		
 		frame.add(this);	
 		frame.setSize(Globals.WIDTH,Globals.HEIGHT);
       	frame.setResizable(false);
       	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		frame.setVisible(true);
	}
	public void drawRect(Graphics2D g2d,int x, int y) {
		//draw Orange rect
		g2d.drawRect(x,y,170,60); 
        g2d.setColor(Color.orange);
        g2d.fillRect(x,y,170,60); 
	} 
	public void drawPaper(Graphics2D g2d) {
		//draw big white rect
		g2d.drawRect(Globals.WIDTH/2-200,0,500,500); 
        g2d.setColor(Color.white);
        g2d.fillRect(Globals.WIDTH/2-200,0,550,550); 
	}
	public void setMenuBAr(Graphics2D g2d) {
		this.drawRect(g2d, playButton.x, playButton.y); 
		g2d.draw(this.playButton);
		g2d.draw(this.quitButton);
		
        
		this.drawRect(g2d, quitButton.x, quitButton.y); 

        
        g2d.setColor(Color.black);
        

        g2d.drawString("Play",  playButton.x,playButton.y+47);
        g2d.drawString("Quit", quitButton.x, quitButton.y+47);
	}
	public void render(Graphics g){
		
		Graphics2D g2d= (Graphics2D)g;
		font= new Font("Century Gothic",Font.BOLD,40);
		g2d.setFont(font);
		g2d.drawImage(map, 0, 0, null);
		if(state.equals(STATE.MENU)) {
			setMenuBAr(g2d);
		}
		else if(state.equals(STATE.MULTIPLAYER)) {
			if(!remove) {
				
				this.add(usernameTextArea);
				this.add(this.ipArea);
				this.add(portArea);
				remove = true;
		
			}
			this.drawRect(g2d, submitButton.x, submitButton.y);
			this.drawRect(g2d, backButton.x, backButton.y);
			g2d.draw(this.submitButton);
			g2d.draw(this.backButton);
			g2d.setColor(Color.black);
		    g2d.drawString("submit",  submitButton.x,submitButton.y+47);
		    g2d.drawString("back",  backButton.x,backButton.y+47);
			
		}
	
	
	}

	public void createTextfield() {
		
        usernameTextArea = new PlaceholderTextField();
        usernameTextArea.setBounds(0, 0, 260, 35);
        usernameTextArea.setFont(new Font("Consolas", 1, 30));
        usernameTextArea.setLocation(Globals.WIDTH / 2 - usernameTextArea.getWidth() / 2, Globals.HEIGHT / 2 - usernameTextArea.getHeight() / 2 - 150);
        usernameTextArea.setBackground(new Color(158, 255, 117
        ));
        usernameTextArea.setForeground(Color.white);
        usernameTextArea.setBorder(BorderFactory.createEmptyBorder());
        usernameTextArea.setPlaceholder("Enter your name");
        usernameTextArea.setHorizontalAlignment(0);
        usernameTextArea.setAlignmentY(0.0F);
	
	}
	
	
	public void CreateIP() {
		ipArea = new PlaceholderTextField();
		ipArea.setBounds(0, 0, 260, 35);
        ipArea.setFont(new Font("Consolas", 1, 30));
        ipArea.setLocation(Globals.WIDTH / 2 - ipArea.getWidth() / 2, Globals.HEIGHT / 2 - ipArea.getHeight() / 2 - 100);
        ipArea.setBackground(new Color(255, 0, 0
        ));
        ipArea.setForeground(Color.white);
        ipArea.setBorder(BorderFactory.createEmptyBorder());
        ipArea.setPlaceholder("Enter IP");
        ipArea.setHorizontalAlignment(0);
        ipArea.setAlignmentY(0.0F);
	}
	public void EnterPort() {
		portArea = new PlaceholderTextField();
		portArea.setBounds(0, 0, 260, 35);
		portArea.setFont(new Font("Consolas", 1, 30));
		portArea.setLocation(Globals.WIDTH / 2 - ipArea.getWidth() / 2, Globals.HEIGHT / 2 - portArea.getHeight() / 2 - 50);
		portArea.setBackground(Color.magenta);
		portArea.setForeground(Color.white);
        portArea.setBorder(BorderFactory.createEmptyBorder());
        portArea.setPlaceholder("Enter Port");
        portArea.setHorizontalAlignment(0);
        portArea.setAlignmentY(0.0F);
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

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public boolean isInsideRect(Rectangle button,int x,int y){
		return ( x >= button.getMinX() && x <= button.getMaxX() ) && ( y >= button.getMinY() && y <= button.getMaxY());
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX=e.getX();
		int mouseY=e.getY();
		if(isInsideRect(this.playButton,mouseX,mouseY)) {
			this.state = STATE.MULTIPLAYER;
		}
		else if(isInsideRect(this.quitButton,mouseX,mouseY)) {
			System.exit(1);
		}
		else if(isInsideRect(backButton, mouseX, mouseY)&&state.equals(STATE.MULTIPLAYER)) {
			this.state = STATE.MENU;
			remove = false;
			this.remove(this.ipArea);
			this.remove(usernameTextArea);
			this.remove(portArea);
		}
	
		else if(isInsideRect(submitButton, mouseX, mouseY)&&state.equals(STATE.MULTIPLAYER)) {
				System.out.println("start connection");
				show = false;
				this.name = usernameTextArea.getText();
				this.Ip = this.ipArea.getText();
				this.port = Integer.parseInt(portArea.getText());
				//ClientMain.flag = false;
				frame.setVisible(false);
				frame.dispose();
				System.out.println("closing menu bar..");
		        ConnectionToServer connect =   new ConnectionToServer(GetName(), GetIp(), GetPort());


		

	
		
		}
	
		
	
		
	}
	public int GetPort() {
		return port;
	}
	public String GetName() {
		return name;
	}
	public String GetIp() {
		return Ip;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
		
	}
	

}
