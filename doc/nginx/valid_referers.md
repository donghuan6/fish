## nginx 防盗链配置支持，配置在 server 中
```text
# 对源站点验证
valid_referers *.imooc.com;
# 不是同一个源会进入下方判断
if ($valid_referers) {
    return 403;
}
```

