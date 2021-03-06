package com.zz.bms.system.base.query.impl;

import com.zz.bms.core.db.mybatis.query.QueryImpl;
import com.zz.bms.system.base.entity.VsUserEntity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用于链式查询
 * @author Administrator
 * 主键 外键  eq ne in notin
 * 字典 枚举 eq ne in notin
 * 数字 eq ne gt ge lt le
 * 日期 同上
 * 普通字符串 eq ne in notin like notlike
 * 参考 EnumSearchType
 */
public abstract class VsUserAbstractQueryImpl<PK extends Serializable> extends QueryImpl<VsUserEntity,PK> {


    protected PK id ;
    protected PK id_NE ;


    protected String userName;
    protected String userName_NE;
    protected String userName_LIKE;
    protected String userName_NOTLIKE;

    protected String loginName;
    protected String loginName_NE;
    protected String loginName_LIKE;
    protected String loginName_NOTLIKE;

    protected String status;
    protected String status_NE;

    protected PK leadId;
    protected PK leadId_NE;

    protected String phone;
    protected String phone_NE;
    protected String phone_LIKE;
    protected String phone_NOTLIKE;

    protected String email;
    protected String email_NE;
    protected String email_LIKE;
    protected String email_NOTLIKE;

    protected String openId;
    protected String openId_NE;

    protected String unionId;
    protected String unionId_NE;

    protected String systemAdmin;
    protected String systemAdmin_NE;

    protected PK depId;
    protected PK depId_NE;

    protected PK organId;
    protected PK organIdAdmin_NE;


    protected Timestamp createTime;
    protected Timestamp createTime_NE;
    protected Timestamp createTime_GT;
    protected Timestamp createTime_GE;
    protected Timestamp createTime_LT;
    protected Timestamp createTime_LE;

    protected PK createUserId;
    protected PK createUserId_NE;

}
