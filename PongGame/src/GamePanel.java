import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (5.0/9.0));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1,paddle2;
    Ball ball;
    Score score;
    JButton startButton;

    GamePanel() {
        newPaddle();
        newBall();
        score = new Score(GAME_WIDTH,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        startButton = new JButton("Start");
        startButton.setBackground(Color.white);
        startButton.setFont(new Font("Consolas", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(150, 80));
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame(); // Call the startGame() method when the button is clicked
            }
        });

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, GAME_HEIGHT/2 - 50));
        // Add the start button to the panel
        this.add(startButton);

    }

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT - BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }

    public void newPaddle() {
        paddle1 = new Paddle(0, (GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle((GAME_WIDTH - PADDLE_WIDTH), (GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }   

    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
    	paddle1.draw(g);
    	paddle2.draw(g);
    	ball.draw(g);
    	score.draw(g);
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollison() {
    	//bounce ball off top & bottom window edges
    	if (ball.y <= 0) {
    		ball.setYDirection(-ball.yVelocity);
    	}
    	if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
    		ball.setYDirection(-ball.yVelocity);
    	}
    	//bounce ball off paddles
    	if (ball.intersects(paddle1)) {
    		ball.setXDirection(-ball.xVelocity);
    		ball.xVelocity++;
    		if (ball.yVelocity > 0) {
    			ball.yVelocity++;
    		} else
    			ball.yVelocity--;
    	}
    	if (ball.intersects(paddle2)) {
    		ball.setXDirection(-ball.xVelocity);
    		ball.xVelocity--;
    		if (ball.yVelocity > 0) {
    			ball.yVelocity++;
    		} else
    			ball.yVelocity--;
    	}
    	
    	//stop paddles at the window edges
    	if (paddle1.y <= 0) {
    		paddle1.y = 0;
    	}
    	if (paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT)) {
    		paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
    	}
    	if (paddle2.y <= 0) {
    		paddle2.y = 0;
    	}
    	if (paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT)) {
    		paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
    	}
    	//give player 1 point and create new ball, paddles
    	if (ball.x <= 0) {
    		score.player2++;
    		newPaddle();
    		newBall();
    	}
    	if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
    		score.player1++;
    		newPaddle();
    		newBall();
    	}
    }

    @Override
    public void run() {
    	 long lastTime = System.nanoTime();
         double amountOfTicks = 60.0;	//60fps
         double ns = 1000000000.0 / amountOfTicks;
         double delta = 0;
         while (true) {
             long now = System.nanoTime();
             delta += (now - lastTime)/ns;
             lastTime = now;
             if (delta >= 1) {
                 move();
                 checkCollison();
                 repaint();
                 delta--;
         
             }
         }
    }

    private void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
        startButton.setVisible(false);
    }
    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
        	paddle1.keyPressed(e);
        	paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
        	paddle1.keyReleased(e);
        	paddle2.keyReleased(e);
        }
    }
}
