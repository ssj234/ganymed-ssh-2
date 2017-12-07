package ch.ethz.ssh2.extend;

/**
 * 用来获取返回结果
 * @author shishengjie
 *
 */
public class ResultLogProcess implements LogProcesser {
	
	private StringBuffer buff = new StringBuffer();
	private Exception exception = null;
	public void log(Exception exception) {
		this.exception = exception;
		buff.append(exception.getMessage() +" " + exception.getCause()+ System.getProperty("line.separator","\n"));
	}

	public void log(String line) {
		buff.append(line + System.getProperty("line.separator","\n"));
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	public String getContent() {
		return buff.toString();
	}

}
