package com.meesho.notificationserver.repository;

import com.meesho.notificationserver.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface NotifyRepo extends JpaRepository<Notify, UUID> {



}
