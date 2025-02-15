-- 时间窗口

local minuteKey = KEYS[1]
local hourKey = KEYS[2]
local dayKey = KEYS[3]

local maxPerMinute = tonumber(ARGV[1])
local maxPerHour = tonumber(ARGV[2])
local maxPerDay = tonumber(ARGV[3])

local minuteCount = redis.call('incr', minuteKey)
local hourCount = redis.call('incr', hourKey)
local dayCount = redis.call('incr', dayKey)

if minuteCount == 1 then
    redis.call('expire', minuteKey, 60)
end
if hourCount == 1 then
    redis.call('expire', hourKey, 3600)
end
if dayCount == 1 then
    redis.call('expire', dayKey, 86400)
end

return (minuteCount <= maxPerMinute and hourCount <= maxPerHour and dayCount <= maxPerDay) and 1 or 0


