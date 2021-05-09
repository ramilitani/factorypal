package com.factorypal.speedmetrics.repository;

import com.factorypal.speedmetrics.entity.MachineParametersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MachineParametersDataRepository extends MongoRepository<MachineParametersEntity, String> {

    List<MachineParametersEntity> findByTimestampGreaterThan(Date start);

}
