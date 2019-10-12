import java.util.ArrayList;

public class Board {

    private int[][] tiles;
    private int[][] correct;
    private int size;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.size = tiles.length;
        correct = new int[size][size];
        int number = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != size - 1 || j != size - 1) {
                    correct[i][j] = number;
                    number++;
                } else {
                    correct[i][j] = 0;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(String.format("%2d ", tiles[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int res = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != correct[i][j]&&tiles[i][j]!=0)
                    res++;
            }
        }
        return res;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int res = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = tiles[i][j];
                int rightX = 0;
                int righgY = 0;
                if (value != 0) {
                    rightX = (value - 1) / size;
                    righgY = (value - 1) % size;
                    res = res + Math.abs(rightX - i);
                    res = res + Math.abs(righgY - j);
                }

            }
        }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int h = hamming();
        if (h == 0)
            return true;
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if(y==null)
            return false;
        if(!(y instanceof Board))
            return false;
        Board anotherBoard = (Board) y;
        if (anotherBoard.size != this.size)
            return false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (anotherBoard.tiles[i][j] != this.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    private Board copyBoard() {
        int[][] newTiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newTiles[i][j] = tiles[i][j];
            }
        }
        Board b = new Board(newTiles);
        return b;
    }

    private Board exchange(int x1, int y1, int x2, int y2) {
        Board b = copyBoard();
        int tmp = b.tiles[x1][y1];
        b.tiles[x1][y1] = b.tiles[x2][y2];
        b.tiles[x2][y2] = tmp;
        return b;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<Board>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    if (i > 0) {
                        Board b = exchange(i, j, i - 1, j);
                        boards.add(b);
                    }
                    if (i < size - 1) {
                        Board b = exchange(i, j, i + 1, j);
                        boards.add(b);
                    }
                    if (j > 0) {
                        Board b = exchange(i, j, i, j - 1);
                        boards.add(b);
                    }
                    if (j < size - 1) {
                        Board b = exchange(i, j, i, j + 1);
                        boards.add(b);
                    }

                }
            }
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x1 = -1;
        int y1 = -1;
        int x2 = -1;
        int y2 = -1;
        int counter = 0;
        Board b = copyBoard();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (b.tiles[i][j] != 0) {
                    if (counter == 0) {
                        x1 = i;
                        y1 = j;
                        counter++;
                    } else if (counter == 1) {
                        x2 = i;
                        y2 = j;
                        counter++;
                    }
                }
            }
            if (counter == 2)
                break;
        }
        int tmp = b.tiles[x1][y1];
        b.tiles[x1][y1] = b.tiles[x2][y2];
        b.tiles[x2][y2] = tmp;
        return b;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
