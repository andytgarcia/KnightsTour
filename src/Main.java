import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Scanner;

import javax.swing.JOptionPane;


///moves
//get all possible moves, don't move to locs that are on the board or in the exhausted list, choose move, push stack, add move to exhausted
//if ur stuck
//pop stack, clear board location, clear exhausted for undone move

/*
 * getPossibleMoves;
 * getNextMOve
 * if getNextMOve ! inExhausted
 *  stack.push
 *  addExhausted
 * else
 *  stack.pop
 *  clearexhausted
 */

/*
    0   1   2   3   4
0   304 0   56  0   304
1   0   56  0   56  0
2   56  0   64  0   56
3   0   56  0   56  0
4   304 0   56  0   304

 */


public class Main {

    final static int rowL = 5;//number of rows for chess board
    final static int colL = 5;//number of cols for chess board
    static Stack<Location> stack = new Stack<Location>(); //store moves in order (backtrack capability)

    //list of exhausted locations for each location.  Must use method convertLocToIndex to find a Locations proper index
    static ArrayList<ArrayList<Location>> exhausted = new ArrayList<ArrayList<Location>>(64);
    static int board[][] = new int[rowL][colL];//2d array used to store the order of moves
    static boolean visited[][] = new boolean[rowL][colL];//2d array used to store what locations have been used. NOT REQUIRED
    static Location startLoc;

    public static void main(String[] args) {



        System.out.println("START");
        initExhausted();

        ArrayList<Location> currentPossible;
        obtainStartLoc();
        System.out.println("Start Loc is " + startLoc);

        printPossibleMoveLocations(startLoc);
        printBoard();

        stack.push(startLoc);
        visited[startLoc.getRow()][startLoc.getCol()] = true;
        board[startLoc.getRow()][startLoc.getCol()] = 1;



        
        currentPossible = getPossibleMoves(startLoc);
        Location prev = startLoc;
        Location next = getNextMove(startLoc, currentPossible);

        
        System.out.println(exhausted.get(6).size());
        System.out.println(exhausted.get(0).size());

        int count = 2;
        Location temp;

        printBoard();

        while(stack.size() != rowL * colL && stack.size() != 0)
        {
            
            if(next != null && (board[next.getRow()][next.getCol()] == 0 && !inExhausted(prev, next))) {
            
                stack.push(next);
                addToExhausted(prev, next);
                printExhausedList(prev);
                board[next.getRow()][next.getCol()] = count;
                count++;
                temp = next;
                currentPossible = getPossibleMoves(temp);
                next = getNextMove(temp, currentPossible);
                prev = temp;
                printBoard();
            }
            
            else {
                    stack.pop();
                    clearExhausted(prev);
                    count--;
                    board[prev.getRow()][prev.getCol()] = 0;
                    prev = stack.peek();
                    currentPossible = getPossibleMoves(prev);
                    next = getNextMove(prev, currentPossible);
                    printBoard();
            
            

        }
        }
        /*if (inExhausted(prev, getNextMove(prev,currentPossible))) {
            currentPossible.remove(0);
            if (getNextMove(prev, currentPossible) == null) {
                clearExhausted(prev);
                stack.pop();
            }
        }
        else {
            printBoard();
            addToExhausted(prev, next);
            printExhausedList(prev);
            System.out.println();
            System.out.println("Possible moves for this loc: " + getPossibleMoves(prev));
            stack.push(next);
            board[next.getRow()][next.getCol()] = count;
            count++;
            temp = next;
            currentPossible = getPossibleMoves(temp);
            next = getNextMove(temp, currentPossible);
            prev = temp;

        }*/
        }
    
    
    

    /*
     * Printed out the exhausted list for a given Location
     */
    public static void printExhausedList(Location loc)
    {
        System.out.println("Exhausted List for " + loc + ": ");
        System.out.println();
        for (int i = 0; i < exhausted.get(convertLocToIndex(loc)).size(); i++) {
            System.out.print(exhausted.get(convertLocToIndex(loc)).get(i));

        }
    }

    /*
     * Prints out the possible move locations for a given Location
     */
    public static void printPossibleMoveLocations(Location loc)
    {
        ArrayList<Location> ans = getPossibleMoves(loc);
        System.out.print("Possible Moves: ");
        for (int i = 0; i < ans.size(); i++) {
            System.out.print(ans.get(i));
        }
    }

