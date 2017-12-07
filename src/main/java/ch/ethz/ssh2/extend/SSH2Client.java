package ch.ethz.ssh2.extend;

import java.io.IOException;

import ch.ethz.ssh2.Connection;

/**
 * ssh客户端
 * @author shishengjie
 *
 */
public class SSH2Client extends AbsSSH2Transport{

	Connection conn = null;
	LogProcesser defaultLogProcesser = null;
	public SSH2Client(String serverHost,String username,String password){
		super( serverHost, username, password);
	}
	
	public SSH2Client(String serverHost,String username,String filePath,String password){
		super( serverHost, username,filePath, password);
	}
	
	public void init() throws IOException{
		if(!isLocal())
			this.conn = super.login();
	}
	
	public SessionContext newSession() throws IOException{
		if(isLocal())
			return new SessionContext(new LocalSession(), defaultLogProcesser);
		return new SessionContext(this.conn.openSession(),defaultLogProcesser);
	}
	
	public SessionContext newSession(LogProcesser processer) throws IOException{
		if(isLocal())
			return new SessionContext(new LocalSession(), processer);
		return new SessionContext(this.conn.openSession(),processer);
	}
	
	public void close(){
		if(conn != null)
			conn.close();
	}

	public LogProcesser getDefaultLogProcesser() {
		return defaultLogProcesser;
	}

	public void setDefaultLogProcesser(LogProcesser defaultLogProcesser) {
		this.defaultLogProcesser = defaultLogProcesser;
	}
	
	public ShellClient newShellClient(){
		if(this.defaultLogProcesser == null){
			throw new NullPointerException("please invoke function setDefaultLogProcesser,otherwise we cannot handle exception!");
		}
		return new ShellClient(this);
	}
	
	private boolean isLocal(){
		String host = getServerHost();
		if("localhost|127.0.0.1|".indexOf(host) > -1){
			return true;
		}
		return false;
	}
}
