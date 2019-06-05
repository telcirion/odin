/* Copyright 2019 Peter Jansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package odin.infrastructure;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.invoke.MethodHandles;
import java.sql.SQLException;

public class H2DBServer {
	private Server webServer;
    private Server server;
    private boolean running = false;

    public boolean startServer() {
    	final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        if (!isRunning()) {
            try {
                webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082").start(); // (4a)
                server = Server.createTcpServer("-tcpAllowOthers").start()  ;  // (4b)
                running = true;
                logger.info("H2 database server started.");
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
        return running;

    }

    public void stopServer() {
        if (running) {
            server.stop();
            webServer.stop();
            running = false;
        }
     }

    public boolean isRunning() {
        return running;
    }
}
