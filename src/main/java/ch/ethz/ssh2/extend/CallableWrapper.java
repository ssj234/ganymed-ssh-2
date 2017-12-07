package ch.ethz.ssh2.extend;
import java.util.concurrent.CountDownLatch;

import ch.ethz.ssh2.Session;

/**
 * 包装调用
 * @author shishengjie
 *
 */
public class CallableWrapper extends Thread{
	Session session;
	CountDownLatch latch;
	LogProcesser logProcesser;
	ResultLogProcess resultProcesser;
	Callable callable;
	public CallableWrapper(Session session,CountDownLatch latch,LogProcesser logProcesser,Callable callable,ResultLogProcess resultProcesser){
		this.latch = latch;
		this.logProcesser = logProcesser;
		this.callable = callable;
		this.session = session;
		this.resultProcesser = resultProcesser;
	}
	
	public void run() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			if(logProcesser !=  null)
				logProcesser.log(e);
		}
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
		Result resultBean = new Result(result,session.getExitStatus(),exception);
		try {
			if(this.callable != null)
				this.callable.call(resultBean);
		} catch (Exception e) {
			if(logProcesser !=  null)
				logProcesser.log(e);
		}
		if(session != null)
			session.close();
	}
}
