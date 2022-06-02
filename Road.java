package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Road extends GameObject {
	private Cars [][]CARS;
	private List<Frog> frogs = new ArrayList<Frog>();
	private int id=0;
	private List<Cars> listOfCars = new ArrayList<Cars>();
	private CarLine [] carLines = new CarLine[2];
	public Road(int x,int y,int w,int h, int numCar1, int numCar2, List<Frog> frogs,int line1x,int line2x){
		super(x,y,w,h,0);
		changeSprite("img/road2.png");
		CARS = new Cars[2][0];
		CARS[0] = new Cars[numCar1];
		CARS[1] = new Cars[numCar2];
		this.frogs = frogs;
		this.carLines[0] = new CarLine(CARS[0], this.y, true,line1x);
		this.carLines[1] = new CarLine(CARS[1], this.y+50, false,line2x);
		for(int i = 0;i<this.carLines.length;i++) {
			carLines[i].SetCarLine(listOfCars);
		}
		
	}
	public void SETCARS(Cars[][]cars) {
		CARS = cars;
	}

	


	public Cars[][] getCARS() {
		CARS[0] = this.carLines[0].getCars();
		CARS[1] = this.carLines[1].getCars();
		return CARS;
	}
	public List<Cars> getCars(){
		return this.listOfCars;
	}

	


	public void moveCars() {
		this.carLines[0].moveCars();
		this.carLines[1].moveCars();
	}
	public void render(Graphics g, DisplayClient d){
		Graphics2D g2d= (Graphics2D)g;
		g2d.drawImage(image, rect.x, rect.y, d);
	
		this.carLines[0].render(g,d);
		this.carLines[1].render(g,d);
		
	
			
		
	}
		

	
}
