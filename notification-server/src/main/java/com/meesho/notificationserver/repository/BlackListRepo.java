package com.meesho.notificationserver.repository;

import com.meesho.notificationserver.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface BlackListRepo extends JpaRepository<BlackList, UUID> {
    Optional<BlackList> findBlackListsByPhoneNumber(String phoneNumber);

}
