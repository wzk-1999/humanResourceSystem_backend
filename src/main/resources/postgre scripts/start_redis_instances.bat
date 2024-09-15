@echo off
:: Set the Redis installation directory
set REDIS_DIR="C:\Program Files\Redis"

:: Start Redis instances in separate command prompt windows
start cmd /k "cd %REDIS_DIR% && redis-server redis.windows-6381.conf"
start cmd /k "cd %REDIS_DIR% && redis-server redis.windows-6382.conf"
start cmd /k "cd %REDIS_DIR% && redis-server redis.windows-6383.conf"
start cmd /k "cd %REDIS_DIR% && redis-server redis.windows-6384.conf"
start cmd /k "cd %REDIS_DIR% && redis-server redis.windows-6385.conf"
start cmd /k "cd %REDIS_DIR% && redis-server redis.windows-6386.conf"

:: Exit the batch script
exit
