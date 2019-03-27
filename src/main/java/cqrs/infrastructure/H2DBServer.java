// Copyright 2019 Peter Jansen
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package cqrs.infrastructure;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

import java.sql.SQLException;

public class H2DBServer {
	private static Server webServer;
    private static Server server;
    private static boolean running = false;

    public static void startServer() {
    	final Logger logger=LogManager.getLogger();
        if (!running) {
            try {
                webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082").start(); // (4a)
                server = Server.createTcpServer("-tcpAllowOthers").start()  ;  // (4b)
                running = true;
            } catch (SQLException ex) {
                logger.log(Level.ERROR, ex);
            }
        }

    }

    public static void stopServer() {
        if (running) {
            server.stop();
            webServer.stop();
            running = false;
        }
     }

    public static boolean isRunning() {
        return running;
    }
}
