package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Frog extends GameObject implements KeyListener{
	private FROGCOLOR id;
	private int deaths =0;
	private int score = 0;
	private boolean isDead = false;
	private boolean wonLevel = false;
	private boolean interactsLog = false;
	private boolean onWater=false;
	
	private DisplayClient displayClient;
	private long TimeStarted;
	
	boolean ismove= false;
	private List<Frog> frogs;
	private Timer t;
	private boolean SendData = false;
	private int level=0;
	//private String Frogname;
	public enum FROGCOLOR{
		BLUE,
		RED
	};
	public FROGCOLOR getColor() {
		return this.id;
	}
	public void SetTimeStarted(long time) {
		this.TimeStarted = time;
	}
	Frog(int x,int y,int w,int h,FROGCOLOR i){
		super(x,y,w,h,0);	
		
		id = i;
		if(id ==FROGCOLOR.BLUE)
			this.changeSprite("img/ido_right.png");
		else
			this.changeSprite("img/ido_right2.png");
		this.Sleep = false;
		t=new Timer(10, new RunFrog());
		t.start();
		
	}
	

	public void reset() {
		if(this.getFrog().x>=1280)
			isDead = true;
		if(isDead) {
			this.getFrog().x=250;
			this.getFrog().y=DisplayClient.HEIGHT-90;
			
			isDead=false;
			wonLevel=false;
		}
		else if(wonLevel) {
			for (Frog frog : frogs) {
				frog.getFrog().x=250;
				frog.getFrog().y=DisplayClient.HEIGHT-90;
		
				}
			this.displayClient.AddScore();
			this.score++;
			FinishLevel(System.currentTimeMillis());
			this.displayClient.initializeGame();
			isDead=false;
			wonLevel=false;
		}
		
	}
	private void FinishLevel(long currentTimeMillis) {
		//the client won the level
		ConnectionToServer.SendingDataToServer("WONLEVEL"+this.getColor()+"="+this.level+"="+((currentTimeMillis-this.TimeStarted)/1000));
		//level++;
		
	}
	public  void HopToNextLevel() {
		//this.TimeToFinishLevel[level] = -1;
		level++;
		
		killFrog();
	}
	public void killFrog() {
		this.getFrog().x=250;
		this.getFrog().y=DisplayClient.HEIGHT-90;
	}
	public void setFrogs(List<Frog> fs) {
		this.frogs =fs;
		
	}
	public boolean isInteractLog() {
		return this.interactsLog;
	}
	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isWonLevel() {
		return wonLevel;
	}
	public boolean onWater() {return this.onWater;}

	public void setWonLevel(boolean wonLevel) {
		this.wonLevel = wonLevel;
	}

	public void SetSendData(boolean flag) {
		SendData = flag;
	}
	public void didIntersectCar(){
		//lock the carsRoad
		synchronized (DisplayClient.lock) {
			
		 
		for(Cars[][] cars: DisplayClient.carsRoad) {
			for(Cars []road:cars) {
				for(Cars car :road) {
					if(this.getFrog().getBounds().intersects(car.getRect().getBounds())){
						this.increaseDeaths();
						isDead = true;
						//send the server that the client is dead
						ConnectionToServer.SendingDataToServer("DEAD"); 
						
					}		
				}			
			}
		}
		}
	
	}
	


	

	public void render(Graphics g,DisplayClient d){
		Graphics2D g2d=(Graphics2D)g;
		g2d.drawImage(this.image,rect.x,rect.y,d);
	}

	


	public void mover(int speed){
		this.getRect().x=this.getRect().x+speed;
	}
	public void movery(int speed){
		this.getRect().y=this.getRect().y+speed;
	}
	public void stopMove(){
		mover(0);
	}
	public int roundTo(int number){
		return number - (number%50);
	}
	


	
	 private class RunFrog implements ActionListener {
	        
	        public void actionPerformed(ActionEvent e) {
	        	RunFrog();
	        }
	    }

	    private void RunFrog() {
	    
	    	if(this.SendData) {
	    		this.increaseScore();
	    		this.reset();
	    		this.didIntersectCar();
	    	}
	    }


	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public void increaseDeaths() {
		this.deaths++;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void increaseScore() {
		if(this.getFrog().getCenterY()<=0) {
			wonLevel = true;
			
		}
	}
	private boolean Sleep;

	public void setFrog(Rectangle frog) {
		this.rect = frog;
	}
	

	
	public void Sleep() {
		this.Sleep = true;
	}
	public void WakeUp() {
		this.Sleep= false;
	}

	
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public Rectangle getFrog() {
		return this.rect;
	}
	public void moveFrog(int key, boolean SendData) {
		if(id==FROGCOLOR.RED) {
			this.moveRedFrog(key,SendData);
		}
		if(id==FROGCOLOR.BLUE) {
			this.moveBlueFrog(key,SendData);
		}
		
	}
	private void moveBlueFrog(int key, boolean SendData) {
		if (key==KeyEvent.VK_RIGHT) {
			if(SendData)
				ConnectionToServer.SendingDataToServer("right");

			if(rect.getMaxX()+1*DisplayClient.GRID<DisplayClient.WIDTH&&this.sprite=="img/ido_right.png"){
				this.setSpeed(1*DisplayClient.GRID);
				rect.x=rect.x+1*DisplayClient.GRID;					
				rect.setLocation(rect.x, rect.y);
			}
			else{
				this.changeSprite("img/ido_right.png");
			}
		}
		else if(key==KeyEvent.VK_LEFT) {
			if(SendData)
				ConnectionToServer.SendingDataToServer("left");
			if(rect.getMaxX()-1*DisplayClient.GRID>0&&this.sprite=="img/ido_left.png"){
				rect.x=rect.x-1*DisplayClient.GRID;
				rect.setLocation(rect.x, rect.y);
			}
			else{
				this.changeSprite("img/ido_left.png");
			}
		}
		
		else if(key==KeyEvent.VK_UP) {
			if(SendData)
				ConnectionToServer.SendingDataToServer("up");
			if(rect.getMaxY()-1*DisplayClient.GRID>0)
				rect.y=rect.y-1*DisplayClient.GRID;
			rect.setLocation(rect.x, rect.y);
			
			
			if(rect.x%50==0)
				rect.setLocation(rect.x, rect.y);
			else{
				rect.setLocation(roundTo(rect.x), rect.y);
			}
			//frog
			this.changeSprite("img/ido_front.png");

		}
		else if(key==KeyEvent.VK_DOWN) {
			if(SendData)
				ConnectionToServer.SendingDataToServer("down");
			if(rect.getMaxY()+1*DisplayClient.GRID<DisplayClient.HEIGHT)
				rect.y=rect.y+1*DisplayClient.GRID;
			if(rect.x%50==0)
				rect.setLocation(rect.x, rect.y);
			else{
				rect.setLocation(roundTo(rect.x), rect.y);
			}
			this.changeSprite("img/ido_front.png");

		}
		
	}
	private void moveRedFrog(int key, boolean SendData) {
		// TODO Auto-generated method stub
		if (key==KeyEvent.VK_RIGHT) {
			if(SendData)
				ConnectionToServer.SendingDataToServer("right");
			if(rect.getMaxX()+1*DisplayClient.GRID<DisplayClient.WIDTH&&this.sprite=="img/ido_right2.png"){
				this.setSpeed(1*DisplayClient.GRID);
				rect.x=rect.x+1*DisplayClient.GRID;					
				rect.setLocation(rect.x, rect.y);
			}
			else{
				this.changeSprite("img/ido_right2.png");
			}
		}
		else if(key==KeyEvent.VK_LEFT) {
			if(SendData)
				ConnectionToServer.SendingDataToServer("left");
			if(rect.getMaxX()-1*DisplayClient.GRID>0&&this.sprite=="img/ido_left2.png"){
				rect.x=rect.x-1*DisplayClient.GRID;
				rect.setLocation(rect.x, rect.y);
			}
			else{
				this.changeSprite("img/ido_left2.png");
			}
		}
		
		else if(key==KeyEvent.VK_UP) {
			if(SendData)
				ConnectionToServer.SendingDataToServer("up");
			if(rect.getMaxY()-1*DisplayClient.GRID>0)
				rect.y=rect.y-1*DisplayClient.GRID;
			rect.setLocation(rect.x, rect.y);
			
			
			if(rect.x%50==0)
				rect.setLocation(rect.x, rect.y);
			else{
				rect.setLocation(roundTo(rect.x), rect.y);
			}
			//frog
			this.changeSprite("img/ido_front2.png");

		}
		else if(key==KeyEvent.VK_DOWN) {
			if(SendData)
				ConnectionToServer.SendingDataToServer("down");
			if(rect.getMaxY()+1*DisplayClient.GRID<DisplayClient.HEIGHT)
				rect.y=rect.y+1*DisplayClient.GRID;
			if(rect.x%50==0)
				rect.setLocation(rect.x, rect.y);
			else{
				rect.setLocation(roundTo(rect.x), rect.y);
			}
			this.changeSprite("img/ido_front2.png");

		}
	}
	
	public void StopTimer() {
		t.stop();
	}
	public void setDisplay(DisplayClient displayClient) {
		this.displayClient = displayClient;
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(!this.Sleep) {
			int key = e.getKeyCode();
			if(id==FROGCOLOR.BLUE) {
				this.moveBlueFrog(key, true);
			}
			else {
				this.moveRedFrog(key, SendData);
			}
			
		}
		
	}

	
}
