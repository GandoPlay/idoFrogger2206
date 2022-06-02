package frogger;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Player {
    private String name="";
    private String color;
    private Socket socket;
    private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;	
	private LevelScore levelScore;

    public Player(Socket socket,String name,String color,LevelScore levelScore) {
        this.socket = socket;
        this.name = name;
        this.levelScore = levelScore;
        this.color = color;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public LevelScore getLevelScore() {
		return levelScore;
	}

	public void setLevelScore(LevelScore levelScore) {
		this.levelScore = levelScore;
	}

	public String getName() {
        return name;
    }

    public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	public void setBufferedWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}

	public Socket getSocket() {
        return socket;
    }

  
}
