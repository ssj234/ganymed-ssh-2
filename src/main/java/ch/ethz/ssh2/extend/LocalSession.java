package ch.ethz.ssh2.extend;

import java.io.IOException;
import java.io.InputStream;

import ch.ethz.ssh2.Session;

/**
 * 本地session
 * @author shishengjie
 *
 */
public class LocalSession extends Session{

	Process process = null;
	LocalSession() {
		super();
	}
	
	@Override
	public void execCommand(String cmd) throws IOException {
		process = Runtime.getRuntime().exec(cmd);
	}
	
	
	@Override
	public InputStream getStdout() {
		return  process.getInputStream();
	}
	
	@Override
	public InputStream getStderr() {
		return  process.getErrorStream();
	}
	
	@Override
	public Integer getExitStatus() {
		try {
			return process.waitFor();
		} catch (InterruptedException e) {
			return -99999;
		}
//		return process.exitValue();
	}
	
	@Override
	public void close() {
		// blank
	}
}

