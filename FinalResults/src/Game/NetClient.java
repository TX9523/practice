package Game;

import Main.TankClient;
import Main.TankServer;
import Message.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * �����������ӵĿͻ�����
 */
public class NetClient {
	TankClient tc;

	private int udpPort;

	String IP; // server IP

	DatagramSocket ds = null;
	
	/**
	 * ���ݳ�����������ͻ���
	 * @param tc ��Ϸ����
	 */
	public NetClient(TankClient tc) {
		this.tc = tc;

	}
	
	/**
	 * ���ӷ�����
	 * @param IP ������IP
	 * @param port �������˿�
	 */
	public void connect(String IP, int port) {

		this.IP = IP;

		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		Socket s = null;
		try {
			s = new Socket(IP, port);
			//ʹ������������������
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.myTank.id = id;
			//ͨ���˿ںŵ���ż�����ж�̹�˵ĺû�
			if (id % 2 == 0)
				tc.myTank.good = false;
			else
				tc.myTank.good = true;

			System.out.println("Connected to server! and server give me a ID:"
					+ id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//ÿ��������Ӻ�Client�˶��ᴫ���Server��̹���µ�������Ϣ��
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
		//�����߳�
		new Thread(new UDPRecvThread()).start();
	}
	
	/**
	 * ������Ϣ
	 * @param msg �����͵���Ϣ
	 */
	public void send(Msg msg) {
		//send����������װ�������Ϣ�Ĵ���
		//�˴����ֶ�̬�ĺô�����ͬ������Ϣȥʵ��ͬһ���ӿڣ�����send����ʱ��������ת�ͣ�ͨ����̬��ʵ�ֲ�ͬ��𷽷��Ĵ���
		msg.send(ds, IP, TankServer.UDP_PORT);
	}

	private class UDPRecvThread implements Runnable {
		//Client�˽�����Ϣ�̣߳����ܴ�Server�˷��͹�����UDP��
		byte[] buf = new byte[1024];
		//�Ѵ�Server�˴������İ���ȡ��������
		public void run() {

			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					parse(dp);//������������ݰ�
					System.out.println("a packet received from server!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//�������
		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp
					.getLength());
			DataInputStream dis = new DataInputStream(bais);
			int msgType = 0;
			try {
				msgType = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Msg msg = null;
			//�жϴ������ݰ�������
			switch (msgType) {
				//tank����Ϣ
			case Msg.TANK_NEW_MSG:
				msg = new TankNewMsg(NetClient.this.tc);//�ڲ����з��ʷ�װ��Ķ���
				//ʵ��������̹����Ϣ��������������Ϣ������ɲ��
				msg.parse(dis);
				break;
				//tank�ƶ�����Ϣ
			case Msg.TANK_MOVE_MSG:
				msg = new TankMoveMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
				//�ӵ�����Ϣ
			case Msg.MISSILE_NEW_MSG:
				msg = new MissileNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
				//tank��������Ϣ
			case Msg.TANK_DEAD_MSG:
				msg = new TankDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
				//�ӵ���������Ϣ
			case Msg.MISSILE_DEAD_MSG:
				msg = new MissileDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			}

		}

	}
	
	/**
	 * ȡ��UDP�˿�(�ͻ��˽���������)
	 * @return
	 */
	public int getUdpPort() {
		return udpPort;
	}
	
	/**
	 * �趨UDP�˿�(�ͻ��˽���������)
	 * @param udpPort
	 */
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
}
