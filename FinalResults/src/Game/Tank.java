package Game;

import Main.TankClient;
import Message.MissileNewMsg;
import Message.TankMoveMsg;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ����̹�˵���
 *
 */
public class Tank {
	public int id;//����tank��id
	public static final int SPEED = 5;//����tank���ٶ�
	public static final int WIDTH = 40;//����tank�Ŀ��
	public static final int HEIGHT = 50;//����tank�ĸ߶�
	public boolean good;//����tank�ĺû������ҷ�tank�͵з�tank
	public int x;//����tank�ĺ������λ��
	public int y;//����tank���������λ��

	private boolean live = true;//����̹�˵�����
	private static Image[] playerOne=null;
	private static Image[] playerTwo=null;
	//������ϣ������ӦͼƬ�����ϣ���У�Ȼ�����good��ѡȡplayerOne����playerTwo��ͼƬ
	private static Map<String, Image> images = new HashMap<>();
	private static Map<String, Image> imagesOne = new HashMap<>();
	private static Map<String, Image> imagesTwo = new HashMap<>();
	/**
	 * playerOne��playerTwo����ֱ���ɶ�ӦͼƬ��ʼ��������������˳�����
	 */
	static {
		playerOne=new Image[] {
				Toolkit.getDefaultToolkit().getImage("images/player1/p1tankU.gif"),
				Toolkit.getDefaultToolkit().getImage("images/player1/p1tankD.gif"),
				Toolkit.getDefaultToolkit().getImage("images/player1/p1tankL.gif"),
				Toolkit.getDefaultToolkit().getImage("images/player1/p1tankR.gif")
		};
		imagesOne.put("U",playerOne[0]);
		imagesOne.put("D",playerOne[1]);
		imagesOne.put("L",playerOne[2]);
		imagesOne.put("R",playerOne[3]);
	}

	static {
		playerTwo=new Image[] {
				Toolkit.getDefaultToolkit().getImage("images/player2/p2tankU.gif"),
				Toolkit.getDefaultToolkit().getImage("images/player2/p2tankD.gif"),
				Toolkit.getDefaultToolkit().getImage("images/player2/p2tankL.gif"),
				Toolkit.getDefaultToolkit().getImage("images/player2/p2tankR.gif")
		};
		imagesTwo.put("U",playerTwo[0]);
		imagesTwo.put("D",playerTwo[1]);
		imagesTwo.put("L",playerTwo[2]);
		imagesTwo.put("R",playerTwo[3]);
	}
	TankClient tc;

	boolean bL, bU, bR, bD;

	public Dir dir = Dir.STOP;

	public Dir ptDir = Dir.D;
	
	/**
	 * ����λ�úͺû�����̹��
	 * @param x
	 * @param y
	 * @param good
	 */
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	
	/**
	 * ����������Թ���̹��
	 * @param x
	 * @param y
	 * @param good
	 * @param dir
	 * @param tc ��Ϸ�ĳ���
	 */
	public Tank(int x, int y, boolean good, Dir dir, TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}
	
	/**
	 * ����̹��
	 * @param g ����
	 */
	public void draw(Graphics g) {
		if (!live) {
			if (!good) {
				tc.tanks.remove(this);
			}
			return;
		}

		if (good)
			images=imagesOne;
		else
			images=imagesTwo;
		g.setColor(Color.BLACK);//���ñ�����ɫ
		g.setFont(new Font("����",Font.BOLD,15));
		g.drawString("id:" + id, x, y-10);
		//�ж�tank�ķ�����ѡ����Ӧ��ͼƬ��������tank��ת��
		switch (ptDir) {
		case L://����
			g.drawImage(images.get("L"),x,y,null);
			break;
		case U://����
			g.drawImage(images.get("U"),x,y,null);
			break;
		case R://����
			g.drawImage(images.get("R"),x,y,null);
			break;
		case D://����
			g.drawImage(images.get("D"),x,y,null);
			break;
		}

		move();
	}
//��дһ��tank���ƶ�����
	private void move() {
		switch (dir) {
		case L:
			x -= SPEED;
			break;
		case U:
			y -= SPEED;
			break;
		case R:
			x += SPEED;
			break;
		case D:
			y += SPEED;
			break;
		case STOP:
			break;
		}
//�жϾɵķ���
		if (dir != Dir.STOP) {
			ptDir = dir;
		}
//�ж�tank���������
		if (x < 0)
			x = 0;
		if (y < 30)
			y = 30;
		if (x + WIDTH > TankClient.GAME_WIDTH)
			x = TankClient.GAME_WIDTH - WIDTH;
		if (y + HEIGHT > TankClient.GAME_HEIGHT)
			y = TankClient.GAME_HEIGHT - HEIGHT;

	}
	
	/**
	 * �����µ���Ϣ����
	 * @param e �����¼�
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A:
			bL = true;
			break;
		case KeyEvent.VK_W:
			bU = true;
			break;
		case KeyEvent.VK_D:
			bR = true;
			break;
		case KeyEvent.VK_S:
			bD = true;
			break;
		}
		locateDirection();
	}
//�ж��ƶ�����ķ���
	private void locateDirection() {
		Dir oldDir = this.dir;
		//����
		if (bL && !bU && !bR && !bD)
			dir = Dir.L;
		//����
		else if (!bL && bU && !bR && !bD)
			dir = Dir.U;
		//����
		else if (!bL && !bU && bR && !bD)
			dir = Dir.R;
		//=����
		else if (!bL && !bU && !bR && bD)
			dir = Dir.D;
		//������ֹͣ
		else if (!bL && !bU && !bR && !bD)
			dir = Dir.STOP;
		//�������һ�£��������tank���䷽��
		if (dir != oldDir) {
			TankMoveMsg msg = new TankMoveMsg(id, x, y, dir, ptDir);
			tc.nc.send(msg);
		}
	}
	
	/**
	 * ��̧�����Ϣ����
	 * @param e ̧����Ϣ
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_A:
			bL = false;
			break;
		case KeyEvent.VK_W:
			bU = false;
			break;
		case KeyEvent.VK_D:
			bR = false;
			break;
		case KeyEvent.VK_S:
			bD = false;
			break;
		}
		locateDirection();
	}
   	//��дһ������ķ���
	private Missile fire() {
		//���tank�Ѿ����������ܴ�������ķ���
		if (!live)
			return null;
   		//��д�ӵ���ʲô�ط����ֵ�
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(id, x, y, this.good, this.ptDir, this.tc);
		tc.missiles.add(m);
		//���ӵ������ʱ�򣬸�����tank����һ���ӵ�����Ϣ
		MissileNewMsg msg = new MissileNewMsg(m);
		tc.nc.send(msg);

		return m;
	}
	
	/**
	 * ȡ��̹�˵����з���
	 * @return ̹�˵�����Rectangle
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * ���̹���Ƿ񻹻���
	 * @return
	 */
	public boolean isLive() {
		return live;
	}
	
	/**
	 * �趨̹�˵�����״̬
	 * @param live
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
}
