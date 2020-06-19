package com.txs.springbootes.es.bean.repository;

import com.txs.springbootes.es.bean.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 用户接口
 */
public interface UserRepository  extends ElasticsearchRepository<User,Long> {

    /**
     * 搜索name或者desc   name or desc
     * @param name
     * @param desc
     * @return
     */
  List<User> findByNameOrDesc(String name,String desc);


    List<User> findByDesc(String desc);

}
