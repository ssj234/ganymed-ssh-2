package ch.ethz.ssh2.extend.demo;

import java.io.IOException;

import ch.ethz.ssh2.extend.AbsSSH2Transport;
import ch.ethz.ssh2.extend.Callable;
import ch.ethz.ssh2.extend.DebugLogProcess;
import ch.ethz.ssh2.extend.Result;
import ch.ethz.ssh2.extend.SSH2Client;


public class SSH2ClientTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		String hostname = "xxx.xxx.xxx.xxx";
		String username = "username";
		String password = "password";
		
		SSH2Client client = new SSH2Client(hostname, username, password);
		// 2. 设置默认的登录模式，不设置时会便利测试
		client.setLoginMode(AbsSSH2Transport.LOGIN_MODE_KEYBOARD); 
		// 3. 初始化客户端
		client.init();
		System.out.println("========================================");
		// 4.设置默认的日志处理器
		client.setDefaultLogProcesser(new DebugLogProcess());
		
		// 4.1 执行uname
		client.newSession().exec("uname -a");
		
		// 4.2  执行 uanme -a 并获取结果
		Result result = client.newSession().execResult("uname -a").syncResult();
		System.out.println("uname -a result: " + result.getContent());
		
		// 4.3  异步执行
		client.newSession().execResult("uname -a")
		 .async(new Callable() {

				public void call(Result result) {
					System.out.println("uname -a result: " + result.getContent());
				}

			});
		System.out.println("=============end");
	}
}
