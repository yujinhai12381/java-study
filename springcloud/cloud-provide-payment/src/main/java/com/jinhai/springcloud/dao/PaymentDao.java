package com.jinhai.springcloud.dao;

import com.jinhai.springcloud.pojo.Payment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PaymentDao {
    //@Insert("insert into payment (serial) values (#{serial})")
    int create(Payment payment);

    //注解生效，但是 xml未生效
    //@Select("select * from payment where id=#{id}")
    Payment queryById(@Param("id") long id);
}