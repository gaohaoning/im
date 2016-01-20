package network.client;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import network.codec.*;
import network.util.*;
import network.ui.*;

public class Network_Client_Line
{
	//#######################################################################
	public Codec codec;
	//#######################################################################
	public Socket socket;
	public ObjectInputStream in_obj;
	public ObjectOutputStream out_obj;
	//#######################################################################
	public BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
	//#######################################################################
	//#######################################################################
	public Login_UI login_UI;
	public Network_Client_UI network_Client_UI;
	public Update_UI update_UI;
	
	public Login_Info login_Info;//【本地用户登陆信息】
	public String key_Codec_Local;//【本地用户加密信息】//只有当本地用户修改了资料并同步到服务器了才给它赋值
	
	public Client_Info client_Info_Local;//【本地用户】信息
	public Vector<Client_Info> infos_Online;// 所有【在线用户】可以动态修改
	public Vector<Client_Info> infos_Reg;// 所有【注册用户】初始时从文件读取
	public String str_Client_Info_Online;//以String方式维护用户ID和Name信息
	public String str_Client_Info_Reg;//以String方式维护用户ID和Name信息
	
	public Client_Thread client_Thread;//与服务器通信的线程
	//#######################################################################################
	public Network_Client_Line()
	{
		//#######################################################################
		codec=new Codec(this);
		//#######################################################################
		client_Info_Local = new Client_Info();
		infos_Online = new Vector<Client_Info>();
		infos_Reg = new Vector<Client_Info>();
		
		login_UI=new Login_UI(this);//创建 GUI
		network_Client_UI=new Network_Client_UI(this);//创建 GUI
//		network_Client_UI.frame.setVisible(false);
		update_UI=new Update_UI(this);//创建 GUI
//		update_UI.frame.setVisible(false);
	}
	//#######################################################################################
	//#######################################################################################
	public Network_Client_Line(boolean is_Line_Mode_On)
	{
		//#######################################################################
		codec=new Codec(this);
		//#######################################################################
		client_Info_Local = new Client_Info();
		infos_Online = new Vector<Client_Info>();
		infos_Reg = new Vector<Client_Info>();
		//#######################################################################
		Command_Line();//处理命令行		
		//#######################################################################
	}
	//#######################################################################################
	public boolean init_Socket()
	{
		try
		{
			InetAddress server_ip=InetAddress.getByName(login_Info.address_server_str);
			int server_port=Integer.parseInt(login_Info.port_server);
			show_Login("【连接服务器】"+"地址: "+server_ip+" 端口: "+server_port+"...");
			
			socket=new Socket(server_ip, server_port);
			show_Login("【连接服务器】 OK");
			
			out_obj=new ObjectOutputStream(socket.getOutputStream());
			in_obj=new ObjectInputStream(socket.getInputStream());
			show_Login("【输入输出流】 OK");
		} catch (Exception e)
		{
			// TODO: handle exception
			show_Login("【初始化连接】 异常");
			return false;
		}
		show_Login("==================================================");
		return true;
	}
	//#######################################################################################
	//#######################################################################################
	public boolean init_Socket(int port_Local)
	{
		try
		{
			InetAddress server_ip=InetAddress.getByName(login_Info.address_server_str);
			int server_port=Integer.parseInt(login_Info.port_server);
			show_Login("【连接服务器】"+"地址: "+server_ip+" 端口: "+server_port+"...");
			
			//socket=new Socket(server_ip, server_port);
			socket=new Socket(server_ip, server_port,InetAddress.getLocalHost() , port_Local);//指定本地端口
			show_Login("【连接服务器】 OK");
			
			out_obj=new ObjectOutputStream(socket.getOutputStream());
			in_obj=new ObjectInputStream(socket.getInputStream());
			show_Login("【输入输出流】 OK");
		} catch (Exception e)
		{
			// TODO: handle exception
			show_Login("【初始化连接】 异常");
			return false;
		}
		show_Login("==================================================");
		return true;
	}
	//#######################################################################################
	public void close_Socket(Thread thread)
	{
		try
		{
			in_obj.close();
			out_obj.close();
			socket.close();
			show("【与服务器断开连接】");
			
			if(thread.isAlive())
			{
				show("【关闭 监听线程】 : "+thread.getId()+" "+thread.getName());
				thread.interrupt();
				show("【关闭 监听线程】 : OK");				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("close_Socket 异常");
		}
	}
	//#######################################################################################
	// ======================================================================
	public void on_Btn_Login(String address_server_str, String port_server,String id_login, String password_login)
	{
		login_Info=new Login_Info(address_server_str, port_server, id_login, password_login);
		if(!init_Socket())//连接服务器 输入输出流
		{
			show_Login("无法连接到服务器，请检查[地址]和[端口]");
			return;
		}
		
		client_Thread=new Client_Thread();
		client_Thread.start();
		
		send_Msg_Login();		
	}
	// ======================================================================
	public void on_Btn_Register(String address_server_str, String port_server,String id_login, String password_login)
	{
		login_Info=new Login_Info(address_server_str, port_server, id_login, password_login);		
		if(!init_Socket())//连接服务器 输入输出流
		{
			show_Login("无法连接到服务器，请检查[地址]和[端口]");
			return;
		}
		
		client_Thread=new Client_Thread();
		client_Thread.start();
		
		send_Msg_Register();
	}
	// ======================================================================
	//#######################################################################################
	
	//#######################################################################################
	// ======================================================================
	public void on_Btn_Login(int port_Local,String address_server_str, String port_server,String id_login, String password_login)
	{
		login_Info=new Login_Info(address_server_str, port_server, id_login, password_login);
		if(!init_Socket(port_Local))//连接服务器 输入输出流
		{
			show_Login("无法连接到服务器，请检查[地址]和[端口]");
			return;
		}
		
		client_Thread=new Client_Thread();
		client_Thread.start();
		
		send_Msg_Login();		
	}
	// ======================================================================
	public void on_Btn_Register(int port_Local,String address_server_str, String port_server,String id_login, String password_login)
	{
		login_Info=new Login_Info(address_server_str, port_server, id_login, password_login);		
		if(!init_Socket(port_Local))//连接服务器 输入输出流
		{
			show_Login("无法连接到服务器，请检查[地址]和[端口]");
			return;
		}
		
		client_Thread=new Client_Thread();
		client_Thread.start();
		
		send_Msg_Register();
	}
	// ======================================================================
	//#######################################################################################
	public void on_Btn_Edit()
	{
		update_UI.frame.setVisible(true);
		update_UI.textField_id.setText(client_Info_Local.ID);
	}
	
	//#######################################################################################
	public void on_Button_Send_Update(Update_Info update_Info)
	{
		Msg msg_Update=new Msg(Msg_Type.user_data_update);
		msg_Update.msg_Update_Info=update_Info;
		send_Msg(msg_Update);
	}
	//#######################################################################################
	//#######################################################################################
	public boolean send_Msg(Msg msg)
	{
		try
		{
			out_obj.writeObject(msg);
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("send_Msg 异常");
			return false;
		}
	}
	//#######################################################################################
	public void send_Msg_Login()
	{
		try
		{
			// ======================================================================
			Msg msg=new Msg(Msg_Type.login);
			msg.msg_Client_Info=new Client_Info();			
			msg.msg_Content="This is a Login Request";
			msg.msg_Client_Info.ID=login_Info.id_login;
			msg.msg_Client_Info.Password=login_Info.password_login;			
			out_obj.writeObject(msg);
			// ======================================================================
		} catch (Exception e)
		{
			e.printStackTrace();
			login_UI.show("send_Msg_Login 异常");
		}
	}
	//#######################################################################################
	public void send_Msg_Register()
	{
		try
		{
			// ======================================================================
			Msg msg=new Msg(Msg_Type.register);
			msg.msg_Client_Info=new Client_Info();			
			msg.msg_Content="This is a Register Request";
			msg.msg_Client_Info.ID=login_Info.id_login;
			msg.msg_Client_Info.Password=login_Info.password_login;			
			out_obj.writeObject(msg);
			// ======================================================================
		} catch (Exception e)
		{
			e.printStackTrace();
			login_UI.show("send_Msg_Register 异常");
		}
	}
	//#######################################################################################
	//#######################################################################################
	public class Client_Thread extends Thread
	{
		//#######################################################################################
		private long thread_ID = this.getId();
		private String thread_Name = this.getName();
		Msg msg_R;
		//#######################################################################################
		@Override
		public void run()
		{
			//#######################################################################################
			show_Login("【开始监听 Server 消息...】");
			show_Login("【线程】ID : "+Long.toString(thread_ID)+" Name : "+thread_Name);
			//#######################################################################################
			try
			{
				while(true)
				{
					msg_R = (Msg) in_obj.readObject();					
					show("==================================================");
					show("收到【服务器消息】【类型】: " + msg_R.msg_Type);
					show("==================================================");
					
					switch (msg_R.msg_Type)
					{
					case login_echo_accept:
						// ======================================================================
						show("登录成功 Login Success");
						out("登录成功 Login Success");
//						login_UI.frame.setVisible(false);//不可见 login_UI
//						network_Client_UI.frame.setVisible(true);
//						network_Client_UI.frame.setTitle("Client : "+login_Info.id_login);//标题栏予以标识不同客户端
						break;
						// ======================================================================						
					case login_echo_reject:
						// ======================================================================
						show("登录失败 Login Failed");
						out("登录失败 Login Failed");
						close_Socket(this);
						break;
						// ======================================================================
					case register_echo_accept:
						// ======================================================================
						show("注册成功 Register Success");
						out("注册成功 Register Success");
//						login_UI.frame.setVisible(false);//不可见 login_UI
//						network_Client_UI.frame.setVisible(true);
//						network_Client_UI.frame.setTitle("Client : "+login_Info.id_login);
						break;
						// ======================================================================
					case register_echo_reject:
						// ======================================================================
						show("注册失败 Register Failed");
						out("注册失败 Register Failed");
						close_Socket(this);
						break;
						// ======================================================================
					case users_update:
						//#######################################################################
						show("收到【在线】和【注册】用户更新信息 $$$$$$$$$$$$$$$$$$$$ : ");	
						//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ 用 String 更新客户端列表
						show(msg_R.online_String);
						show(msg_R.reg_String);
						
//						update_User_List_Online_by_String(msg_R.online_String);
//						update_User_List_Reg_by_String(msg_R.reg_String);
						
						str_Client_Info_Online=msg_R.online_String;
						str_Client_Info_Reg=msg_R.reg_String;
						//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ 用 String 更新客户端列表
						//#######################################################################
						//#######################################################################
						break;
						//#######################################################################
						case kickedout:
						// ======================================================================		
						close_Socket(this);
						out("【你被踢出】 \n"+" 原因: \n 1.相同ID用户异地登陆 \n 2.服务器踢你");
						break;
					    // ======================================================================
					case broadcast:
						out("收到【服务器广播】"+msg_R.msg_Content);
						break;
						// ======================================================================
					case chat:
						String str_chat="["+msg_R.senderID+":"+msg_R.senderName+"]对["
								+msg_R.recieverID+":"+msg_R.recieverName+"]说 : "+msg_R.msg_Content;
						out(str_chat);
						break;
						// ======================================================================
					case chat_codec:
						//show_Chat_CODEC_Msg_on_textArea(msg_R);//在这里边做解码并显示
						//先显示收到密文
//						out("["+msg_R.senderID+":"+msg_R.senderName+"]对["
//								+msg_R.recieverID+":"+msg_R.recieverName+"]说 : ");
//						out(msg_R.msg_Content+"[密文]");
						//根据本地用户维护的密钥信息 KEY 进行 解密并显示
						String decoded=codec.Decode(key_Codec_Local, msg_R.msg_Content);						
						String str_chat_codec="["+msg_R.senderID+":"
						+get_ReceiverName_from_Reg_String_By_ID(msg_R.senderID)
						+"]对["
						+msg_R.recieverID+":"
						+get_ReceiverName_from_Reg_String_By_ID(msg_R.recieverID)
						+"]说 : "+decoded+"[已解密]";
						
						out(str_chat_codec);
						
						break;
						// ======================================================================
					case user_data_update:
						show("收到【服务器端注册用户信息更新】");
						show(msg_R.msg_Update_Info.user_ID);
						show(msg_R.msg_Update_Info.user_Password);
						show(msg_R.msg_Update_Info.user_Name);
						show(msg_R.msg_Update_Info.user_Key);						
						set_Server_Echo_Detail_To_Local_And_Update_UI(msg_R);
						break;
//					    // ======================================================================
//						// ======================================================================
//						// ======================================================================
//						// ======================================================================
//						// ======================================================================
//						// ======================================================================
//						// ======================================================================
//						// ======================================================================
//						// ======================================================================
					default:
						show("【未知信息类型】");
					}
					
					//terminate_Thread_and_Restart_Thread();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				show("客户端监听线程异常 : Run Exception");
			}
			
		}
	}
	// ======================================================================
	//#######################################################################################
	// ======================================================================
	public void show_Chat_CODEC_Msg_on_textArea(Msg msg)//在这里边做解码并显示
	{
		//先显示收到密文
		show("收到 : ["+msg.senderID+"] 发给 ["+msg.recieverID+"] 的密文 :");
		//show_Chat("收到 : ["+msg.senderID+"] 发给 ["+msg.recieverID+"] 的密文 :");
		show(msg.msg_Content);
		//show_Chat(msg.msg_Content);
		//根据本地用户维护的密钥信息 KEY 进行 解密并显示
		String decoded=codec.Decode(key_Codec_Local, msg.msg_Content);
		
		String str_chat="["+msg.senderID+":"+msg.senderName+"]对["
				+msg.recieverID+":"+msg.recieverName+"]说 : "+decoded+"[已解密]";
		
		show(str_chat);
		show_Chat(str_chat);
	}
	// ======================================================================
	//#######################################################################################
	public void set_Server_Echo_Detail_To_Local_And_Update_UI(Msg msg_R)
	{
		client_Info_Local.ID=msg_R.msg_Update_Info.user_ID;
		client_Info_Local.Password=msg_R.msg_Update_Info.user_Password;
		client_Info_Local.Name=msg_R.msg_Update_Info.user_Name;
		client_Info_Local.Key=msg_R.msg_Update_Info.user_Key;
		
		key_Codec_Local=msg_R.msg_Update_Info.user_Key;
		
//		update_UI.textField_id.setText(client_Info_Local.ID);
//		update_UI.textField_password.setText(client_Info_Local.Password);
//		update_UI.textField_name.setText(client_Info_Local.Name);
//		update_UI.textField_key.setText(client_Info_Local.Key);
//		
//		network_Client_UI.frame.setTitle(client_Info_Local.ID+":"+client_Info_Local.Name);
	}
	//#######################################################################################
	//#######################################################################################
	//#######################################################################################
	//#######################################################################################
	public static void main(String[] args)
	{
		//new Network_Client_Line();
		new Network_Client_Line(true);
	}
	//#######################################################################################
	public void show(String string)
	{
		string=get_Time()+string;
		//System.out.println(string);
	}
	public void show_(String string)//不换行输出
	{
		//System.out.print(string);
	}
	public void show_Login(String string)
	{
		string=get_Time()+string;
		//System.out.println(string);
	}
	public void show_UI_Dialog(String string)//以对话框方式显示提示信息
	{
		string=get_Time()+string;
		//System.out.println(string);
	}
	//#######################################################################################
	public void show_Chat(String string)
	{
		string=get_Time()+string;
	}
	//#######################################################################################
	public String get_Time()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String str_time = simpleDateFormat.format(new Date());
		return "["+str_time+"]";
	}
	//#######################################################################################
	public void update_User_List_Online_by_String(String string)
	{	
		try
		{	
			String string_temp[]=string.split(";");	
			network_Client_UI.defaultListModel_online.clear();
			for(int i=0;i<string_temp.length;i++)
			{
				network_Client_UI.defaultListModel_online.addElement(string_temp[i]);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("update_User_List_Online_by_String 【更新 Online】异常");
		}
	}
	
	public void update_User_List_Reg_by_String(String string)
	{	
		try
		{	
			String string_temp[]=string.split(";");	
			network_Client_UI.defaultListModel_reg.clear();
			for(int i=0;i<string_temp.length;i++)
			{
				network_Client_UI.defaultListModel_reg.addElement(string_temp[i]);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("update_User_List_Reg_by_String 【更新 Reg】异常");
		}
	}
	//#######################################################################################
	public String get_Client_ID_Name_from_Str_Online_By_Index(int index)
	{
		String string_temp[]=str_Client_Info_Online.split(";");
		return string_temp[index];
	}
	public String get_Client_ID_Name_from_Str_Reg_By_Index(int index)
	{
		String string_temp[]=str_Client_Info_Reg.split(";");
		return string_temp[index];
	}
	//#######################################################################################
	//#######################################################################################
	// ======================================================================
	public void show_Msg(String title,String msg)
	{
		JOptionPane optionPane=new JOptionPane();
		optionPane.showConfirmDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}
	// =======================================================================
	//#######################################################################################
	//#######################################################################################
	public void Command_Line()
	{
		Command_Line_Login();
		Command_Line_Listen();
	}
	
	public void Command_Line_Login()
	{
		try
		{
			out("【Network_Client】 【命令行版】");
			out("请输入操作代码:");
			out("1.登录");
			out("2.注册");
			out("3.退出");
			
			while(true)
			{
				String in=input.readLine();

				if (in.equals("1"))
				{
					out("请输入服务器[地址][端口][ID][密码]");
					out_("地址 :");
					String address_server_str = input.readLine();
					out_("端口 :");
					String port_server = input.readLine();
					out_("本地端口 :");
					String port_server_Local = input.readLine();
					out_("ID :");
					String id_login = input.readLine();
					out_("密码 :");
					String password_login = input.readLine();

					on_Btn_Login(address_server_str, port_server, id_login,password_login);

					break;
				} else if (in.equals("2"))
				{
					out("请输入服务器[地址][端口][ID][密码]");
					out_("地址 :");
					String address_server_str = input.readLine();
					out_("端口 :");
					String port_server = input.readLine();
					out_("本地端口 :");
					String port_server_Local = input.readLine();
					out_("ID :");
					String id_login = input.readLine();
					out_("密码 :");
					String password_login = input.readLine();

					on_Btn_Register(address_server_str, port_server, id_login,password_login);

					break;
				} else if (in.equals("3"))
				{
					System.exit(0);
				} else
				{
					out("输入错误，请重新输入。");
				}
			}
		} catch (Exception e)
		{
			//e.printStackTrace();
		}
	}
	public void Command_Line_Listen()
	{
		try
		{
			out("登录成功 :");
			while(true)
			{
				out("【请输入操作代码】");
				out("1.查看在线用户");
				out("2.查看注册用户");
				out("3.修改用户资料");
				out("4.明文聊天");
				out("5.密文聊天");
				out("6.退出");
				String in=input.readLine();
				
				if(in.equals("1"))
				{
					out("【在线用户】");
					String [] online=str_Client_Info_Online.split(";");
					for(int i=0;i<online.length;i++)
					{
						out(online[i]);
					}
					out("【在线用户】");
				}else if(in.equals("2"))
				{
					out("【注册用户】");
					String [] reg=str_Client_Info_Reg.split(";");
					for(int i=0;i<reg.length;i++)
					{
						out(reg[i]);
					}
					out("【注册用户】");
				}else if(in.equals("3"))
				{
					Command_Line_Listen_Update();
				}else if(in.equals("4"))
				{
					Command_Line_Listen_Send();
				}else if(in.equals("5"))
				{
					Command_Line_Listen_Send_Codec();
				}else if(in.equals("6"))
				{
					System.exit(0);
				}else
				{
					out("输入有误，请重新输入。");
				}
			}
		} catch (Exception e)
		{
			//e.printStackTrace();
		}
    }
	//#######################################################################################
	public void Command_Line_Listen_Update()
	{
		try
		{
			//========================================================
			Update_Info update_Info=new Update_Info();
			
			out("【修改用户资料】");
			out("ID : "+client_Info_Local.ID);
			out("名称 : "+client_Info_Local.Name);
			out("密码 : "+client_Info_Local.Password);
			out("密钥 : "+client_Info_Local.Key);
			
			update_Info.user_ID=client_Info_Local.ID;
			
			out_("新名称: ");
			update_Info.user_Name=input.readLine();
			out_("新密码: ");
			update_Info.user_Password=input.readLine();
			while(true)
			{
				out_("新密钥: ");
				update_Info.user_Key=input.readLine();
				if(codec.check_codepolicy(update_Info.user_Key))
				{
					break;
				}
				out("密钥策略不正确 请重新输入");
			}
			
			//一定不要忘了更新本地加密策略 :
			key_Codec_Local=update_Info.user_Key;
			//这里还要加上更新本地用户信息 client_Info_Local
			client_Info_Local.ID=update_Info.user_ID;
			client_Info_Local.Password=update_Info.user_Password;
			client_Info_Local.Name=update_Info.user_Name;
			client_Info_Local.Key=update_Info.user_Key;
			//再向服务器发送数据
			on_Button_Send_Update(update_Info);
			//========================================================
		} catch (Exception e)
		{
			//e.printStackTrace();
		}
	}
	//#######################################################################################
	public void Command_Line_Listen_Send()
	{
		out("输入[back]返回上级菜单");
		try
		{
			while(true)
			{
				out("【命令格式】: "+"[接收者ID] + ':' + [消息内容]");
				String str_input=input.readLine();
				if(str_input.equals("back"))
				{
					break;
				}
				try
				{
					String []string_send=str_input.split(":");
					Msg msg_send=new Msg(Msg_Type.chat);
					msg_send.msg_Content=string_send[1];
					msg_send.senderID=client_Info_Local.ID;
					msg_send.senderName=client_Info_Local.Name;
					msg_send.recieverID=string_send[0];
					msg_send.recieverName=get_ReceiverName_from_Reg_String_By_ID(msg_send.recieverID);
					
					if(!msg_send.recieverID.equals(""))//【接收者】不为空才发送消息
					{
						//先在本地显示 
						String str_chat="["+msg_send.senderID+":"+msg_send.senderName+"]对["
								+msg_send.recieverID+":"+msg_send.recieverName+"]说 : "+msg_send.msg_Content;
						out(str_chat);
						//再发送至服务器
						send_Msg(msg_send);//发送聊天信息
					}
				} catch (Exception e)
				{
					//e.printStackTrace();
				}
			}
		} catch (Exception e)
		{
			//e.printStackTrace();
		}
	}
	//#######################################################################################
	public void Command_Line_Listen_Send_Codec()
	{
		out("输入[back]返回上级菜单");
		try
		{
			while(true)
			{
				out("【命令格式】: "+"[接收者ID] + ':' + [消息内容]");
				String str_input=input.readLine();
				if(str_input.equals("back"))
				{
					break;
				}
				try
				{
					//########################################################
					//(1)先检查本地密钥 key_Codec_Local 是否已经设置 未设置时值为 "$"
					//if(network_Client.key_Codec_Local.equals("$"))
					if(key_Codec_Local==null)
					{
						//network_Client.show_UI_Dialog("编码策略尚未设置,请在[更新用户信息]中设置并通告服务器");
						out("编码策略尚未设置,请在[更新用户信息]中设置并通告服务器");
						break;
					}
					else
					{						
						try
						{
							String []string_send=str_input.split(":");
							Msg msg_send=new Msg(Msg_Type.chat_codec);
							msg_send.senderID=client_Info_Local.ID;
							msg_send.senderName=client_Info_Local.Name;
							msg_send.recieverID=string_send[0];
							msg_send.recieverName=get_ReceiverName_from_Reg_String_By_ID(msg_send.recieverID);

							msg_send.key_Codec=key_Codec_Local;
							
							msg_send.msg_Content=codec.Code(key_Codec_Local,string_send[1]);
							
							if(!msg_send.recieverID.equals(""))//【接收者】不为空才发送消息
							{
								//先在本地显示 
								//String str_chat="["+msg_send.senderID+"]对["+msg_send.recieverID+"]说 : "+msg_send.msg_Content;
								String str_chat="["+msg_send.senderID+":"+msg_send.senderName+"]对["
										+msg_send.recieverID+":"+msg_send.recieverName+"]说 : "+string_send[1]+"[加密后发送]";
								out(str_chat);
								out("[发送密文] "+msg_send.msg_Content);
								
								//再发送至服务器
								send_Msg(msg_send);//发送聊天信息
							}							
						} catch (Exception e)
						{
							//e.printStackTrace();
						}
					}					
					//########################################################
				} catch (Exception e)
				{
					//e.printStackTrace();
				}
			}
		} catch (Exception e)
		{
			//e.printStackTrace();
		}
	}
	//#######################################################################################
	public void out(String string)//带换行
	{
		System.out.println(string);
	}
	public void out_(String string)//不带换行
	{
		System.out.print(string);
	}
	//#######################################################################################
	public String get_ReceiverName_from_Reg_String_By_ID(String id)
	{
		String []reg=str_Client_Info_Reg.split(";");
		for(int i=0;i<reg.length;i++)
		{
			if(reg[i].split(":")[0].equals(id))
			{
				return reg[i].split(":")[1];				
			}
		}
		return null;
	}
	//#######################################################################################
	//#######################################################################################
}