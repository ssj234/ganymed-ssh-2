package ch.ethz.ssh2.extend;

/**
 * 日志处理
 * @author shishengjie
 *
 */
public interface LogProcesser {
	public void log(Exception exception);
	public void log(String line);
}
