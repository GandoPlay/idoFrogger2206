package frogger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JFrame;


public class ConnectionToServer {
    private final int PORT ;//= 5555;
    private final String IP;// = "127.0.0.1";// "192.168.1.105";
    private static Socket socket;
    private int i=0;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private String name;
    private boolean win=false;
    ArrayList<DataBaseObject> db = new ArrayList<DataBaseObject>();
	private JFrame frame;
    public ConnectionToServer(String playerName,String IP,int PORT) {
        this.IP=IP;
        this.PORT=PORT;
        name = playerName;
        
        System.out.println("Trying to connect to server");
        StartConnectionToServer();
        System.out.println("Connect to server");
        
            
    		 //sending the server the client's name
            SendingDataToServer(playerName);
            
            //creating the game frame
            DisplayClient Game = new DisplayClient();           
            frame= new JFrame(name); 		
            frame.add(Game);	
            frame.setSize(Globals.WIDTH,Globals.HEIGHT);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            		
            
            //StartReceivingData(Game);
            new Thread(new Runnable() {
    			  @Override
    			  public void run() {
    				  try {
						StartReceivingData(Game);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			  }
    		  }).start();
        
        
        

    }

    public static void SendingDataToServer(String data) {
    	
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    private void StartReceivingData( DisplayClient Game)  throws IOException {
    //this function receive data from the server	
    
        try {
         
        	 InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
            	String dataFromServer = "";
            	dataFromServer  = bufferedReader.readLine();
            	//if dataFromServer is null that means the connection is over.
            	if(dataFromServer==null) {
            		break;
            		
            	}
            	//the client receive the time when the game will begin.
            	if(dataFromServer.contains("TIME")) {
            		LocalTime time = LocalTime.parse(dataFromServer.substring(4));
            		while(time.isAfter(LocalTime.now())) {
            		}
            		//starting the game
            		Game.StartGame("start");            		
            	}
            	//the client's color is red
            	 if(dataFromServer.equals("Red")) {
            		Game.SetColor("Red");
            	}
            	//the client's color is blue
            	else if(dataFromServer.equals("Blue")) {
            		Game.SetColor("Blue");
            	}
            	 //the client won the game
            	else if(dataFromServer.equals("WON")) {
            		this.win = true;
            		socket.close();
            	}
            	//the client lost the game
            	else if(dataFromServer.equals("LOST")) {
            		this.win = false;
            		socket.close();
            	}
            	 //the other client moved right
            	else if(dataFromServer.equals("right")) {
            		Game.MoveOtherFrog(KeyEvent.VK_RIGHT);
            	}
            	//the other client moved left
            	else if(dataFromServer.equals("left")) {
            		Game.MoveOtherFrog(KeyEvent.VK_LEFT);
            	}
            	//the other client moved down
            	else if(dataFromServer.equals("down")) {
            		Game.MoveOtherFrog(KeyEvent.VK_DOWN);
            	}
            	//the other client moved up
            	else if(dataFromServer.equals("up")) {
            		Game.MoveOtherFrog(KeyEvent.VK_UP);
            	}
            	//the other client is dead
            	else if(dataFromServer.equals("DEAD")) {
            		Game.KillOtherFrog();
            	}
            	//moving to the next level
            	else if(dataFromServer.contains("NEXTLEVEL")) {
            		int score = Integer.parseInt(dataFromServer.split("=")[1]);
            		Game.AddScoreAndResetFrogs(score);
            	}
            	 //the client receive the score and names of the database.
            	else if(dataFromServer.contains("DATABASE")) {
       			
       			String[] arr = dataFromServer.split("@");
       			for(int i =1;i<arr.length;i++) {
       				String [] player = arr[i].split(",");
       				System.out.println(player[0]);
       				db.add(new DataBaseObject(player[0], Integer.parseInt(player[1])));
       			}

            	}

            }
          
        } catch (IOException e) {
            System.out.println("There was an error receiving data.");
    
        }
       frame.setVisible(false);
       frame.dispose();
       ScoreMenu endFrame = new ScoreMenu(this.isWin(), this.GetDataBase());
       endFrame.RunMenu(); 
}
    public  ArrayList<DataBaseObject> GetDataBase(){
    	return db;
    }
    public boolean IsSocketClosed() {
    	System.out.println(socket.isClosed());
    	return socket.isClosed();
    }
    

    private void StartConnectionToServer() {
        try {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            socket = new Socket(IP, PORT);
            
            try {
    			inputStream = socket.getInputStream();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        } catch (IOException e) {
            StartConnectionToServer();
        }
    }

	public boolean isWin() {
		return win;
	}

}
