
public class Loc{

	public int x,y;
	public Loc(int x_loc, int y_loc){
		x = x_loc;
		y = y_loc;
	}//gets the increment or decrement based on the Direction
	public static int get_inc(boolean b, int num, Dir d){
		// True is x, false is y
		switch(d){
		case NORTH:
			return b?num-1:num;
		case WEST:
			return b?num:num-1;
		case SOUTH:
			return b?num+1:num;
		case EAST:
			return b?num:num+1;
		case NORTHEAST:
			return b?num-1:num+1;
		case NORTHWEST:
			return b?num-1:num-1;
		case SOUTHWEST:
			return b?num+1:num-1;
		case SOUTHEAST:
			return b?num+1:num+1;
		default:
			return 0;
		}
	}
	// prints location of a Color
	public String toString(){
		return "(" + x + ", " + y + ")";
	}
	// overrides equals to compare locations
	@Override
	public boolean equals(Object l){
		if(l != null && l instanceof Loc)
			return ((Loc) l).x == x && ((Loc) l).y == y;
		else{
			return false;
		}
	}
	

}
