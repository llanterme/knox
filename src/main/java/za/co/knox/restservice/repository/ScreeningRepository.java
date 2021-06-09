package za.co.knox.restservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.knox.restservice.entity.ScreeningEntity;

import java.util.List;

@Repository
public interface ScreeningRepository extends CrudRepository<ScreeningEntity,Integer> {

    @Query("SELECT DISTINCT a.screenDate FROM ScreeningEntity a where a.companyId = :companyId")
    List<Object[]> findDistinctByScreenDate(@Param("companyId") int companyId);

    @Query("SELECT p FROM ScreeningEntity p WHERE p.companyId = :companyId AND p.screenDate = :screeningDate")
    List<ScreeningEntity> findScreeningsByDateAndCompanyID(@Param("companyId") int companyId, @Param("screeningDate") String screeningDate);



    @Query(value = "SELECT COUNT(*) FROM (SELECT DISTINCT screening_id FROM screening where company_id  = :companyId and screen_date = :screeningDate) AS screening_id", nativeQuery = true)
    int findCountByDateAndCompanyId(@Param("companyId") int companyId, @Param("screeningDate") String screeningDate);


}
