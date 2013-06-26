package io.qdb.perf;

import java.io.IOException;
import java.util.Random;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        try {
            QdbClient qdb = new QdbClient("http://127.0.0.1:9554/");
            qdb.POST("/q/perf");
            int n = 10000;
            long start = System.currentTimeMillis();
            byte[] buf = new byte[4096];
            Random rnd = new Random();
            rnd.nextBytes(buf);
            for (int i = 0; i < n; i++) {
                int sz = rnd.nextInt(buf.length);
                qdb.POST("/q/perf/messages", buf, sz);
            }
            int ms = (int)(System.currentTimeMillis() - start);
            System.out.printf(ms + " ms, " + (n * 1000.0 / ms) + " messages/sec");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
