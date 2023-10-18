package ru.chuikov.ComputerService.dao

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name ="account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Int,
    var email:String,
    var pass:String,
    @Enumerated(EnumType.STRING)
    var role: Role,

):UserDetails {
    override fun getAuthorities(): List<SimpleGrantedAuthority> = listOf(SimpleGrantedAuthority(role.name))

    override fun getPassword(): String = pass

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
