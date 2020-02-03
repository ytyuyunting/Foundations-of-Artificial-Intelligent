
import java.awt.Point;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.LinkedList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Set;
import java.util.TreeMap;
import java.io.FileWriter; 
import java.io.IOException;
import java.lang.Math;

public class homework{
	public static class Action{
		public Point start;
		public Point end;
		public Deque<Point> path;
		public Action() {
			this.start= new Point();
			this.end = new Point();
			this.path= null;
		}
		public Action(Point start,Point end, Deque<Point> path) {
			this.start= start;
			this.end = end;
			this.path= path;
		}
	}
	public static int COLS = 16;
	public static int ROWS = 16;
	static String color;
	static int maxdepth=1;
	static int breath=10;
	static double alpha;
	static double beta;
	int x;
	int y;	
	static Set<Point> leftTopCamp = new HashSet<Point>();
	static Set<Point> rightBottomCamp = new HashSet<Point>();
	//initial variables
	static char[][] chessBoard = new char[ROWS][COLS];
	static String playMethod = null;
	static float time;
	static HashSet<Point> setPositions = new HashSet<Point>();
	
    
	public static void main(String[] args) {
		//read file
		readfile();	
		/*
		 * for(int i=0; i<16; i++) { for(int j=0; j<16; j++) {
		 * System.out.print(chessBoard[i][j]); } System.out.print("\n"); }
		 */
		
		constructLeftTopCamp();
	    constructRightBottomCamp();		
	    setPositions= piecesPositions(chessBoard);
		//main
		String strout = "";
		Action lastAction = new Action();
		lastAction= readplaydata();
		Action max_action = MiniMax(chessBoard, lastAction);
		Point startPoint= max_action.start;
		Point endPoint = max_action.end;
		String moveMethod="";		
		if(Math.abs(endPoint.x-startPoint.x)<2 && Math.abs(endPoint.y-startPoint.y)<2) {
			moveMethod ="E";
			strout+= moveMethod+ " "+startPoint.y+","+ startPoint.x+" "+ endPoint.y+"," +endPoint.x;
		}
		else {
			moveMethod ="J";
			Deque<Point> jumpPath= max_action.path;
			Point secondPoint;
			for(Point point: jumpPath) {
				if(!point.equals(startPoint)) {
					secondPoint = point;
					strout += moveMethod+ " "+startPoint.y+","+ startPoint.x+" "+ secondPoint.y+"," +secondPoint.x +"\n";
					startPoint= secondPoint;
				}			
			}
			strout= strout.substring(0, strout.length()-1);
		}						
		String playdata_str= ""+max_action.start.x+" "+ max_action.start.y+" "+ max_action.end.x+" "+max_action.end.y;  
		outputdata(playdata_str);

		//write output file
		outputfile(strout);
		System.out.println(strout);
		///timer.cancel();

	}
	//readplaydata();
	public static Action readplaydata() {
		Action lastAction= new Action();
		try {
		      File inputObj = new File("../playdata.txt");
		      Scanner inputReader = new Scanner(inputObj);
		      int i=0;
		      while (inputReader.hasNext()) {
		        String str = inputReader.next();
		        if(i==0) {lastAction.end.x = Integer.parseInt(str);}
		        else if(i==1) {lastAction.end.y = Integer.parseInt(str);}
		        else if(i==2) {lastAction.start.x = Integer.parseInt(str);}
		        else if(i==3) {lastAction.start.y = Integer.parseInt(str);}
		        i++;
		      }
		      inputReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("There is no playdata.");
		    }
		return lastAction;
	}
	// output playdata file
		public static void outputdata(String str) {
			try {
			      FileWriter myWriter = new FileWriter("../playdata.txt");
			      myWriter.write(str);			      
			      myWriter.close();
			
			    } catch (IOException e) {
			      System.out.println("An error occurred when writing a file.");
			      e.printStackTrace();
			    }
		}	
	//read input file
	public static void readfile() {
		// read input file
		try {
		      File inputObj = new File("../input.txt");
		      Scanner inputReader = new Scanner(inputObj);
		      int i=0;
		      while (inputReader.hasNextLine()) {
		        String str = inputReader.nextLine();
		        if(i==0) {
		        	playMethod = str;
		        }
		        else if(i==1) {
		        	color = str;
		        }
		        else if(i==2) {
		        	time = Float.parseFloat(str);
		        }
		        else {
		        	for(int j=0; j<ROWS; j++) {
			        	chessBoard[i-3][j] = str.charAt(j);
			        }
		        }		        
		        i++;
		      }
		      inputReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred when opening the input file.");
		    }
	}
	// output file
	public static void outputfile(String str) {
		try {
		      FileWriter myWriter = new FileWriter("../output.txt");
		      myWriter.write(str);
		      myWriter.close();
		     
		    } catch (IOException e) {
		      System.out.println("An error occurred when writing a file.");
		      e.printStackTrace();
		    }
	}
	// output time remaining file
	public static void outputtime(String str) {
		try {
			 FileWriter myWriter = new FileWriter("calibrate.txt");
			 myWriter.write(str);
			 myWriter.close();
			 System.out.println("Successfully wrote to the file(time).");
		} catch (IOException e) {
			System.out.println("An error occurred when writing a file(time).");
			e.printStackTrace();
		}
	}
	// read time remaining
	public static double readtime() {
		double remaintime= time;
		try {
		      File inputObj = new File("calibrate.txt");
		      Scanner inputReader = new Scanner(inputObj);
		      while (inputReader.hasNext()) {
		        String str = inputReader.next();
		        remaintime= Double.parseDouble(str);
		      }
		      inputReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("There is no calibrate.txt.");
		    }
		return remaintime;
	}
	// construct RightBottomCamp
	public static void constructRightBottomCamp() {
		for(int i=15; i>10; i--) {
			switch (i) {
			  case 15:
			    for(int j=15; j>10; j--) {
			    	rightBottomCamp.add(new Point(i,j));
			    }
			    break;
			  case 14:
				  for(int j=15; j>10; j--) {
					  rightBottomCamp.add(new Point(i,j));
				    }
			    break;
			  case 13:
			    for(int j=15; j>11; j--) {
			    	rightBottomCamp.add(new Point(i,j));
			    }
			    break;
			  case 12:
				  for(int j=15; j>12; j--) {
					  rightBottomCamp.add(new Point(i,j));
				    }
			    break;
			  case 11:
				  for(int j=15; j>13; j--) {
					  rightBottomCamp.add(new Point(i,j));
				    }
			    break;			  
			}		
		}
	}
	// construct LeftTopCamp
	public static void constructLeftTopCamp() {
	    for(int i=0; i<5; i++) {
			switch (i) {
			  case 0:
			    for(int j=0; j<5; j++) {
			    	leftTopCamp.add(new Point(i,j));
			    }
			    break;
			  case 1:
				  for(int j=0; j<5; j++) {
					  leftTopCamp.add(new Point(i,j));
				    }
			    break;
			  case 2:
			    for(int j=0; j<4; j++) {
			    	leftTopCamp.add(new Point(i,j));
			    }
			    break;
			  case 3:
				  for(int j=0; j<3; j++) {
					  leftTopCamp.add(new Point(i,j));
				    }
			    break;
			  case 4:
				  for(int j=0; j<2; j++) {
					  leftTopCamp.add(new Point(i,j));
				    }
			    break;			  
			}		
		}
	    
	}
	// return pieces' positions
	public static HashSet<Point> piecesPositions(char[][] board){
		HashSet<Point> setPositions=new HashSet<Point>();
		for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLS; j++) {
				char c;
				if(color.equals("WHITE")) { c= 'W';}
				else { c= 'B';}
				if(board[i][j]== c) {
					setPositions.add(new Point(i,j));
				}
			}
		}
		return  setPositions;
	}
	// return opponent's positions
	public static HashSet<Point> opponentPositions(char[][] board){
			HashSet<Point> setopponents=new HashSet<Point>();
			for (int i=0; i<ROWS; i++) {
				for (int j=0; j<COLS; j++) {
					char c;
					if(color.equals("WHITE")) { c= 'B';}
					else { c= 'W';}
					if(board[i][j]== c) {
						setopponents.add(new Point(i,j));
					}
				}
			}
			return  setopponents;
		}
	
	private static HashSet<Point> copySet(HashSet<Point> originSet) {
		HashSet<Point> newSet = new HashSet<Point>();
		for (Point piece : originSet) {
			newSet.add(piece);
		}
		return newSet;
	}
	//Return a new state after an action on Black pieces on the original state
	public static char[][] Result(char[][] resultboard, Action action) {
		resultboard[action.end.x][action.end.y]=resultboard[action.start.x][action.start.y];
		resultboard[action.start.x][action.start.y]='.';
		if(setPositions.contains(action.start)) {
			setPositions.remove(action.start);
			setPositions.add(action.end);
		}		
		return resultboard;
	}
	//Return a new state after an action on Black pieces on the original state
	public static char[][] Restore(char[][] resultboard, Action action) {
			resultboard[action.start.x][action.start.y]=resultboard[action.end.x][action.end.y];
			resultboard[action.end.x][action.end.y]='.';
			if(setPositions.contains(action.end)) {
			setPositions.remove(action.end);
			setPositions.add(action.start);
			}
			return resultboard;

		}
	//Return an action according to this situation
	public static Action MiniMax(char[][] board, Action lastAction) {
		double max_cur = Double.NEGATIVE_INFINITY;
		int b=breath;
		alpha= Double.NEGATIVE_INFINITY;
		beta= Double.POSITIVE_INFINITY;
		Action max_action = new Action();
		HashSet<Point> Positions = new HashSet<Point>();
		int flag=1;// check if these two alternatives are possible or not
		boolean ownCamp= false;
		boolean add= false;
		while(flag==1||flag==0 || flag==-1) {
			HashSet<Point> list_own = noleaveOwn(board,color);
			if(list_own.size()==0||flag==0) {
				Positions= setPositions;
			}else {
				Positions = list_own;
				ownCamp= true;
			}						
			HashSet<Action> actions= new HashSet<Action>();
			TreeMap<Double, Action> orignActions= new TreeMap<Double, Action>();
			HashSet<Point> newPositions = copySet(Positions);
			for(Point piece: newPositions) {
				if(flag==-1) {
					add=true;
				}
				orignActions.putAll(Actions(piece, board, color, add));
			}			
			actions = generateActions(b, orignActions);
			for(Action action: actions) {
				if(action.start.equals(lastAction.start) && action.end.equals(lastAction.end)) {
					continue;
				}
				double cur = MinValue(Result(board, action), 0 , b, add);
				board = Restore(board, action);				
				//System.out.println(action.path+"\n");
				//System.out.println("cur="+cur+"\n");	
				if(cur> max_cur) {
					max_cur = cur;
					max_action = action;					
					}
				if(max_cur>= beta) {
					break;
				}
				alpha = Math.max(alpha, max_cur);
				}
			if(actions.size()!=0) {
				flag=2;
			}
			if(ownCamp) {
				if(flag==1) {flag=0;}//actions is empty
				else {flag=2;}
			}
			else {flag=2;}
			//add more options to avoid repetition
			if(max_action.path!=null && max_action.start.equals(max_action.end)) {
				flag=-1;
			}
			
		}
		
		return max_action;
	}
	//Return a utility value(evaluation function)
	public static double MinValue(char[][]board, int cur_depth, int b, boolean add) {
		double minValue;

		if(cutoffTest(board,cur_depth)) {
			//System.out.println(evalFunc(setPositions, false));
			return evalFunc(setPositions, true);
		}
		minValue= Double.MAX_VALUE;		
		HashSet<Action> actions= new HashSet<Action>();	
		TreeMap<Double, Action> orignActions= new TreeMap<Double, Action>();
		String oppoColor;
		if (color.equals("BLACK")) { oppoColor= "WHITE";}
		else { oppoColor= "BLACK";}
		HashSet<Point> Positions = copySet(setPositions);
		for(Point piece: Positions) {
			orignActions. putAll(Actions(piece, board, oppoColor, add));		
		}
		actions = generateActions(b,orignActions);
		for(Action action: actions) {
			double cur = MaxValue(Result(board, action),cur_depth, b, add);
			board = Restore(board, action);
			if(cur< minValue) {
				minValue = cur;
			}
			if(minValue<= alpha) {
				break;
			}
			beta = Math.min(beta, minValue);
		}
		return minValue;
	}
	
	//Return a utility value(evaluation function)
	public static double MaxValue(char[][]board,int cur_depth, int b, boolean add) {
		double maxValue;
		if(cutoffTest(board,cur_depth)) {
			return evalFunc(setPositions, true);
		}
		maxValue= Double.NEGATIVE_INFINITY;		
		HashSet<Action> actions= new HashSet<Action>();
		TreeMap<Double, Action> orignActions= new TreeMap<Double, Action>();	
		HashSet<Point> Positions = copySet(setPositions);
		
		for(Point piece: Positions) {
			orignActions.putAll(Actions(piece, board, color, add));		
		}
		actions = generateActions(b,orignActions);
		for(Action action: actions) {				
			double cur = MinValue(Result(board, action), cur_depth+1 , b, add);
			board = Restore(board, action);
			if(cur> maxValue) {
				maxValue = cur;
			}
			if(maxValue>= beta) {
				break;
			}
			alpha = Math.max(alpha, maxValue);
		}
		
		return maxValue;
	}
	
	/*Return true if the game is over or it reaches the maxdepth.
	*/
	public static boolean cutoffTest(char[][] board,int depth) {		
		if(depth>=maxdepth) {
			return true;
		}
		if(goalTest(board)) {return true;}
		return false;		
	}
	
	//Return the evaluation in this situation(the piece's location)
	//@para
	public static double evalFunc(HashSet<Point> positions, boolean isplayer) {		
		double utility=0;		
		Point destPlayer;
		Point destOppo;
		if(color.equals("WHITE")) {
			destPlayer= new Point(0,0);
			destOppo =new Point(15,15);
		}
		else {
			destPlayer= new Point(15,15);
			destOppo= new Point(0,0);
		}
		if(isplayer) {
			for(Point piece: positions) {			
				utility += Math.sqrt((piece.x-destPlayer.x)*(piece.x-destPlayer.x) + (piece.y-destPlayer.y)*(piece.y-destPlayer.y));
			}
		}
		else {
			for(Point piece: positions) {			
				utility += Math.sqrt((piece.x-destOppo.x)*(piece.x-destOppo.x) + (piece.y-destOppo.y)*(piece.y-destOppo.y));
			}
		}		
		utility= -utility;
		return utility;		
	}
	//
	public static double evalAction(Action action, String color) {
		double valueDiff;
		double startValue;
		double endValue;
		Point des;
		if(color.equals("WHITE")) {
			des=new Point(0,0);
		}
		else {
			des=new Point(15,15);
		}
		startValue= Math.sqrt((action.start.x-des.x)*(action.start.x-des.x) + (action.start.y-des.y)*(action.start.y-des.y));
		endValue= Math.sqrt((action.end.x-des.x)*(action.end.x-des.x) + (action.end.y-des.y)*(action.end.y-des.y));
		valueDiff = endValue-startValue;
		//Random rd = new Random();
		//valueDiff += 0.1*rd.nextDouble();
		return valueDiff;
	}
	//generate limited actions
	public static HashSet<Action> generateActions(int b, TreeMap<Double, Action> orignActions) {
		HashSet<Action> actions= new HashSet<Action>();
		Iterator<Action> iter = orignActions.values().iterator();
		int num=0;
		while(iter.hasNext() && num<=b) {
			Action action =iter.next();
			if(inToOut(action.start, action.end)) {
				actions.add(action);
				num++;
			}						
		}
		if(actions.isEmpty()) {
			Iterator<Action> iter2 = orignActions.values().iterator();
			while(iter2.hasNext() && num<=b) {
				Action action2 =iter2.next();
				if(!actions.contains(action2)) {
					actions.add(action2);
					num++;
				}
			}
		}		
		return actions;
	}
	/* Return an TreeMap which contains legal move to adjacent pieces
	 * map.key= point; map.value= parent point
	 * Move to the empty square that is adjacent to the piece's original position(with 8-adjacency)	
	 * @param (a,b) represents the location of original piece.
	 */
	public static TreeMap<Double, Action> Actions(Point point, char[][] board, String color, boolean add) {
		
		int a= point.x;		
		int b= point.y;
		TreeMap<Double, Action> actions= new TreeMap<Double, Action>();
		HashMap<Point,Point> mapActions=new HashMap<Point,Point>();		
		mapActions.putAll(moveToAdjacent(a, b, board, color, add));		
		Iterator<Map.Entry<Point, Point>> iter = mapActions.entrySet().iterator(); 	
		while (iter.hasNext()) { 
			  Map.Entry<Point, Point> entry = iter.next();
			  Deque<Point> path= new LinkedList<Point>();
			  path.addFirst(entry.getKey());
			  path.addFirst(entry.getValue());
			  Action action= new Action(entry.getValue(),entry.getKey(),path);			  
			  actions.put(evalAction(action, color),action);
			}			
		Deque<Point> list =new LinkedList<Point>();
	    jumpGreedy(list, a, b, board, color, add);
		if(!list.isEmpty()) {
			list.addFirst(new Point(a,b));
			Action jumpAction = new Action(list.getFirst(),list.getLast(), list);
			actions.put(evalAction(jumpAction, color),jumpAction);
		}
		
		return actions;
	}
	
	/* Return an HashMap which contains legal move to adjacent pieces
	 * Move to the empty square that is adjacent to the piece's original position(with 7-adjacency)(except(a-1,b-1))
	 * At the same time, change every node's location()	
	 * @param (a,b) represents the location of original piece.
	 */
	public static HashMap<Point,Point> moveToAdjacent(int a, int b, char[][] board, String color, boolean add) {
		boolean black=true;
		boolean white= true;
		if(color.equals("BLACK") && leftTopCamp.contains(new Point(a,b))) {black=false;}
		if(color.equals("WHITE") && rightBottomCamp.contains(new Point(a,b))) {white=false;}
		HashMap<Point,Point> mapAdjacent=new HashMap<Point,Point>();
	    
		if(0<=a+1 && a+1<ROWS && 0<=b && b<COLS ) {
			if(color.equals("BLACK")||add) {
				if(board[a+1][b]== '.' && white) {		    	
		    		Point adj = new Point(a+1, b);
		    		mapAdjacent.put(adj, new Point(a, b));		    			    			    			    	
			    }
			}
	    	
	    }
	    if(0<=a+1 && a+1<ROWS && 0<=b+1 && b+1<COLS) {
	    	if(color.equals("BLACK")) {
	    		if(board[a+1][b+1]== '.' && white) {
	    			Point adj = new Point(a+1, b+1);
		    		mapAdjacent.put(adj, new Point(a, b));   		
			    }
	    	}	    	
	    }
	    if(0<=a && a<ROWS && 0<=b+1 && b+1<COLS) {
	    	if(color.equals("BLACK")||add) {
	    		if(board[a][b+1]== '.' && white) {
		    			Point adj = new Point(a, b+1);
		    			mapAdjacent.put(adj, new Point(a, b));		    	
			    }
	    	}
	    	
	    }
	    if(0<=a-1 && a-1<ROWS && 0<=b+1 && b+1<COLS) {
	    	if(board[a-1][b+1]== '.' && black && white) {
	    		if(checkCondition(a,b, a-1, b+1)) {
	    			Point adj = new Point(a-1, b+1); 
	    			mapAdjacent.put(adj, new Point(a, b)); 
	    		}
		    }
	    }
	    if(0<=a-1 && a-1<ROWS && 0<=b && b<COLS) {
	    	if(color.equals("WHITE")||add) {
	    		if(board[a-1][b]== '.' && black) {
		    			Point adj = new Point(a-1, b);
		    			mapAdjacent.put(adj, new Point(a, b));		    	
			    }
	    	}
	    	
	    }
	    if(0<=a && a<ROWS && 0<=b-1 && b-1<COLS) {
	    	if(color.equals("WHITE")||add) {
	    		if(board[a][b-1]== '.' && black) {
		    			Point adj = new Point(a, b-1);
		    			mapAdjacent.put(adj, new Point(a, b));
			    }
	    	}	    	
	    }
	    if(0<=a+1 && a+1<ROWS && 0<=b-1 && b-1<COLS) {
	    	if(board[a+1][b-1]== '.' && black && white) {
	    		if(checkCondition(a,b, a+1, b-1)) {
	    			Point adj = new Point(a+1, b-1);
	    			mapAdjacent.put(adj, new Point(a, b));	
	    		}	    			    	
		    }
	    }
	    if(0<=a-1 && a-1<ROWS && 0<=b-1 && b-1<COLS) {
	    	if(color.equals("WHITE")) {
	    		if(board[a-1][b-1]== '.' && black) {	    		    			
		    			Point adj = new Point(a-1, b-1);
			    		mapAdjacent.put(adj, new Point(a, b));    				    	
			    }
	    	}	    	
	    }
		return mapAdjacent;
	}
	
	/* Return an HashMap which contains legal jumps.
	 * Just one jump over adjacent pieces(with 5-adjacency).
	 * An adjacent piece of any color can be jumped if there is 
	 * an empty square on the directly opposite side of that piece. 
	 * @param (a,b) represents the location of original piece.
	 */
	public static void jumpGreedy(Deque<Point> list, int a, int b, char[][] board, String color, boolean add) {	
		if(color.equals("BLACK")) {
			if(0<=a+2 && a+2<ROWS && 0<=b+2 && b+2<COLS && board[a+1][b+1]!= '.' && board[a+2][b+2]== '.') {			
			    	Point jump = new Point(a+2, b+2);
			    	list.addLast(jump);
			    	jumpGreedy(list, jump.x, jump.y, board, color, add);	    	
			 }
			else if(0<=a+2 && a+2<ROWS && 0<=b && b<COLS &&board[a+1][b]!= '.'&& board[a+2][b]== '.') {
				    Point jump = new Point(a+2, b);
				    list.addLast(jump);
				    jumpGreedy(list, jump.x, jump.y, board, color, add);	    	
			 }
			else if(0<=a && a<ROWS && 0<=b+2 && b+2<COLS && board[a][b+1]!= '.' &&board[a][b+2]== '.') {
				    Point jump = new Point(a, b+2);		
				    list.addLast(jump);
				    jumpGreedy(list, jump.x, jump.y, board, color, add);			    	
			}	
			else if(0<a+2 && a+2<ROWS && 0<b-2 && b-2<COLS && checkCondition(a, b, a+2, b-2) && board[a+1][b-1]!= '.' && board[a+2][b-2]== '.' ) {
				Point jump = new Point(a+2, b-2);
				boolean repeat=false;
				if(list.size()>=2) {
					list.pollLast();
					Point jumppre= list.getLast();
					if(!jump.equals(jumppre)) {
						list.addLast(new Point(a,b));
					} 
					else {
						repeat=true;
					}
				}			
				if(repeat==false) {					
					list.addLast(jump);
					jumpGreedy(list, jump.x, jump.y, board, color, add);
				}
									    	 	
			}
			else if(0<a-2 && a-2<ROWS && 0<b+2 && b+2<COLS && checkCondition(a, b, a-2, b+2) && board[a-1][b+1]!= '.' && board[a-2][b+2]== '.' ) {
				Point jump = new Point(a-2, b+2);
				boolean repeat=false;
				if(list.size()>=2) {
					list.pollLast();
					Point jumppre= list.getLast();
					if(!jump.equals(jumppre)) {
						list.addLast(new Point(a,b));
					} 
					else {
						repeat=true;
					}
				}			
				if(repeat==false) {					
					list.addLast(jump);
					jumpGreedy(list, jump.x, jump.y, board, color, add);
				}
			  									    	 						    	 	
			}
		}
				    						    		
		else {
			if(0<a-2 && a-2<ROWS && 0<b && b<COLS  && board[a-1][b]!= '.' && board[a-2][b]== '.') {
				  Point jump = new Point(a-2, b);
				  list.addLast(jump);
				  jumpGreedy(list, jump.x, jump.y, board, color, add);					         	
			}
			else if(0<a-2 && a-2<ROWS && 0<b-2 && b-2<COLS && board[a-1][b-1]!= '.'&& board[a-2][b-2]== '.') {	
				Point jump = new Point(a-2, b-2);
				list.addLast(jump);
				jumpGreedy(list, jump.x, jump.y, board, color, add);					      	
		 }
			else if(0<a && a<ROWS && 0<b-2 && b-2<COLS && board[a][b-1]!= '.' && board[a][b-2]== '.' ) {
				Point jump = new Point(a, b-2);
				list.addLast(jump);
				jumpGreedy(list, jump.x, jump.y, board, color, add);					    	 	
			}
			else if(0<a+2 && a+2<ROWS && 0<b-2 && b-2<COLS && checkCondition(a, b, a+2, b-2) && board[a+1][b-1]!= '.' && board[a+2][b-2]== '.' ) {
				Point jump = new Point(a+2, b-2);				
				boolean repeat=false;
				if(list.size()>=2) {
					list.pollLast();
					Point jumppre= list.getLast();
					if(!jump.equals(jumppre)) {
						list.addLast(new Point(a,b));
					} 
					else {
						repeat=true;
					}
				}			
				if(repeat==false) {					
					list.addLast(jump);
					jumpGreedy(list, jump.x, jump.y, board, color, add);
				}				    	 	
			}
			else if(0<a-2 && a-2<ROWS && 0<b+2 && b+2<COLS && checkCondition(a, b, a-2, b+2) && board[a-1][b+1]!= '.' && board[a-2][b+2]== '.' ) {
				Point jump = new Point(a-2, b+2);
				boolean repeat=false;
				if(list.size()>=2) {
					list.pollLast();
					Point jumppre= list.getLast();
					if(!jump.equals(jumppre)) {
						list.addLast(new Point(a,b));
					} 
					else {
						repeat=true;
					}
				}			
				if(repeat==false) {					
					list.addLast(jump);
					jumpGreedy(list, jump.x, jump.y, board, color, add);
				}						    	 	
			}
			
		} 
		
	}
	
	/*check if a move starts outside own camp and ends up in own camp
	* check if a move starts inside opposite camp and ends up outside opposite camp
	* Return false on above two conditions.
	*/
	public static boolean checkCondition(int a, int b, int c, int d) {
		if(noReachOppose(a,b)==false && noReachOppose(c,d)) {
			return false;
		}
		if(noReachOwn(a,b)==true && noReachOwn(c,d)==false) {
			return false;
		}
		return true;
	}
	
	/* Return HashSet of the Points which is in the player's own camp; 
	 * If there is no piece in player's own camp , 
	 * return empty HashSet(size=0) if there is no pieces in the player's camp.
	 * @param board represents current situation.
	 * @param color represents which color is own
	*/
	public static HashSet<Point> noleaveOwn(char[][] board, String color) {
		HashSet<Point> list_own= new HashSet<Point>();
		if(color.equals("BLACK")) {
			for (Point p:leftTopCamp) {
				if(board[p.x][p.y]=='B') {list_own.add(p);}
			}
		}
		if(color.equals("WHITE")) {
			for (Point p:rightBottomCamp) {
				if(board[p.x][p.y]=='W') {list_own.add(p);}
			}
		}
		return list_own;
	}
	/* Check if a piece hasn't reached the opposing camp
	 * Return false if the piece has reached; true if it hasn't reached.
	 * @param (a,b) represents the location of final piece.
	 * @param color represents which color is own
	*/
	public static boolean noReachOppose(int a, int b) {
		if(color.equals("BLACK")) {
			if(rightBottomCamp.contains(new Point(a,b))) {
				return false;				
			}
		}
		if(color.equals("WHITE")) {
			if(leftTopCamp.contains(new Point(a,b))) {
				return false;
			}
		}
		return true;
	}
	/* Check if a piece hasn't reached own camp
	 * Return false if the piece has reached; true if it doesn't reached.
	 * @param (a,b) represents the location of final piece.
	 * @param color represents which color is own
	*/
	public static boolean noReachOwn(int a, int b) {
		if(color.equals("BLACK")) {
			if(leftTopCamp.contains(new Point(a,b))) {
				return false;
			}
			
		}
		if(color.equals("WHITE")) {
			if(rightBottomCamp.contains(new Point(a,b))) {
				return false;
			}			
		}
		return true;
	}
	/* Check if a piece has leave own camp and end up outside the camp
	 * Return true if it reach outside.
	 * @param (a,b) represents the location of final piece.
	 * @param color represents which color is own
	*/
	public static boolean inToOut(Point p, Point q) {		
		if(color.equals("BLACK")) {
			if(leftTopCamp.contains(p) && !leftTopCamp.contains(q)) {
				return true;
			}
		}
		if(color.equals("WHITE")) {
			if(rightBottomCamp.contains(p) && !rightBottomCamp.contains(q)) {
				return true;
			}
		}
		return false;
	}
	/* Check if the game is over or not
	 * Return true if the game is over.
	 * @param board (input board)
	 */
	public static boolean goalTest(char[][] board) {
		if(checkWWin(board)) {return true;}
		if(checkBWin(board)) {return true;}
		return false;

	}
	/* Check if the white player wins or not
	 * Return true if the white player wins.
	 * @param board (input board)
	 */
	public static boolean checkWWin(char[][] board) {
		int numW=0;
		for (Point p:leftTopCamp) {
			if(board[p.x][p.y]=='.') {return false;}
	    	else if(board[p.x][p.y]=='W') {numW++;}
		}
		if(numW>=1) {return true;}
		return false;
	}
	/* Check if the white player wins or not
	 * Return true if the white player wins.
	 * @param board (input board)
	 */
	public static boolean checkBWin(char[][] board) {
		int numB=0;
		for (Point p:rightBottomCamp) {
			if(board[p.x][p.y]=='.') {return false;}
	    	else if(board[p.x][p.y]=='B') {numB++;}
		}
		if(numB>=1) {return true;}
		return false;
	}
	
	

}