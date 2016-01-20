package network.ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import network.server.Network_Server;
import network.util.Client_Info;
import network.util.Msg;
import network.util.Msg_Type;
import network.util.Update_Info;

import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Update_UI_Add
{
	// ======================================================================
	public Network_Server network_Server;
	// ======================================================================
	public JFrame frame;
	
	public JTextField textField_id;
	public JTextField textField_password;
	public JTextField textField_name;
	public JTextField textField_key;
	
	
	public JLabel label_key;
	public JLabel label_password;
	public JLabel label_name;
	public JLabel label_id;
	
	public JButton button_send_update_ADD;
	public JButton button_cancel;
	
	public JTextArea textArea;
	public JScrollPane scrollPane;
	// ======================================================================

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Update_UI_Add window = new Update_UI_Add();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Update_UI_Add()
	{
		initialize();
		frame.setVisible(true);//============================================
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public Update_UI_Add(Network_Server network_Server)
	{
		this.network_Server = network_Server;
		
		initialize();
		frame.setVisible(true);//============================================
	}
	
	public Update_UI_Add(Network_Server network_Server,String user_ID)
	{
		this.network_Server = network_Server;
		
		initialize();
		frame.setVisible(true);//============================================
		textField_id.setText(user_ID);
	}
	
	// ===================================================================
	// ===================================================================

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 310, 350);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("�޸��û���Ϣ");
		// ===================================================================
		label_id = new JLabel("�˺�");
		label_id.setBounds(20, 30, 60, 30);
		label_id.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_id);

		label_password = new JLabel("����");
		label_password.setBounds(20, 70, 60, 30);
		label_password.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_password);
		
		label_name = new JLabel("����");
		label_name.setBounds(20, 110, 60, 30);
		label_name.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_name);

		label_key = new JLabel("��Կ");
		label_key.setBounds(20, 150, 60, 30);
		label_key.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_key);
		//===================================================================
		textField_id = new JTextField();
		textField_id.setBounds(80, 30, 200, 30);
		frame.getContentPane().add(textField_id);
		textField_id.setColumns(10);
		textField_id.setText("newid");
		
		textField_password = new JTextField();
		textField_password.setBounds(80, 70, 200, 30);
		frame.getContentPane().add(textField_password);
		textField_password.setColumns(10);
		textField_password.setText("newpassword");
		
		textField_name = new JTextField();
		textField_name.setBounds(80, 110, 200, 30);
		frame.getContentPane().add(textField_name);
		textField_name.setColumns(10);
		textField_name.setText("newname");
		
		textField_key = new JTextField();
		textField_key.setBounds(80, 150, 200, 30);
		frame.getContentPane().add(textField_key);
		textField_key.setColumns(10);
		textField_key.setText("edcba");
		//===================================================================
		button_send_update_ADD = new JButton("����");
		button_send_update_ADD.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{			
			}
		});
		button_send_update_ADD.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//#######################################################
				//�ȱ��ؼ�� �������
				if (!network_Server.codec.check_codepolicy(textField_key.getText()))
				{
					//network_Server.show_UI_Dialog("��Կ���Բ���ȷ ����������");
					network_Server.show_Msg("", "��Կ���Բ���ȷ ����������");
					return;
				}
				//��Ҫ���Ҫ���ӵ��û���Ϣ ��ID�Ƿ��Ѿ�����
				if (network_Server.is_Client_ID_Already_Exist(textField_id.getText()))// �µ� ID �Ѿ�������ʹ��
				{
					//network_Server.show_UI_Dialog("���û�ID�Ѿ����� ����������");
					network_Server.show_Msg("", "���û�ID�Ѿ����� ����������");
					return;
				}
				Update_Info update_Info = new Update_Info();
				update_Info.user_ID = textField_id.getText();
				update_Info.user_Password = textField_password.getText();
				update_Info.user_Name = textField_name.getText();
				update_Info.user_Key = textField_key.getText();

				Client_Info client_Info_Add = new Client_Info();
				client_Info_Add.ID = update_Info.user_ID;
				client_Info_Add.Password = update_Info.user_Password;
				client_Info_Add.Name = update_Info.user_Name;
				client_Info_Add.Key = update_Info.user_Key;

				network_Server.infos_Reg.addElement(client_Info_Add);
				
				//==========================================================
				network_Server.network_Server_UI.tabbedPane_Info.updateUI();//�ô˷�������ˢ���б� ���ӳ�
				//==========================================================
				//==========================================================
				network_Server.network_Server_UI.update_List_Online(network_Server.infos_Online);
				network_Server.network_Server_UI.update_List_Reg(network_Server.infos_Reg);
				//==========================================================
				network_Server.send_Broadcast_Client_Info_Online_Reg();
				//==========================================================
				frame.setVisible(false);
				// #######################################################
			}
		});
		button_send_update_ADD.setBounds(20, 250, 130, 30);
		frame.getContentPane().add(button_send_update_ADD);
		
		button_cancel = new JButton("ȡ��");
		button_cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
		button_cancel.setBounds(150, 250, 130, 30);
		frame.getContentPane().add(button_cancel);
			
		//===================================================================
		//JScrollPane �� JTextArea ������
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setAutoscrolls(true);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(300, 30, 250, 250);
		scrollPane.setAutoscrolls(true);
		scrollPane.setAutoscrolls(true);
		frame.getContentPane().add(scrollPane);
		//===================================================================
		
		//===================================================================
		//===================================================================
		//===================================================================
		//===================================================================
		//===================================================================
	}
}



