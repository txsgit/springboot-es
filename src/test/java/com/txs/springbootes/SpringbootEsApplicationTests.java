package com.txs.springbootes;

import com.txs.springbootes.es.bean.User;
import com.txs.springbootes.es.bean.repository.UserRepository;
import lombok.val;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class SpringbootEsApplicationTests {



    //创建索引
//    @Test
//    public void createIndex() throws  Exception {
//        //初始化RestHighLevelClient
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.31.206", 9200, "http")));
//        //创建索引
//        CretateIndexRequest createIndexRequest=new CreateIndexRequest("user_index");
//        CreaeIndexResponse createIndexResponse=restHighLevelClient.indices().create(createIndexRequest,RequestOptions.DEFAULT);
//        System.out.println(createIndexResponse);
//
//    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    //复杂搜索 基于多字段、分词、过滤、分页、高亮
    @Test
    public void searchhigh()
    {
        //设置高亮字段
//        HighlightBuilder.Field desc=new HighlightBuilder.Field("desc")
//                //关闭多个字段匹配 true表示只有查询的字段匹配高亮  false表示多个字段都匹配
//                .requireFieldMatch(false)
//                .preTags("<span style='color:red'>")
//                .postTags("</span>");
//
//        HighlightBuilder.Field name=new HighlightBuilder.Field("name")
//                //关闭多个字段匹配
//                .requireFieldMatch(false)
//                .preTags("<span style='color:red'>")
//                .postTags("</span>");
        //高亮多个字段设置
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("desc");
        highlightBuilder.field("name");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");

        //简单字段查询 查询User下的索引
        NativeSearchQuery query=new NativeSearchQueryBuilder()

                //一个字段查询
                .withQuery(QueryBuilders.matchQuery("desc","张三一个"))
                //多字段查询  desc和name都设置成text ik_max_word
//                .withQuery(QueryBuilders.queryStringQuery("张三一个").field("desc").field("name"))
                //添加分页，注意页码从0开始
                ///pageable的实现类PageRequest的静态方法of
                .withPageable(PageRequest.of(0,6))
                //过滤大小 lt 小于 gt大于  lte小于等于 gte大于等于
//                .withFilter(QueryBuilders.rangeQuery("age").gte(10))
                //排序 使用字段排序返回结果中的score为null
                //根据字段排序fieldSort("字段名")   .order(SortOrder.ASC/DESC)
//                .withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC))
//                .withHighlightFields(name,desc)
                .withHighlightBuilder(highlightBuilder)
                .build();
        //获取hits数据

        SearchHits<User> search = elasticsearchRestTemplate.search(query, User.class);
        System.out.println(search);
        search.forEach(u-> System.out.println(u));
        //处理高亮
        List<SearchHit<User>> searchHits = search.getSearchHits();
        if(!CollectionUtils.isEmpty(searchHits))
        {

          //不为空遍历
            List<User> userList=searchHits.stream().map(hit->{
               //获取高亮数据
                Map<String, List<String>> highlightFields = hit.getHighlightFields();
                //更换高亮数据,判断高亮字段是否存在
                if(!CollectionUtils.isEmpty(highlightFields.get("desc")))
                {
                    hit.getContent().setDesc(highlightFields.get("desc").get(0));
                }
                if(!CollectionUtils.isEmpty(highlightFields.get("name")))
                {
                    hit.getContent().setName(highlightFields.get("name").get(0));
                }

                return hit.getContent();


            }).collect(Collectors.toList());

            userList.forEach(u-> System.out.println(u));
        }

    }
    /**
     * 创建索引和文档，更新文档内容
     */
    @Test
    public void saveIndex()
    {
        User user=new User(1l,"txs",18,"我是中国人");
        userRepository.save(user);
    }

    /**
     * 批量添加或更新
     */
    @Test
    public void bulksave()
    {
        List<User> userList=new ArrayList<>();
        userList.add(new User(1l,"txs-1",20,"这是一个神奇的铁路。"));
        userList.add(new User(2l,"txs-2",30,"Spring是一个非常好用的开源框架。"));
        userList.add(new User(3l,"张三-1",15,"法外狂徒张三来了一个张三。"));
        userList.add(new User(4l,"张三-2",36,"如果怒回去一个了。"));
        userList.add(new User(5l,"李四-1",40,"这是来了吗。"));
        userList.add(new User(6l,"李四-2",60,"不回来怎么办。"));

        userRepository.saveAll(userList);
    }

    /**
     * 删除操作
     */
    @Test
    public void delete()
    {
        userRepository.deleteById(1l);
    }

    /**
     * 搜索
     */
    @Test
    public void search(){

        //根据id查询
//     Optional<User> user= userRepository.findById(2l);
//        System.out.println(user.isPresent()?user.get():"空值");

        //查询所有
        Iterable<User> list=  userRepository.findAll();
        list.forEach(u-> System.out.println(u));

        //按照条件查询
//       List<User> list= userRepository.findByNameOrDesc("txs","一个");

//        List<User> list=  userRepository.findByDesc("张三");
//        list.forEach(u-> System.out.println(u));
    }

}
