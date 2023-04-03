import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws Exception {

        ByteBuffer allocate = ByteBuffer.allocate(4);
        System.out.println(allocate.remaining());

        allocate.put("1234".getBytes(StandardCharsets.UTF_8));

        System.out.println(allocate.remaining());

        allocate.flip();

        allocate.clear();

        System.out.println(Arrays.toString(allocate.array()));
        System.out.println(allocate.remaining());

        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 9999);

        System.out.println(URI.create("hdfs://mycluster").getScheme());
        System.out.println(URI.create("hdfs://mycluster").getAuthority());
        System.out.println(URI.create("hdfs://mycluster").getHost());

        InetSocketAddress socketAddress = InetSocketAddress.createUnresolved(URI.create("hdfs://mycluster").getAuthority(), 8020);
        System.out.println(socketAddress.toString());
        System.out.println(socketAddress.getHostName());

        System.out.println(URI.create("hdfs://mycluster:8020/mycluster").getPath());

        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

}
