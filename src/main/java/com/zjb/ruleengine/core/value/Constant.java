package com.zjb.ruleengine.core.value;

import com.google.common.collect.Sets;
import com.zjb.ruleengine.core.Context;
import com.zjb.ruleengine.core.enums.DataTypeEnum;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Objects;


/**
 * 变量
 *
 * @author zjb
 * @Date 07/11/2019
 **/
public class Constant extends Value {

    private static final Logger log = LogManager.getLogger();
    /**
     * 常量值
     */
    private Object value;

    /**
     * 数据类型
     */
    private DataTypeEnum dataType;

    public Constant(DataTypeEnum dataType, Object value) {
        super();
        Validate.notNull(dataType);
        Validate.notNull(value);
        this.value = value;
        this.dataType = dataType;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public Collection<Element> collectParameter() {
        return Sets.newHashSet();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Constant)) {
            return false;
        }
        Constant constant = (Constant) other;
        if (this.dataType != constant.dataType) {
            return false;
        }
        Object curValue = dataConversion(this.value, dataType);
        Object constantValue = dataConversion(constant.getValue(), dataType);
        return curValue.equals(constantValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dataType,this.value);
    }

    /**
     * constant类型
     *
     * @param context 上下文
     * @return List 常量提前被加载，故直接返回值
     */
    @Override
    public Object getValue(Context context) {
        return dataConversion(value, dataType);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}