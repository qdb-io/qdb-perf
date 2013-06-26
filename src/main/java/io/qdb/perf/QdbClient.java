package io.qdb.perf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */
public class QdbClient {

    private final String serverUrl;

    public QdbClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String POST(String endpoint) throws IOException {
        return POST(endpoint, null, 0);
    }

    public String POST(String endpoint, byte[] data) throws IOException {
        return POST(endpoint, data, data.length);
    }

    public String POST(String endpoint, byte[] data, int sz) throws IOException {
        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        if (data != null) {
            con.setDoOutput(true);
            con.getOutputStream().write(data);
        }
        int rc = con.getResponseCode();
        if (rc == 200 || rc == 201) {
            return readString(con.getInputStream());
        } else {
            String msg = readString(con.getErrorStream());
            throw new IOException("POST " + url + " " + rc + (msg == null ? "" : ": " + msg));
        }
    }

    private String readString(InputStream in) throws IOException {
        if (in == null) return null;
        return new String(readAll(in), "UTF8");
    }

    private byte[] readAll(InputStream in) throws IOException {
        try {
            byte[] buf = new byte[8192];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (;;) {
                int sz = in.read(buf);
                if (sz < 0) break;
                bos.write(buf, 0, sz);
            }
            return bos.toByteArray();
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
    }
}
