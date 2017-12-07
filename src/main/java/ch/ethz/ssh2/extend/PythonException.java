package ch.ethz.ssh2.extend;

/**
 * 只是Python异常
 * @author shishengjie
 *
 */
public class PythonException extends Exception{
	private static final long serialVersionUID = 9841627384651L;
	public PythonException(String error){
		super(error);
	}
	public PythonException(Exception exception){
		super(exception);
	}
}