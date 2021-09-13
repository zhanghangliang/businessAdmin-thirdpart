package com.gov.wiki.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *  所有Repository的基类。所有的接口都要继承此接口
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseRepository<T,ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
