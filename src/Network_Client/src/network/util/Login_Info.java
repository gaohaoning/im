package network.util;

import java.io.Serializable;

public class Login_Info implements Serializable
{
	public String address_server_str;
	public String port_server;
	public String id_login;
	public String password_login;
	public Login_Info(String address_server_str, String port_server,
			String id_login, String password_login)
	{
		super();
		this.address_server_str = address_server_str;
		this.port_server = port_server;
		this.id_login = id_login;
		this.password_login = password_login;
	}
}
