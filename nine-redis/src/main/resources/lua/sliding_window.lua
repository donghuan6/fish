-- 滑动窗口限流

local key = KEYS[1]
local maxRequests = tonumber(ARGV[1])
local windowSize = tonumber(ARGV[2])
local currentTime = tonumber(ARGV[3])

redis.call('ZREMRANGEBYSCORE', key, 0, currentTime - windowSize)
local requestCount = redis.call('ZCARD', key)

if requestCount < maxRequests then
    redis.call('ZADD', key, currentTime, currentTime)
    redis.call('EXPIRE', key, windowSize + 1)
    return 1
else
    return 0
end
