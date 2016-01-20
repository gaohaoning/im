package network.util;

import java.io.Serializable;

public enum Msg_Type implements Serializable
{	
	login,
	login_echo_accept,
	login_echo_reject,
	
	register,
	register_echo_accept,
	register_echo_reject,
	
	users_update,
	
	online_users,
	online_users_update,
	
	reg_users,
	reg_users_update,
	
	broadcast_user_info_online,
	
	server_info,
	
	kickedout,
	
	disconnect,
	
	chat,	
	chat_codec,
	
	user_data_update,

	unicast,
	multicast,
	broadcast
}
