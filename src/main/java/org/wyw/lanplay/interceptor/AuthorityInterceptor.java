package org.wyw.lanplay.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.wyw.lanplay.annotation.LoginAdminRequired;
import org.wyw.lanplay.annotation.LoginUserRequired;
import org.wyw.lanplay.entity.UserRecordEntity;
import org.wyw.lanplay.service.CommonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Service
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

    private CommonService commonService;

    public AuthorityInterceptor(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        LoginAdminRequired methodAnnotation = method.getAnnotation(LoginAdminRequired.class);
        LoginUserRequired loginUserRequired = method.getAnnotation(LoginUserRequired.class);

        String token = request.getHeader("token");
        if(token == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print("token not find");
            return false;
        }

        if (methodAnnotation != null) {

            UserRecordEntity userRecordEntity = commonService.verifyAdmin(token);
            if(ObjectUtils.isEmpty(userRecordEntity) || userRecordEntity.getStatus() != 0){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().print("token not find");
                return false;
            }

            if(StringUtils.contains(userRecordEntity.getIdentity(),"ADMIN")){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().print("权限不足");
                return false;
            }
            return true;
        }

        if (loginUserRequired != null) {
            UserRecordEntity userRecordEntity = commonService.verifyAdmin(token);
            if(ObjectUtils.isEmpty(userRecordEntity) || userRecordEntity.getStatus() != 0){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().print("token not find");
                return false;
            }
            if(StringUtils.contains(userRecordEntity.getIdentity(),"USER")){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().print("权限不足");
                return false;
            }
            return true;
        }
        return false;
    }
}
