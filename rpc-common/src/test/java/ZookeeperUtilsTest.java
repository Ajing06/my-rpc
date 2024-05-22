import com.pai.rpc.registry.zookeeper.util.ZooKeeperUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

public class ZookeeperUtilsTest {

    private ZooKeeperUtils zooKeeperUtils;

    @Before
    public void setZooKeeperUtils() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("47.108.208.206:2181")
                .connectionTimeoutMs(15000)
                .sessionTimeoutMs(60000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        this.zooKeeperUtils = new ZooKeeperUtils(client);
    }

    /**
     * 测试获取子节点
     */
    @Test
    public void testZookeeperUtils() {
        String path = "/registry";
        zooKeeperUtils.getChildren(path).forEach(System.out::println);

    }

    /**
     * node是否存在
     */
    @Test
    public void existsTest(){
        //boolean exists = zooKeeperUtils.exists("/registry", "com.pai.rpc.api.HelloService");
        boolean exists = zooKeeperUtils.exists("/registry", "com.pai.service.HelloService-1.0");
        System.out.println(exists);
    }

    @Test
    public void getTest(){
        String a = "com.ikun.home/172.30.176.1";
        String b = "com.pai.service.HelloService-1.0";

        String s = zooKeeperUtils.getData("/registry/com.pai.service.HelloService-2.0/");
        System.out.println(s);

    }

    @Test
    public void createNodeTest(){
        String s = zooKeeperUtils.createNode("/registry/com.ikun.home", CreateMode.PERSISTENT);
        System.out.println(s);
    }


}
