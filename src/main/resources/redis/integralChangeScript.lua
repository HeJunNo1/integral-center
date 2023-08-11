redis.call("incrby", KEYS[1], ARGV[1])
redis.call("XADD", KEYS[2], "*", "record" ,ARGV[2])
return 1