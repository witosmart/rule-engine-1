package com.zjb.ruleengine.core.rule;

import com.zjb.ruleengine.core.Context;
import com.zjb.ruleengine.core.Execute;
import com.zjb.ruleengine.core.condition.AbstractCondition;
import com.zjb.ruleengine.core.config.PostProcessor;
import com.zjb.ruleengine.core.config.PreProcessor;
import com.zjb.ruleengine.core.Weight;
import com.zjb.ruleengine.core.enums.RuleResultEnum;
import com.zjb.ruleengine.core.value.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author 赵静波 <zjbhnay@163.com>
 */
public abstract class AbstractRule implements Execute, Weight, Serializable {

    private static final Logger log = LogManager.getLogger();
    private static final long serialVersionUID = 6727628276386208126L;

    /**
     * 规则id
     */
    private String id;
    /**
     * 规则条件
     */
    private AbstractCondition condition;
    /**
     * 动作，如果condition==true,则执行actionValue并返回
     */
    private Value action;
    /**
     * 后置处理器
     */
    private List<PostProcessor> postProcessors;
    /**
     * 前置处理器
     */
    private List<PreProcessor> preProcessors;


    public AbstractRule(String id, AbstractCondition condition, Value action) {
        this.id = id;
        this.condition = condition;
        this.action = action;
        this.build();
    }

    /**
     * 增加后置处理器
     * @param postProcessor
     */
    public void addPostProcessor(PostProcessor postProcessor) {
        if (postProcessors == null) {
            postProcessors = new ArrayList<>(8);
        }
        postProcessors.add(postProcessor);
    }

    /**
     * 增加前置处理器
     * @param preProcessor
     */
    public void addPreProcessor(PreProcessor preProcessor) {
        if (preProcessors == null) {
            preProcessors = new ArrayList<>(8);
        }
        preProcessors.add(preProcessor);
    }
    @Override
    public Object execute(Context context) {
        log.debug("开始执行规则：{}", id);
        Object result;
        boolean conditionResult = executeCondition(context);
        postProcessors.forEach(postProcessor ->
                postProcessor.postProcessorBeforeActionExecute(this, context));
        if (conditionResult) {
            result = action.getValue(context);
        }else{
            result = RuleResultEnum.NULL;
        }
        for (PostProcessor postProcessor : postProcessors) {
            result = postProcessor.afterProcessorBeforeActionExecute(this, context, result);
        }
        log.debug("规则执行结果：{}", result);
        return result;
    }

    public abstract Boolean executeCondition(Context context);

    public AbstractCondition getCondition() {
        return condition;
    }

    @Override
    public int getWeight() {
        return this.condition.getWeight();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    public Value getAction() {
        return action;
    }


    /**
     * 编译规则
     */
    public abstract void build();

}