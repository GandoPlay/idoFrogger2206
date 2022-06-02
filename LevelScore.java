package frogger;

public class LevelScore {
	private int PlayerId;
	private int timeElspsed;
	public LevelScore() {
		timeElspsed = -1;
		PlayerId = -1;
	}
	public int calculateScore(LevelScore[] arr) {
		int sumPoints = 0;
		int sumTime = 0;
		for(LevelScore item:arr) {
			if(item.getPlayerId()==this.PlayerId) {
				sumPoints+=100;
				sumTime = item.getTimeElspsed();
			}
		}
		this.timeElspsed = sumTime;
		if(sumTime>50){
			return sumPoints-50; 
		}
		return sumPoints-sumTime;
		
	}
	public int getPlayerId() {
		return PlayerId;
	}
	public void setPlayerId(int playerId) {
		PlayerId = playerId;
	}
	public int getTimeElspsed() {
		return timeElspsed;
	}
	public void setTimeElspsed(int timeElspsed) {
		this.timeElspsed = timeElspsed;
	}
	
}
