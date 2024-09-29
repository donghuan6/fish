-- 原子设置值，并设置过期时间（秒），返回设置后的值

local key = KEYS[1]
local value = ARGV[1]
local expireTime = tonumber(ARGV[2])

if redis.call('set', key, value, 'NX', 'EX', expireTime) then
    return value
else
    return nil
end
