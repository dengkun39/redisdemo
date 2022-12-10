# redisdemo
This project is using three methods to connect Redis in Java using Spring
(1) redis-template-sample is using RedisTemplate 
(2) redis-repository-sample is using repository
(3) redis-cache-demo is using cache

before run code, you need to use docker to run redis
this is the command
docker pull redis
docker run --name my_redis -d -p 6379:6379 redis

connect to your docker redis
docker exec -it my_redis bash
redis-cli
