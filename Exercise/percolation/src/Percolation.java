import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf1;
    private WeightedQuickUnionUF uf2;
    private int points_amount;
    private int open_points_amount;
    private boolean[] grid;
    private int n;

    public Percolation(int n) {
        this.open_points_amount = 0;
        this.n = n;
        points_amount = n * n + 2;

        uf1 = new WeightedQuickUnionUF(points_amount);
        uf2 = new WeightedQuickUnionUF(points_amount-1);

        grid = new boolean[points_amount];

        for (int i = 0; i < points_amount; i++) {
            grid[i] = false;
        }

        grid[points_amount - 2] = true;
        grid[points_amount - 1] = true;

        for (int p = 0; p < n; p++) {
            uf1.union(p,points_amount-2);
            uf2.union(p,points_amount-2);
        }

        for (int p = n * n - n; p < n * n; p++) {
            uf1.union(p, points_amount - 1);
        }

    }

    public int numberOfOpenSites(){
        return this.open_points_amount;
    }

    private int getIndex(int row, int col) {
        if (row > 0 && row < n + 1 && col > 0 && col < n + 1) {
            return (row - 1) * n + (col - 1);
        }
        return -1;
    }

    public void open(int row, int col) {
        int index = getIndex(row, col);
        grid[index] = true;
        open_points_amount++;
        unionPoints(index, row - 1, col);
        unionPoints(index, row + 1, col);
        unionPoints(index, row, col - 1);
        unionPoints(index, row, col + 1);
    }

    private void unionPoints(int index, int row, int col) {
        int nearIndex = getIndex(row, col);
        if (nearIndex >= 0 && isOpen(row, col)) {
            uf1.union(index, nearIndex);
            uf2.union(index, nearIndex);
        }
    }

    public boolean isOpen(int row, int col) {
        int index = getIndex(row, col);
        return index >= 0 && grid[index];
    }

    public boolean isFull(int row, int col) {
        int index = getIndex(row, col);
        return uf2.connected(index, points_amount - 2) && grid[index];
    }

    public boolean percolates() {
        return uf1.connected(points_amount - 1, points_amount - 2);
    }
}
