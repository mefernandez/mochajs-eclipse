package jsrunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class MochaProcess {

	public String run(File test) throws IOException, InterruptedException {
		// FIXME We're just giving for granted mocha is installed in this path
		ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/mocha", "-R", "json", test.getAbsolutePath());
		builder.redirectErrorStream(true);
		Map<String, String> env = builder.environment();
		// This path is necessary for ProcessBuilder to find mocha binary
		env.put("PATH", "/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin");
		Process p = builder.start();
		p.waitFor();
		StringBuffer sb = new StringBuffer();
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((line = input.readLine()) != null) {
			sb.append(line);
		}
		input.close();
		return sb.toString();
	}
	
	

}
