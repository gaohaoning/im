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
import network.server.Network_Server.Server_Thread;
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

public class Update_UI_Search
{
	// ======================================================================
	public Network_Server network_Server;
	// ======================================================================
	public JFrame frame;
	
	public JTextField textField_id;
	public JTextField textField_password;
	public JTextField textField_name;
	public JTextField textField_key;
	
	public JLabel label_id;
	public JLabel label_password;
	public JLabel label_name;
	public JLabel label_key;
	
	public JTextField textField_search_key;
	public JLabel label_search_key;
	
	public JButton button_send_update_SEARCH;
	public JButton button_cancel;
	
	public JTextArea textArea;
	public JScrollPane scrollPane;
	// ======================================================================
	// ========================================
	public int index_To_Replace;
	public Server_Thread server_Thread_To_Notify_Change;
	// ========================================

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
					Update_UI_Search window = new Update_UI_Search();
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
	public Update_UI_Search()
	{
		initialize();
		frame.setVisible(true);//============================================
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public Update_UI_Search(Network_Server network_Server)
	{
		this.network_Server = network_Server;
		
		initialize();
		frame.setVisible(true);//============================================
		
		frame.getRootPane().setDefaultButton(button_send_update_SEARCH);//按钮获取焦点
	}
	
	//修改哪一行需要从 AbstractTableModel 获得参数
	public Update_UI_Search(Network_Server network_Server,int index)//修改哪一行需要从 AbstractTableModel 获得参数
	{
		this.network_Server = network_Server;
		this.index_To_Replace=index;
		
		initialize();
		frame.setVisible(true);
		
		Client_Info client_Info_To_Set=network_Server.infos_Reg.elementAt(index);//把这个显示在修改菜单上
		
		textField_id.setText(client_Info_To_Set.ID);
		textField_password.setText(client_Info_To_Set.Password);
		textField_name.setText(client_Info_To_Set.Name);
		textField_key.setText(client_Info_To_Set.Key);
		
		//#############################################################################
		//在构造函数中先得到这个线程 然后资料有所修改时用这个线程通告该用户
		server_Thread_To_Notify_Change=network_Server.get_Server_Thread_by_Client_ID(client_Info_To_Set.ID);
		//#############################################################################
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
		frame.setTitle("修改用户信息");
		// ===================================================================
		label_id = new JLabel("账号");
		label_id.setBounds(20, 140, 60, 30);
		label_id.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_id);

		label_password = new JLabel("密码");
		label_password.setBounds(20, 180, 60, 30);
		label_password.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_password);
		
		label_name = new JLabel("名称");
		label_name.setBounds(20, 220, 60, 30);
		label_name.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_name);

		label_key = new JLabel("密钥");
		label_key.setBounds(20, 260, 60, 30);
		label_key.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_key);
		//===================================================================
		textField_id = new JTextField();
		textField_id.setEditable(false);
		textField_id.setBounds(80, 140, 200, 30);
		frame.getContentPane().add(textField_id);
		textField_id.setColumns(10);
		
		textField_password = new JTextField();
		textField_password.setEditable(false);
		textField_password.setBounds(80, 180, 200, 30);
		frame.getContentPane().add(textField_password);
		textField_password.setColumns(10);
		
		textField_name = new JTextField();
		textField_name.setEditable(false);
		textField_name.setBounds(80, 220, 200, 30);
		frame.getContentPane().add(textField_name);
		textField_name.setColumns(10);
		
		textField_key = new JTextField();
		textField_key.setEditable(false);
		textField_key.setBounds(80, 260, 200, 30);
		frame.getContentPane().add(textField_key);
		textField_key.setColumns(10);
		//===================================================================
		button_send_update_SEARCH = new JButton("搜索");
		button_send_update_SEARCH.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});
		button_send_update_SEARCH.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//===================================================================
				String search_Key=textField_search_key.getText();
				for(int i=0;i<network_Server.infos_Reg.size();i++)
				{
					if((network_Server.infos_Reg.elementAt(i).ID.equals(search_Key))||
							(network_Server.infos_Reg.elementAt(i).Name.equals(search_Key)))
					{
						textField_id.setText(network_Server.infos_Reg.elementAt(i).ID);
						textField_password.setText(network_Server.infos_Reg.elementAt(i).Password);
						textField_name.setText(network_Server.infos_Reg.elementAt(i).Name);
						textField_key.setText(network_Server.infos_Reg.elementAt(i).Key);
						return;
					}
				}
				//network_Server.show_UI_Dialog("找不到相关用户信息");
				network_Server.show_Msg("", "找不到相关用户信息");
				//===================================================================
			}
		});
		button_send_update_SEARCH.setBounds(20, 90, 130, 30);
		frame.getContentPane().add(button_send_update_SEARCH);
		
		button_cancel = new JButton("取消");
		button_cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
		button_cancel.setBounds(150, 90, 130, 30);
		frame.getContentPane().add(button_cancel);
			
		//===================================================================
		//JScrollPane 是 JTextArea 的容器
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
		label_search_key = new JLabel("关键词：(ID 或 名称 结果在下面显示)");
		label_search_key.setBounds(20, 10, 260, 30);
		label_search_key.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_search_key);
		
		textField_search_key = new JTextField();
		textField_search_key.setBounds(20, 50, 260, 30);
		frame.getContentPane().add(textField_search_key);
		textField_search_key.setColumns(10);
		
		//===================================================================
		//===================================================================
		//===================================================================
		//===================================================================
	}
}



