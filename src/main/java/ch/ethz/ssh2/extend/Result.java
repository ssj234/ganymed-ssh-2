package ch.ethz.ssh2.extend;

/**
 * 包装结果
 * @author shishengjie
 *
 */
public class Result{
		private Integer exitStatus;
		private String content;
		private Exception exception;
		public Result(String content,Integer exitStatus, Exception exception){
			this.exitStatus = exitStatus;
			this.content = content;
			this.exception = exception;
		}
		public Integer getExitStatus() {
			return exitStatus;
		}
		public void setExitStatus(Integer exitStatus) {
			this.exitStatus = exitStatus;
		}
		public String getContent() {
			if(content != null)
				content = content.trim();
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Exception getException() {
			return exception;
		}
		public void setException(Exception exception) {
			this.exception = exception;
		}
		
	}