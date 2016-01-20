package network.ui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import network.client.Network_Client;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Inf_UI
{
	// ======================================================================
	public Network_Client network_Client;
	// ======================================================================
	public JFrame frame;
	public JTextArea textArea;
	public JButton button_OK;
	public JButton button_Cancel;
	public JScrollPane scrollPane;

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
					Inf_UI window = new Inf_UI();
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
	public Inf_UI()
	{
		initialize();
		frame.setVisible(true);//============================================
	}
	public Inf_UI(String string)
	{
		initialize();
		frame.setVisible(true);//============================================
		textArea.setText(string);
	}
	public Inf_UI(Network_Client network_Client)
	{
		this.network_Client = network_Client;
		
		initialize();
		frame.setVisible(true);//============================================
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(200, 200, 400, 300);
		//此处【注释掉】是不想让此UI被关闭时主程序也被关闭
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		//===================================================================
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setAutoscrolls(true);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 10, 370, 200);
		scrollPane.setAutoscrolls(true);
		scrollPane.setAutoscrolls(true);
		frame.getContentPane().add(scrollPane);
		//===================================================================
		
		button_OK = new JButton("OK");
		button_OK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				frame.dispose();
			}
		});
		button_OK.setBounds(10, 230, 120, 30);
		frame.getContentPane().add(button_OK);
		
		button_Cancel = new JButton("Cancel");
		button_Cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frame.dispose();
			}
		});
		button_Cancel.setBounds(260, 230, 120, 30);
		frame.getContentPane().add(button_Cancel);
	}
	public void show_Inf_Dialog(String inf)
	{
		textArea.setText(inf);
	}
}
