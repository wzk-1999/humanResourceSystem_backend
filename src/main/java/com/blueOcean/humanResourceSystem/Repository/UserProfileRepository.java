package com.blueOcean.humanResourceSystem.Repository;

import com.blueOcean.humanResourceSystem.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,String> {
}
