package ch.ethz.ssh2.extend;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.InteractiveCallback;

public abstract class AbsSSH2Transport implements SSH2Transport {
	public static int LOGIN_MODE_NONE = 0x0;
	public static int LOGIN_MODE_PASSWORD = 0x1;
	public static int LOGIN_MODE_PUBLICKEY = 0x2;
	public static int LOGIN_MODE_KEYBOARD = 0x3;
	private String serverHost;
	private String username;// 用户名
	private String password;// 密码
	private String privateFilePath;// 文件路径
	private String methods[]; // 支持的连接方法
	private int loginMode = 0; // 登录类型
	private int port = 22; // 默认的端口
	
	public AbsSSH2Transport(String serverHost,String username,String password){
		this.serverHost = serverHost;
		this.username = username;
		this.password = password;
	}
	
	public AbsSSH2Transport(String serverHost,String username,String privateFilePath,String password){
		this.serverHost = serverHost;
		this.username = username;
		this.privateFilePath = privateFilePath;
		this.password = password;
	}
	
	protected Connection login() throws IOException{
		 Connection conn = connected();
		 boolean isAuthenticated = authenticate(conn);
		 if (isAuthenticated == false)
				throw new IOException("Authentication failed.");
		 return conn;
	}
	
	
	private boolean  authenticateWithPassword(Connection conn) throws IOException{
		if(username == null || username.trim().length() == 0){
			throw new IOException("username is null");
		}
		if(password == null || password.trim().length() == 0){
			throw new IOException("password is null");
		}
		return conn.authenticateWithPassword(username, password);
	}
	private boolean  authenticateWithPublickey(Connection conn) throws IOException{
		if(username == null || username.trim().length() == 0){
			throw new IOException("username is null");
		}
		if(password == null || password.trim().length() == 0){
			throw new IOException("password is null");
		}
		if(privateFilePath == null || privateFilePath.trim().length() == 0){
			throw new IOException("privateFilePath is null");
		}
		return  conn.authenticateWithPublicKey(username, new File(privateFilePath),password);
	}
	private boolean  authenticateWithKeyboardInteractive(Connection conn) throws IOException{
		if(username == null || username.trim().length() == 0){
			throw new IOException("username is null");
		}
		if(password == null || password.trim().length() == 0){
			throw new IOException("password is null");
		}
		boolean isAuthenticated = conn.authenticateWithKeyboardInteractive(username, new InteractiveCallback() {
			public String[] replyToChallenge(String name, String instruction,
					int numPrompts, String[] prompt, boolean[] echo) throws Exception {
				if(numPrompts == 1)
					return new String[]{password};//password
				else
					return new String[0];
			}
		});
		return isAuthenticated;
	}
	/**
	 * ��¼��֤
	 * @return
	 * @throws IOException 
	 */
	private boolean authenticate(Connection conn) throws IOException{
		boolean ret = false;
		//methods
		if(conn == null)
			throw new IOException("Connection of this session is null");
		if(methods == null || methods.length == 0){
			throw new IOException("Connection of this session is null");
		}
		if(loginMode == LOGIN_MODE_NONE){ // NOT SET LOGIN MODE
			for(int i = 0 ; i < methods.length ;  i++){
				String method = methods[i];
				if("password".equals(method)){
					try{
						ret = this.authenticateWithPassword(conn);
						loginMode = LOGIN_MODE_PASSWORD;
					}catch (Exception e) {}
				}else if("publickey".equals(method)){
					try{
						ret= this.authenticateWithPublickey(conn);
						loginMode = LOGIN_MODE_PUBLICKEY;
					}catch (Exception e) {}
				}else if("keyboard-interactive".equals(method)){
					try{
						ret = this.authenticateWithKeyboardInteractive(conn);
						loginMode = LOGIN_MODE_KEYBOARD;
					}catch (Exception e) {}
				}
				if(ret) return ret;
			}
			
		}else{
			if(loginMode == LOGIN_MODE_KEYBOARD){
				return this.authenticateWithKeyboardInteractive(conn);
			}else if(loginMode == LOGIN_MODE_PUBLICKEY){
				return this.authenticateWithPublickey(conn);
			}else if(loginMode == LOGIN_MODE_PASSWORD){
				return this.authenticateWithPassword(conn);
			}
		}
		return ret;
	}
	/**
	 * ������������ȡ��¼��ʽ
	 * @return
	 * @throws IOException
	 */
	private Connection connected() throws IOException{
		Connection conn = new Connection(serverHost,port);
		conn.connect();
		methods = conn.getRemainingAuthMethods(username);
		return conn;
	}
	
	public Object send(String command) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object send(Map input) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrivateFilePath() {
		return privateFilePath;
	}

	public void setPrivateFilePath(String privateFilePath) {
		this.privateFilePath = privateFilePath;
	}

	public int getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(int loginMode) {
		this.loginMode = loginMode;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	
}
