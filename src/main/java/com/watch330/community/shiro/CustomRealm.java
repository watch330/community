//package com.watch330.community.shiro;
//
//import com.watch330.community.mapper.UserMapper;
//import com.watch330.community.model.User;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
//public class CustomRealm extends AuthorizingRealm {
//    @Autowired
//    private UserMapper userMapper;
//
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        //获取用户民
//        String username = (String) authenticationToken.getPrincipal();
//
//        //在数据库中查找实体
//        User user = userMapper.findByName(username);
//        if (user == null)
//            return null;
//
//        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,user.getAccountId(),getName());
//        return null;
//    }
//
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        return null;
//    }
//
//
//}
