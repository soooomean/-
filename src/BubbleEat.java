import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.File;
import javax.swing.Timer;

import javax.sound.sampled.*;
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
    private Image bubblepop = new ImageIcon("C:\\Users\\user\\Desktop\\Main.java\\BubbleEat\\src\\image\\bubblepop.png").getImage();

    private int playerX, playerY;   // 플레이어 위치
    private int playerWidth = player.getWidth(null);
    private int playerHeight = player.getHeight(null);  // 플레이어 가로, 세로 크기
    private int bubbleX, bubbleY;   // 버블 위치
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
                    case KeyEvent.VK_UP:
                        up = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        down = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        left = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = true;
                        break;
                }
            }

            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        up = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        down = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        left = false;
                        break;
                    case KeyEvent.VK_RIGHT:
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

        playSound("C:\\Users\\user\\Desktop\\Main.java\\BubbleEat\\src\\audio\\background.wav", true);
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
            playSound("C:\\Users\\user\\Desktop\\Main.java\\BubbleEat\\src\\audio\\pop.wav", false);
            bubbleX = (int) (Math.random() * (501 - playerWidth));
            bubbleY = (int) (Math.random() * (501 - playerHeight - 30)) + 30;

            bubble = bubblepop;
            repaint();

            Timer timer = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bubble = new ImageIcon("C:\\Users\\user\\Desktop\\Main.java\\BubbleEat\\src\\image\\bubble.png").getImage();
                    repaint();
                }
            });
            timer.setRepeats(false);
            timer.start();

            bubbleX = (int) (Math.random() * (501 - playerWidth));
            bubbleY = (int) (Math.random() * (501 - playerHeight - 30)) + 30;
        }
    }   // 플레이어와 코인 충돌 체크

    public void playSound(String audio, boolean isLoop) {
        try {
            clip = AudioSystem.getClip();
            File audioFile = new File(audio);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioStream);
            if (audio.equals("C:\\Users\\user\\Desktop\\Main.java\\BubbleEat\\src\\audio\\background.wav")) {
                // FloatControl을 사용하여 볼륨을 가져오고 조절
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = -10.0f;
                gainControl.setValue(dB);
            }

            clip.start();
            if (isLoop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	// 오디오 재생

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