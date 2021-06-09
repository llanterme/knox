package za.co.knox.restservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.knox.restservice.entity.CompanyEntity;

@Repository
public interface CompanyRepository extends CrudRepository<CompanyEntity,Integer> {

    @Query("SELECT p FROM CompanyEntity p WHERE p.mobileNumber = :mobileNumber AND p.password = :password")
    CompanyEntity authenticateUser(@Param("mobileNumber") String mobileNumber, @Param("password") String password);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CompanyEntity c WHERE c.mobileNumber = :mobileNumber")
    boolean validateUserAlreadyRegistered(@Param("mobileNumber") String mobileNumber);

    @Query("SELECT p FROM CompanyEntity p WHERE p.companyId = :companyId AND p.accountLocked =0")
    CompanyEntity validateCompanyNotLocked(@Param("companyId") int companyId);


    CompanyEntity getCompanyEntitiesByMobileNumber(@Param("mobileNumber") String mobileNumber);



}
