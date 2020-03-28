package org.wyw.lanplay.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.CommonService;
import org.wyw.lanplay.service.UserRecordService;

import java.util.*;

@Service
public class CommonServiceImpl implements CommonService {

    private UserRecordService userRecordService;

    public CommonServiceImpl(UserRecordService userRecordService) {
        this.userRecordService = userRecordService;
    }

    @Override
    public String createToken(UserRecordEntity userRecordEntity){
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.HOUR, 2);
        Date expireDate = calendar.getTime(); // 2小过期

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        Map<String, Object> userMap = new HashMap<>();
        map.put("username", userRecordEntity.getUsername());
        map.put("identity", userRecordEntity.getIdentity());

        Algorithm algorithm = Algorithm.HMAC256("secret");
        return JWT.create()
                .withHeader(map)
                .withClaim("id", userRecordEntity.getId())
                .withClaim("user", Base64Utils.encodeToString(JSON.toJSONString(userMap).getBytes()))
                .withIssuer("SERVICE")// 签名是有谁生成
                .withSubject("LAN_PLAY_TOKEN")// 签名的主题
                .withAudience("APP")// 签名的观众 也可以理解谁接受签名的
                .withIssuedAt(nowDate) // 生成签名的时间
                .withExpiresAt(expireDate)// 签名过期的时间
                .sign(algorithm);//签名 Signature
    }

    /**
     * 对密码进行加密
     * @param pwd 密码
     * @return 加密后的串
     */
    @Override
    public String encryptedPwd(String key,String pwd){
        return DigestUtils.md5Hex(key + pwd + "-lanPlay").toUpperCase();
    }

    private UserRecordEntity verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("SERVICE").build(); // Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("id");
        if(ObjectUtils.isNotEmpty(claim.asLong())){
            return userRecordService.getById(claim.asLong());
        }
        return null;
    }

    @Override
    public UserRecordEntity verifyAdmin(String token) {
        UserRecordEntity userRecordEntity = verify(token);
        if(userRecordEntity == null ){
            return null;
        }
        if(StringUtils.contains(userRecordEntity.getIdentity(), "ADMIN")){
            return userRecordEntity;
        }
        return null;
    }

    @Override
    public UserRecordEntity verifyUser(String token) {
        UserRecordEntity userRecordEntity = verify(token);
        if(userRecordEntity == null ){
            return null;
        }
        if(StringUtils.contains(userRecordEntity.getIdentity(), "USER")){
            return userRecordEntity;
        }
        return null;
    }
}
