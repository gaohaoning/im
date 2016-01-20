package network.ui;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.*;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JList;

import network.client.Network_Client_Line;
import network.util.Client_Info;
import network.util.Msg;
import network.util.Msg_Type;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class Network_Client_UI
{
	// ======================================================================
	public Network_Client_Line network_Client;
	// ======================================================================
	public JFrame frame;
	
	public JTextArea textArea;
	public JScrollPane scrollPane;
	
	public JTextArea textArea_Chat;
	public JScrollPane scrollPane_Chat;
	
	public DefaultListModel defaultListModel_online;
	public JList list_online;
	public JScrollPane scrollPane_online;//
	
	public DefaultListModel defaultListModel_reg;
	public JList list_reg;
	public JScrollPane scrollPane_reg;//
	
	public JTabbedPane tabbedPane_Info;
	public JTabbedPane tabbedPane_List;	
	
	public JTextField textField_send;
	public JButton button_send;
	
	public JTextField textField_send_codec;
	public JButton button_send_codec;
	
	public JTextArea textArea_recieverID;
	
	public JButton button_Edit;
	// ======================================================================

	/**
	 * Launch the application.
	 */
	//================================================================
	//������Ϊ�˴�UI�����ܹ��������� �����UI����������������ִ�����
	//���������й���UI����ʱ main() ��������ִ��
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					//================================================================
					Network_Client_UI window = new Network_Client_UI();
					window.frame.setVisible(true);
					//================================================================
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	//================================================================

	/**
	 * Create the application.
	 */
	public Network_Client_UI()
	{
		initialize();
		frame.setVisible(true);//============================================
	}
	public Network_Client_UI(Network_Client_Line network_Client)
	{
		this.network_Client = network_Client;
		
		initialize();
		frame.setVisible(false);//============================================
	}
	// #######################################################################
	public void show(String string)
	{
		this.textArea.append(string+"\n");
		//��ӹ������Զ���������//�˷������ OK
		// =========================================================
		this.textArea.setSelectionStart(this.textArea.getText().length());
		// =========================================================
	}
	// #######################################################################
	// #######################################################################
	public void show_(String string)//���������
	{
		this.textArea.append(string);
		//��ӹ������Զ���������//�˷������ OK
		// =========================================================
		this.textArea.setSelectionStart(this.textArea.getText().length());
		// =========================================================
	}
	// #######################################################################
	public void show_Chat(String string)
	{
		this.textArea_Chat.append(string+"\n");
		//��ӹ������Զ���������//�˷������ OK
		// =========================================================
		this.textArea_Chat.setSelectionStart(this.textArea_Chat.getText().length());
		// =========================================================
	}
	// #######################################################################
	public void update_List_Reg(Vector<Client_Info> vector)
	{
		try
		{
			defaultListModel_reg.clear();
			for(int i=0;i<vector.size();i++)
			{
				String str_list=vector.elementAt(i).ID+":"+vector.elementAt(i).Name;
				defaultListModel_reg.addElement(str_list);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("update_List_Reg �쳣");
		}
	}
	// #######################################################################
	public void update_List_Online(Vector<Client_Info> vector)
	{
		try
		{
			defaultListModel_online.clear();
			for(int i=0;i<vector.size();i++)
			{
				String str_list=vector.elementAt(i).ID+":"+vector.elementAt(i).Name;
				defaultListModel_online.addElement(str_list);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("update_List_Online �쳣");
		}
	}
	// #######################################################################

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(10, 10, 760, 430);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Client");
		
		//===================================================================
		//JScrollPane �� JTextArea ������
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setAutoscrolls(true);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setAutoscrolls(true);
		scrollPane.setAutoscrolls(true);
		//===================================================================
		//===================================================================
		//JScrollPane �� JTextArea ������
		textArea_Chat = new JTextArea();
		textArea_Chat.setEditable(false);
		textArea_Chat.setBackground(Color.LIGHT_GRAY);
		textArea_Chat.setAutoscrolls(true);
		
		scrollPane_Chat = new JScrollPane(textArea_Chat);
		scrollPane_Chat.setAutoscrolls(true);
		scrollPane_Chat.setAutoscrolls(true);
		//===================================================================
		// ###################################################################
		tabbedPane_Info = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_Info.setBounds(10, 10, 500, 300);
		frame.getContentPane().add(tabbedPane_Info);
		
		tabbedPane_Info.addTab("��Ϣ", null, scrollPane, null);
		tabbedPane_Info.addTab("�Ự", null, scrollPane_Chat, null);
		tabbedPane_Info.setSelectedComponent(scrollPane_Chat);//�ͻ���Ĭ��ֻ��ʾ�Ự��Ϣ
		// ###################################################################
		//===================================================================
		
		//===================================================================
		//JList �� DefaultListModel ������
		defaultListModel_online=new DefaultListModel();
		defaultListModel_online.addElement("�����û��б�");
		list_online = new JList(defaultListModel_online);
		list_online.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_online.setBackground(Color.LIGHT_GRAY);
		list_online.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
//				int index=list.getSelectedIndex();
//				String string_ListModel=defaultListModel.elementAt(index).toString();
//				String temp[]=string_ListModel.split(":");
//				String clientID=temp[0];
//				String clientName=temp[1];
//				textArea_recieverID.setText(clientID+":"+clientName+":"+"["+Integer.toString(index)+"]");
				
				//===================================================================
				int index=list_online.getSelectedIndex();
				String string_ID_Name=network_Client.get_Client_ID_Name_from_Str_Online_By_Index(index);
				textArea_recieverID.setText(string_ID_Name+":"+"["+Integer.toString(index)+"]");
				//===================================================================
			}
		});
		list_online.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
			}
		});
		
		scrollPane_online = new JScrollPane(list_online);
		scrollPane_online.setAutoscrolls(true);
		scrollPane_online.setAutoscrolls(true);
		//===================================================================
		// ===================================================================
		// ===================================================================
		// ===================================================================
		// JList �� DefaultListModel ������
		defaultListModel_reg = new DefaultListModel();
		defaultListModel_reg.addElement("ע���û��б�");
		list_reg = new JList(defaultListModel_reg);
		list_reg.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_reg.setBackground(Color.LIGHT_GRAY);
		list_reg.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
