package com.blueOcean.humanResourceSystem.Repository;

import com.blueOcean.humanResourceSystem.Model.BasicLawSalary;
import com.blueOcean.humanResourceSystem.Model.BasicSalaryId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BasicLawSalaryRepository extends JpaRepository<BasicLawSalary, BasicSalaryId> {
    @Query("SELECT b FROM BasicLawSalary b WHERE b.Year = :year AND b.Month = :month AND b.TB_Staff_id = :username")
    BasicLawSalary findByYearMonthAndUsername(@Param("year") String year,
                                              @Param("month") String month,
                                              @Param("username") String username);


    // Corrected query for pagination using Pageable
    @Query("SELECT new BasicLawSalary(b.Year,b.Month) FROM BasicLawSalary b WHERE b.TB_Staff_id = :username ORDER BY b.Year DESC, cast(b.Month as int) DESC")
    List<BasicLawSalary> findBasicLawSalaryByUsername(@Param("username") String username, Pageable pageable);

    @Query("SELECT COUNT(b) FROM BasicLawSalary b WHERE b.TB_Staff_id = :TB_Staff_id")
    long countByTB_Staff_id(@Param("TB_Staff_id") String TB_Staff_id);
}
