
添加了发送命令获取结果以及日志的功能

```
SSH2Client client = new SSH2Client(hostname, username, password);
//  设置默认的登录模式，不设置也可以
client.setLoginMode(AbsSSH2Transport.LOGIN_MODE_KEYBOARD); 
//  初始化
client.init();
System.out.println("========================================");
// 设置日志处理
client.setDefaultLogProcesser(new DebugLogProcess());

//  执行ls -a命令
client.newSession().exec("ls -a");

//  获取ls -a结果
Result result = client.newSession().execResult("ls -a").syncResult();
System.out.println("ls -a result: " + result.getContent());

//  异步执行ls -a
client.newSession().execResult("ls -a")
 .async(new Callable() {

		public void call(Result result) {
			System.out.println("ls -a result: " + result.getContent());
		}

	});
System.out.println("=============end");
```

Ganymed SSH-2 for Java - build 260
========================================

http://www.cleondris.ch/opensource/ssh2/

Ganymed SSH-2 for Java is a library which implements the SSH-2 protocol in pure Java
(tested on J2SE 1.4.2, 5 and 6). It allows one to connect to SSH servers from within
Java programs. It supports SSH sessions (remote command execution and shell access),
local and remote port forwarding, local stream forwarding, X11 forwarding, SCP and SFTP.
There are no dependencies on any JCE provider, as all crypto functionality is included.

Ganymed SSH-2 for Java was originally developed for the Ganymed replication project
and a couple of other projects at the IKS group at ETH Zurich (Switzerland).

This distribution contains the source code, examples, javadoc and the FAQ.
It also includes a pre-compiled jar version of the library which is ready to use.

- Please read the included LICENCE.txt
- Latest changes can be found in HISTORY.txt

The latest version of the FAQ is available on the website.

Zurich, August 2010
