package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import javax.imageio.ImageIO;

public class GameObject   implements Serializable {
	protected Rectangle rect;
	protected int speed,x, y, w, h;
	protected BufferedImage image;
	protected String sprite;
	protected Random rand= new Random();



	GameObject(int x,int y,int w,int h,int speed){
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.speed=speed;	
		rect=new Rectangle(x,y,w,h);
	}
	

	
	
	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getW() {
		return w;
	}


	public void setW(int w) {
		this.w = w;
	}


	public int getH() {
		return h;
	}


	public void setH(int h) {
		this.h = h;
	}


	public int getSpeed() {
		return speed;
	}


	public void changeSprite(String sprite){
		try {
			image= ImageIO.read(getClass().getResourceAsStream(sprite));
			this.sprite = sprite;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void move(){
		rect.x=rect.x+speed;
	}
	public void randomSpeed() {
		if(isOut()) {
			speed = rand.nextInt(5)+1;
			
		}
	}
	public boolean isOut(){
		return rect.getMinX()>=DisplayClient.WIDTH||rect.getMaxX()<=0;
	}
	public Rectangle getRect() {
		return rect;
	}
	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	public int distanceBetweenObjects(GameObject other) {
		return Math.abs(other.x-this.x);
	}

	
}
