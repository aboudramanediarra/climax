package com.demo.climax.repository;

import com.demo.climax.bean.Moyenne;
import com.demo.climax.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
   public final static String GET_MOYENNE_PROFESSION = "SELECT profession, sum(salaire)/count(profession) as moyenne FROM Client group by profession";
   @Query(GET_MOYENNE_PROFESSION)
   List<Object[]> findByProfession();
}

