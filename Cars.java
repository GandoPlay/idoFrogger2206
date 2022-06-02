package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;




public class Cars extends GameObject {
	private boolean dir;
	private List<Frog> frogs = new ArrayList<Frog>();
    private int MinSpeed = 2;
    private int MaxSpeed = 4;
    boolean level = true;
	public Cars(int x,int y,int w,int h,int speed, boolean dir,int carType, List<Frog> frogs){
		super(x,y,w,h,speed);
		this.frogs = frogs;
		this.dir = dir;
		setCarType(carType);
		
	}
	public void SetMinMaxSpeed(int min, int max) {
		this.MinSpeed = min;
		this.MaxSpeed = max;
	}
	
	
	public String toString() {
		return "\nCars\n"+"dir=" + dir+"\n"+super.toString()+"\n";
	}
	
	
	
	public void setCarType(int temp){
		if(dir){
			if(temp==1)
				changeSprite("img/car_rightB.png");
			else 
				changeSprite("img/car_rightR.png");
		}
		else{
			this.x = Globals.WIDTH;
			this.speed = -this.speed;
			if(temp==1)
				changeSprite("img/car_leftR.png");
			else 
				changeSprite("img/car_leftB.png");
		}
	}
	public void setSpeed(int s) {
		this.speed = s;
		
	}

	   public void moveCars() {
		   move();
			if(isOut()){
				//if moving right
				if(dir) {
					if(this.speed == this.MaxSpeed ) {
						this.speed = MinSpeed;
					}
					else {
						this.speed++;
					}
				}
				//if moving left
				if(!this.dir) {
					if(this.speed>0)
						this.speed = -this.speed;
					if(this.speed==this.MaxSpeed*-1) {
						this.speed--;
					}
					else {
						this.speed = this.MinSpeed*-1;
					}
					//if the car is at the most left - get back to the other side
					if(rect.x<0) {
						
						rect.x=Globals.WIDTH;
						
					}
			
				}
				//if the car is at the most right - get back to the other side
				else {		
				if(rect.x>=Globals.WIDTH) {
					rect.x=-100;
					
				}
			
		}
		}
		}
	public void render(Graphics g, DisplayClient d){
		Graphics2D g2d= (Graphics2D)g;
		g2d.drawImage(image, rect.x, rect.y, d);
	}
	

	
	

	public void draw(Graphics g2d, DisplayClient d) {
        g2d.drawImage(this.image, rect.x, rect.y, this.image.getWidth(), this.image.getHeight(), d);//draws the paddle on this screen

		
	}
}

	

