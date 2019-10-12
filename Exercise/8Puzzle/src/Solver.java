import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Solver {
    private GameTreeNode solveNode;
    private boolean canSolve = false;

    private class GameTreeNode implements Comparable<GameTreeNode>{
        GameTreeNode father;
        Board board;
        int moveNum;
        int priority;
        GameTreeNode(Board b, GameTreeNode father)
        {
            this.board = b;
            this.father = father;

            if(father!=null)
            {
                this.moveNum = father.moveNum+1;
                this.priority = this.board.manhattan()+this.moveNum;
            }
            else
            {
                this.moveNum = 0;
                this.priority = this.board.manhattan()+this.moveNum;
            }
        }

        @Override
        public int compareTo(GameTreeNode o) {

            return this.priority - o.priority;
        }


    }
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        GameTreeNode root = new GameTreeNode(initial, null);
        GameTreeNode rootTwin = new GameTreeNode(initial.twin(),null);
        MinPQ<GameTreeNode> q = new MinPQ<>();
        MinPQ<GameTreeNode> qTwin = new MinPQ<>();
        q.insert(root);
        qTwin.insert(rootTwin);
        while(true)
        {
            GameTreeNode curNode = q.delMin();
            if(curNode.board.isGoal())
            {
                canSolve = true;
                solveNode = curNode;
                break;
            }
            else
            {
                for(Board b:curNode.board.neighbors())
                {
                    if (curNode.father == null || !b.equals(curNode.father.board)) {
                        GameTreeNode node = new GameTreeNode(b, curNode);
                        q.insert(node);
                    }
                }
            }

            GameTreeNode curNodeTwin = qTwin.delMin();
            if(curNodeTwin.board.isGoal())
            {
                canSolve = false;
                solveNode = null;
                break;
            }
            else
            {
                for(Board b:curNodeTwin.board.neighbors())
                {
                    if(curNodeTwin.father==null||!b.equals(curNodeTwin.father.board))
                    {
                        GameTreeNode node = new GameTreeNode(b,curNodeTwin);
                        qTwin.insert(node);
                    }
                }
            }

        }



    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return canSolve;
    }

    // min number of moves to solve initial board
    public int moves(){
        return solveNode.moveNum;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution(){
        GameTreeNode curNode = solveNode;
        ArrayList<Board> boards = new ArrayList<>();
        while(curNode!=null)
        {
            boards.add(0,curNode.board);
            curNode = curNode.father;
        }
        return boards;
    }

    // test client (see below)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}