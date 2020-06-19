# springboot-es
springboot2.3.1.RELEASE  elasticsearch7.7.1

## git 提交命令
```
git init
git add .
git commit -m 'Initial Commit'
git remote add origin https://github.com/txsgit/springboot-es.git
git push -u origin master
```
## 先安装elasticsearch7.7集群

1. 搭建环境
  - centos7  内存至少1G和磁盘空间至少10G
  - jdk8
  - elasticsearch7.7.1
  - IK分词器
 [源码下载：](https://github.com/medcl/elasticsearch-analysis-ik)
 ```
 解压到到ES_HOME/plugins/ik目录下面(直接包含一个conf文件夹和一堆.jar包)
重新启动ES，启动 看到try load config ……IK相关信息，说明启动完成和安装IK插件完成。
 ```
  
2. elasticsearch国内下载地址

[https://www.newbe.pro/Mirrors/Mirrors-Elasticsearch/](https://www.newbe.pro/Mirrors/Mirrors-Elasticsearch/) 

3. 集群配置文件elasticsearch7.7.1/config/elasticsearch.yml如下
```
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
cluster.name: my-es    #集群名称
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
node.name: node-206   #节点名称
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
#path.data: /path/to/data
#
# Path to log files:
#
#path.logs: /path/to/logs
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 0.0.0.0    #外网可访问
#
# Set a custom port for HTTP:
#
http.port: 9200      #当前节点占用的端口号，默认9200 
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when this node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#集群列表 启动当前es节点时会去这个ip列表中去发现其他节点，此处不需配置自己节点的ip,这里支持ip和ip:port形式,不加端口号默认使用ip:9300去发现节点
discovery.seed_hosts: ["192.168.31.206:9300","192.168.31.177:9300","192.168.31.34:9300"]
#
# Bootstrap the cluster using an initial set of master-eligible nodes:
#
#cluster.initial_master_nodes: ["node-1", "node-2"]
#可作为master节点初始的节点名称
cluster.initial_master_nodes: ["node-206", "node-177", "node-34"]
#
#cluster.initial_master_nodes: ["node-206"]
# For more information, consult the discovery and cluster formation module documentation.
#
# ---------------------------------- Gateway -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
#
#gateway.recover_after_nodes: 3
#
# For more information, consult the gateway module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true
```


4. 依次启动
 - /elasticsearch/bin/elasticsearch
 - 浏览器访问出现如下说明启动成功
 - http://localhost:9200
```
{
  "name" : "node-206",
  "cluster_name" : "my-es",
  "cluster_uuid" : "_na_",
  "version" : {
    "number" : "7.7.1",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "ad56dce891c901a492bb1ee393f12dfff473a423",
    "build_date" : "2020-05-28T16:30:01.040088Z",
    "build_snapshot" : false,
    "lucene_version" : "8.5.1",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

5. 使用elasticsearch-head查看集群状态
 - [下载地址](https://github.com/mobz/elasticsearch-head)
 - 最好使用chrome浏览器插件，方便
 
 6. springboot测试elasticsearch配置
 ```
    src
      ├──test/java/com/txs/springbootes/SpringbootEsApplicationTests.java  -- 主要测试类
      ├── main/java/com/txs/springbootes/es/bean/repository/UserRepository.java ---简单接口
      ├── main/java/com/txs/springbootes/es/bean/User.java -- user索引文档类
      ├── main/resources/application.yml -- 集群配置
     
 ```
    
 
