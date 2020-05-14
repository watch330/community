//package com.watch330.community.shiro;
//
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Configuration
//public class shiroConfig {
//
//    @Bean
//    public CustomRealm customRealm() {
//        return new CustomRealm();
//    }
//
//    public DefaultWebSecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(customRealm());
//        return securityManager;
//    }
//
//    @Bean
//    public ShiroFilterFactoryBean webFilter(){
//    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//
//        Map<String,String> filterChainMap = new LinkedHashMap<>(16);
//
//        filterChainMap.put("/","anon");
//        filterChainMap.put("/login","anon");
//        filterChainMap.put("/**","authc");
//
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
//
//        shiroFilterFactoryBean.setSecurityManager(securityManager());
//
//        return shiroFilterFactoryBean;
//    }
//
//
//}
