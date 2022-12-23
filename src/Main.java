import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class BattleShip {
    Random random = new Random();
    Scanner scanner = new Scanner(System.in);

    // Game config
    int w = 10, h = 10;
    int[] sizes = {5, 4, 3, 3, 2};  // index=shipNum
    int maxShots = 32;


    // Game state
    int[][] grid = new int[h][w];  // -1=No-ship    or   shipNum
    boolean[][] shotsGrid = new boolean[h][w];
    int[] hits = new int[sizes.length];
    int totalSize, totalHits, totalShots;
    int row, col;

    void start() {
        init();
        arrangeShips();
        for (; ; ) {
            // printShipsGrid();
            printGame();
            if (totalSize == totalHits) {
                System.out.println("\033[1;92m YOU WIN!!!!! \033[0m");
                break;
            } else if (totalShots == maxShots) {
                System.out.println("\033[1;92m YOU LOOSE :( \033[0m");
                break;
            }
            takeMove();
            shoot();
        }
    }

    void init() {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                grid[i][j] = -1;
            }
        }

        totalSize = Arrays.stream(sizes).sum();
    }

    void arrangeShips() {
        for (int shipNum = 0; shipNum < sizes.length; shipNum++) {
            arrangeShip:
            for (; ; ) {
                boolean horizontal = random.nextBoolean();
                int colSpan = horizontal ? sizes[shipNum] : 1;
                int rowSpan = !horizontal ? sizes[shipNum] : 1;
                int rowStart = random.nextInt(h - rowSpan);
                int colStart = random.nextInt(w - colSpan);

                for (int i = rowStart; i < rowStart + rowSpan; i++) {
                    for (int j = colStart; j < colStart + colSpan; j++) {
                        if (grid[i][j] != -1) {
                            continue arrangeShip;
                        }
                    }
                }

                for (int i = rowStart; i < rowStart + rowSpan; i++) {
                    for (int j = colStart; j < colStart + colSpan; j++) {
                        grid[i][j] = shipNum;
                    }
                }
                break;
            }
        }
    }

    void printShipsGrid() {
        System.out.print(" ");
        for (int i = 0; i < w; i++) {
            System.out.printf("%3d", i + 1);
        }
        System.out.println();
        for (int i = 0; i < h; i++) {
            System.out.print(Character.toString(i + 65) + " ");
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == -1) {
                    System.out.print(" . ");
                } else {
                    System.out.print("\033[32m " + grid[i][j] + " \033[0m");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    void printGame() {
        System.out.print(" ");
        for (int i = 0; i < w; i++) {
            System.out.printf("%3d", i + 1);
        }
        System.out.println();
        for (int i = 0; i < h; i++) {
            System.out.print(Character.toString(i + 65) + " ");
            for (int j = 0; j < w; j++) {
                if (!shotsGrid[i][j]) {
                    System.out.print(" · ");
                } else if (grid[i][j] == -1) {
                    System.out.print("\033[1;94m ~ \033[0m");
                } else if (hits[grid[i][j]] == sizes[grid[i][j]]) {
                    System.out.print("\033[1;91m # \033[0m");
                } else {
                    System.out.print("\033[31m * \033[0m");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.print("Ships damage: ");
        for (int shipNum = 0; shipNum < hits.length; shipNum++) {
            System.out.print(hits[shipNum] + "/" + sizes[shipNum] + "    ");
        }
        System.out.println();
        System.out.println("Total damage: " + totalHits + "/" + totalSize);
        System.out.println("Total shoots: " + totalShots + "/" + maxShots);
    }

    void takeMove() {
        for (; ; ) {
            String line = scanner.nextLine();

            if (line.length() == 2 || line.length() == 3) {
                row = line.charAt(0) - 64;
                col = line.charAt(1) - 48;

                if (line.length() == 3) {
                    col *= 10;
                    col += line.charAt(2) - 48;
                }

                row--;
                col--;

                if (row >= 0 && row < h && col >= 0 && col < w && !shotsGrid[row][col]) {
                    break;
                }
            }
            System.out.println("\033[1;93m Invalid shoot \033[0m");
        }
    }

    void shoot() {
        totalShots++;
        shotsGrid[row][col] = true;

        if (grid[row][col] != -1) {
            hits[grid[row][col]]++;
            totalHits++;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        new BattleShip().start();
    }
}