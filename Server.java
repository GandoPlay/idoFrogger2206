package frogger;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;



public class Server {
    private static final int PORT = 5555;
    public static final int MAX_USERS = 2;
    private static ArrayList<Player>players = new ArrayList<Player>();
    private static LevelScore[] ArrayLevel = new LevelScore[4];
	private static int level=0; 
	private String [] ColorArr = new String[2];
	 private static Semaphore semaphore =  new Semaphore(1);
	 private static DB dataBase = new DB();

	
	
	private static void AddItemToArrayLevel(int numPlayer, int levelPlayer,int timeToFinish, Player p) {
		//numPlayer=> the id of the player
		//levelPlayer=> the level that the player just won
		//p=> the player.	
		try {
			semaphore.acquire();
			//if the player won the relevant level
			if(level==levelPlayer) {
				
				//if the array[level] is empty(no other player put his id)
				if(ArrayLevel[level].getPlayerId()==-1) {
					System.out.println(level+"before");
					ArrayLevel[level].setPlayerId(numPlayer);
					ArrayLevel[level].setTimeElspsed(timeToFinish);
					//the server tells to all the players to move to the next level
					
					level++;
					SendDataToAllSockets2("NEXTLEVEL="+level);
					
					// the game is over: 
					if(level==4) {
						for (int i = 0; i < players.size(); i++) {
							String name = players.get(i).getName();
		    				int score = players.get(i).getLevelScore().calculateScore(ArrayLevel);
		    				AddPlayerToDataBase(name,score);
		    				
						} 
						 SendDataBase();
						 if(numPlayer==0) {
							 SendDataToPlayer("WON", players.get(0));
							 SendDataToPlayer("LOST", players.get(1));
						 }
						 else {
							 SendDataToPlayer("WON", players.get(1));
							 SendDataToPlayer("LOST", players.get(0));
						 }						
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 
		semaphore.release();
		
	}
	
    public Server() {
    	ColorArr[0] = "Red";
    	ColorArr[1] = "Blue";
    	for(int i =0;i<ArrayLevel.length;i++) {
    		ArrayLevel[i] = new LevelScore();
    	}

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        	System.out.println(serverSocket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server is ON");
        int numberOfClientsConnected = 0;

        System.out.println("Waiting for " + (MAX_USERS - numberOfClientsConnected) + " more users");
        
        Socket socket;
       
        while(MAX_USERS>numberOfClientsConnected) {
        	try {
    			socket = serverSocket.accept();
    		    System.out.println("User " + socket.getInetAddress().getHostName() + " has connected");
    	        Player player;
    	        LevelScore levelScore = new LevelScore();
    	        levelScore.setPlayerId(numberOfClientsConnected);
    	        
                player = new Player(socket,"",ColorArr[numberOfClientsConnected],levelScore);// (numberOfClientsConnected));
                String name = player.getBufferedReader().readLine();
                player.setName(name);
                System.out.println(name);
               // players[numberOfClientsConnected] = player;
                players.add(player);
                numberOfClientsConnected++;
	      		 socket = player.getSocket();
	      		 
	      		  new Thread(new Runnable() {
	      			  @Override
	      			  public void run() {
	      				  StartReceivingData(player);
	      			  }
	      		  }).start();
	      

             
    	}
    		 catch(Exception e) {
           	  e.printStackTrace();
             }
        }
        
        for (int i = 0; i < players.size(); i++) {
			SendDataToPlayer(ColorArr[i], players.get(i));
		}
		 
		this.startGame();
    }
		  



  

    public static void CloseAllSockets() throws IOException {
       // System.out.println("Closing all sockets.");
        if(players!=null)
        for (Player player : players) {
            Socket socket = player.getSocket();
            System.out.println("Closing Socket " + socket.getInetAddress().getHostName());
            if (socket.isConnected())
                socket.close();
        }
    }




    private static void SendDataBase() {
    	// send to the clients the database information.
    	ArrayList<DataBaseObject> db = dataBase.showDataBase();
    	String send = "";
    	for(DataBaseObject item:db) {
    		send+=item.getName()+","+item.getScore()+"@";
    	}
    	SendDataToAllSockets2("DATABASE@"+send);
    	
    }
    private void startGame() {
        System.out.println("Game Started.");

        //the server send when all the clients need to start the game
        LocalTime time = LocalTime.now().plusSeconds(1);
        
        SendDataToAllSockets2("TIME"+time.toString());
		
    }
    
    
    private void StartReceivingData(Player player) {
    	
        
            try {

                String dataFromClient = "";
                while (true) {
             		
            	
                    dataFromClient = player.getBufferedReader().readLine();
                    if(dataFromClient==null) {
                    	continue;
                    }
                    if(level<4&&dataFromClient.contains("=")) {
                        for(int i = 0;i<this.ColorArr.length;i++) {
                        	String[] messageArr = dataFromClient.split("=");
                       	 	if(messageArr[0].equals("WONLEVEL"+this.ColorArr[i].toUpperCase())) {
                            	AddItemToArrayLevel(i, Integer.parseInt(messageArr[1]),Integer.parseInt(messageArr[2]),player);	
                            }
                       }
                    }
                               
                    else {       
                    BroadcastMessage(player,dataFromClient);
                    }

                    
                    }

                
        
                    
            } catch (IOException e) {
            	try {
    				CloseAllSockets();
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
                System.out.println("There was an error receiving data.");
            }
        
    }
 private static void AddPlayerToDataBase(String name, int score) {
	 dataBase.insertToDataBase(name, score);
		
	}



public static void BroadcastMessage(Player sender, String data) {
	 for(Player player: players) {
		 if(player!=null) {
			 
		 
		 if(!player.getName().equals(sender.getName())) {
			 try {
		         player.getBufferedWriter().write(data);
				 player.getBufferedWriter().newLine();
				 player.getBufferedWriter().flush();
			 }
		    catch (IOException e) {
		    	try {
					CloseAllSockets();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            e.printStackTrace();
	        }
		 }
	 }
	 }
 }
 public static void SendDataToPlayer(String data, Player player) {
		 try {
		 player.getBufferedWriter().write(data);
	     player.getBufferedWriter().newLine();
	     player.getBufferedWriter().flush();
		 }
	  catch (IOException e) {
			try {
				CloseAllSockets();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	     e.printStackTrace();
	 }
 }
 public static void SendDataToAllSockets2(String data) {
	 for(Player player: players) {
        try {
            player.getBufferedWriter().write(data);
            player.getBufferedWriter().newLine();
            player.getBufferedWriter().flush();
        } catch (IOException e) {
        	try {
				CloseAllSockets();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            e.printStackTrace();
        }
	 }
        
    }
  
    }

