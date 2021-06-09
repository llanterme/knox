package za.co.knox.restservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import za.co.knox.restservice.entity.ApiEntity;
import za.co.knox.restservice.entity.ScreeningQuestionsEntity;

@Repository
public interface ScreeningQuestionsRepository extends CrudRepository<ScreeningQuestionsEntity,Integer> {



}
