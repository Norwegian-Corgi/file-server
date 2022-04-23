package com.norwegiancorgi.fileserver.repositories;

import com.norwegiancorgi.fileserver.models.Filez;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FilezRepository extends JpaRepository<Filez, Long> {

    @Query(
            value = "SELECT * FROM Filez f WHERE f.owner_id = :ownerId",
            nativeQuery = true
    )
    List<Filez> findByOwner(@Param("ownerId") Long ownerId);

    @Transactional
    @Modifying(
            clearAutomatically = true
    )
    @Query(
            "UPDATE Filez f set f.numberOfDownloads = :numberOfDownloads WHERE f.id = :id"
    )
    void updateNumberOfDownloads(@Param("id") Long id, @Param("numberOfDownloads") Integer numberOfDownloads);

}
