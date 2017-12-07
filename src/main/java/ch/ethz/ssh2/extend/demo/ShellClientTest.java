package ch.ethz.ssh2.extend.demo;

import java.io.IOException;

import ch.ethz.ssh2.extend.DebugLogProcess;
import ch.ethz.ssh2.extend.SSH2Client;
import ch.ethz.ssh2.extend.ShellClient;



public class ShellClientTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		String hostname = "localhost";
		String username = "xxxxxxxx";
		String password = "xxxxxxxxx";
		
		// 1. 初始化
		SSH2Client client = new SSH2Client(hostname, username, password);
		// 2. 设置默认登录模式
//		client.setLoginMode(AbsSSH2Transport.LOGIN_MODE_KEYBOARD); 
		// 3. 初始化
		client.init();
		System.out.println("========================================");
		
		// 4. 设置日志处理
		client.setDefaultLogProcesser(new DebugLogProcess());
		
		// 5. 创建一个ShellClient
		ShellClient shell = client.newShellClient();
		try {
			System.out.println("=============use shell util============");
			//System.out.println(shell.cat("/home/applog/abc.py"));
			String rs = shell.ls("/home/").trim();
			System.out.println(">>>"+rs);
			String str[] = rs.split(",");
			for(String s:str){
				System.out.println(" filename:"+ s.replace("/","").trim() + " isdir:" + s.endsWith("/"));
			}
//			shell.mkdir(path);
//			shell.rmdir(path);
//			shell.python(absFilename, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("=============end");
	}
}
