package com.pai.rpc.entity;

import lombok.Data;

/**
 * RpcRequest
 * 请求实体类
 */
@Data
public class RpcRequest {

    private String requestId; // 请求的Id, 唯一标识该请求
    private String interfaceName; // 接口名称
    private String serviceVersion; // 版本
    private String methodName; // 方法名称
    private Class<?>[] parameterTypes; // 参数类型
    private Object[] parameters; // 具体参数

    public String parametersToString() {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object parameter : parameters) {
            sb.append(parameter).append(".");
        }
        return sb.toString();
    }
}
