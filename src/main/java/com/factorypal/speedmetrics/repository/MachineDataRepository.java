package com.factorypal.speedmetrics.repository;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineDataRepository extends MongoRepository<MachineDataEntity, String> {

    MachineDataEntity findByKey(String key);
}
