package ch.ethz.ssh2.extend;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author shishengjie
 *
 */
public class ShellClient {

	private SSH2Client client;
	private String python = "python";
	public ShellClient(SSH2Client client){
		this.client = client;
	}
	private void handleResult(Result result) throws Exception{
		if(result.getException() != null){
			throw new PythonException(result.getException());
		}
		if(result.getExitStatus() != 0){
			throw new PythonException("ExitStatus is :" + result.getExitStatus() + " .Result is " + result.getContent());
		}
	}
	public void python(String absFilename,Map args) throws Exception{
		StringBuffer strBuff = new StringBuffer();
		if(args != null){
			Set keys = args.entrySet();
			Iterator it = keys.iterator();
			while(it.hasNext()){
				Entry entry = (Entry) it.next();
				String key  = (String) entry.getKey();
				String value  = (String) entry.getValue();
				if(key!=null && key.trim().length()>0 && value!=null && value.trim().length() >0)
					strBuff.append(String.format(" --%s=%s", key, value));
			}
		}
		Result result =this.client.newSession().execResult(String.format("%s %s %s", new String[]{python,absFilename,strBuff.toString()})).syncResult();
		handleResult(result);
	}
	
	public void mkdir(String path) throws Exception{
		Result result = this.client.newSession().execResult(String.format("mkdir %s", new String[]{path})).syncResult();
		handleResult(result);
	}
	
	public void rmdir(String path) throws Exception{
		Result result = this.client.newSession().execResult(String.format("rmdir %s", new String[]{path})).syncResult();
		handleResult(result);
	}
	
	public String cat(String path) throws Exception{
		Result result = this.client.newSession().execResult(String.format("cat %s", new String[]{path})).syncResult();
		handleResult(result);
		return result.getContent();
	}
	
	public String ls(String path) throws Exception{
		Result result = this.client.newSession().execResult(String.format("ls -mp %s", new String[]{path})).syncResult();
		if(result.getException() != null){
			throw result.getException();
		}
		String cnt = result.getContent();
		return cnt.trim();
	}
	public String getPython() {
		return python;
	}
	public void setPython(String python) {
		this.python = python;
	}
	
}

