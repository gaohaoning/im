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
	//这里是为了此UI程序能够独立运行 如果此UI依附于其他类则不需执行这个
	//在其他类中构建UI对象时 main() 函数不被执行
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
		//添加滚动条自动滚动到底//此方法最简单 OK
		// =========================================================
		this.textArea.setSelectionStart(this.textArea.getText().length());
		// =========================================================
	}
	// #######################################################################
	// #######################################################################
	public void show_(String string)//不换行输出
	{
		this.textArea.append(string);
		//添加滚动条自动滚动到底//此方法最简单 OK
		// =========================================================
		this.textArea.setSelectionStart(this.textArea.getText().length());
		// =========================================================
	}
	// #######################################################################
	public void show_Chat(String string)
	{
		this.textArea_Chat.append(string+"\n");
		//添加滚动条自动滚动到底//此方法最简单 OK
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
			show("update_List_Reg 异常");
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
			show("update_List_Online 异常");
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
		//JScrollPane 是 JTextArea 的容器
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setAutoscrolls(true);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setAutoscrolls(true);
		scrollPane.setAutoscrolls(true);
		//===================================================================
		//===================================================================
		//JScrollPane 是 JTextArea 的容器
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
		
		tabbedPane_Info.addTab("信息", null, scrollPane, null);
		tabbedPane_Info.addTab("会话", null, scrollPane_Chat, null);
		tabbedPane_Info.setSelectedComponent(scrollPane_Chat);//客户端默认只显示会话信息
		// ###################################################################
		//===================================================================
		
		//===================================================================
		//JList 是 DefaultListModel 的容器
		defaultListModel_online=new DefaultListModel();
		defaultListModel_online.addElement("在线用户列表");
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
		// JList 是 DefaultListModel 的容器
		defaultListModel_reg = new DefaultListModel();
		defaultListModel_reg.addElement("注册用户列表");
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

		tabbedPane_List.addTab("在线用户", null, scrollPane_online, null);
		tabbedPane_List.addTab("注册用户", null, scrollPane_reg, null);
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
		//textField_send.setText("这是一个客户端单播测试");
		
		button_send = new JButton("发送明文");
		button_send.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//textField_send.setText(msg_send.recieverID);//测试获取的 recieverID OK
				
				Msg msg_send=new Msg(Msg_Type.chat);
				msg_send.msg_Content=textField_send.getText();
				msg_send.senderID=network_Client.login_Info.id_login;
				msg_send.recieverID=get_ReceiverID_from_textArea_recieverID();//此函数中【判断接收者是否为空】

				msg_send.senderName=network_Client.client_Info_Local.Name;
				msg_send.recieverName=get_ReceiverName_from_textArea_recieverID();
				
				if(!msg_send.recieverID.equals(""))//【接收者】不为空才发送消息
				{
					//先在本地显示 
					String str_chat="["+msg_send.senderID+":"+msg_send.senderName+"]对["
					+msg_send.recieverID+":"+msg_send.recieverName+"]说 : "+msg_send.msg_Content;
					show(str_chat);
					show_Chat(str_chat);
					//再发送至服务器
					network_Client.send_Msg(msg_send);//发送聊天信息
				}
				else
				{
					//提示【接收者】为空//已经提示过了
					//network_Client.show_Inf_UI_Dialog("请指定消息接收对象");
				}
				textField_send.setText("");//输入框清空
				frame.getRootPane().setDefaultButton(button_send);//按钮获取焦点
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
		textField_send_codec.setText("这是一个客户端单播测试");
		textField_send_codec.setText("ILOVEYOUTOO");
		
		button_send_codec = new JButton("发送密文");
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
				//(1)先检查本地密钥 key_Codec_Local 是否已经设置 未设置时值为 "$"
				//if(network_Client.key_Codec_Local.equals("$"))
				if(network_Client.key_Codec_Local==null)
				{
					//network_Client.show_UI_Dialog("编码策略尚未设置,请在[更新用户信息]中设置并通告服务器");
					network_Client.show_Msg("", "编码策略尚未设置,请在[更新用户信息]中设置并通告服务器");
				}
				else
				{
					//(2)再检测接受者是否为空
					if(!get_ReceiverID_from_textArea_recieverID().equals(""))//【接收者】不为空才发送消息
					{
						//(3)再检查密钥策略是否合法
						if(network_Client.codec.check_codepolicy(network_Client.key_Codec_Local))
						{
							//这才能建立消息 msg_send
							Msg msg_send=new Msg();
							msg_send.msg_Type=Msg_Type.chat_codec;
							msg_send.senderID=network_Client.login_Info.id_login;
							msg_send.recieverID=get_ReceiverID_from_textArea_recieverID();//此函数中【判断接收者是否为空】
							msg_send.senderName=network_Client.client_Info_Local.Name;
							msg_send.recieverName=get_ReceiverName_from_textArea_recieverID();
							msg_send.key_Codec=network_Client.key_Codec_Local;
							
							msg_send.msg_Content=network_Client.codec.Code(network_Client.key_Codec_Local,
									textField_send_codec.getText());//编码里边有检测过滤信息内容的功能
							
							//先在本地显示 
							//String str_chat="["+msg_send.senderID+"]对["+msg_send.recieverID+"]说 : "+msg_send.msg_Content;
							String str_chat="["+msg_send.senderID+":"+msg_send.senderName+"]对["
									+msg_send.recieverID+":"+msg_send.recieverName+"]说 : "+textField_send_codec.getText()
									+"[已加密]";
							
							show(str_chat);
							show("发送密文: "+msg_send.msg_Content);
							show_Chat(str_chat);
							
							//再发送至服务器
							network_Client.send_Msg(msg_send);//发送聊天信息
							
							//textField_send_codec.setText("");//输入框清空
							textField_send_codec.requestFocusInWindow();//输入框获取焦点
						}else
						{
							//network_Client.show_UI_Dialog("加密策略不合法");
							network_Client.show_Msg("", "加密策略不合法");
						}
					}
					else
					{
						//提示【接收者】为空//已经提示过了
						//network_Client.show_Inf_UI_Dialog("请指定消息接收对象");
					}		
				}				
				textField_send_codec.setText("");//输入框清空
				frame.getRootPane().setDefaultButton(button_send_codec);//按钮获取焦点
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
		//textArea_recieverID.setText("接收者");
		frame.getContentPane().add(textArea_recieverID);
		// ===================================================================
		// ===================================================================
		// ===================================================================
		button_Edit = new JButton("更新用户信息");
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
		//初始状态让 [发送明文按钮]获取焦点
		frame.getRootPane().setDefaultButton(button_send);//按钮获取焦点
		// ===================================================================
		// ===================================================================
	}
	// ===================================================================
	// ===================================================================
	public String get_ReceiverID_from_textArea_recieverID()
	{
		if(textArea_recieverID.getText().equals(""))
		{
			//network_Client.show_UI_Dialog("请指定消息接收对象");
			network_Client.show_Msg("", "请指定消息接收对象");
			return "";
		}
		String [] temp=textArea_recieverID.getText().split(":");
		return temp[0];//返回被":"分隔的第一个String
	}
	// ===================================================================
	public String get_ReceiverName_from_textArea_recieverID()
	{
		if(textArea_recieverID.getText().equals(""))
		{
			//network_Client.show_UI_Dialog("请指定消息接收对象");
			network_Client.show_Msg("", "请指定消息接收对象");
			return "";
		}
		String [] temp=textArea_recieverID.getText().split(":");
		return temp[1];//返回被":"分隔的第二个String
	}
	// ===================================================================
	// ===================================================================
}
