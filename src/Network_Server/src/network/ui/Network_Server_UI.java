package network.ui;

import javax.swing.*;
import java.awt.*;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JList;

import network.server.Network_Server;
import network.util.Client_Info;
import network.util.Msg;
import network.util.Msg_Type;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.Option;
import javax.tools.OptionChecker;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class Network_Server_UI
{
	public Network_Server network_Server;
	private JFrame frame;
	
	public JTextArea textArea;
	public JScrollPane scrollPane;
	
	public DefaultListModel defaultListModel_online;
	public JList list;
	public JScrollPane scrollPane_online;//
	
	public DefaultListModel defaultListModel_reg;
	public JList list_reg;
	public JScrollPane scrollPane_reg;//
	
	public JTabbedPane tabbedPane_Info;
	public JTabbedPane tabbedPane_List;
	
	public JTextField textField_broadcast;
	public JTextField textField_broadcast_codec;
	public JButton button_broadcast;
	
	public JButton button_Detail;
	public JButton button_Kickout;
	
	public JTextArea textArea_recieverID;
	
	JMenuBar menuBar;
	JMenu mnUserdata;	
	JMenuItem mntmImport;	
	JMenuItem mntmExport;	
	JMenu mnAbout;	
	JMenuItem mntmInf;
	
	JMenu mn_Network;	
	JMenuItem mn_Network_Config;

	/**
	 * Launch the application.
	 */
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
					Network_Server_UI window = new Network_Server_UI();
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
	
	public Network_Server_UI()
	{
		initialize();
		frame.setVisible(true);
	}
	public Network_Server_UI(Network_Server network_Server)
	{
		this.network_Server = network_Server;
		initialize();
		frame.setVisible(true);
		
		textArea.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
	}
	public void show(String string)
	{
		this.textArea.append(string+"\n");
		//添加滚动条自动滚动到底//此方法最简单 OK
		this.textArea.setSelectionStart(this.textArea.getText().length());
	}
	public void show_(String string)//不换行输出
	{
		this.textArea.append(string);
		//添加滚动条自动滚动到底//此方法最简单 OK
		this.textArea.setSelectionStart(this.textArea.getText().length());
	}
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(300, 300, 760, 410);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Server");
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mn_Network = new JMenu("网络");
		menuBar.add(mn_Network);
		
		mn_Network_Config = new JMenuItem("网络设置");
		mn_Network_Config.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				new Config_UI(network_Server);
			}
		});
		mn_Network.add(mn_Network_Config);
		
		mnUserdata = new JMenu("用户数据");
		menuBar.add(mnUserdata);
		
		mntmImport = new JMenuItem("导入用户数据");
		mntmImport.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.showOpenDialog(null);
				String file_String=fileChooser.getSelectedFile().getPath();
				System.out.println(file_String);
				
				network_Server.read_User_Info_Reg_from_File(file_String);
			}
		});
		mnUserdata.add(mntmImport);
		
		mntmExport = new JMenuItem("导出用户数据");
		mntmExport.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.showSaveDialog(null);
				String file_String=fileChooser.getSelectedFile().getPath();
				System.out.println(file_String);
				
				network_Server.write_User_Info_Reg_to_File(file_String);
			}
		});
		mnUserdata.add(mntmExport);
		
		mnAbout = new JMenu("关于");
		menuBar.add(mnAbout);
		
		mntmInf = new JMenuItem("软件信息");
		mntmInf.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane optionPane=new JOptionPane();
				String inf=new String(
						"天津大学电子信息工程学院 2009级硕士 \n"+
						"高浩宁：\n");
				optionPane.showMessageDialog(null, inf,"软件信息",JOptionPane.DEFAULT_OPTION);
			}
		});
		mnAbout.add(mntmInf);
		//JScrollPane 是 JTextArea 的容器
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setAutoscrolls(true);		
		scrollPane = new JScrollPane(textArea);		
		scrollPane.setAutoscrolls(true);
		scrollPane.setAutoscrolls(true);
		tabbedPane_Info = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_Info.setBounds(10, 10, 500, 300);
		frame.getContentPane().add(tabbedPane_Info);

		tabbedPane_Info.addTab("信息", null, scrollPane, null);
		//tabbedPane_Info.addTab("用户资料", null, scrollPane, null);
		//JList 是 DefaultListModel 的容器
		defaultListModel_online=new DefaultListModel();
		list = new JList(defaultListModel_online);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(Color.LIGHT_GRAY);
		list.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				int index=list.getSelectedIndex();
				String string_ListModel=network_Server.infos_Online.elementAt(index).ID
						+":"+network_Server.infos_Online.elementAt(index).Name;
				textArea_recieverID.setText(string_ListModel);
			}
		});
		list.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{				
			}
		});		
		scrollPane_online = new JScrollPane(list);
		scrollPane_online.setAutoscrolls(true);
		scrollPane_online.setAutoscrolls(true);
		
		// JList 是 DefaultListModel 的容器
		defaultListModel_reg = new DefaultListModel();
		list_reg = new JList(defaultListModel_reg);
		list_reg.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_reg.setBackground(Color.LIGHT_GRAY);
		list_reg.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int index=list_reg.getSelectedIndex();
				String string_ListModel_reg=network_Server.infos_Reg.elementAt(index).ID
						+":"+network_Server.infos_Reg.elementAt(index).Name;
				textArea_recieverID.setText(string_ListModel_reg);
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
		tabbedPane_List = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_List.setBounds(520, 10, 220, 300);
		frame.getContentPane().add(tabbedPane_List);
		
		tabbedPane_List.addTab("在线用户", null, scrollPane_online, null);
		tabbedPane_List.addTab("注册用户", null, scrollPane_reg, null);
		textField_broadcast = new JTextField();
		textField_broadcast.setBounds(10, 320, 380, 30);
		frame.getContentPane().add(textField_broadcast);
		textField_broadcast.setColumns(10);
		textField_broadcast.setText("服务器广播 : 这是一个服务器广播测试");
		
		
		textField_broadcast_codec = new JTextField();
		textField_broadcast_codec.setBounds(10, 360, 380, 30);
		frame.getContentPane().add(textField_broadcast_codec);
		textField_broadcast_codec.setColumns(10);
		textField_broadcast_codec.setText("其他信息");
		
		button_broadcast = new JButton("广播");
		button_broadcast.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{				
				String string_Broadcast=textField_broadcast.getText();
				show("服务器发送【广播】【消息】 : "+string_Broadcast);
				network_Server.on_Btn_Broadcast(string_Broadcast);
			}
		});
		button_broadcast.setBounds(410, 320, 100, 30);
		frame.getContentPane().add(button_broadcast);
		button_Detail = new JButton("用户资料");
		button_Detail.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//(1)
				//network_Server.detail_Info_UI.frame.setVisible(true);
				//旧版本在新窗口中显示 detail 现在改成在 TabbledPane 中显示
				Container container_Detail=network_Server.detail_Reg_Info_UI.frame.getContentPane();
				tabbedPane_Info.addTab("注册资料", null, container_Detail, null);				
				tabbedPane_Info.setSelectedIndex(1);
				
				//(2)
				Container container_Detail_Online=network_Server.detail_Online_Info_UI.frame.getContentPane();
				tabbedPane_Info.addTab("在线用户", null, container_Detail_Online, null);
				tabbedPane_Info.setSelectedIndex(2);
				
				//这个按钮用来【刷新 Online IP Port 信息】
			}
		});
		button_Detail.setBounds(630, 320, 110, 30);
		frame.getContentPane().add(button_Detail);
		button_Kickout = new JButton("踢出");
		button_Kickout.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String Id=get_ReceiverID_from_textArea_recieverID();
				network_Server.KickOut_User_Outside_Thread_by_ID(Id);				
				show("踢出 : "+Id);
			}
		});
		button_Kickout.setBounds(520, 320, 110, 30);
		frame.getContentPane().add(button_Kickout);
		textArea_recieverID = new JTextArea();
		textArea_recieverID.setEditable(false);
		textArea_recieverID.setBackground(Color.LIGHT_GRAY);
		textArea_recieverID.setAutoscrolls(true);
		textArea_recieverID.setBounds(520, 360, 220, 30);
		frame.getContentPane().add(textArea_recieverID);
	}
	
	
	public String get_ReceiverID_from_textArea_recieverID()
	{
		if(textArea_recieverID.getText().equals(""))
		{
			//network_Server.show_UI_Dialog("请指明操作对象");
			network_Server.show_Msg("", "请指明操作对象");
			return "";
		}
		String [] temp=textArea_recieverID.getText().split(":");//注意此处在结尾增加了 " "
		return temp[0];//返回被":"分隔的第一个String
	}
}
