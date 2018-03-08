package com.cloudbox.server;

import com.cloudbox.utils.RSAEncrypt;

/**
 * 
 * Description:使用Jetty运行调试Web应用, 在Console输入回车快速重新加载应用
 *
 * @version v1.0
 *
 */
public class StartServer {
	public static final int PORT = 8090;
	public static final String CONTEXT = "/lishun";
	public static final String[] TLD_JAR_NAMES = new String[] { "spring-webmvc" };

	public static void main(String[] args) throws Exception {
		/*// 设定Spring的profile
		Profiles.setProfileAsSystemProperty(Profiles.DEVELOPMENT);
		// 启动Jetty
		Server server = JettyFactory.createServerInSource(PORT, CONTEXT);
		JettyFactory.setTldJarNames(server, TLD_JAR_NAMES);
		try {
			server.start();
			System.out.println("[INFO] Server running at http://localhost:" + PORT + CONTEXT);
			System.out.println("[HINT] Hit Enter to reload the application quickly");
			// 等待用户输入回车重载应用.
			while (true) {
				char c = (char) System.in.read();
				if (c == '\n') {
					JettyFactory.reloadContext(server);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}*/
		RSAEncrypt.loadPublicKeyByStrDefault();
	}
}