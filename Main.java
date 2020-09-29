package tictactoe;

import java.util.Arrays;
import java.util.Scanner;

enum State {
    NOT_FINISHED,
    X_WINS,
    O_WINS,
    DRAW,
    IMPOSSIBLE
}

public class Main {
    private static int calcChars(char[][] cells, char c) {
        int n = 0;
        for (char[] row : cells) {
            for (char ch : row) {
                if (ch == c) {
                    n++;
                }
            }
        }
        return n;
    }

    private static boolean checkRows(char[][] cells, char c) {
        for (int i = 0; i < 3; i++) {
            if (cells[i][0] == c && cells[i][1] == c && cells[i][2] == c) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkColumns(char[][] cells, char c) {
        for (int i = 0; i < 3; i++) {
            if (cells[0][i] == c && cells[1][i] == c && cells[2][i] == c) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkDiagonals(char[][] cells, char c) {
        return (cells[0][0] == c && cells[1][1] == c && cells[2][2] == c) ||
               (cells[0][2] == c && cells[1][1] == c && cells[2][0] == c);
    }

    private static boolean checkEmpty(char[][] cells) {
        for (char[] row : cells) {
            for (char ch : row) {
                if (ch == ' ' || ch == '_') {
                    return true;
                }
            }
        }
        return false;
    }

    private static void convertCoordinates(int[] coordinates) {
        int temp = coordinates[0];
        coordinates[0] = 3 - coordinates[1];
        coordinates[1] = temp - 1;
    }

    private static int[] getCoordinates(Scanner scanner, char[][] cells) {
        int[] coords = new int[2];
        boolean areNumbers, areInBounds, isEmpty;

        do {
            System.out.print("Enter the coordinates: ");
            String[] coordsStr = new String[2];
            coordsStr[0] = scanner.next();
            coordsStr[1] = scanner.next();

            if (coordsStr[1].isEmpty()) {
                System.out.println("You should enter TWO coordinates!");
                continue;
            }

            areNumbers = coordsStr[0].matches("\\d") && coordsStr[1].matches("\\d");

            if (!areNumbers) {
                System.out.println("You should enter numbers!");
                continue;
            } else {
                coords[0] = Integer.parseUnsignedInt(coordsStr[0]);
                coords[1] = Integer.parseUnsignedInt(coordsStr[1]);
            }

            areInBounds = (coords[0] > 0 && coords[1] > 0 &&
                    coords[0] < 4 && coords[1] < 4);

            if (!areInBounds) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            } else {
                convertCoordinates(coords);
            }

            isEmpty = (cells[coords[0]][coords[1]] == ' ' ||
                    cells[coords[0]][coords[1]] == '_');

            if (!isEmpty) {
                System.out.println("This cell is occupied! Choose another one!");
            } else {
                return coords;
            }
        } while (true);
    }

    private static void fillField(char[][] cells, int[] coords, int player) {
        char symbol = player == 1 ? 'X' : 'O';
        cells[coords[0]][coords[1]] = symbol;
    }

    private static State checkState(char[][] cells) {
        int numberX = calcChars(cells, 'X');
        int numberO = calcChars(cells, 'O');
        boolean hasRowX = checkRows(cells, 'X');
        boolean hasRowO = checkRows(cells, 'O');
        boolean hasColumnX = checkColumns(cells, 'X');
        boolean hasColumnO = checkColumns(cells, 'O');
        boolean hasDiagonalX = checkDiagonals(cells, 'X');
        boolean hasDiagonalO = checkDiagonals(cells, 'O');
        boolean hasEmpty = checkEmpty(cells);

        boolean hasWrongNumbers = (numberX > numberO + 1 ||
                numberO > numberX + 1);
        boolean xWins = hasRowX || hasColumnX || hasDiagonalX;
        boolean oWins = hasRowO || hasColumnO || hasDiagonalO;
        boolean bothWins = xWins && oWins;
        boolean impossible = hasWrongNumbers || bothWins;

        if (impossible) {
            return State.IMPOSSIBLE;
        } else if (!(xWins || oWins)) {
            if (hasEmpty) {
                return State.NOT_FINISHED;
            } else {
                return State.DRAW;
            }
        } else if (xWins) {
            return State.X_WINS;
        } else {
            return State.O_WINS;
        }
    }

    private static void printField(char[][] cells) {
        System.out.println("---------");
        for (char[] row : cells) {
            System.out.print("| ");
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    private static void printResult(State state) {
        switch (state) {
            case X_WINS:
                System.out.println("X wins");
                break;
            case O_WINS:
                System.out.println("O wins");
                break;
            case DRAW:
                System.out.println("Draw");
                break;
            case IMPOSSIBLE:
                System.out.println("Impossible");
                break;
            default:
                System.out.println("Wrong state");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] cells = new char[3][3];
        for (int i = 0; i < 3; i++) {
            Arrays.fill(cells[i], ' ');
        }

        State state;
        int[] coords;
        int turn = 0;
        int player;

        while (true) {
            player = turn++ % 2 + 1;
            printField(cells);
            state = checkState(cells);
            if (state == State.NOT_FINISHED) {
                coords = getCoordinates(scanner, cells);
                fillField(cells, coords, player);
            } else {
                printResult(state);
                break;
            }
        }
    }
}
