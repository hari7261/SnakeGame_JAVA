import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 150;
    private final ArrayList<Integer> snakeX = new ArrayList<>();
    private final ArrayList<Integer> snakeY = new ArrayList<>();
    private int appleX;
    private int appleY;
    private int score;
    private char direction = 'R'; // Initial direction
    private boolean isGameOver = false;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snakeX.clear();
        snakeY.clear();
        snakeX.add(2);
        snakeY.add(2);
        appleX = 10;
        appleY = 10;
        score = 0;
        isGameOver = false;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (!isGameOver) {
            // Draw apple
            g.setColor(Color.RED);
            g.fillOval(appleX * UNIT_SIZE, appleY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < snakeX.size(); i++) {
                g.setColor(Color.GREEN);
                g.fillRect(snakeX.get(i) * UNIT_SIZE, snakeY.get(i) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2);
            g.drawString("Score: " + score, WIDTH / 2 - 50, HEIGHT / 2 + 30);
        }
    }

    public void move() {
        for (int i = snakeX.size() - 1; i > 0; i--) {
            snakeX.set(i, snakeX.get(i - 1));
            snakeY.set(i, snakeY.get(i - 1));
        }

        switch (direction) {
            case 'U':
                snakeY.set(0, snakeY.get(0) - 1);
                break;
            case 'D':
                snakeY.set(0, snakeY.get(0) + 1);
                break;
            case 'L':
                snakeX.set(0, snakeX.get(0) - 1);
                break;
            case 'R':
                snakeX.set(0, snakeX.get(0) + 1);
                break;
        }
    }

    public void checkApple() {
        if (snakeX.get(0) == appleX && snakeY.get(0) == appleY) {
            snakeX.add(appleX);
            snakeY.add(appleY);
            score++;
            spawnApple();

            if (score == 10) {
                showWinnerPopup();
            }
        }
    }

    private void showWinnerPopup() {
        JOptionPane.showMessageDialog(this, "Congratulations! You are the winner!", "Winner", JOptionPane.INFORMATION_MESSAGE);
    }

    public void checkCollision() {
        for (int i = 10; i < snakeX.size(); i++) {
            if (snakeX.get(i) == snakeX.get(0) && snakeY.get(i) == snakeY.get(0)) {
                isGameOver = true;
            }
        }

        if (snakeX.get(0) < 0 || snakeX.get(0) >= WIDTH / UNIT_SIZE ||
                snakeY.get(0) < 0 || snakeY.get(0) >= HEIGHT / UNIT_SIZE) {
            isGameOver = true;
        }
    }

    public void spawnApple() {
        Random random = new Random();
        appleX = random.nextInt((WIDTH / UNIT_SIZE));
        appleY = random.nextInt((HEIGHT / UNIT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
