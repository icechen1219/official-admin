///:UserController.java
package com.erhui.official.web;

import com.erhui.official.common.JsonResult;
import com.erhui.official.domain.SysUserDTO;
import com.erhui.official.service.SysUserService;
import com.erhui.official.util.IpAddressUtil;
import com.erhui.official.util.RandomUtil;
import com.erhui.official.util.SecurityUtil;
import com.erhui.official.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author icechen1219
 * @date 2019/02/04
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    /**
     * cookies失效时间：7天
     */
    private static int expirySeconds = 7 * 24 * 60 * 60;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录API
     *
     * @param request
     * @param response
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public JsonResult login(HttpServletRequest request, HttpServletResponse response, SysUserDTO userDTO) {
        JsonResult result = new JsonResult();

        String usernameFromCookies = getContentFromCookies(request, "username");
        // 如果没有cookie或者cookie里保存的用户信息跟表单传进来的不一致，则认为是新的登录
        if (usernameFromCookies == null || !isCookiesLogin(request, userDTO)) {
            logger.debug("{} new login...", userDTO.getUsername());

            String username = userDTO.getUsername();
            String password = userDTO.getPassword();
            if (StringUtil.isStrEmpty(username) || StringUtil.isStrEmpty(password)) {
                result.setErrorCode(-1);
                result.setErrorMsg("username or password is null");
                return result;
            }

            SysUserDTO loginUser = sysUserService.loginCheck(username, SecurityUtil.buildMd5(password), IpAddressUtil.getIpAddress(request));
            if (loginUser == null) {
                result.setErrorCode(-2);
                result.setErrorMsg("username or password is wrong");
                return result;
            }

            buildSuccessResult(result, loginUser);
            handlerRememberEvent(request, response, loginUser);
        } else {
            logger.debug("{} auto relogin...", usernameFromCookies);

            String passwordFromCookies = getContentFromCookies(request, "password");
            SysUserDTO cookieUser = new SysUserDTO();
            cookieUser.setUsername(usernameFromCookies);
            cookieUser.setPassword(passwordFromCookies);
            buildSuccessResult(result, cookieUser);
            handlerRememberEvent(request, response, cookieUser);
        }

        return result;
    }

    private boolean isCookiesLogin(HttpServletRequest request, SysUserDTO userDTO) {
        String usernameFromCookies = getContentFromCookies(request, "username");
        String passwordFromCookies = getContentFromCookies(request, "password");
        return userDTO.getUsername().equals(usernameFromCookies) && userDTO.getPassword().equals(passwordFromCookies);
    }

    private void handlerRememberEvent(HttpServletRequest request, HttpServletResponse response, SysUserDTO loginUser) {
        String needRemember = request.getParameter("needRemember");
        logger.info("need remember: {}", needRemember);
        if (Boolean.parseBoolean(needRemember)) {
            // 如果勾选了记住密码，则创建coockies
            Cookie usernameCookie = buildCookie("username", loginUser.getUsername(), expirySeconds);
            response.addCookie(usernameCookie);
            Cookie passwordCookie = buildCookie("password", loginUser.getPassword(), expirySeconds);
            response.addCookie(passwordCookie);
            logger.debug("set login info into cookies");
        } else {
            // 取消勾选记住密码，则清除cookies
            Cookie usernameCookie = buildCookie("username", null, 0);
            response.addCookie(usernameCookie);
            Cookie passwordCookie = buildCookie("password", null, 0);
            response.addCookie(passwordCookie);
            logger.debug("remove from cookies");
        }
    }

    /**
     * 注销API
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public JsonResult logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("username")
                        || cookie.getName().equalsIgnoreCase("password")) {
                    // 主动注销，需要删除cookies
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        JsonResult result = new JsonResult();
        buildSuccessMsg(result);
        return result;
    }

    /**
     * 用户注册API
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/add")
    public JsonResult register(SysUserDTO userDTO) {
        JsonResult result = new JsonResult();

        boolean success = false;
        try {
            success = sysUserService.register(userDTO);
        } catch (Exception e) {
            logger.error("用户注册失败", e);
        }
        if (success) {
            buildSuccessMsg(result);
            result.setData(userDTO);
        } else {
            result.setErrorCode(-1101);
            result.setErrorMsg("用户注册失败");
        }

        return result;
    }

    @PostMapping("/check")
    public JsonResult checkUnique(String username) {
        JsonResult result = new JsonResult();
        SysUserDTO uniqueUser = sysUserService.findUniqueUser(username);

        if (uniqueUser != null) {
            SysUserDTO tmpUser = new SysUserDTO();
            tmpUser.setUsername(username + RandomUtil.randFourBit());
            result.setErrorCode(-1102);
            result.setErrorMsg("用户名已存在！");
            result.setData(tmpUser);
        } else {
            buildSuccessMsg(result);
        }


        return result;
    }

    private String getContentFromCookies(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equalsIgnoreCase(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private Cookie buildCookie(String name, String value, int expiry) {
        Cookie coockie = new Cookie(name, value);
        coockie.setPath("/");
        coockie.setMaxAge(expiry);
        return coockie;
    }

    private void buildSuccessResult(JsonResult result, SysUserDTO loginUser) {
        buildSuccessMsg(result);
        result.setData(loginUser);
    }

    private void buildSuccessMsg(JsonResult result) {
        result.setErrorCode(0);
        result.setErrorMsg("OK");
    }

    @PostMapping("/setCookie")
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("test", "same");
        cookie.setPath("/");
        response.addCookie(cookie);
        return "success";
    }

    @PostMapping("/getCookie")
    public JsonResult getCookie(HttpServletRequest request) {
        JsonResult result = new JsonResult();
        buildSuccessMsg(result);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            SysUserDTO userDTO = new SysUserDTO();
            for (Cookie cookie : cookies) {
                logger.debug("name:" + cookie.getName() + "-----value:" + cookie.getValue());
                if ("username".equalsIgnoreCase(cookie.getName())) {
                    userDTO.setUsername(cookie.getValue());
                    continue;
                }
                if ("password".equalsIgnoreCase(cookie.getName())) {
                    userDTO.setPassword(cookie.getValue());
                }
            }
            result.setData(userDTO);
        }
        return result;
    }
}
///:UserController.java
