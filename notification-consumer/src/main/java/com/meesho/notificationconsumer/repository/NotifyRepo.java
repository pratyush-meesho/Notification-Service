package com.meesho.notificationconsumer.repository;

import com.meesho.notificationconsumer.entity.sql.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotifyRepo extends JpaRepository<Notify, UUID> {

}
