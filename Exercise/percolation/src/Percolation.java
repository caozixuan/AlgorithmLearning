public class Percolation {
    private int points_amount;
    private int open_points_amount;
    private int[] id;
    private int[] sz;
    private boolean[] grid;
    private int n;
    private boolean isPercolates;

    public Percolation(int n) {
        this.isPercolates = false;
        this.open_points_amount = 0;
        this.n = n;
        points_amount = n * n + 2;
        id = new int[points_amount];
        sz = new int[points_amount];
        grid = new boolean[points_amount];

        for (int i = 0; i < points_amount; i++) {
            id[i] = i;
            sz[i] = 1;
            grid[i] = false;
        }

        grid[points_amount - 2] = true;
        grid[points_amount - 1] = true;

        for (int p = 0; p < n; p++) {
            union(p, points_amount - 2);
        }

        for (int p = n * n - n; p < n * n; p++) {
            union(p, points_amount - 1);
        }

    }

    public int numberOfOpenSites(){
        return this.open_points_amount;
    }

    private int root(int i) {
        while (i != id[i]) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    private boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    private void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) return;
        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        } else {
            id[j] = i;
            sz[i] += sz[j];
        }
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
        if (nearIndex > 0 && isOpen(row, col)) {
            union(index, nearIndex);
        }
    }

    public boolean isOpen(int row, int col) {
        int index = getIndex(row, col);
        return index > 0 && grid[index];
    }

    public boolean isFull(int row, int col) {
        int index = getIndex(row, col);
        return connected(index, points_amount - 2) && grid[index];
    }

    public boolean percolates() {
        return connected(points_amount - 1, points_amount - 2);
    }
}
