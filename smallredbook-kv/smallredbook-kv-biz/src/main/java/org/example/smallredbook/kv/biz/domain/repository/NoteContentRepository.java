package org.example.smallredbook.kv.biz.domain.repository;

import org.example.smallredbook.kv.biz.domain.dataobject.NoteContentDO;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

/**
 * @Author: tzy
 * @Description: 提供一个用于操作Cassandra数据库中NoteContentDO实体的存储库(库名：xioahashu)。
 * @Date: 2024/9/3 19:50
 */
public interface NoteContentRepository  extends CassandraRepository<NoteContentDO , UUID> {

}
