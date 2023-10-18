package ru.chuikov.ComputerService.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.chuikov.ComputerService.dao.Account

@Repository
interface AccountRepository:BaseRepository<Account>{
    @Query("SELECT a FROM Account a WHERE a.email = :email")
    fun getAccountByEmail(@Param("email") email:String):Account?
}