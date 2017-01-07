import java.util.Scanner;

public class Gamer {
	public void playGame(Board b, Scanner in, Color white, Color black, boolean p1IsComp,boolean p2IsComp){
		b.printBoard();
		boolean keepRun = true;
		System.out.println("");
		if(b.hasValMoves(black)){
			
			if(!p1IsComp){
				b.playInput(in, black);
			}
			else{
				//A.I for player 1
				b.playAI(in, black);
			}
			printScore(b);
		}
		else{
			keepRun = false;
			printScore(b);
			System.out.println("GAME OVER, No moves found for Black");
		}
		
		b.printBoard();

		if(keepRun && b.hasValMoves(white)){
			
			if(!p2IsComp){
				b.playInput(in, white);
			}
			else{
				//A.I for player 2
				b.playAI(in, white);
			}
			printScore(b);
		}
		else if(keepRun){
			keepRun = false;
			printScore(b);
			System.out.println("GAME OVER, No moves found for White");
		}
		
		if(keepRun){
			playGame(b,in,white,black,p1IsComp,p2IsComp);
		}
	}
	//gamer constructor that is made in the main.
	public Gamer(){
		int n = 0;
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the size of the board");
		n = input.nextInt();
		int mode = 0;
		System.out.println("Enter the mode you want to play\n1-Player vs Computer\n2-Computer vs Computer\n3- Player vs Player");
		mode = input.nextInt();
		Board b = new Board(n);
		Color P1 = Color.BLACK;
		Color P2 = Color.WHITE;
		//player vs computer
		if( mode ==1){
		playGame(b,input,P2,P1,false,true);
		}
		//computer vs computer
		else if(mode==2){
		playGame(b,input,P2,P1,true,true);
		//player vs player
		}else if(mode == 3){
		playGame(b,input,P2,P1,false,false);
		}
		else
		{
			System.out.println("quit");
		}

		
	}
	public void printScore(Board b){
		int blackScore = b.getScore(Color.BLACK);
		int whiteScore = b.getScore(Color.WHITE);
		System.out.println("Score: Black: "+blackScore+", White: "+whiteScore);
	}
}
