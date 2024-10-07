package com.blueOcean.humanResourceSystem.Repository;

import com.blueOcean.humanResourceSystem.Model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<UserCredentials,Integer> {
    Optional<UserCredentials> findUserCredentialsByUsername(String username);
}
