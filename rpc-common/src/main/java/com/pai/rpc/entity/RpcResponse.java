package com.pai.rpc.entity;

import lombok.Data;

/**
 * RpcResponse
 * 响应实体类
 */
@Data
public class RpcResponse {
    private String requestId; // 表示对该 requestId 的请求进行响应
    private Exception exception;
    private Object result;
}
