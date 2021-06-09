package za.co.knox.restservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.knox.restservice.entity.CompanyStaffEntity;
import za.co.knox.restservice.entity.ScreeningEntity;

import java.util.List;

@Repository
public interface CompanyStaffRepository extends CrudRepository<CompanyStaffEntity,Integer> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CompanyStaffEntity c WHERE c.userMobile = :mobileNumber")
    boolean validateStaffUserAlreadyRegistered(@Param("mobileNumber") String mobileNumber);

    List<CompanyStaffEntity> getCompanyStaffEntityByCompanyId(int companyId);

}
