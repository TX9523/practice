package login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class LogOnFrm {
    private DbUtil dbUtil=new DbUtil();

    JFrame jf=new JFrame("̹�˴�ս��½ϵͳ");

    final int WIDTH=500;
    final int HEIGHT=300;
    //��װ��ͼ
    public void init(){
        //λ��
        jf.setBounds((ScreenUtils.gerScreenWidth()-WIDTH)/2,(ScreenUtils.gerScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        jf.setResizable(false);//���ܴ������ô�С

        //���ô��ڵ�����
        JPanel jp=new JPanel();
         Box vBox=Box.createVerticalBox();//��ֱ����Box


        //ˮƽ����Box
        Box ubox=Box.createHorizontalBox();
        JLabel uLabel=new JLabel("�û�����");
        JTextField uField=new JTextField(15);


        ubox.add(uLabel);
        ubox.add(Box.createHorizontalStrut(20));//���ü��
        ubox.add(uField);

        //��װ����
        Box pbox=Box.createHorizontalBox();
        JLabel pLabel=new JLabel("��    �룺");
        JTextField pField=new JTextField(15);

        pbox.add(pLabel);
        pbox.add(Box.createHorizontalStrut(20));//���ü��
        pbox.add(pField);



        //��װ��ť
        Box btnbox=Box.createHorizontalBox();
        JButton loginBtn=new JButton("��½ / ע��");

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ�û���������
                String username=uField.getText().trim();
                String password=pField.getText().trim();
                boolean flag ;
                Socket client = null;
                try {
                    client = new Socket("127.0.0.1",6688);
                    //������������˷�������
                    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                    //���ڽ��շ������˷�������
                    DataInputStream dis = new DataInputStream(client.getInputStream());
                    //��װ���ݣ�׼����������˷���
                    User user = new User(username,password);
                    //����
                    oos.writeObject(user);
                    flag = dis.readBoolean();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnbox.add(loginBtn);
        vBox.add(Box.createVerticalStrut(50));
        vBox.add(ubox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pbox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(btnbox);

        jp.add(vBox);
        jf.add(jp);
        //���ڿɼ�
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        }

    public static void main(String[] args) {
        new LogOnFrm().init();
        }
    }