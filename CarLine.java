package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

public class CarLine {
	private Cars[] cars;
	private int  maxSpeed = 4;
	private int minSpeed = 2;
	private int y;
	private int x;
	private boolean dir;
	private List<Cars> listOfCars;
	public CarLine(Cars[] cars,int y, boolean dir, int x) {
		this.cars = cars;
		this.x = x;
		this.y = y;
		this.dir = dir;
		if(!dir) { 
			maxSpeed*=-1;
			minSpeed*=-1;
		}
		
	}
	public Cars[] getCars() {
		return cars;
	}
	public void SetCarLine( List<Cars> listOfCars) {
		int monex=0;
		if(!this.dir)
			minSpeed*=-1;
		for(int i = 0;i<cars.length;i++) {
			cars[i] = new Cars(i*200+monex-x,this.y,100,50,minSpeed,this.dir,1-i%2,null);
			monex+=70;
			listOfCars.add(cars[i]);
		}
		
	}
	public void moveCars() {
		for(Cars car: cars) {
			car.moveCars();
			
		}
		this.avoidCollision(cars,300);
	}
	public void render(Graphics g,DisplayClient d) {
		for(Cars car:this.cars) {
			car.render(g, d);
		}
	}
	
	public void avoidCollision(GameObject[] objects,int distance){
		// a car check if the car in the dir is close, if it, have a speed like them.
		for (int i =1; i<objects.length;i++) {
			if(objects[i-1].distanceBetweenObjects(objects[i])<=distance) {
				objects[i-1].setSpeed(objects[i].speed);
			
		}
		
	}
	
	}
}