//				int index=list_reg.getSelectedIndex();
//				String string_ListModel=defaultListModel_reg.elementAt(index).toString();
//				String temp[]=string_ListModel.split(":");
//				String clientID=temp[0];
//				String clientName=temp[1];
//				textArea_recieverID.setText(clientID+":"+clientName+":"+"["+Integer.toString(index)+"]");
				
				//===================================================================
				int index=list_reg.getSelectedIndex();
				String string_ID_Name=network_Client.get_Client_ID_Name_from_Str_Reg_By_Index(index);
				textArea_recieverID.setText(string_ID_Name+":"+"["+Integer.toString(index)+"]");
				//===================================================================
			}
		});
		list_reg.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
			}
		});
		
		scrollPane_reg = new JScrollPane(list_reg);		
		scrollPane_reg.setAutoscrolls(true);
		scrollPane_reg.setAutoscrolls(true);
		// ===================================================================
		// ###################################################################
		tabbedPane_List = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_List.setBounds(520, 10, 220, 300);
		frame.getContentPane().add(tabbedPane_List);

		tabbedPane_List.addTab("�����û�", null, scrollPane_online, null);
		tabbedPane_List.addTab("ע���û�", null, scrollPane_reg, null);
		// ###################################################################
		//===================================================================
		//===================================================================
		//===================================================================
		//===================================================================
		// ===================================================================		
		textField_send = new JTextField();
		textField_send.setBounds(10, 320, 380, 30);
		frame.getContentPane().add(textField_send);
		textField_send.setColumns(10);
		//textField_send.setText("����һ���ͻ��˵�������");
		
		button_send = new JButton("��������");
		button_send.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//textField_send.setText(msg_send.recieverID);//���Ի�ȡ�� recieverID OK
				
				Msg msg_send=new Msg(Msg_Type.chat);
				msg_send.msg_Content=textField_send.getText();
				msg_send.senderID=network_Client.login_Info.id_login;
				msg_send.recieverID=get_ReceiverID_from_textArea_recieverID();//�˺����С��жϽ������Ƿ�Ϊ�ա�

				msg_send.senderName=network_Client.client_Info_Local.Name;
				msg_send.recieverName=get_ReceiverName_from_textArea_recieverID();
				
				if(!msg_send.recieverID.equals(""))//�������ߡ���Ϊ�ղŷ�����Ϣ
				{
					//���ڱ�����ʾ 
					String str_chat="["+msg_send.senderID+":"+msg_send.senderName+"]��["
					+msg_send.recieverID+":"+msg_send.recieverName+"]˵ : "+msg_send.msg_Content;
					show(str_chat);
					show_Chat(str_chat);
					//�ٷ�����������
					network_Client.send_Msg(msg_send);//����������Ϣ
				}
				else
				{
					//��ʾ�������ߡ�Ϊ��//�Ѿ���ʾ����
					//network_Client.show_Inf_UI_Dialog("��ָ����Ϣ���ն���");
				}
				textField_send.setText("");//��������
				frame.getRootPane().setDefaultButton(button_send);//��ť��ȡ����
			}
		});
		button_send.setBounds(410, 320, 100, 30);
		frame.getContentPane().add(button_send);

		// ===================================================================
		// ===================================================================
		// ===================================================================		
		textField_send_codec = new JTextField();
		textField_send_codec.setBounds(10, 360, 380, 30);
		frame.getContentPane().add(textField_send_codec);
		textField_send_codec.setColumns(10);
		textField_send_codec.setText("����һ���ͻ��˵�������");
		textField_send_codec.setText("ILOVEYOUTOO");
		
		button_send_codec = new JButton("��������");
		button_send_codec.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});
		button_send_codec.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//(1)�ȼ�鱾����Կ key_Codec_Local �Ƿ��Ѿ����� δ����ʱֵΪ "$"
				//if(network_Client.key_Codec_Local.equals("$"))
				if(network_Client.key_Codec_Local==null)
				{
					//network_Client.show_UI_Dialog("���������δ����,����[�����û���Ϣ]�����ò�ͨ�������");
					network_Client.show_Msg("", "���������δ����,����[�����û���Ϣ]�����ò�ͨ�������");
				}
				else
				{
					//(2)�ټ��������Ƿ�Ϊ��
					if(!get_ReceiverID_from_textArea_recieverID().equals(""))//�������ߡ���Ϊ�ղŷ�����Ϣ
					{
						//(3)�ټ����Կ�����Ƿ�Ϸ�
						if(network_Client.codec.check_codepolicy(network_Client.key_Codec_Local))
						{
							//����ܽ�����Ϣ msg_send
							Msg msg_send=new Msg();
							msg_send.msg_Type=Msg_Type.chat_codec;
							msg_send.senderID=network_Client.login_Info.id_login;
							msg_send.recieverID=get_ReceiverID_from_textArea_recieverID();//�˺����С��жϽ������Ƿ�Ϊ�ա�
							msg_send.senderName=network_Client.client_Info_Local.Name;
							msg_send.recieverName=get_ReceiverName_from_textArea_recieverID();
							msg_send.key_Codec=network_Client.key_Codec_Local;
							
							msg_send.msg_Content=network_Client.codec.Code(network_Client.key_Codec_Local,
									textField_send_codec.getText());//��������м�������Ϣ���ݵĹ���
							
							//���ڱ�����ʾ 
							//String str_chat="["+msg_send.senderID+"]��["+msg_send.recieverID+"]˵ : "+msg_send.msg_Content;
							String str_chat="["+msg_send.senderID+":"+msg_send.senderName+"]��["
									+msg_send.recieverID+":"+msg_send.recieverName+"]˵ : "+textField_send_codec.getText()
									+"[�Ѽ���]";
							
							show(str_chat);
							show("��������: "+msg_send.msg_Content);
							show_Chat(str_chat);
							
							//�ٷ�����������
							network_Client.send_Msg(msg_send);//����������Ϣ
							
							//textField_send_codec.setText("");//��������
							textField_send_codec.requestFocusInWindow();//������ȡ����
						}else
						{
							//network_Client.show_UI_Dialog("���ܲ��Բ��Ϸ�");
							network_Client.show_Msg("", "���ܲ��Բ��Ϸ�");
						}
					}
					else
					{
						//��ʾ�������ߡ�Ϊ��//�Ѿ���ʾ����
						//network_Client.show_Inf_UI_Dialog("��ָ����Ϣ���ն���");
					}		
				}				
				textField_send_codec.setText("");//��������
				frame.getRootPane().setDefaultButton(button_send_codec);//��ť��ȡ����
			}
		});
		button_send_codec.setBounds(410, 360, 100, 30);
		frame.getContentPane().add(button_send_codec);
		

		// ===================================================================
		// ===================================================================
		textArea_recieverID = new JTextArea();
		textArea_recieverID.setEditable(false);
		textArea_recieverID.setBackground(Color.LIGHT_GRAY);
		textArea_recieverID.setAutoscrolls(true);
		textArea_recieverID.setBounds(520, 320, 220, 30);
		//textArea_recieverID.setText("������");
		frame.getContentPane().add(textArea_recieverID);
		// ===================================================================
		// ===================================================================
		// ===================================================================
		button_Edit = new JButton("�����û���Ϣ");
		button_Edit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				network_Client.on_Btn_Edit();
			}
		});
		button_Edit.setBounds(520, 360, 220, 30);
		frame.getContentPane().add(button_Edit);
		// ===================================================================
		// ===================================================================
		//��ʼ״̬�� [�������İ�ť]��ȡ����
		frame.getRootPane().setDefaultButton(button_send);//��ť��ȡ����
		// ===================================================================
		// ===================================================================
	}
	// ===================================================================
	// ===================================================================
	public String get_ReceiverID_from_textArea_recieverID()
	{
		if(textArea_recieverID.getText().equals(""))
		{
			//network_Client.show_UI_Dialog("��ָ����Ϣ���ն���");
			network_Client.show_Msg("", "��ָ����Ϣ���ն���");
			return "";
		}
		String [] temp=textArea_recieverID.getText().split(":");
		return temp[0];//���ر�":"�ָ��ĵ�һ��String
	}
	// ===================================================================
	public String get_ReceiverName_from_textArea_recieverID()
	{
		if(textArea_recieverID.getText().equals(""))
		{
			//network_Client.show_UI_Dialog("��ָ����Ϣ���ն���");
			network_Client.show_Msg("", "��ָ����Ϣ���ն���");
			return "";
		}
		String [] temp=textArea_recieverID.getText().split(":");
		return temp[1];//���ر�":"�ָ��ĵڶ���String
	}
	// ===================================================================
	// ===================================================================
}
