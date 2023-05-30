## 一、通用请求接口

### 1. 接口设计

接口名称：/api/common/request
请求方式：POST
请求参数：

```
{
    "url": "http://127.0.0.1/api/common/request",
    "method": "POST",
    "headers": {
        "Content-Type": "application/json"
    },
    "data": {
        "service": "服务名(模块名)",
        
    }
}
```

## 二、异常处理

### 1. BusinessException

业务异常。此异常会被捕获，返回给前端。前端一般需要根据code进行处理并将业务异常信息反馈给用户。

```
{
    "code": "业务异常码",
    "message": "业务异常信息"
}
```

### 2. Exception

其他系统级别异常。此异常需要前端在请求响应拦截器中统一处理。