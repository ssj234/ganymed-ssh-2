package ch.ethz.ssh2.extend;

/**
 * 默认的日志处理器，用来调试
 * @author shishengjie
 *
 */
public class DebugLogProcess implements LogProcesser {


	public void log(Exception exception) {
		System.out.println(Thread.currentThread().getName()+" : "+ exception);
	}

	public void log(String line) {
		System.out.println(Thread.currentThread().getName()+" : "+ line);
	}

}
