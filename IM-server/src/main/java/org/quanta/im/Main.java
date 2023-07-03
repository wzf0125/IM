package org.quanta.im;

import org.quanta.im.server.IMServer;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/2
 */
public class Main {
    public static void main(String[] args) {
        new IMServer(8080).run();
    }
}
