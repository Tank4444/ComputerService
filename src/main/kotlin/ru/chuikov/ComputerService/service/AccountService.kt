package ru.chuikov.ComputerService.service

import org.springframework.security.core.userdetails.UserDetailsService
import ru.chuikov.ComputerService.dao.Account

public interface AccountService:UserDetailsService,BaseService<Account> {

}