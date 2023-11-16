package bubbleGame.component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import bubbleGame.GameFrame;
import service.BackgroundBubbleService;
import bubbleGame.component.*;


public class Bubble extends JLabel {
	
	private Player player;
	private BackgroundBubbleService backgroundBubbleService;
	private GameFrame mContext;
	private PlayerWay launchDirection;

	public int x;
	public int y;
	
	private boolean up;
	private boolean right;
	private boolean left;
	
	private ImageIcon bubble;
	private ImageIcon bubbleRed;
	private ImageIcon bubbleGray;
	private ImageIcon pop;
	
	public Bubble(GameFrame mContext) {
		this.mContext = mContext;
		this.player = mContext.getPlayer();
		initObject();
		initSetting();
	}

	private void initObject() {
		bubble = new ImageIcon("img/bubble.png");
		bubbleRed = new ImageIcon("img/bubbleRed.png");
		bubbleGray = new ImageIcon("img/bubbleGray.png");
		pop = new ImageIcon("img/pop.png");
		
		backgroundBubbleService = new BackgroundBubbleService(this); 
	}

	private void initSetting() {
		up = false;
		left = false;
		right = false;
	
		x = player.getX();
		y = player.getY();
		
		setIcon(bubble);
		setSize(70, 70);
	}
   
    public void left() {
        left = true;
        for (int i = 0; i < 80; i++) {
            x--;
            setLocation(x, y);

            if (backgroundBubbleService.leftWall()) {
                left = false;
                break;
            }
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        up();
    }

   
    public void right() {
        right = true;
        for (int i = 0; i < 80; i++) { 
            x++;
            setLocation(x, y);

            if (backgroundBubbleService.rightWall()) { // true면 (wall 닿으면) 이동 멈춤
                right = false;
                break;
            }

            try { 
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        up();
    }
    
    public void up() {
		up = true;
		while (up) {
			y--;
			setLocation(x, y);

			if (backgroundBubbleService.topWall()) {
				up = false;
				break;
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
			clearBubble();
	}
    
    private void clearBubble() {
    	try {
    		Thread.sleep(1000);
    		setIcon(bubbleRed);
    		Thread.sleep(1000);
    		setIcon(bubbleGray);
			Thread.sleep(1000);
			setIcon(pop);
			Thread.sleep(700);
			
			mContext.getPlayer().getBubbleList().remove(this);
			mContext.remove(this); // bubbleframe의 bubble이 메모리에서 소멸
			mContext.repaint(); // bubbleframe의 전체를 다시 그림(메모리에 없는건 안그림)
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
