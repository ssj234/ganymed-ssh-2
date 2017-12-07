package ch.ethz.ssh2.extend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
/**
 * 
 * @author shishengjie
 *
 */
public class SessionContext {
	public String optThreadPrefix = "SCxt-opt-";
	public String errThreadPrefix = "SCxt-err-";
	Session session;
	CountDownLatch latch = new CountDownLatch(2);
	Integer result = null;
	private static String LP_DEFAULT_NAME = "default";
	private static String LP_RESULT_NAME = "result";
	Map<String,LogProcesser> processers = new HashMap<String,LogProcesser>();
	LogProcesser default_logProcesser = new LogProcesser() {
		public void log(Exception exception) {
			System.out.println("[NONE-PROCESSER]"+exception.getMessage());
		}

		public void log(String line) {
			System.out.println("[NONE-PROCESSER]"+line);
		}
	};
	
	public SessionContext(Session session,LogProcesser processer){
		this.session = session;
		if(processer == null)
			this.processers.put(LP_DEFAULT_NAME, default_logProcesser);
		else
			this.processers.put(LP_DEFAULT_NAME, processer);
	}
	
	public SessionContext exec(String command) throws IOException{
		session.execCommand(command);
		OutputRunnable optRunnable = new OutputRunnable(session.getStdout(),latch,processers,session);
		OutputRunnable errRunnable = new OutputRunnable(session.getStderr(),latch,processers,session);
		Thread optThread = new Thread(optRunnable,optThreadPrefix+session.getName());
		Thread errThread = new Thread(errRunnable,errThreadPrefix+session.getName());
		optThread.start();
		errThread.start();
		return this;
	}
	
	public SessionContext execResult(String command) throws IOException{
		this.processers.put(LP_RESULT_NAME, new ResultLogProcess()); // ���һ������������־������
		session.execCommand(command);
		OutputRunnable optRunnable = new OutputRunnable(session.getStdout(),latch,processers,session);
		OutputRunnable errRunnable = new OutputRunnable(session.getStderr(),latch,processers,session);
		Thread optThread = new Thread(optRunnable,optThreadPrefix+session.getName());
		Thread errThread = new Thread(errRunnable,errThreadPrefix+session.getName());
		optThread.start();
		errThread.start();
		return this;
	}
	
	public Integer sync() throws InterruptedException{
		session.setCloseInProcesser(false);
		latch.await();
		result = session.getExitStatus();
		this.close();
		return result;
	}
	
	public Result syncResult() throws InterruptedException{
		if(processers.get(LP_RESULT_NAME) == null){
			throw new NullPointerException("You should use execResult replace exec!");
		}
		
		ResultLogProcess resultProcesser = (ResultLogProcess) processers.get(LP_RESULT_NAME);
		session.setCloseInProcesser(false);
		latch.await();
		String result = null;
		Exception exception = null;
		if(resultProcesser!=null){
			exception = resultProcesser.getException();
			if(exception != null){
				result = null;
			}else{
				result = resultProcesser.getContent();
			}
		}
		return new Result(result,session.getExitStatus(),exception);
	}
	
	public void async(Callable callable) throws InterruptedException{
		if(processers.get(LP_RESULT_NAME) == null){
			throw new NullPointerException("You should use execResult replace exec!");
		}
		
		ResultLogProcess resultProcesser = (ResultLogProcess) processers.get(LP_RESULT_NAME);
		LogProcesser processer = processers.get(LP_DEFAULT_NAME);
		session.setCloseInProcesser(false);
		CallableWrapper wrapper = new CallableWrapper(session,latch,processer,callable,resultProcesser);
		wrapper.start();
	}
	
	private void close(){
		if(session != null){
			session.close();
			session = null;
		}
	}
	
	public SessionContext setLogProcesser(LogProcesser processer ){
		this.processers.put(LP_DEFAULT_NAME, processer);
		return this;
	}
	
	public Integer getResult(){
		return result;
	}
	
}

class OutputRunnable implements Runnable{
	InputStream output;
	CountDownLatch latch;
	Map<String,LogProcesser> processers = null;
	Session session;
	public OutputRunnable(InputStream output,CountDownLatch latch,Map<String,LogProcesser> processers,Session session){
		this.output = output;
		this.latch = latch;
		this.processers = processers;
		this.session = session;
	}
	private void process(Exception e){
		Set<Entry<String,LogProcesser>> set = processers.entrySet();
		Iterator<Entry<String,LogProcesser>> it = set.iterator();
		while(it.hasNext()){
			Entry<String,LogProcesser> entry = it.next();
			LogProcesser processer = entry.getValue();
			processer.log(e);
		}
	}
	private void process(String e){
		Set<Entry<String,LogProcesser>> set = processers.entrySet();
		Iterator<Entry<String,LogProcesser>> it = set.iterator();
		while(it.hasNext()){
			Entry<String,LogProcesser> entry = it.next();
			LogProcesser processer = entry.getValue();
			processer.log(e);
		}
	}
	public void run() {
		
		InputStream stdout = new StreamGobbler(output);
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		while (true)
		{
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				process(e);
				break;
			}
			if (line == null)
				break;
			process(line);
		}
		latch.countDown();
		if(session.isCloseInProcesser()){
			session.close();
			session = null;
		}
	}
}