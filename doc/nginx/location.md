### location 的匹配规则

- 空格：默认匹配， 普通匹配
```text
location / {
    root /home;
}
```

- =：精确匹配
```text
location = /imooc/img/face1.png {
    root /home;
}
```

- ~*：匹配正则表达式，不区分大小写
```text
# 符合图片的显示
location ~* \.(GIF|jpg|png|jpeg) {
    root /home;
}
```

- ~：匹配正则表达式，区分大小写
```text
location ~ \.(GIF|jpg|png|jpeg) {
    root /home;
}
```

^~：以某个字符路径开关
```text
location ^~ /imooc/img {
    root /home;
}
```
