package network.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import network.server.Network_Server;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.InetAddress;

public class Config_UI
{
	public Network_Server network_Server;
	private JFrame frame;
	private JTextField textField_Ip;
	private JTextField textField_Port;
	private JButton btnOk;
	private JButton btnCancel;
	private JLabel lblIp;
	private JLabel lblPort;

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
					Config_UI window = new Config_UI();
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
	public Config_UI()
	{
		initialize();
		frame.setVisible(true);
	}
	public Config_UI(Network_Server network_Server)
	{
		initialize();
		frame.setVisible(true);
		try
		{
			textField_Ip.setText(InetAddress.getLocalHost().getHostAddress());
			textField_Port.setText(Integer.toString(network_Server.server_Port));
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		this.network_Server=network_Server;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(500, 300, 280, 200);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("服务器设置");
		
		textField_Ip = new JTextField();
		textField_Ip.setText("127.0.0.1");
		textField_Ip.setBounds(100, 20, 150, 30);
		frame.getContentPane().add(textField_Ip);
		textField_Ip.setColumns(10);
		
		textField_Port = new JTextField();
		textField_Port.setText("11111");
		textField_Port.setBounds(100, 60, 150, 30);
		frame.getContentPane().add(textField_Port);
		textField_Port.setColumns(10);
		
		btnOk = new JButton("确定");
		btnOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				frame.setVisible(false);
				//new Network_Server();
				String string_New_Address=textField_Ip.getText();
				String string_New_Port=textField_Port.getText();
				
				try
				{
					//服务器 地址 端口
					frame.setVisible(false);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		btnOk.setBounds(20, 120, 100, 30);
		frame.getContentPane().add(btnOk);
		
		btnCancel = new JButton("取消");
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				//frame.dispose();
				frame.setVisible(false);
			}
		});
		btnCancel.setBounds(150, 120, 100, 30);
		frame.getContentPane().add(btnCancel);
		
		lblIp = new JLabel("地址");
		lblIp.setBounds(50, 20, 30, 30);
		frame.getContentPane().add(lblIp);
		
		lblPort = new JLabel("端口");
		lblPort.setBounds(50, 60, 30, 30);
		frame.getContentPane().add(lblPort);
	}
}
