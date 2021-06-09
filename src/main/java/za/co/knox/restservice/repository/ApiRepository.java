package za.co.knox.restservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import za.co.knox.restservice.entity.ApiEntity;

@Repository
public interface ApiRepository extends CrudRepository<ApiEntity,Integer> {



}
