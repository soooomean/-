2023-2  고급자바프로그래밍 프로젝트

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class BubbleEat extends JFrame {

    GameTimer gt = new GameTimer(this); // 수정: BubbleEat 클래스의 인스턴스를 전달
    private Image bufferImage;
    private Graphics screenGraphic;

    private Clip clip;

    private Image background = new ImageIcon("C:\\Users\\user\\Desktop\\Main.java\\BubbleEat\\src\\image\\mainScreen.png").getImage();
    private Image player = new ImageIcon("C:\\Users\\user\\Desktop\\Main.java\\BubbleEat\\src\\image\\player.png").getImage();
    private Image bubble = new ImageIcon("C:\\Users\\user\\Desktop\\Main.java\\BubbleEat\\src\\image\\bubble.png").getImage();

    private int playerX, playerY;   // 플레이어 위치
    private int playerWidth = player.getWidth(null);
    private int playerHeight = player.getHeight(null);  // 플레이어 가로, 세로 크기
    private int bubbleX, bubbleY;   // 코인 위치
    private int bubbleWidth = bubble.getWidth(null);
    private int bubbleHeight = bubble.getHeight(null);  // 코인 가로, 세로 크기

    private int score;  // 점수

    private boolean up, down, left, right;  // 키 눌림

    public BubbleEat() {
        setTitle("버블 터트리기 게임");
        setVisible(true);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        up = true;
                        break;
                    case KeyEvent.VK_S:
                        down = true;
                        break;
                    case KeyEvent.VK_A:
                        left = true;
                        break;
                    case KeyEvent.VK_D:
                        right = true;
                        break;
                }
            }

            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        up = false;
                        break;
                    case KeyEvent.VK_S:
                        down = false;
                        break;
                    case KeyEvent.VK_A:
                        left = false;
                        break;
                    case KeyEvent.VK_D:
                        right = false;
                        break;
                }
            }
        }); // 키보드 움직임 처리를 위한 키리스너 부착
        init(); // 게임 초기화
        gt.start(); // 수정: 타이머 스레드 시작
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            keyProcess();
            crashCheck();
        }
    }

    public void init() {
        score = 0;

        playerX = (500 - playerWidth) / 2;
        playerY = (500 - playerHeight) / 2;

        bubbleX = (int) (Math.random() * (501 - playerWidth));
        bubbleY = (int) (Math.random() * (501 - playerHeight - 30)) + 30;  // 점수 초기화, 플레이어와 코인 위치 설정
    }

    public void keyProcess() {
        if (up && playerY - 3 > 30) playerY -= 3;
        if (down && playerY + playerHeight + 3 < 500) playerY += 3;
        if (left && playerX - 3 > 0) playerX -= 3;
        if (right && playerX + playerWidth + 3 < 500) playerX += 3;
    }   // 플레이어 움직임

    public void crashCheck() {
        if (playerX + playerWidth > bubbleX && bubbleX + bubbleWidth > playerX && playerY + playerHeight > bubbleY
                && bubbleY + bubbleHeight > playerY) {
            score += 10;
            bubbleX = (int) (Math.random() * (501 - playerWidth));
            bubbleY = (int) (Math.random() * (501 - playerHeight - 30)) + 30;
            repaint();
        }
    }   // 플레이어와 코인 충돌 체크

    public void paint(Graphics g) {
        bufferImage = createImage(500, 500);
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage, 0, 0, null);
    }   // 더블 버퍼링

    public void screenDraw(Graphics g) {
        g.drawImage(background, 0, 0, null);
        g.drawImage(bubble, bubbleX, bubbleY, null);
        g.drawImage(player, playerX, playerY, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("SCORE : " + score, 30, 80);
        this.repaint();
    }

    public static void main(String[] args) {
        new BubbleEat();
    }

    class GameTimer extends Thread {
        private BubbleEat bubbleEat; // 수정: BubbleEat 클래스의 인스턴스를 저장할 변수

        public GameTimer(BubbleEat bubbleEat) {
            this.bubbleEat = bubbleEat;
        }

        @Override
        public void run() {
            for (int i = 30; i >= 1; i--) {
                if (bubbleEat == null) { // 수정: BubbleEat 인스턴스 확인
                    return;
                }
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("BUBBLE POP END");
            System.exit(0);
        }
    }
}
