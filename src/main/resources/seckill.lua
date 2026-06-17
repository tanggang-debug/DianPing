if (tonumber(redis.call('get', KEYS[1]) or "0") <= 0) then
    return 1
end
if (redis.call('sismember', KEYS[2], ARGV[1]) == 1) then
    return 2
end
redis.call('incrby', KEYS[1], -1)
redis.call('sadd', KEYS[2], ARGV[1])
return 0