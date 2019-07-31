package com.test.response;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends OAuth2AuthorizationServerConfiguration {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private ClientDetailsService clientDetailsService;
	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	public OAuth2AuthorizationServerConfig(BaseClientDetails details,
			AuthenticationConfiguration authenticationConfiguration,
			ObjectProvider<TokenStore> tokenStore,
			ObjectProvider<AccessTokenConverter> tokenConverter,
			AuthorizationServerProperties properties,
			JwtAccessTokenConverter jwtAccessTokenConverter, ClientDetailsService clientDetailsService) throws Exception {
		super(details, authenticationConfiguration, tokenStore, tokenConverter, properties);
		this.jwtAccessTokenConverter = jwtAccessTokenConverter;
		this.clientDetailsService = clientDetailsService;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		// 서버에서 키값을 얻어오는 방법
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

		// 직접 RSA (private, public) 키값을 기술하는 방법
//		KeyPair keyPair = new KeyStoreKeyFactory(new FileSystemResource("src/main/resources/mypass.jks"),
//				"mypass".toCharArray()).getKeyPair("mypass");
//		converter.setKeyPair(keyPair);

		return converter;
	}

	@Bean
	@Primary
	public ResourceServerTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
//		RemoteTokenServices tokenService = new RemoteTokenServices();
//		tokenService.setCheckTokenEndpointUrl("http://localhost:8081/spring-security-oauth-server/oauth/check_token");
//		tokenService.setClientId("fooClientIdPassword");
//		tokenService.setClientSecret("secret");
//		return tokenService;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer.withClientDetails(clientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
				.allowFormAuthenticationForClients();
	}

	@Bean
	@Primary
	public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
		return new JdbcClientDetailsService(dataSource);
	}

}
