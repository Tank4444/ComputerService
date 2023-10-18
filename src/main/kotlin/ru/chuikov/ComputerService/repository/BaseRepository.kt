package ru.chuikov.ComputerService.repository

import org.springframework.data.jpa.repository.JpaRepository

interface BaseRepository<T>: JpaRepository<T, Int> {

}