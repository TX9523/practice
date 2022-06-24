package Main;

import Game.*;
import Message.MissileDeadMsg;
import Message.TankDeadMsg;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * ̹�˵���Ϸ����
 */
public class TankClient extends Frame {
	/**
	 * ��Ϸ�����Ŀ��
	 */
	public static final int GAME_WIDTH = 800;
	
	/**
	 * ��Ϸ�����ĸ߶�
	 */
	public static final int GAME_HEIGHT = 610;

	public Tank myTank = new Tank(50, 100, true, Dir.STOP, this);

	public List<Missile> missiles = new ArrayList<>();

	public List<Explode> explodes = new ArrayList<>();

	public List<Tank> tanks = new ArrayList<>();

	Image offScreenImage = null;

	public NetClient nc = new NetClient(this);

	ConnDialog dialog = new ConnDialog();
	
	@Override
	/**
	 * ��д������ػ�����
	 */
	public void paint(Graphics g) {
		//ȡ���ӵ�
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			//��tank
			if (m.hitTank(myTank)) {
				TankDeadMsg msg = new TankDeadMsg(myTank.id);
				nc.send(msg);//����tank��Ϣ
				MissileDeadMsg mdmMsg = new MissileDeadMsg(m.tankId, m.id);
				nc.send(mdmMsg);//�����ӵ�����Ϣ
			}
			m.draw(g);
		}
		//��ը
		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		//��tank
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.draw(g);
		}

		myTank.draw(g);

	}

	@Override
	/**
	 * ��д�����update��������ʵ��˫����
	 */
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GRAY);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	/**
	 * ���ڵ���������
	 *
	 */
	public void launchFrame() {
		//����
		this.setTitle("̹�˴�ս");
		//���ڳ�ʼ��С
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		//ʹ��Ļ����
		this.setLocationRelativeTo(null);
		//��ӹر��¼�
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
		//�û����ܵ�����С
		this.setResizable(false);
		//���ñ���ɫ
		this.setBackground(Color.gray);
		//��Ӽ����¼�
		this.addKeyListener(new KeyMonitor());
		//ʹ���ڿɼ�
		this.setVisible(true);
		//����GUI���߳�����ػ�
		while (true){
			repaint();
			try {
				//�߳�����  1�� = 1000����
				Thread.sleep(30);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}


	class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		//����c�����Ը��ĺ�
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_C) {
				dialog.setVisible(true);
			} else {
				myTank.keyPressed(e);
			}
		}

	}
//���ý����ʱ�����ö˿ں�
	class ConnDialog extends Dialog {
		JButton b=new JButton("ȷ��");

		TextField tfIP = new TextField("127.0.0.1", 12);

		TextField tfPort = new TextField("" + TankServer.TCP_PORT, 4);

		TextField tfMyUDPPort = new TextField("2223", 4);

		public ConnDialog() {
			super(TankClient.this, true);

			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("My UDP Port:"));
			this.add(tfMyUDPPort);
			this.add(b);
			this.setLocation(750,450);
			this.pack();
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			b.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String IP = tfIP.getText().trim();
					int port = Integer.parseInt(tfPort.getText().trim());
					int myUDPPort = Integer.parseInt(tfMyUDPPort.getText()
							.trim());
					nc.setUdpPort(myUDPPort);
					nc.connect(IP, port);
					setVisible(false);
				}

			});
		}

	}

}
