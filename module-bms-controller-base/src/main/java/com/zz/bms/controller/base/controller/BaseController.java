package com.zz.bms.controller.base.controller;

import com.zz.bms.controller.base.converteditor.BigDecimalConvertEditor;
import com.zz.bms.controller.base.converteditor.DateConvertEditor;
import com.zz.bms.controller.base.converteditor.TimestampConvertEditor;
import com.zz.bms.core.Constant;
import com.zz.bms.core.db.entity.BaseEntity;
import com.zz.bms.core.db.entity.ILoginUserEntity;
import com.zz.bms.core.ui.Pages;
import com.zz.bms.shiro.utils.ShiroUtils;
import com.zz.bms.system.base.entity.TsUserEntity;
import com.zz.bms.util.base.spring.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基础控制器，其他控制器需集成此控制器获得initBinder自动转换的功能
 * 
 */
public abstract class BaseController {


    public final static String REMEMBER = Constant.REMEMBER;
    public final static String SESSION_USER = Constant.SESSION_USER;


    public Logger logger = Logger.getLogger(this.getClass());
    final  Pattern pattern= Pattern.compile ("^([a-z]{2})_([A-Z]{2})$");

    @Autowired(required = false)
    private SessionLocaleResolver localeResolver;
    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateConvertEditor());
        binder.registerCustomEditor(BigDecimal.class, new BigDecimalConvertEditor());
        binder.registerCustomEditor(Timestamp.class, new TimestampConvertEditor());
    }


    protected ILoginUserEntity getSessionUser(HttpServletRequest request){

        ILoginUserEntity loginUser = getSessionUser();
        if(loginUser == null){
            return (ILoginUserEntity)request.getSession().getAttribute(Constant.SESSION_USER);
        }
        return loginUser;
    }


    protected ILoginUserEntity getSessionUser(){
        try {
            ILoginUserEntity loginUser = (ILoginUserEntity) ShiroUtils.getSubject().getPrincipal();
            if (loginUser == null) {
                RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
                 if (requestAttributes != null) {
                     HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                     return (ILoginUserEntity)request.getSession().getAttribute(Constant.SESSION_USER);
                 }
            }
        }catch(Exception e){
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                return (ILoginUserEntity)request.getSession().getAttribute(Constant.SESSION_USER);
            }
        }
        return null;
    }

    protected Locale getLocale(HttpServletRequest req){
        return RequestContextUtils.getLocale (req);
    }




    public void setLocale(HttpServletRequest request,HttpServletResponse response,Locale locale){
        localeResolver.setLocale (request, response, locale);
        request.getSession ().setAttribute ("locale", locale);
    }
    public Locale parseLocale(String slocale){
        try {
            Matcher matcher = pattern.matcher (slocale);
            if (matcher.find ()) {
                String language = matcher.group (1);
                String country = matcher.group (2);
                return new Locale (language,country);
            } else {
                return null;
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace ();
            return null;
        }
    }


    protected String getMsg(String key,HttpServletRequest request,Object ... args){
        return SpringUtil.getMessage (getLocale (request) ,key, args );
    }




    /**
     * 设置cookie
     *
     * @param res
     * @param name      cookie名字
     * @param value     cookie值
     * @param maxAge    cookie生命周期 以秒为单位
     */
    protected void addCookie(HttpServletResponse res, String name, String value, int maxAge){
        try {
            value = URLEncoder.encode(value, "utf-8");
        }catch (Exception e){}
        Cookie cookie = new Cookie (name,value);
        cookie.setPath ("/");
        if (maxAge > 0) {cookie.setMaxAge (maxAge);}
        res.addCookie (cookie);
    }

    /**
     * 删除Cookie
     * @param req
     * @param res
     * @param name
     */
    public void removeCookie(HttpServletRequest req,HttpServletResponse res,String name) {
        Cookie cookie = this.getCookieByName(req,name);
        if(cookie != null) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            res.addCookie(cookie);
        }
    }

    /**
     * 根据名字获取cookie
     *
     * @param request
     * @param name      cookie名字
     * @return
     */
    protected Cookie getCookieByName(HttpServletRequest request,String name){
        Map<String, Cookie> cookieMap = ReadCookieMap (request);
        if (cookieMap.containsKey (name)) {
            Cookie cookie = (Cookie) cookieMap.get (name);
            return cookie;
        } else {
            return null;
        }
    }


    protected  String getCookieVal(Cookie cookie){
        if(cookie==null) {
            return "";
        }else {
            String val = cookie.getValue();

            try {
                val = URLDecoder.decode(val,"UTF-8");
            } catch (Exception e) {
            }
            return val;

        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    protected Map<String, Cookie> ReadCookieMap(HttpServletRequest request){
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies ();
        if (null != cookies) {
            for ( Cookie cookie : cookies ) {
                cookieMap.put (cookie.getName (), cookie);
            }
        }
        return cookieMap;
    }


    protected  String getServerPath(HttpServletRequest request){
        return request.getSession().getServletContext().getRealPath("/");
    }


    public String getFullURL(HttpServletRequest request) {

        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append("?");
            url.append(request.getQueryString());
        }
        try {
            return URLDecoder.decode(url.toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    /**
     * 处理分页信息， 设置页数 和每页记录数
     * @param pages
     * @param request
     */
    public void processPages(Pages pages , HttpServletRequest request){

    }

    /**
     * 判断是否存在对象
     * 如果数据库中没有查出对象， 表示不存在， 返回false
     * 如果数据库中有对象    id == entity.id  还表示不存在 ， 返回false,  否则返回 ture;
     * @param entity        根据条件查询出数据库里对象
     * @return
     */
    public boolean isEntityExist(BaseEntity entity){
        if(entity == null) {
            return false;
        }else{
            return true;
        }
    }









}
