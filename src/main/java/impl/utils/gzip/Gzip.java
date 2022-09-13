package impl.utils.gzip;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
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
           ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
           GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);

           int len;
           byte[] buffer = new byte[256];

           while ((len = inputStream.read(buffer)) > 0) {
                gzipOutputStream.write(buffer, 0, len);
                gzipOutputStream.flush();
           }

           gzipOutputStream.finish();
           return outputStream.toByteArray();

        } catch (Exception e) {logger.error(e.getMessage());}
        return null;
    }
}
