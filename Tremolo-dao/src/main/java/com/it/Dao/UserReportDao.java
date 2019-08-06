package com.it.Dao;

import com.it.pojo.UsersReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportDao extends JpaRepository<UsersReport,String> {
}
