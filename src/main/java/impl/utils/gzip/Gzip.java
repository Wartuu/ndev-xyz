package impl.utils.gzip;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

public class Gzip {

    private static Logger logger = LoggerFactory.getLogger(Gzip.class);
    public static byte[] createGzip(String content) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);

            int len;
            byte[] buff = new byte[128];

            while ((len = inputStream.read(buff)) > 0) {
                gzipOutputStream.write(buff, 0, len);
                gzipOutputStream.flush();
            }

            gzipOutputStream.finish();
            gzipOutputStream.close();
            inputStream.close();

            return outputStream.toByteArray();

        } catch (Exception e) {logger.error(e.getMessage());}
        return null;
    }
    public static byte[] createGzip(byte[] content) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);

            int len;
            byte[] buff = new byte[128];

            while ((len = inputStream.read(buff)) > 0) {
                gzipOutputStream.write(buff, 0, len);
                gzipOutputStream.flush();
            }



            gzipOutputStream.finish();
            gzipOutputStream.close();
            inputStream.close();

            return outputStream.toByteArray();

        } catch (Exception e) {logger.error(e.getMessage());}
        return null;
    }

}
