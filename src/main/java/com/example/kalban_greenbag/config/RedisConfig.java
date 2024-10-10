//package com.example.kalban_greenbag.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.cache.annotation.EnableCaching;
//
//import java.time.Duration;
//
//@Configuration
//@EnableRedisRepositories
//@EnableCaching
//public class RedisConfig {
//
//    @Value("${redis.host}")
//    private String redisHost;
//
//    @Value("${redis.port}")
//    private int redisPort;
//
//    @Value("${spring.data.redis.password}")
//    private String redisPassword;
//
//    @Value("${spring.data.redis.ssl.enabled}")
//    private boolean redisSslEnabled;
//
//    @Bean
//    public JedisConnectionFactory connectionFactory() {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setHostName(redisHost);
//        configuration.setPort(redisPort);
//        configuration.setPassword(redisPassword);  // Set password
//
//        // Set up Jedis client configuration with SSL
//        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfig = JedisClientConfiguration.builder();
//
//        if (redisSslEnabled) {
//            jedisClientConfig.useSsl().and().readTimeout(Duration.ofSeconds(60));  // Enable SSL and set timeout
//        }
//
//        return new JedisConnectionFactory(configuration, jedisClientConfig.build());
//    }
//
//    @Bean
//    RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
//        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
//        template.setEnableTransactionSupport(true);
//        template.afterPropertiesSet();
//        return template;
//    }
//
//
//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager();
//    }
//
//    public void clearAllCaches() {
//        CacheManager cacheManager = cacheManager();
//        cacheManager.getCacheNames().forEach(cacheName -> {
//            cacheManager.getCache(cacheName).clear();
//        });
//    }
//}
