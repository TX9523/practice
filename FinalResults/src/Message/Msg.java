package Message;

import java.io.DataInputStream;
import java.net.DatagramSocket;
/**
 * ��������Э������ݽӿ�
 */
public interface Msg {
	/**
	 * ̹�˲�������Ϣ
	 */
	int TANK_NEW_MSG = 1;
	
	/**
	 * ̹���ƶ�����Ϣ
	 */
	int TANK_MOVE_MSG = 2;
	
	/**
	 * �ӵ���������Ϣ
	 */
	int MISSILE_NEW_MSG = 3;
	
	/**
	 * ̹����������Ϣ
	 */
	int TANK_DEAD_MSG = 4;
	
	/**
	 * �ӵ���������Ϣ
	 */
	int MISSILE_DEAD_MSG = 5;
	
	/**
	 * ��������
	 * @param ds
	 * @param IP
	 * @param udpPort
	 */
	void send(DatagramSocket ds, String IP, int udpPort);
	
	/**
	 * ���ղ���������
	 * @param dis
	 */
	void parse(DataInputStream dis);
}
