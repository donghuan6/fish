## nginx 配置允许跨域访问

```text
server {
    listen 80;
    server_name localhost;
    
    # 允许跨域请求的域，* 代表所有
    add_header 'Access-Control-Allow-Origin ' *;
    # 允许带上 cookie 请求
    add_header 'Access-Control-Allow-Credentials' 'true';
    # 允许请求的方法，比如 GET/POST/PUT/DELETE
    add_header 'Access-Control-Allow-Methods' *;
    # 允许请求的 header
    add_header 'Access-Control-Allow-Headers' *;
    
    location / {
        root /home;
        index index.html;
    }

}

```