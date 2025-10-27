package com.example.PizzUMBurgUM.repositories;

import com.example.PizzUMBurgUM.entities.Hamburguesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HamburguesaRepository extends JpaRepository<Hamburguesa, Long> {
}