    /*
     * prints out the board (numbers correspond to moves)
     */
    public static void printBoard()
    {
        for (int i = 0; i < board.length; i++) {
            System.out.println();
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j]);
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.println();
    }

    /*
     * prints out true/false for what spaces have been visited
     */
    public static void printVisited()
    {
        System.out.print("All moves: ");
        for (int i = 0; i < stack.size(); i++) {
            System.out.print(stack.get(i));
        }
    }

    /*
     * clear out the exhausted list for a given Location
     * This needs to be done everytime a Location is removed from the Stack
     */
    public static void clearExhausted(Location loc)
    {
        for (int i = 0; i < exhausted.size(); i++) {
            if (i == convertLocToIndex(loc)){
                for (int j = exhausted.get(i).size() - 1; j > 0; j--) {
                    exhausted.get(i).remove(exhausted.get(i).get(j));
                }
            }
        }
    }

    /*
     * set up the exhausted list with empty exhausted lists.
     */
    public static void initExhausted()
    {
        for (int i = 0; i < 25; i++) {
            exhausted.add(new ArrayList<Location>(25));
        }

    }

    /*
     * is this dest Location exhausted from the source Location
     */
    public static boolean inExhausted(Location source, Location dest)
    {
        for (int j = 0; j < exhausted.get(convertLocToIndex(source)).size(); j++) {
            if (exhausted.get(convertLocToIndex(source)).get(j).getRow() == dest.getRow() && exhausted.get(convertLocToIndex(source)).get(j).getCol() == dest.getCol()) {
                return true;
                   }
               }
        return false;
    }

    /*
     * returns the next valid move for a given Location on a given ArrayList of possible moves
     */
    public static Location getNextMove(Location loc, ArrayList<Location> list)
    {
        for (int i = 0; i < list.size(); i++) {
            if (!inExhausted(loc,list.get(i))) {
                return list.get(i);
            }
        }
        return null;
    }

    /*
     * converts a (row,col) to an array index for use in the exhausted list
     */
    public static int convertLocToIndex(Location loc)
    {
        return (loc.getRow()*5) + (loc.getCol());
    }

    /*
     * adds a dest Location in the exhausted list for the source Location
     */
    public static void addToExhausted(Location source, Location dest)
    {
        for (int i = 0; i < exhausted.size(); i++) {
            if (i == convertLocToIndex(source)) {
                exhausted.get(i).add(dest);
            }
        }
    }

    /*
     * is this Location a valid one
     */
    public static boolean isValid(Location loc) {
        return loc.getRow() >= 0 && loc.getRow() < board.length && loc.getCol() >= 0 && loc.getCol() < board[0].length;
    }

    /*
     * returns another Location for the knight to move in.  If no more possible move
     * locations exist from Location loc, then return null
     */
    public static ArrayList<Location> getPossibleMoves(Location loc)
    {
        ArrayList<Location> posLocs = new ArrayList<>();
        Location p1, p2, p3, p4, p5, p6, p7, p8;
        p1 = new Location(loc.getRow() -2, loc.getCol() + 1);
        p2 = new Location(loc.getRow() -1, loc.getCol() + 2);
        p3 = new Location(loc.getRow() + 1, loc.getCol() + 2);
        p4 = new Location(loc.getRow() + 2, loc.getCol() + 1);
        p5 = new Location(loc.getRow() + 2, loc.getCol()-1);
        p6 = new Location(loc.getRow()+1, loc.getCol() - 2);
        p7 = new Location(loc.getRow() -1, loc.getCol() - 2);
        p8 = new Location(loc.getRow() - 2, loc.getCol() - 1);

        if (isValid(p1))
            posLocs.add(p1);
        if (isValid(p2))
            posLocs.add(p2);
        if (isValid(p3))
            posLocs.add(p3);
        if (isValid(p4))
            posLocs.add(p4);
        if (isValid(p5))
            posLocs.add(p5);
        if (isValid(p6))
            posLocs.add(p6);
        if (isValid(p7))
            posLocs.add(p7);
        if (isValid(p8))
            posLocs.add(p8);

        return posLocs;
    }


    /*
     * prompt for input and read in the start Location
     */
    public static void obtainStartLoc()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your start location: ");
        System.out.println("Enter the starting row: ");
        int row = scanner.nextInt();
        System.out.println("Enter the starting column: ");
        int col = scanner.nextInt();
        startLoc = new Location(row,col);

    }

}
