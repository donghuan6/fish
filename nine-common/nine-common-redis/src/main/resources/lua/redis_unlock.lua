-- 验证锁持有者，如果是持有者，则释放锁，否则返回nil

local key = KEYS[1]
local value = ARGV[1]

-- 检查锁是否存在且值是匹配的
if redis.call('get', key) == value then
    return redis.call('del', key)
else
    return 0
end
