package com.pai.rpc.registry.zookeeper.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ZooKeeperUtils {
    /**
     * 路径分隔符
     */
    private static final String PATH_SEPARATOR = "/";

    /**
     * zk连接
     */
    private final CuratorFramework client;

    public ZooKeeperUtils(CuratorFramework client) {
        this.client = client;
    }

    /**
     * 创建空节点，默认持久节点
     * @param path 节点路径
     * @return 完整路径
     */
    public String createNode(String path) {
        return createNode(path, CreateMode.PERSISTENT);
    }

    /**
     * 创建带类型的空节点
     * @param path     节点路径
     * @param createMode
     *            类型 CreateMode.PERSISTENT: 创建节点后，不删除就永久存在
     *            CreateMode.PERSISTENT_SEQUENTIAL：节点path末尾会追加一个10位数的单调递增的序列
     *            CreateMode.EPHEMERAL：创建后，回话结束节点会自动删除
     *            CreateMode.EPHEMERAL_SEQUENTIAL：节点path末尾会追加一个10位数的单调递增的序列
     * @return 路径
     */
    public String createNode(String path, CreateMode createMode) {
        try {
            client.create().creatingParentsIfNeeded().withMode(createMode).forPath(path);
            return path;
        } catch (Exception e) {
            return null;
        }
    }

    public void createNodeAndValue(String path, String value, CreateMode createMode) {

        try {
            client.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, value.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取节点数据
     *
     * @param path
     *            路径
     * @return 完整路径
     */
    public String getData(String path) {
        try {
            byte[] bytes = client.getData().forPath(path);
            if (bytes.length > 0) {
                return new String(bytes);
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 更新节点数据
     *
     * @param path
     *            节点路径
     * @param value
     *            更新值
     * @return 完整路径
     */
    public String update(String path, String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("value is null");
        }
        try {
            client.setData().forPath(path, value.getBytes());
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除节点
     *
     * @param path
     * @return 路径
     */
    public boolean delete(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 获取子节点
     *
     * @param path
     *            节点路径
     * @return
     */
    public List<String> getChildren(String path) {
        if (StringUtils.isEmpty(path)) {
            log.info("path value is empty");
            return null;
        }
        if (!path.startsWith(PATH_SEPARATOR)) {
            path = PATH_SEPARATOR + path;
        }
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("get children failed with path: {}", path, e);
        }
        return null;
    }

    /**
     * 判断指定路径下是否存在指定节点
     *
     * @param path 路径
     * @param node 节点名称
     * @return 结果
     */
    public boolean exists(String path, String node) {
        try {
            List<String> list = getChildren(path);
            return !CollectionUtils.isEmpty(list) && list.contains(node);
        } catch (Exception e) {
            return false;
        }
    }

}
