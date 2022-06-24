package Game;

import Main.TankClient;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �����ӵ�����
 */
public class Missile {
	/**
	 * �ӵ����ٶ�
	 */
	public static final int SPEED = 10;
	/**
	 * �ӵ��Ŀ��
	 */
	public static final int WIDTH = 10;
	/**
	 * �ӵ��ĸ߶�
	 */
	public static final int HEIGHT = 10;

	private static Map<String, Image> missileImages = new HashMap<>();
	static Image[] images=new Image[2];
	static {
		//��̬����飬�����ӵ�����ɫ
			images[0]=Toolkit.getDefaultToolkit().getImage("images/missile/missileYellow.gif");
			images[1]=Toolkit.getDefaultToolkit().getImage("images/missile/missileGreen.gif");
			missileImages.put("Y",images[0]);
			missileImages.put("G",images[1]);
	}
	private static int ID = 1;

	TankClient tc;

	public int tankId;

	public int id;

	public int x, y;

	public Dir dir = Dir.R;

	public boolean live = true;

	public boolean good;
	
	/**
	 * ����λ�õ����Թ����ӵ�
	 * @param tankId ����̹�˵�id��(���������)
	 * @param x �ӵ�������x����
	 * @param y �ӵ�������y����
	 * @param good �ӵ��������Ǻû��ǻ�
	 * @param dir �ӵ��ķ���
	 * @see Dir
	 */
	
	public Missile(int tankId, int x, int y, boolean good, Dir dir) {
		this.tankId = tankId;
		this.x = x;
		this.y = y;
		this.good = good;
		this.dir = dir;
		this.id = ID++;
	}
	
	/**
	 * ����λ�ú�TankClient�����ӵ�
	 * @param tankId
	 * @param x
	 * @param y
	 * @param good
	 * @param dir
	 * @param tc �ӵ������ĳ���
	 * @see TankClient
	 */
	public Missile(int tankId, int x, int y, boolean good, Dir dir,
			TankClient tc) {
		this(tankId, x, y, good, dir);
		this.tc = tc;
	}
	
	/**
	 * �����ӵ�
	 * @param g ����
	 */
	public void draw(Graphics g) {
		//�ӵ������������Ƴ��ӵ�
		if (!live) {
			tc.missiles.remove(this);
			return;
		}
//�ж��ӵ��ĺû���Ҳ�����Լ�̹�˷�����ӵ��͵з�������ӵ�
		if (good)
			g.drawImage(missileImages.get("Y"),x+5,y+5,null);
		else
			g.drawImage(missileImages.get("G"),x+5,y+5,null);
		move();
	}
//�ӵ��ƶ�����
	private void move() {
		switch (dir) {
		case L://����
			x -= SPEED;
			break;
		case U://�Ϸ���
			y -= SPEED;
			break;
		case R://�ҷ���
			x += SPEED;
			break;
		case D:///�·���
			y += SPEED;
			break;
			//����ɾ��
		case STOP://ֹͣ��ʱ��
			break;
		}
//�ж��Ƿ����
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH
				|| y > TankClient.GAME_HEIGHT) {
			live = false;
		}
	}
	
	/**
	 * ȡ���ӵ������з���
	 * @return �ӵ�������Rectangle
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * ����ӵ��Ƿ�ײ��̹��
	 * @param t ������̹��
	 * @return ���ײ������true,���򷵻�false
	 */
	public boolean hitTank(Tank t) {
		if (this.live && t.isLive() && this.good != t.good
				&& this.getRect().intersects(t.getRect())) {
			this.live = false;
			t.setLive(false);
			tc.explodes.add(new Explode(x, y, tc));
			return true;
		}
		return false;
	}

}
