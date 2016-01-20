package network.ui;

import java.awt.*;
import javax.swing.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import network.client.Network_Client;
import network.util.Login_Info;

import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class Login_UI
{
	// ======================================================================
	public Network_Client network_Client;
	// ======================================================================
	public JFrame frame;
	public JTextField textField_ip;
	public JTextField textField_port;
	public JTextField textField_port_local;
	public JTextField textField_id;
	public JPasswordField passwordField;
	
	public JLabel label_ip;
	public JLabel label_port;
	public JLabel label_port_local;
	public JLabel label_id;
	public JLabel label_password;
	
	public JButton button_login;
	public JButton button_register;
	
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
					Login_UI window = new Login_UI();
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
	public Login_UI()
	{
		initialize();
		frame.setVisible(true);
		set_Default_Info();
	}
	public Login_UI(Network_Client network_Client)
	{
		this.network_Client = network_Client;
		
		initialize();
		frame.setVisible(true);
		set_Default_Info();
	}
	// #######################################################################
	public void show(String string)
	{
		this.textArea.append(string+"\n");
		// 添加滚动条自动滚动到底//此方法最简单 OK
		// =========================================================
		this.textArea.setSelectionStart(this.textArea.getText().length());
		// =========================================================
	}
	// #######################################################################
	// ===================================================================
	public void set_Default_Info()
	{
		textField_ip.setText("127.0.0.1");
		textField_port.setText("11111");
		textField_id.setText("");
		passwordField.setText("000");
	}
	// ===================================================================

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("登陆服务器");
		// ===================================================================
		label_ip = new JLabel("地址");
		label_ip.setBounds(20, 30, 60, 30);
		label_ip.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_ip);

		label_port = new JLabel("端口");
		label_port.setBounds(20, 70, 60, 30);
		label_port.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_port);
		
		label_port_local = new JLabel("本机端口");
		label_port_local.setBounds(20, 110, 60, 30);
		label_port_local.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_port_local);

		label_id = new JLabel("账号");
		label_id.setBounds(20, 150, 60, 30);
		label_id.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_id);

		label_password = new JLabel("密码");
		label_password.setBounds(20, 190, 60, 30);
		label_password.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(label_password);
		//===================================================================
		textField_ip = new JTextField();
		textField_ip.setBounds(80, 30, 200, 30);
		frame.getContentPane().add(textField_ip);
		textField_ip.setColumns(10);
		
		textField_port = new JTextField();
		textField_port.setBounds(80, 70, 200, 30);
		frame.getContentPane().add(textField_port);
		textField_port.setColumns(10);
		
		textField_port_local = new JTextField();
		textField_port_local.setBounds(80, 110, 200, 30);
		frame.getContentPane().add(textField_port_local);
		textField_port_local.setColumns(10);
		textField_port_local.setText("随机分配");
		
		textField_id = new JTextField();
		textField_id.setBounds(80, 150, 200, 30);
		frame.getContentPane().add(textField_id);
		textField_id.setColumns(10);
		textField_id.requestFocusInWindow();//输入框获取焦点
		
		passwordField = new JPasswordField();
		passwordField.setBounds(80, 190, 200, 30);
		frame.getContentPane().add(passwordField);
		//===================================================================
		button_login = new JButton("登录");
		button_login.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(textField_port_local.getText().equals("随机分配"))
				{
					network_Client.on_Btn_Login(textField_ip.getText(),
							textField_port.getText(),
							textField_id.getText(),
							passwordField.getText());					
				}
				else
				{
					try
					{
						int port_Input=Integer.parseInt(textField_port_local.getText());
						network_Client.on_Btn_Login(port_Input,
								textField_ip.getText(),
								textField_port.getText(),
								textField_id.getText(),
								passwordField.getText());						
					} catch (Exception e2)
					{
						e2.printStackTrace();
						network_Client.show_Msg("端口错误", "端口错误 请重新输入");
					}
				}
				
			}
		});
		button_login.setBounds(20, 250, 130, 30);
		frame.getContentPane().add(button_login);
		//===================================================================
		button_register = new JButton("注册");
		button_register.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(textField_port_local.getText().equals("随机分配"))
				{
					network_Client.on_Btn_Register(textField_ip.getText(),
							textField_port.getText(),
							textField_id.getText(),
							passwordField.getText());
				}
				else
				{
					try
					{
						int port_Input=Integer.parseInt(textField_port_local.getText());
						network_Client.on_Btn_Register(port_Input,
								textField_ip.getText(),
								textField_port.getText(),
								textField_id.getText(),
								passwordField.getText());						
					} catch (Exception e2)
					{
						e2.printStackTrace();
						network_Client.show_Msg("端口错误", "端口错误 请重新输入");
					}
				}
			}
		});
		button_register.setBounds(150, 250, 130, 30);
		frame.getContentPane().add(button_register);
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
		//===================================================================
		//===================================================================
		//===================================================================
		//===================================================================
	}
}
