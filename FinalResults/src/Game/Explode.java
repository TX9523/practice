package Game;

import Main.TankClient;

import java.awt.*;

/**
 * ����ը����
 * ʹ�ô�С��һ��Բ����ģ��
 */
public class Explode {
	int x, y;//���屬ըʱ�������

	private boolean  live=true;//�����Ƿ����˱�ը
	public TankClient tc;
	int explodeCount=0;
	//����ÿ������ͼƬ����Ҫ��Ӳ�̻�ȡ�ļ�������ֱ������Ϊ��̬
	static Image[] images=new Image[8];
	//
	static {
		for (int i = 0; i < 8; i++) {
			images[i]=Toolkit.getDefaultToolkit().getImage("images/explode/explode" +(i + 1)+".gif");
		}
	}


	private static boolean init=false;
	/**
	 * ����λ�ò����µı�ը
	 * @param x ��ը���x����
	 * @param y ��ը���y����
	 * @param tc ��ը�������ĳ���
	 * @see TankClient
	 */
	public Explode(int x, int y, TankClient tc) {
		this.x = x-37;
		this.y = y-40;
		this.tc = tc;
	}
	
	/**
	 * ������ը��ǰ��Բ
	 * @param g ����
	 * @see Graphics
	 */
	public void draw(Graphics g) {
		/*����δ������Ͼ�ʵ��draw������������ʹͼƬ�޷������
		��������ɳ�ʼ����ʹ���ڻ��е�һ������ʱҲ�ܲ�����ը*/
		if(!init) {
			for (int i = 0; i < images.length; i++) {
				g.drawImage(images[i], -100, -100, null);
			}
			init = true;
		}
		if (!live) {
			tc.explodes.remove(this);
			return;
		}

		//��TankClient�˻�����ػ��̣߳�ͨ��explodeCount�������Ӷ�չ�ֳ���ըЧ��
		if(explodeCount<8) {
			g.drawImage(images[explodeCount],x,y,null);
			explodeCount++;
		}
	}
}
