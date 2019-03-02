package com.example.studyrestapi.demo.events;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.parser.Entity;

public interface EventRepository extends JpaRepository<Event,Integer> {
}
