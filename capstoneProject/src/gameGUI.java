import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class gameGUI extends screenGUI{
    public static void main(String[] args) {
        JFrame frame = new JFrame("TicTacToe");
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gamePanel.getScoreManager().saveScores();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}


// GamePanel handles UI and connects with game logic
class GamePanel extends JPanel implements ActionListener {
    private final GameLogic gameLogic;
    private final ScoreManager scoreManager;
    private final JButton resetButton;

    public GamePanel() {
        setPreferredSize(new Dimension(420, 300));
        setBackground(new Color(0x80bdab));
        gameLogic = new GameLogic();
        scoreManager = new ScoreManager();

        addMouseListener(new XOListener());
        resetButton = new JButton("Play Again?");
        resetButton.addActionListener(this);
        resetButton.setBounds(315, 210, 100, 30);
        resetButton.setVisible(false);
        add(resetButton);
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawUI(g);
        drawGame(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(new Color(0x3f3f44));
        int lineWidth = 5;
        int lineLength = 270;
        int offset = 95;
        int x = 15, y = 100;
        g.fillRoundRect(x, y, lineLength, lineWidth, 5, 30);
        g.fillRoundRect(x, y + offset, lineLength, lineWidth, 5, 30);
        g.fillRoundRect(y, x, lineWidth, lineLength, 30, 5);
        g.fillRoundRect(y + offset, x, lineWidth, lineLength, 30, 5);
    }

    private void drawUI(Graphics g) {
        g.setColor(new Color(0x3f3f44));
        g.fillRect(300, 0, 120, 300);
        g.setColor(Color.WHITE);
        g.drawString("Win Count", 310, 30);
        g.drawString("X: " + scoreManager.getPlayer1Wins(), 310, 60);
        g.drawString("O: " + scoreManager.getPlayer2Wins(), 310, 90);

        if (gameLogic.isGameDone()) {
            g.drawString("Winner: " + gameLogic.getWinnerText(), 310, 150);
        } else {
            g.drawString("Turn: " + gameLogic.getCurrentPlayerText(), 310, 150);
        }
    }

    private void drawGame(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = 30 + i * 95;
                int y = 30 + j * 95;

                if (gameLogic.getBoardValue(i, j) == 1) { // Draw X
                    g2.setColor(Color.WHITE);
                    g2.drawLine(x, y, x + 50, y + 50);
                    g2.drawLine(x, y + 50, x + 50, y);
                } else if (gameLogic.getBoardValue(i, j) == 2) { // Draw O
                    g2.setColor(Color.WHITE);
                    g2.drawOval(x, y, 50, 50);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            gameLogic.resetGame();
            resetButton.setVisible(false);
            repaint();
        }
    }

    private class XOListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            if (gameLogic.isGameDone()) return;

            int selX = e.getX() / 100;
            int selY = e.getY() / 100;

            if (selX < 3 && selY < 3 && gameLogic.makeMove(selX, selY)) {
                if (gameLogic.isGameDone()) {
                    if (gameLogic.getWinner() == 1) scoreManager.incrementPlayer1Wins();
                    else if (gameLogic.getWinner() == 2) scoreManager.incrementPlayer2Wins();
                    resetButton.setVisible(true);
                }
                repaint();
            }
        }

        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
    }
}


// GameLogic handles game state and winner logic
class GameLogic {
    private int[][] board = new int[3][3];
    private boolean playerX = true;
    private boolean gameDone = false;
    private int winner = -1;

    public void resetGame() {
        board = new int[3][3];
        playerX = true;
        gameDone = false;
        winner = -1;
    }

    public boolean makeMove(int x, int y) {
        if (board[x][y] != 0) return false;
        board[x][y] = playerX ? 1 : 2;
        playerX = !playerX;
        checkWinner();
        return true;
    }

    private void checkWinner() {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                setWinner(board[i][0]);
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                setWinner(board[0][i]);
        }
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            setWinner(board[0][0]);
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            setWinner(board[0][2]);

        // Check tie
        if (winner == -1) {
            boolean tie = true;
            for (int[] row : board)
                for (int cell : row)
                    if (cell == 0) tie = false;

            if (tie) {
                gameDone = true;
                winner = 3;
            }
        }
    }

    private void setWinner(int winner) {
        this.winner = winner;
        this.gameDone = true;
    }

    public int getBoardValue(int x, int y) {
        return board[x][y];
    }

    public boolean isGameDone() {
        return gameDone;
    }

    public int getWinner() {
        return winner;
    }

    public String getWinnerText() {
        return winner == 1 ? "X" : winner == 2 ? "O" : "Tie";
    }

    public String getCurrentPlayerText() {
        return playerX ? "X" : "O";
    }
}

// ScoreManager: Handles score persistence
class ScoreManager {
    private int player1Wins = 0;
    private int player2Wins = 0;

    public void saveScores() {
        try (PrintWriter pw = new PrintWriter("score.txt")) {
            pw.println("Player X Wins: "+player1Wins);
            pw.println("Player O Wins: "+player2Wins);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void incrementPlayer1Wins() {
        player1Wins++;
    }

    public void incrementPlayer2Wins() {
        player2Wins++;
    }

    public int getPlayer1Wins() {
        return player1Wins;
    }

    public int getPlayer2Wins() {
        return player2Wins;
    }
}
