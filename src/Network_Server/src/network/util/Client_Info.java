package network.util;

import java.io.Serializable;
import java.net.InetAddress;

public class Client_Info implements Serializable
{
	//基本信息
	//======================================================
	public String ID;
	public String Name;
	public String Password;
	//======================================================
	//详细信息
	//======================================================
	public String Info_Detail;
	//======================================================
	//密码策略
	//======================================================
	public String Key;
	//======================================================
	//网络信息
	//======================================================
	public InetAddress ip_Address;
	public int ip_Port;
	//======================================================
	
	public Client_Info()
	{
		super();
	}
	
	public Client_Info(String clientID, String clientName, String clientPassword)
	{
		super();
		this.ID = clientID;
		this.Name = clientName;
		this.Password = clientPassword;
	}
}
