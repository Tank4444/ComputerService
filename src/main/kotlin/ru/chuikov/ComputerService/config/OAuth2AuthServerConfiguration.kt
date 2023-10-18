package ru.chuikov.ComputerService.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory


@Configuration
@EnableAuthorizationServer
class OAuth2AuthServerConfiguration(
    private val authenticationManager:AuthenticationManager,
    private val passwordEncoder: PasswordEncoder
): AuthorizationServerConfigurerAdapter() {
    @Value("\${user.oauth.clientId}")
    private val clientID: String? = null

    @Value("\${user.oauth.clientSecret}")
    private val clientSecret: String? = null

    @Value("\${user.oauth.redirectUris}")
    private val redirectURLs: String? = null

    @Value("\${user.oauth.accessTokenValidity}")
    private val accessTokenValidity = 0

    @Value("\${user.oauth.refreshTokenValidity}")
    private val refreshTokenValidity = 0

    //@Value("\${key.private}")
    private val privateKey:String="-----BEGIN PRIVATE KEY-----\n" +
            "      MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCNKS/eo6wPXhOI\n" +
            "      txxGMLyWmmZ76rlRCE3OVm1q02BItyRMecDSo/K9XNqMK264wZBtL2xUGbDqKT59\n" +
            "      BijVmoJuUnKaV29QA7Sq3LNt+kBx0j/Ey+Bxf3jpPpnsZcQrXbJ75qsDBp1/ZSxh\n" +
            "      ZQVDGl/9+crS2D7jwycEqUNUWNOClbMyfmH6fyJX6Lcr3cDOPda+C3LwwsVOgtsk\n" +
            "      2uvCzmplwScYCaa8jGmeOaGTHYda6rRXdGtlD12KmM+UaxpH5QvEF/OVofGPS05j\n" +
            "      vLd27tUNDErYpFAv0oS8Iu89ykOFci3pc5z/sqY5qpP2BgIHbd8Zx4JgKxfttQ7P\n" +
            "      Yl+oBKlB3hzj+RCD0MAW8euSGQukOOSXUZkJAzxhAoGBAIBMOCzLIQ2VrG3gwWc1\n" +
            "      kmFvi3/yRkpUH0N8d8/7nQThMoEoI8L+0OhXcQKeUJO9K5DAt3WGmjYuILl6ZK5d\n" +
            "      OPUdYBFiinxRlF+I+rEac7Le34G/9zYfY+e4fvULp7yu+IAF0MA6Wu3ZH7q2ULTs\n" +
            "      MUzyz2hHLNBtTL5SNFKDPJNx\n" +
            "      -----END PRIVATE KEY-----"

    //@Value("\${key.public}")
    private val publicKey:String = "-----BEGIN PUBLIC KEY-----                                      \n" +
            "    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjSkv3qOsD14TiLccRjC8\n" +
            "    lppme+q5UQhNzlZtatNgSLckTHnA0qPyvVzajCtuuMGQbS9sVBmw6ik+fQYo1ZqC\n" +
            "    blJymldvUAO0qtyzbfpAcdI/xMvgcX946T6Z7GXEK12ye+arAwadf2UsYWUFQxpf\n" +
            "    /fnK0tg+48MnBKlDVFjTgpWzMn5h+n8iV+i3K93Azj3Wvgty8MLFToLbJJpNyhFr\n" +
            "    CYLvHX6/Wck1T06hNiFmy0oJES2b+3XTMC5mEii2JIFjLo2NQl7AOS1zmEHMAibk\n" +
            "    FwyvukMoju2TZaIzYgCXNPKhWcjrX4OaG9f8xIrR2ayyA52Y7/WLsanP+EeRc24p\n" +
            "    PwIDAQAB\n" +
            "    -----END PUBLIC KEY-----"

    @Throws(Exception::class)
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        oauthServer.tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients
            .inMemory()
            .withClient(clientID)
            .secret(passwordEncoder.encode(clientSecret))
            .authorizedGrantTypes("password", "authorization_code", "refresh_token")
            .scopes("user_info")
            .authorities("READ_ONLY_CLIENT")
            .redirectUris(redirectURLs)
            .accessTokenValiditySeconds(accessTokenValidity)
            .refreshTokenValiditySeconds(refreshTokenValidity)
    }

    @Bean
    fun tokenEnhancer(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
//        converter.setSigningKey(privateKey)
//        converter.setVerifierKey(publicKey)
        converter.setKeyPair(
            KeyStoreKeyFactory(ClassPathResource("jwt.jks"),"password".toCharArray()).getKeyPair("jwt")
        )
        return converter
    }

    @Bean
    fun tokenStore(): JwtTokenStore {
        return JwtTokenStore(tokenEnhancer())
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
            .accessTokenConverter(tokenEnhancer());
    }
}