package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;


import frogger.Frog.FROGCOLOR;


public class DisplayClient extends JPanel implements ActionListener{
	public static int GRID=50; 
	public static int ERRORY=10;
	public static int ERRORX=16;
	public static int WIDTH=1280+ERRORX;
	public static int HEIGHT=720-ERRORY;
    private boolean isThereRoad = false;
    //the timer which run the game
    private Timer t;
    //list containing all the cars in the level.
    public static List<Cars[][]> carsRoad = new ArrayList<Cars[][]>();
	private boolean StartGame = false;
	//the object we lock for our critical section
	public static Object lock = new Object();
	//the image of the background
	private BufferedImage image;
	//which level are we right now
	private int score=0;
	//list of all the roads in the level
	public static  List<Road> ROADS = new ArrayList<Road>();
	//list of all the frogs in the game
	private List<Frog> frogs = new ArrayList<Frog>();

	private String color;
	
	public DisplayClient(){
	
		setFrogs();
		loadMap();
		initializeGame();	
		setFocusable(true);
		t=new Timer(10, new GameCycle());
	}
	
	public void SetColor(String color) {
		this.color = color;
		if(this.color.equals("Blue")) {
			this.addKeyListener(frogs.get(0));
			this.frogs.get(0).SetSendData(true);
		}
		else if(this.color.equals("Red")) {
			this.addKeyListener(frogs.get(1));
			this.frogs.get(1).SetSendData(true);
		}
	}
	
	 private class GameCycle implements ActionListener {
	        
	        public void actionPerformed(ActionEvent e) {
	        
	            doGameCycle();
	        }
	    }

	    private void doGameCycle() {
	    	if(isThereRoad) {
	    		//lock the ROADS list and move every car.
	    		synchronized (lock) {
	    			for(Road r :ROADS) {
	    		        
	        			r.moveCars();
	        			}
				}
        	
        		}
	    	repaint();
	    	
	    }

	
	public void StartGame(String d) {
		//starting the game
		if(!this.StartGame) {
		if(d.equals("start")) {
			this.t.start();
			this.StartGame = true;
			}
		}
	}



	

	private void setFrogs() {
		//set the frogs
		Frog frog= new Frog(Globals.X_FROG,HEIGHT-90,Globals.W_FROG,Globals.H_FROG,FROGCOLOR.BLUE);
		Frog frog2= new Frog(Globals.X_FROG-100,HEIGHT-90,Globals.W_FROG,Globals.H_FROG,FROGCOLOR.RED);
		
		frogs.add(frog);
		frogs.add(frog2);
		for(Frog f:frogs) {
			f.setFrogs(frogs);
			f.setDisplay(this);
		}
	
	}

	public void avoidCollision(GameObject[] objects,int distance){
		for (int i =1; i<objects.length;i++) {
			if(objects[i-1].distanceBetweenObjects(objects[i])<=distance) {
				objects[i-1].setSpeed(objects[i].speed);
	
		}
		
	}

	}
	public void AddScore() {
		this.score++;
	}
	public void AddScoreAndResetFrogs(int level) {
		this.score=level;
		for (Frog frog : frogs) {
			frog.HopToNextLevel();
		}
		initializeGame();
	}
	public  void  initializeGame(){
		//initialize every level
		//this function get called from a thread sometimes so we want to protect the lists with sync
		resetLists();
		//everytime we start a level we set a timer to count how many sec it takes to complete the level.
		// for every level we lock frogs and ROADS and carROADS
		synchronized (lock) {
			
		
		if(score==0) {
			level1();
			for (Frog frog:frogs) {
				frog.SetTimeStarted(System.currentTimeMillis());
			}	
		
		}
		else if(score==1) {
			level2();
			for (Frog frog:frogs) {
				frog.SetTimeStarted(System.currentTimeMillis());
			}	

		}
		else if(score==2) {
			level3();
			for (Frog frog:frogs) {
				frog.SetTimeStarted(System.currentTimeMillis());
			}	

		}
		else if(score==3) {
			level4();
			for (Frog frog:frogs) {
				frog.SetTimeStarted(System.currentTimeMillis());
				}	

			}
		}
	}


	public void resetLists() {
	
		//clear all the lists
		this.isThereRoad = false;
		//locks the frogs ROADS and carROAD lists
		synchronized (lock) {
			for(Frog f:frogs) {
			
				f.setWonLevel(false);
				f.setDead(false);
			}
		
		
		
			ROADS.removeAll(ROADS);
		
			this.carsRoad.removeAll(this.carsRoad);

		}
		
	
		
		
	}
	private void level1() {
		int numofroads=2;
		this.isThereRoad = true;
		int dis =0;
		for(int i = 0;i<numofroads;i++) {
			ROADS.add(new Road(0,Globals.HEIGHT-((DisplayClient.GRID*(i+4)+dis)),1,1,5,5,this.frogs,i*70,i*35));
			this.carsRoad.add(ROADS.get(i).getCARS());
			dis+=DisplayClient.GRID*4;
		}
	}

	private void level2() {
		this.isThereRoad = true;
		int numofroads= 3;
		int dis =0;
		
		for(int i = 0;i<numofroads;i++) {
			ROADS.add(new Road(0,Globals.HEIGHT-((DisplayClient.GRID*(i+4)+dis)),1,1,2+i,2+i,this.frogs,i*70,i*35));
			this.carsRoad.add(ROADS.get(i).getCARS());
			dis+=DisplayClient.GRID*2;
		}
	}
	
	
	private void level3() {
		int numofroads=5;
		this.isThereRoad = true;
		for(int i = 0;i<numofroads;i++) {
			ROADS.add( new Road(0,Globals.HEIGHT-(2)*(DisplayClient.GRID*(i+2)),1,1,i+1,i+1,this.frogs,i*70,i*35));
			this.carsRoad.add(ROADS.get(i).getCARS());
		}
		
	}
	private void level4() {
		int numofroads=5;
		this.isThereRoad = true;
		for(int i = 0;i<numofroads;i++) {
			ROADS.add( new Road(0,Globals.HEIGHT-(2)*(DisplayClient.GRID*(i+2)),1,1,5,5,this.frogs,i*70,i*35));
			this.carsRoad.add(ROADS.get(i).getCARS());
		}
		
	}


	public void loadMap(){
		try {
			image= ImageIO.read(getClass().getResourceAsStream("img/map4.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	

	public void renderGame(Graphics g){
		removeAll();
		g.drawImage(image, 0, 0, this);
		synchronized (lock) {
			for(Road r :ROADS) {
				r.render(g,this);
				}
			
		
			for(Frog f:this.frogs) {
				f.render(g,this);
			}
			
		}
		
	
	} 
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		if(this.StartGame) {
		renderGame(g);

		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void KillFrog() {
		this.frogs.get(1).killFrog();
		
	}
	//note: frogs[blue,red]
	public void KillOtherFrog() {
		if(this.color.equals("Blue")) {
			this.frogs.get(1).killFrog();
		}
		else if(this.color.equals("Red")) {
			this.frogs.get(0).killFrog();
		}
		
	}
	public void MoveOtherFrog(int key) {
		if(this.color.equals("Blue")) {
			this.frogs.get(1).moveFrog(key,false);
		}
		else {
			this.frogs.get(0).moveFrog(key,false);
		}
		
	}
	

	





}
