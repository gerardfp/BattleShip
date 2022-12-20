import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class BattleShip {
    Random random = new Random();
    Scanner scanner = new Scanner(System.in);

    // Game config
    int w = 10, h = 10;
    int[] sizes = {5, 4, 3, 3, 2};
    int maxShoots = 32;


    int[][] shipsGrid = new int[h][w];
    boolean[][] shootsGrid = new boolean[h][w];
    int[] damages = new int[sizes.length];
    int totalSize, totalDamage, totalShoots;
    int row, col;

    void start() {
        init();
        arrangeShips();
        for (; ; ) {
            printShipsGrid();
            printShootsGrid();
            printShoots();
            if (totalSize == totalDamage) {
                System.out.println("\033[1;92m YOU WIN!!!!!!! \033[0m");
                break;
            } else if (totalShoots == maxShoots) {
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
                shipsGrid[i][j] = -1;
            }
        }

        totalSize = Arrays.stream(sizes).sum();
    }

    void arrangeShips() {
        int arranged = 0;

        while (arranged != sizes.length) {
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
                            if (shipsGrid[i][j] != -1) {
                                continue arrangeShip;
                            }
                        }
                    }

                    for (int i = rowStart; i < rowStart + rowSpan; i++) {
                        for (int j = colStart; j < colStart + colSpan; j++) {
                            shipsGrid[i][j] = shipNum;
                        }
                    }
                    arranged++;
                    break;
                }
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
                if (shipsGrid[i][j] == -1) {
                    System.out.print(" . ");
                } else {
                    System.out.print("\033[32m " + shipsGrid[i][j] + " \033[0m");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    void printShootsGrid() {
        System.out.print(" ");
        for (int i = 0; i < w; i++) {
            System.out.printf("%3d", i + 1);
        }
        System.out.println();
        for (int i = 0; i < h; i++) {
            System.out.print(Character.toString(i + 65) + " ");
            for (int j = 0; j < w; j++) {
                if (!shootsGrid[i][j]) {
                    System.out.print(" · ");
                } else {
                    if (shipsGrid[i][j] == -1) {
                        System.out.print("\033[1;94m ~ \033[0m");
                    } else if (damages[shipsGrid[i][j]] == sizes[shipsGrid[i][j]]) {
                        System.out.print("\033[1;91m # \033[0m");
                    } else {
                        System.out.print("\033[31m * \033[0m");
                    }
                }
            }
            System.out.println();
        }
    }

    void printShoots() {
        System.out.println();
        System.out.print("Ships damage: ");
        for (int i = 0; i < damages.length; i++) {
            System.out.print(damages[i] + "/" + sizes[i] + "    ");
        }
        System.out.println();
        System.out.println("Total damage: " + totalDamage + "/" + totalSize);
        System.out.println("Total shoots: " + totalShoots + "/" + maxShoots);
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

                if (row >= 0 && row < h && col >= 0 && col < w) {
                    if (!shootsGrid[row][col]) {
                        break;
                    }
                }
            }
            System.out.println("\033[1;93m Invalid shoot \033[0m");
        }
    }

    void shoot() {
        totalShoots++;
        shootsGrid[row][col] = true;

        if (shipsGrid[row][col] != -1) {
            damages[shipsGrid[row][col]]++;
            totalDamage++;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        new BattleShip().start();
    }
}