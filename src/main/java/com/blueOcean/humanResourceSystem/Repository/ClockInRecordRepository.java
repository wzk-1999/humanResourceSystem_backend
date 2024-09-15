package com.blueOcean.humanResourceSystem.Repository;

import com.blueOcean.humanResourceSystem.Model.ClockInRecord;
import com.blueOcean.humanResourceSystem.Model.ClockInRecordId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClockInRecordRepository extends JpaRepository<ClockInRecord, ClockInRecordId> {

}
