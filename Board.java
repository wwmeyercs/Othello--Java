import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	//debug boolean
	public boolean debug = false;
	// array that holds the array of Color enums that make the board
	public Color arr [][];
	public static int size = 4;
	public Board(){
		this(size);
		}
	// Checks direction to see if the piece is out of bounds
	public boolean checkDir(int x, int y, Dir d){
		if(debug)
			System.out.println("Checking Direction");
		if(d==Dir.EAST){
			return y<size;
		}
		if(d==Dir.WEST){
			return y>=0;
		}
		if(d==Dir.SOUTH){
			return x<size;
		}
		if(d==Dir.NORTH){
			return(x>=0);
		}
		if(d==Dir.NORTHWEST){
			return (x>=0&&y>=0);
		}
		if(d==Dir.NORTHEAST){
			return y<size&&x>=0;
		}
		if(d==Dir.SOUTHWEST){
			return y>=0&&x<size;
		}		
		if(d==Dir.SOUTHEAST){
			return x<size&&y<size;
		}
		return false;
	}
	//returns number of moves
	public int getCountMoves(Loc l,Color c){
		return findMoves(l,c).size();
	}
	//returns an ArrayList of the possible moves returned from getMoves of each direction
	public ArrayList<Loc> findMoves(Loc l, Color c){
		ArrayList<Loc> count = new ArrayList<Loc>();
		count.addAll(getMoves(l,c,Dir.NORTH));
		count.addAll(getMoves(l,c,Dir.NORTHEAST));
		count.addAll(getMoves(l,c,Dir.EAST));
		count.addAll(getMoves(l,c,Dir.SOUTHEAST));
		count.addAll(getMoves(l,c,Dir.SOUTH));
		count.addAll(getMoves(l,c,Dir.SOUTHWEST));
		count.addAll(getMoves(l,c,Dir.WEST));
		count.addAll(getMoves(l,c,Dir.NORTHWEST));

		return count;
	}//returns an ArrayList the possible moves from a direction
	public ArrayList<Loc> getMoves(Loc l, Color c, Dir d){
		ArrayList<Loc> moves = new ArrayList<Loc>();
		boolean matchingTileFound = false;
		//resets x & y using the get_inc function in Loc class
		int y = Loc.get_inc(false,l.y,d);
		int x = Loc.get_inc(true,l.x,d);
		//while the tile in d direction is valid keep running
		while(checkDir(x,y,d)){
			//resets getColor after arr[][] increments
			Color getColor  = arr[x][y];
			// when color of arr[][] is the same as passed c
			if(c == getColor){
				matchingTileFound = true;
				break;
			}
			// when the tile is empty
			else if(getColor==Color.NONE){
				break;
			}
			// add to moves when not invalid placement
			else
			{
				moves.add(new Loc(x,y));
			}
			y = Loc.get_inc(false,y,d);
			x = Loc.get_inc(true,x,d);
			
		}
		//if no matching tile clear moves
		if(!matchingTileFound){
			moves.clear();
		}
		
		return moves;
	}//returns a Set of scores from the HashMap getAllMoveScores
	public Set<Loc> getAllMoves(Color c){
		Set<Loc> p = getAllMoveScores(c).keySet();
		return p;
	}
	
	//assigns the locations to a score to be used with the user move validation and the AI
	public HashMap<Loc,Integer> getAllMoveScores(Color c){
		if(debug)
			System.out.println("DEBUG");
		HashMap<Loc,Integer> p = new HashMap<Loc,Integer>();
		//for that counts how many point the player or AI would gain from that move
		for(int i=0; i<size;i++){
			for(int j=0;j<size;j++){
				if(arr[i][j] == Color.NONE){
					if(debug)
						System.out.println("CHECKING");
					int steps = getCountMoves(new Loc(i,j),c);
					if(steps>0){
						
						p.put(new Loc(i,j), new Integer(steps));
					}
				}
			}
		}
		return p;
	}
	//gets player input
	public void playInput(Scanner in, Color c){
		System.out.println(c+": Enter desired move as \"Row, Column\"");
		String line = null;
		line = in.next();
		boolean valid = true;
		String[] linearr = line.split(",");
		//if the user doesn't enter the right format is invalid 
			try{
				 Integer.valueOf(linearr[1]);
				 Integer.valueOf(linearr[0]);

			}
			// catch to call playInput again to get new input
			catch(Exception e){
				System.out.println("Invalid move, try again");
				playInput(in,c);		
				valid = false;
				}
		if(valid){
		Loc l = new Loc(Integer.valueOf(linearr[0]),Integer.valueOf(linearr[1]));
		ArrayList<Loc> possMoves = new ArrayList<Loc>();
		//possMoves keeps track of all possible moves
		possMoves.addAll(getAllMoves(c));
		System.out.println("Trying to move to " + l);
		if(possMoves.contains(l)){
			ArrayList<Loc> userLoc = findMoves(l,c);
			changeLocations(userLoc,c);
			arr[l.x][l.y] = c;
		}
		else{
			System.out.println("Invalid move, try again");
			playInput(in,c);
		}
		}
	}
	public void playAI(Scanner in, Color c){
		//A.I
		if(debug)
			System.out.println("DEBUG A.I.");
		Map.Entry<Loc, Integer> maxEntry = null;
		HashMap<Loc,Integer> scores = getAllMoveScores(c);
		// uses hashmap to find the moves with the largest points gained
		for (Map.Entry<Loc, Integer> entry : scores.entrySet())
		{
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		    {
		        maxEntry = entry;
		    }
		}
		//AI makes the best move of that turn
		ArrayList<Loc> userLoc = findMoves(maxEntry.getKey(),c);
		changeLocations(userLoc,c);
		arr[maxEntry.getKey().x][maxEntry.getKey().y] = c;
		System.out.print("Press enter to Continue");
		in.nextLine();
		in.nextLine();
	}
	// gets the number of tiles of a certain color
	public int getScore(Color c){
		int count = 0;
		for(int i = 0; i<size; i++){
			for(int j = 0; j<size; j++){
				if(arr[i][j] == c)
					count++;
			}
			}
				return count;
	}
	// AI logic to change location
	public void changeLocations(ArrayList<Loc> arrList, Color c){
		for(Loc i:arrList){
			arr[i.x][i.y] = c;
		}
	}
	// bool to check if a player has any valid moves
	public boolean hasValMoves(Color c){
		return getAllMoves(c).size()>0;
	}
	// prints the initial board
	public Board(int n){
		size = n;
		arr = new Color[n][n];
		for(int i = 0; i<n; i++){
			for(int j = 0; j<n; j++){
				
				arr[i][j] = Color.NONE;
				if((i==n/2-1&&j==n/2)||i==n/2&&j==n/2-1){
					arr[i][j] = Color.BLACK;
				}
				if((i==n/2-1&&j==n/2-1)||i==n/2&&j==n/2){
					arr[i][j] = Color.WHITE;
				}
			}
		}
	}
	// prints the board any time after the first print
	public void printBoard(){
		for(int i = 0; i<size; i++){
			for(int j = 0; j<size; j++){
				
				System.out.print(getShortcut(arr[i][j]));
				}
			System.out.println();
			}
	}
	// returns B W or _ based on the corresponding enum values
	public String getShortcut(Color c)
	{
		switch(c){
		case BLACK:
			return "B";
		case WHITE:
			return "W";
		default:
			return "_";
		}
	}
		
	}

