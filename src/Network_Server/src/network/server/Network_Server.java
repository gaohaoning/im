package network.server;

import java.awt.Container;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import javax.swing.*;

import network.codec.*;
import network.util.*;
import network.ui.*;

public class Network_Server
{
	public Codec codec;
	public ServerSocket server_Socket;
	public int server_Port = 11111;
	public Vector<Server_Thread> server_Threads;// 所有 与客户端通信线程 的集合
	public Vector<Client_Info> infos_Online;// 所有【在线用户】可以动态修改
	public Vector<Client_Info> infos_Reg;// 所有【注册用户】初始时从文件读取
	public String str_Client_Info_Online;//以String方式维护用户ID和Name信息
	public String str_Client_Info_Reg;//以String方式维护用户ID和Name信息
	public Network_Server_UI network_Server_UI;// 主UI一直存在	
	public Detail_Reg_Info_UI detail_Reg_Info_UI;
	public Detail_Online_Info_UI detail_Online_Info_UI;
	public Inf_UI inf_UI;
	
	public boolean read_Infos_Reg_Default()//这里先从在本地设置 然后加上是从文件中读取和写入功能
	{
		try
		{
			infos_Reg.addElement(new Client_Info("1", "Beckham", "000"));
			infos_Reg.addElement(new Client_Info("2", "Ronaldo", "000"));
			infos_Reg.addElement(new Client_Info("3", "Messi", "000"));
			infos_Reg.addElement(new Client_Info("4", "Zidane", "000"));
			infos_Reg.addElement(new Client_Info("5", "Figo", "000"));
			infos_Reg.addElement(new Client_Info("6", "Carlos", "000"));
			infos_Reg.addElement(new Client_Info("7", "Giggs", "000"));
			infos_Reg.addElement(new Client_Info("8", "Raul", "000"));
			infos_Reg.addElement(new Client_Info("9", "Inzaghi", "000"));
			infos_Reg.addElement(new Client_Info("10", "Robinho", "000"));
			infos_Reg.addElement(new Client_Info("11", "Nedved", "000"));
			infos_Reg.addElement(new Client_Info("12", "Henry", "000"));
			infos_Reg.addElement(new Client_Info("13", "Owen", "000"));
			infos_Reg.addElement(new Client_Info("14", "Crespo", "000"));
			infos_Reg.addElement(new Client_Info("15", "Totti", "000"));
			show("读取默认注册用户信息 成功");
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("读取默认注册用户信息 异常");
			return false;
		}
	}
	
