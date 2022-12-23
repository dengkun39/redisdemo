1, use zookeeper in docker
docker pull zookeeper
docker run --name my_zookeeper -d -p 2181:2181 zookeeper
docker exec -it my_zookeeper zkCli.sh
ls /services  ## 服务启动后，可以看到服务

2,  use consul in docker
docker pull consul
docker run --name my_consul -d -p 8500:8500 -p 8600:8600/udp consul



