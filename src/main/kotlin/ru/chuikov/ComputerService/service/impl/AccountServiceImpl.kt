package ru.chuikov.ComputerService.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.AbstractPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.chuikov.ComputerService.dao.Account
import ru.chuikov.ComputerService.repository.AccountRepository
import ru.chuikov.ComputerService.service.AccountService
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer.ThrowException

@Service
class AccountServiceImpl(

): AccountService {
    @Autowired
    private lateinit var accRepository:AccountRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    override fun loadUserByUsername(email: String): Account =
        accRepository.getAccountByEmail(email) ?: throw UsernameNotFoundException("User with email $email not found")

    override fun add(data: Account): Account? = accRepository.saveAndFlush(data.copy(pass = passwordEncoder.encode(data.password)))

    override fun delete(id: Int) {
        accRepository.deleteById(id)
    }

    override fun getById(id: Int): Account? = accRepository.findById(id).get() ?: throw UsernameNotFoundException("User with id $id not found")

    override fun edit(data: Account): Account? = accRepository.saveAndFlush(data.copy())

    override fun getAll(): List<Account>? = accRepository.findAll() ?: throw UsernameNotFoundException("User not found")


}