	public boolean write_User_Info_Reg_to_File(String file_String)//将【注册用户】信息 保存
	{
		try
		{
			File file_UserInfo=new File(file_String);
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(file_UserInfo));	
			out.writeObject(infos_Reg);
			out.close();
			show("write_User_Info_Reg_to_File 成功");
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("write_User_Info_Reg_to_File 异常");
			return false;
		}
	}

	public boolean read_User_Info_Reg_from_File(String file_String)
	{
		try
		{
			File file_UserInfo=new File(file_String);
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(file_UserInfo));
			infos_Reg=(Vector<Client_Info>)in.readObject();
			in.close();
			show("read_User_Info_Reg_from_File 成功 : "+infos_Reg.toString());
			network_Server_UI.update_List_Reg(infos_Reg);
			send_Broadcast_Client_Info_Online_Reg();
			detail_Reg_Info_UI.vector_UI=infos_Reg;			
			network_Server_UI.tabbedPane_Info.updateUI();//用此方法立即刷新列表 无延迟
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("read_User_Info_Reg_from_File 异常");
			return false;
		}
	}

	synchronized public void add_Info_from_Reg_to_Online_by_ID(String id)
	{
		for(int i=0;i<infos_Reg.size();i++)
		{
			if(infos_Reg.elementAt(i).ID.equals(id))
			{
				infos_Online.addElement(infos_Reg.elementAt(i));
				return;
			}
		}
	}

	synchronized public void add_Info_to_Reg_by_Client_Info(Client_Info client_Info)
	{
		infos_Reg.addElement(client_Info);
	}

	synchronized public boolean replace_ClientInfo_fromReg_toOnline_byID(String str_ID)
	{
		for(int i=0;i<infos_Reg.size();i++)
		{
			if(infos_Reg.elementAt(i).ID.equals(str_ID))
			{			
				for(int ii=0;ii<infos_Online.size();ii++)
				{
					if(infos_Online.elementAt(ii).ID.equals(str_ID))
					{
						infos_Online.elementAt(ii).Password=
								infos_Reg.elementAt(i).Password;
						infos_Online.elementAt(ii).Name=
								infos_Reg.elementAt(i).Name;
						infos_Online.elementAt(ii).Key=
								infos_Reg.elementAt(i).Key;						
						return true;
					}
				}
			}
		}
		return false;
	}

	public Network_Server()
	{
		try
		{
			codec = new Codec(this);
			network_Server_UI = new Network_Server_UI(this);
			inf_UI =new Inf_UI(this);
			inf_UI.frame.setVisible(false);
			server_Threads =new Vector<Server_Thread>();
			infos_Online =new Vector<Client_Info>();
			infos_Reg =new Vector<Client_Info>();
			read_Infos_Reg_Default();
			network_Server_UI.update_List_Reg(infos_Reg);
			detail_Reg_Info_UI=new Detail_Reg_Info_UI(this);
			detail_Reg_Info_UI.frame.setVisible(false);
			detail_Online_Info_UI=new Detail_Online_Info_UI(this);
			detail_Online_Info_UI.frame.setVisible(false);
			server_Socket = new ServerSocket(server_Port);
			show("【服务器初始化】【端口】 : "+server_Port+"【成功】");
			Socket incoming;
			while (true)
			{			
				incoming = server_Socket.accept();//阻塞
				new Server_Thread_Verification(incoming).start();//执行 Thread.run();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("【服务器初始化】 异常");
		}
	}

	public Network_Server(InetAddress address,int server_Port)
	{
		try
		{
			this.server_Port=server_Port;
			codec = new Codec(this);
			network_Server_UI = new Network_Server_UI(this);
			inf_UI =new Inf_UI(this);
			inf_UI.frame.setVisible(false);
			server_Threads =new Vector<Server_Thread>();
			infos_Online =new Vector<Client_Info>();
			infos_Reg =new Vector<Client_Info>();
			read_Infos_Reg_Default();
			network_Server_UI.update_List_Reg(infos_Reg);
			detail_Reg_Info_UI=new Detail_Reg_Info_UI(this);
			detail_Reg_Info_UI.frame.setVisible(false);
			detail_Online_Info_UI=new Detail_Online_Info_UI(this);
			detail_Online_Info_UI.frame.setVisible(false);
			server_Socket = new ServerSocket(server_Port);
			show("【服务器初始化】【端口】 : "+server_Port+"【成功】");	
			Socket incoming;
			while (true)
			{			
				incoming = server_Socket.accept();//阻塞
				new Server_Thread_Verification(incoming).start();//执行 Thread.run();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			show("【服务器初始化】 异常");
		}
	}
	
	public class Server_Thread_Verification extends Thread
	{
		public Client_Info client_Info_Thread;// 维护线程内 用户信息
		public Socket socket_Thread;
		public InetAddress address_Thread;
		public int port_Thread;
		public ObjectInputStream in_obj;
		public ObjectOutputStream out_obj;
		public Server_Thread_Verification(Socket socket)
		{
			try
			{
				socket_Thread = socket;
				address_Thread=socket_Thread.getInetAddress();
				port_Thread=socket_Thread.getPort();				
				show("建立客户端连接 :");
				show("Client IP : " + address_Thread + " Port : "+port_Thread);
				
				in_obj=new ObjectInputStream(socket_Thread.getInputStream());
				out_obj=new ObjectOutputStream(socket_Thread.getOutputStream());
				show("【初始化输入输出流】OK");
			} catch (Exception e)
			{
				e.printStackTrace();
				show("【初始化输入输出流】 异常");
			}
		}
		@Override
		public void run()
		{
			try
			{
				Msg msg = (Msg) in_obj.readObject();
				show("【收到】【客户端消息】 : " + msg.msg_Type);
				switch (msg.msg_Type)
				{
				case login:
					server_Msg_Handler_Login(msg);
					break;
				case register:
					server_Msg_Handler_Register(msg);
					break;
				}
			} catch (SocketException socketException)//客户端私自断开时的异常
			{				
				socketException.printStackTrace();
				show("客户端私自断开时的异常 "+"SocketException");//需要关闭相应线程//移除该在线用户资料
			} catch(NullPointerException nullPointerException)
			{				
				nullPointerException.printStackTrace();
				show("服务器监听线程异常 "+"NullPointerException");
			} catch (Exception e)
			{
				e.printStackTrace();
				show("Server_Thread_Verification 异常");
			}
		}
		
		public boolean send_Unicast(Msg msg)//由于单播函数在 线程类中 所以不需要指定IP地址 直接使用线程中的输出流
		{
			try
			{
				out_obj.writeObject(msg);
				out_obj.flush();
				return true;
			} catch (Exception e)
			{
				show("send_Unicast 异常");
				return false;
			}
		}
		
		synchronized public boolean server_Msg_Handler_Login(Msg msg)
		{
			try
			{
				client_Info_Thread = msg.msg_Client_Info;
				show("【收到消息】:【客户端登陆】:");
				show(msg.msg_Content);
				show(msg.msg_Client_Info.ID+msg.msg_Client_Info.Name+msg.msg_Client_Info.Password);
				if (user_Verification_Login(msg.msg_Client_Info))// 认证成功 接受 accept
				{
					show("【用户验证成功】: " + msg.msg_Client_Info.ID);
					// 还需要判断该用户是否先前已经在别处登录
					if(!is_Client_Already_Online_by_ID(msg.msg_Client_Info.ID))// 该用户尚未登录
					{
						show("【用户验证成功】【未在别处登录】: " + msg.msg_Client_Info.ID);						
						Msg msg_Accept=new Msg(Msg_Type.login_echo_accept);
						send_Unicast(msg_Accept);
						add_Info_from_Reg_to_Online_by_ID(msg.msg_Client_Info.ID);
						network_Server_UI.update_List_Online(infos_Online);
						network_Server_UI.update_List_Reg(infos_Reg);
						Server_Thread server_Thread=new Server_Thread(client_Info_Thread, socket_Thread, 
								address_Thread, port_Thread, in_obj, out_obj);
						server_Thread.start();
						//登录成功后 要向Client发送客户资料更新信息 类型 Update_Info
						send_Detail_Update_Info_Back_To_Login_User(msg);
					} else// 该用户已经在别处登录
					{
						show("【用户验证成功】【已经在别处登录】");
						//先把已经在线的踢出
						Server_Thread thread_To_Kickout=get_Server_Thread_by_Client_ID(msg.msg_Client_Info.ID);
						Msg msg_Kickout=new Msg(Msg_Type.kickedout);
						thread_To_Kickout.send_Unicast(msg_Kickout);
						//踢出后该线程Socket产生异常 //该异常会在 通信线程中处理
						thread_To_Kickout.close_Socket_Inside_Thread();
						thread_To_Kickout.remove_Thread_Inside_Thread();
						thread_To_Kickout.remove_Info_Online_Inside_Thread();
						//再把当前的登录上
						show("【用户验证成功】【已经别处登录】【将其踢出】 ");						
						Msg msg_Accept=new Msg(Msg_Type.login_echo_accept);
						send_Unicast(msg_Accept);
						//(1)
						add_Info_from_Reg_to_Online_by_ID(msg.msg_Client_Info.ID);
						network_Server_UI.update_List_Online(infos_Online);
						network_Server_UI.update_List_Reg(infos_Reg);
						Server_Thread server_Thread=new Server_Thread(client_Info_Thread, socket_Thread, 
								address_Thread, port_Thread, in_obj, out_obj);
						server_Thread.start();
						//(2)
						add_Info_from_Reg_to_Online_by_ID(msg.msg_Client_Info.ID);
						network_Server_UI.update_List_Online(infos_Online);
						network_Server_UI.update_List_Reg(infos_Reg);
						//登录成功后 要向Client发送客户资料更新信息 类型 Update_Info
						send_Detail_Update_Info_Back_To_Login_User(msg);
						send_Broadcast_Client_Info_Online_Reg();
						show_Num_of_server_Threads_clientInfos_online();
					}
				} else// 认证失败 拒绝 reject				
				{
					show("【用户验证失败】: " + msg.msg_Client_Info.ID);
					Msg msg_Reject=new Msg(Msg_Type.login_echo_reject);
					send_Unicast(msg_Reject);
				}
				show_Num_of_server_Threads_clientInfos_online();
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("server_Msg_Handler_Login 异常");
				return false;
			}
		}

		synchronized public boolean server_Msg_Handler_Register(Msg msg)
		{
			try
			{
				client_Info_Thread = msg.msg_Client_Info;
				show("【收到消息】:【客户端注册】:");
				show(msg.msg_Content);
				show(msg.msg_Client_Info.ID+msg.msg_Client_Info.Name+msg.msg_Client_Info.Password);
				if (user_Verification_Register(msg.msg_Client_Info))// 认证成功 接受// accept【注册成功后直接登陆】																
				{
					show("【用户注册成功】: " + msg.msg_Client_Info.ID);
					Msg msg_Accept=new Msg(Msg_Type.register_echo_accept);
					send_Unicast(msg_Accept);
					add_Info_to_Reg_by_Client_Info(msg.msg_Client_Info);
					network_Server_UI.update_List_Reg(infos_Reg);
					add_Info_from_Reg_to_Online_by_ID(msg.msg_Client_Info.ID);
					network_Server_UI.update_List_Online(infos_Online);
					network_Server_UI.update_List_Reg(infos_Reg);
					Server_Thread server_Thread=new Server_Thread(client_Info_Thread, socket_Thread, 
							address_Thread, port_Thread, in_obj, out_obj);
					server_Thread.start();
					//登录成功后 要向Client发送客户资料更新信息 类型 Update_Info
					send_Detail_Update_Info_Back_To_Login_User(msg);
				} else// 认证失败 拒绝 reject
				{
					show("【用户验证失败】: " + msg.msg_Client_Info.ID);
					Msg msg_Reject=new Msg(Msg_Type.register_echo_reject);
					send_Unicast(msg_Reject);
				}
				show_Num_of_server_Threads_clientInfos_online();
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("server_Msg_Handler_Register 异常");
				return false;
			}
		}
		
		synchronized public void send_Detail_Update_Info_Back_To_Login_User(Msg msg)
		{
			Msg msg_Update_Info=new Msg(Msg_Type.user_data_update);
			msg_Update_Info.msg_Update_Info=new Update_Info();//这里的 new 不能少
			
			for(int i=0;i<infos_Reg.size();i++)
			{
				if(infos_Reg.elementAt(i).ID.equals(msg.msg_Client_Info.ID))
				{
					show("向["+infos_Reg.elementAt(i).ID+"] 发送用户详细信息 :");
					show(infos_Reg.elementAt(i).ID);
					show(infos_Reg.elementAt(i).Password);
					show(infos_Reg.elementAt(i).Name);
					show(infos_Reg.elementAt(i).Key);
					
					msg_Update_Info.msg_Update_Info.user_ID=infos_Reg.elementAt(i).ID;
					msg_Update_Info.msg_Update_Info.user_Password=infos_Reg.elementAt(i).Password;
					msg_Update_Info.msg_Update_Info.user_Name=infos_Reg.elementAt(i).Name;
					msg_Update_Info.msg_Update_Info.user_Key=infos_Reg.elementAt(i).Key;								
				}
			}
			send_Unicast(msg_Update_Info);			
		}
	}
	
	public boolean user_Verification_Login(Client_Info client_Info)// 用户登陆认证
	{
		for (int i = 0; i < infos_Reg.size(); i++)
		{
			if (infos_Reg.elementAt(i).ID.equals(client_Info.ID))// 验证是否存在此ID
			{
				if (infos_Reg.elementAt(i).Password.equals(client_Info.Password))// 验证密码
				{
					return true;
				}
			}
		}
		return false;// 数据库中无此用户资料，则认证失败
	}
	
	public boolean user_Verification_Register(Client_Info client_Info)// 用户这侧认证
	{
		for (int i = 0; i < infos_Reg.size(); i++)
		{
			if (infos_Reg.elementAt(i).ID.equals(client_Info.ID))// 验证是否存在此ID
			{
				return false;
			}
		}
		return true;
	}

	public boolean is_Client_Already_Online_by_ID(String id)
	{
		for(int i=0;i<infos_Online.size();i++)
		{
			if(infos_Online.elementAt(i).ID.equals(id))
			{
				return true;
			}
		}
		return false;
	}

	public boolean is_Client_ID_Already_Exist(String id)
	{
		for(int i=0;i<infos_Reg.size();i++)
		{
			if(infos_Reg.elementAt(i).ID.equals(id))
			{
				return true;
			}
		}
		return false;
	}

	public boolean send_Broadcast(Msg msg)
	{		
		try
		{
			for(int i=0;i<server_Threads.size();i++)
			{
				server_Threads.elementAt(i).send_Unicast(msg);//从每一个线程中向客户端发送广播
			}
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("send_Broadcast 异常");
			return false;
		}
	}

	public boolean send_Broadcast_Client_Info_Online_Reg()//使用广播
	{
		try
		{
			Msg msg=new Msg(Msg_Type.users_update);	
			
//			//(1) 用Object [] 发送用户列表 有用户更改信息时其他客户端无法更新Name
//			msg.online=infos_Online.toArray();
//			msg.reg=infos_Reg.toArray();
			
			//(2) 用String 发送列表最稳健 还是用这个吧
			generate_Str_Client_Info_Online();
			msg.online_String=str_Client_Info_Online;
			generate_Str_Client_Info_Reg();
			msg.reg_String=str_Client_Info_Reg;			
			
			send_Broadcast(msg);//广播	
			show("【广播用户列表信息】");
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("【广播用户列表信息异常】");
			return false;
		}
	}

	public boolean on_Btn_Broadcast(String string)
	{
		try
		{
			Msg msg=new Msg(Msg_Type.broadcast);
			msg.msg_Content=string;			
			send_Broadcast(msg);//广播			
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			show("on_Btn_Broadcast 异常");
			return false;
		}
	}

	public Server_Thread get_Server_Thread_by_Client_ID(String str_ID)
	{
		for(int i=0;i<server_Threads.size();i++)
		{
			if(server_Threads.elementAt(i).client_Info_Thread.ID.equals(str_ID))
			{
				return server_Threads.elementAt(i);
			}
		}
		return null;
	}

	synchronized public void KickOut_User_Outside_Thread_by_ID(String str_ID)
	{
		for(int i=0;i<server_Threads.size();i++)
		{
			if(server_Threads.elementAt(i).client_Info_Thread.ID.equals(str_ID))
			{
				server_Threads.elementAt(i).KickOut_User_Inside_Thread();
				send_Broadcast_Client_Info_Online_Reg();
				return;
			}
		}
	}

	//【这个函数的用法体现了顶尖Java高手的【超级必杀技】】
	public String get_Key_Codec_from_Reg_by_ID(String receiverID)
	{
		for(int i=0;i<infos_Reg.size();i++)
		{
			if(infos_Reg.elementAt(i).ID.equals(receiverID))
			{
				//String.equals() 如果参数是 null 会抛出异常 【注意】
				// null 不等于 ""
				//找到了该接受者的注册资料，没有密钥返回 【空String】 【""】
				//我要让它返回 "" 而不是 null
				if(String.valueOf(infos_Reg.elementAt(i).Key).equals("null"))
				{
					return "";
				}
				else
				{
					return infos_Reg.elementAt(i).Key;
				}
			}
		}
		return "";//没找到也返回 空 "" 由于肯定找得到 所以这里一般不会被执行
	}

	public class Server_Thread extends Thread
	{
		public Client_Info client_Info_Thread;// 维护线程内 用户信息
		private long thread_ID = this.getId();
		private String thread_Name = this.getName();
		public Socket socket_Thread;
		public InetAddress address_Thread;
		public int port_Thread;
		public ObjectInputStream in_obj;
		public ObjectOutputStream out_obj;
		public Server_Thread(Client_Info client_Info_Thread,
				Socket socket_Thread, InetAddress address_Thread,
				int port_Thread, ObjectInputStream in_obj,
				ObjectOutputStream out_obj)
		{
			this.client_Info_Thread = client_Info_Thread;
			this.socket_Thread = socket_Thread;
			this.address_Thread = address_Thread;
			this.port_Thread = port_Thread;
			this.in_obj = in_obj;
			this.out_obj = out_obj;
			this.client_Info_Thread.ip_Address=this.address_Thread;
			this.client_Info_Thread.ip_Port=this.port_Thread;
			show("用户【登陆/注册】成功 开始监听客户端消息");
			show("Thread_ID : " + Long.toString(thread_ID) + " Thread_Name : " + thread_Name);
			show("Client IP : " + address_Thread + " Port : "+port_Thread);
			server_Threads.addElement(this);
			show("【服务器】维护【客户线程数】 : "+server_Threads.size());
			//在Verification线程中做 :	//infos_Online++;	//infos_Reg++;
			send_Broadcast_Client_Info_Online_Reg();//广播到所有在线用户更新列表
		}
		@Override
		public void run()
		{
			try
			{
				while(true)
				{
					Msg msg = (Msg) in_obj.readObject();
					show("【收到】【客户端消息】 : " + msg.msg_Type);
					switch (msg.msg_Type)
					{
					case chat:
						show("["+msg.senderID+"]TO["+msg.recieverID+"] : "+msg.msg_Content);
						server_Msg_Handler_Chat(msg);
						break;
					case user_data_update:
						show("【收到】【用户资料更新】");
						server_Msg_Handler_User_Data_Update(msg);
						break;
					case chat_codec://接收到带有编码策略的用户消息
						show("["+msg.senderID+"]TO["+msg.recieverID+"] : "+msg.msg_Content);
						server_Msg_Handler_Chat_Codec(msg);
						break;
					}
				}
			} catch (SocketException socketException)//客户端私自断开时的异常
			{				
				socketException.printStackTrace();
				show("客户端私自断开时的异常 "+"SocketException");//需要关闭相应线程//移除该在线用户资料
				close_Socket_Inside_Thread();
				remove_Thread_Inside_Thread();
				remove_Info_Online_Inside_Thread();
				
				network_Server_UI.update_List_Online(infos_Online);
				network_Server_UI.update_List_Reg(infos_Reg);
				
				send_Broadcast_Client_Info_Online_Reg();
				show_Num_of_server_Threads_clientInfos_online();
			} catch(NullPointerException nullPointerException)
			{				
				nullPointerException.printStackTrace();
				show("服务器监听线程异常 "+"NullPointerException");
			} catch (Exception e)
			{
				e.printStackTrace();
				show("Server_Thread 异常");
			}
			
		}

		//由于单播函数在 线程类中 所以不需要指定IP地址 直接使用线程中的输出流
		public boolean send_Unicast(Msg msg)//由于单播函数在 线程类中 所以不需要指定IP地址 直接使用线程中的输出流
		{
			try
			{
				out_obj.writeObject(msg);
				out_obj.flush();
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("Server_Thread send_Unicast 异常");
				return false;
			}
		}

		synchronized public void close_Socket_Inside_Thread()
		{
			try
			{
				out_obj.close();
				in_obj.close();
				socket_Thread.close();
			} catch (Exception e)
			{
				e.printStackTrace();
				show("close_Socket_Inside_Thread 异常");
			}
		}
		
		synchronized public void remove_Thread_Inside_Thread()
		{
			try
			{
				server_Threads.removeElement(this);
			} catch (Exception e)
			{
				e.printStackTrace();
				show("remove_Thread_Inside_Thread 异常");
			}
		}
		
		synchronized public void remove_Info_Online_Inside_Thread()
		{
			try
			{
				for(int i=0;i<infos_Online.size();i++)
				{
					if(infos_Online.elementAt(i).ID.equals(client_Info_Thread.ID))
					{
						infos_Online.removeElement(infos_Online.elementAt(i));
						return;//这句加的关键
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				show("remove_Info_Online_Inside_Thread 异常");
			}
		}
		
		synchronized public void KickOut_User_Inside_Thread()
		{
			Msg msg_Kickout=new Msg(Msg_Type.kickedout);
			send_Unicast(msg_Kickout);
			//踢出后该线程Socket产生异常 //该异常会在 通信线程中处理
			close_Socket_Inside_Thread();
			remove_Thread_Inside_Thread();
			remove_Info_Online_Inside_Thread();
			network_Server_UI.update_List_Online(infos_Online);
			network_Server_UI.update_List_Reg(infos_Reg);
		}
		
		synchronized public boolean server_Msg_Handler_Chat(Msg msg)
		{
			try
			{
				Server_Thread receiver_Thread=get_Server_Thread_by_Client_ID(msg.recieverID);
				if(receiver_Thread==null)
				{
					show("【Chat】【接收者】 【不在线】");
					return false;
				}
				//将从某一个线程接收的消息按【recieverID】发送至另一个线程
				receiver_Thread.send_Unicast(msg);
				//将从某一个线程接收的消息按【recieverID】发送至另一个线程				
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("server_Msg_Handler_Chat 异常");
				return false;
			}
		}
		
		synchronized public boolean server_Msg_Handler_Chat_Codec(Msg msg)
		{
			try
			{
				//(0)首先在服务器端根据密钥把信息解码出来
				//(0)(1) 检测下服务器端应该维护的 KEY 是否存在
				String key_For_Receive=get_Key_Codec_from_Reg_by_ID(msg.senderID);
				if(key_For_Receive.equals(""))
				{
					show_UI_Dialog("服务器丢失了发送者 : ["+msg.senderID+"]的密钥");	
					return false;
				}
				//(0)(2) 若存在 KEY 则把密文解码出来
				String message_decoded=codec.Decode(key_For_Receive, msg.msg_Content);
				show("解密了 : ["+msg.senderID+"]发给["+msg.recieverID+"]的密文 内容为:");
				show(message_decoded);
				//(1)先检查消息接受者是否在线
				Server_Thread receiver_Thread=get_Server_Thread_by_Client_ID(msg.recieverID);
				if(receiver_Thread==null)
				{
					show("【Chat】【接收者】 【不在线】");
					return false;
				}
				//(2)再检查检查该用户是否已经设置编码密钥
				// A.没有密钥就用【明文】转发 B.有密钥就用密钥【加密】后转发
				//String.equals() 如果参数是 null 会抛出异常 【注意】
				//【这个函数的用法体现了顶尖Java高手的【超级必杀技】】
				String key_For_Send=get_Key_Codec_from_Reg_by_ID(msg.recieverID);
				if(key_For_Send.equals(""))
				{
					//明文转发
					Msg msg_codec_off=new Msg(Msg_Type.chat);
					msg_codec_off.senderID=msg.senderID;
					msg_codec_off.recieverID=msg.recieverID;
					msg_codec_off.msg_Content=message_decoded;
					
					show("发送明文 :");
					show(message_decoded);
					receiver_Thread.send_Unicast(msg_codec_off);
				}
				else
				{
					//密文转发
					//先加密
					String message_coded=codec.Code(key_For_Send, message_decoded);
					show("加密后密文 :");
					show(message_coded);
					//再转发
					Msg msg_codec_on=new Msg(Msg_Type.chat_codec);
					msg_codec_on.senderID=msg.senderID;
					msg_codec_on.recieverID=msg.recieverID;
					msg_codec_on.msg_Content=message_coded;
					
					show("发送密文 :");
					show(message_coded);					
					receiver_Thread.send_Unicast(msg_codec_on);					
				}
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				show("server_Msg_Handler_Chat_Codec 异常");
				return false;
			}
		}
		
		synchronized public boolean server_Msg_Handler_User_Data_Update(Msg msg)
		{
			try
			{
				String id_Change=msg.msg_Update_Info.user_ID;
				for(int i=0;i<infos_Reg.size();i++)
				{
					if(infos_Reg.elementAt(i).ID.equals(id_Change))
					{
						infos_Reg.elementAt(i).Password=msg.msg_Update_Info.user_Password;
						infos_Reg.elementAt(i).Name=msg.msg_Update_Info.user_Name;
						infos_Reg.elementAt(i).Key=msg.msg_Update_Info.user_Key;
						//不是 【sync】将Reg中信息【增加至】Online
						//而是 【replace】将Reg中信息【替换至】Online
						replace_ClientInfo_fromReg_toOnline_byID(id_Change);
						
						network_Server_UI.update_List_Online(infos_Online);
						network_Server_UI.update_List_Reg(infos_Reg);
						
						send_Broadcast_Client_Info_Online_Reg();
						show("【广播】【在线用户资料更新】");
					}
				}
				network_Server_UI.tabbedPane_Info.updateUI();//用此方法立即刷新列表 无延迟
				return true;
			} catch (Exception e)
			{
				show("server_Msg_Handler_User_Data_Update 异常");
				return false;
			}
		}	
	}
	
	public static void main(String[] args)
	{
		new Network_Server();
	}
	
	public void show(String string)
	{
		string=get_Time()+string;
		System.out.println(string);
		network_Server_UI.show(string);
	}
	
	public void show_(String string)//不换行输出
	{
		System.out.print(string);
		network_Server_UI.show_(string);
	}
	
	public void show_UI_Dialog(String string)//以对话框方式显示提示信息
	{
		string=get_Time()+string;
		System.out.println(string);
		new Inf_UI(string);
	}
	
	public void show_Num_of_server_Threads_clientInfos_online()
	{
		int num1=server_Threads.size();
		int num2=infos_Online.size();
		
		network_Server_UI.textField_broadcast.setText("[服务器端] 用户线程数 :"+Integer.toString(num1)
				+"[在线用户数] : "+Integer.toString(num2));
	}
	
	public String get_Time()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String str_time = simpleDateFormat.format(new Date());
		return "["+str_time+"]";
	}
	
	public void generate_Str_Client_Info_Online()
	{
		str_Client_Info_Online="";
		for(int i=0;i<infos_Online.size();i++)
		{
			str_Client_Info_Online=str_Client_Info_Online+infos_Online.elementAt(i).ID+":"
					+infos_Online.elementAt(i).Name+";";
		}
	}
	
	public void generate_Str_Client_Info_Reg()
	{
		str_Client_Info_Reg="";
		for(int i=0;i<infos_Reg.size();i++)
		{
			str_Client_Info_Reg=str_Client_Info_Reg+infos_Reg.elementAt(i).ID+":"
					+infos_Reg.elementAt(i).Name+";";
		}
	}
	
	public void show_Msg(String title,String msg)
	{
		JOptionPane optionPane=new JOptionPane();
		optionPane.showConfirmDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}

}
