package com.norwegiancorgi.fileserver.repositories;

import com.norwegiancorgi.fileserver.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @Query(
            "SELECT o FROM Owner o WHERE o.email = ?1"
    )
    Optional<Owner> findOwnerByEmail(String email);

    @Transactional
    @Modifying(
            clearAutomatically = true
    )
    @Query(
            "UPDATE Owner o set o.password = :password WHERE o.id = :id"
    )
    void updatePassword(@Param("id") Long id, @Param("password") String password);
}
