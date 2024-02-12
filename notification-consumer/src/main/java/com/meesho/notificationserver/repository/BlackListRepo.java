package com.meesho.notificationserver.repository;

import com.meesho.notificationserver.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface BlackListRepo extends JpaRepository<BlackList, UUID> {

}
