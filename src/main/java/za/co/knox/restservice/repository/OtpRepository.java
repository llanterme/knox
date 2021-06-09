package za.co.knox.restservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.knox.restservice.entity.OtpEntity;

@Repository
public interface OtpRepository extends CrudRepository<OtpEntity,Integer> {

    @Query("SELECT p FROM OtpEntity p WHERE p.companyId = LOWER(:companyId) AND p.code = LOWER(:otpCode)")
    OtpEntity validateOtp(@Param("companyId") int companyId, @Param("otpCode") int otpCode);


}
