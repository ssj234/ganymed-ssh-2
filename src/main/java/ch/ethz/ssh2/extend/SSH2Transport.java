package ch.ethz.ssh2.extend;

import java.util.Map;

public interface SSH2Transport {
	
	public Object send(String command);
	
	public Object send(Map input);

}
