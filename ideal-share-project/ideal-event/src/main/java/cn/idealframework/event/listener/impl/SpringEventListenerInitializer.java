/*
 * Copyright 2021 cn.idealframework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.idealframework.event.listener.impl;

import cn.idealframework.event.condition.EventCondition;
import cn.idealframework.event.listener.*;
import cn.idealframework.event.message.EventContext;
import cn.idealframework.event.message.EventMessage;
import cn.idealframework.json.JacksonUtils;
import cn.idealframework.lang.StringUtils;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/4/23
 */
@SuppressWarnings("DuplicatedCode")
@CommonsLog
public class SpringEventListenerInitializer
  implements EventListenerInitializer, ApplicationContextAware, SmartInitializingSingleton {
  private final List<EventListenerInitializedListener> listeners = new ArrayList<>();
  private ApplicationContext applicationContext;

  @Override
  public void initialize() {
    initMethodEventProcessor();
    initRemoteClassEventProcessor();
    initLocalClassEventProcessor();
    initIndiscriminateEventProcessor();
    for (EventListenerInitializedListener listener : listeners) {
      listener.completed();
    }
  }

  @Override
  public void addCompleteListener(EventListenerInitializedListener listener) {
    listeners.add(listener);
  }

  private void initMethodEventProcessor() {
    Map<String, Object> beanMapping = applicationContext
      .getBeansWithAnnotation(EventHandlerBean.class);
    Collection<Object> beans = beanMapping.values();
    for (Object bean : beans) {
      String className = bean.getClass().getName();
      Class<?> aClass = ClassUtils.getUserClass(bean);
      Method[] methods = aClass.getMethods();
      for (Method method : methods) {
        String methodName = method.getName();
        RemoteEventHandler remoteEventHandler = method.getAnnotation(RemoteEventHandler.class);
        if (remoteEventHandler != null) {
          String topic = remoteEventHandler.topic();
          if (StringUtils.isBlank(topic)) {
            log.error(className + "#" + methodName + " 未指定 topic");
            continue;
          }
          Parameter[] parameters = method.getParameters();
          if (parameters.length != 1) {
            log.error(className + "#" + methodName + " 参数列表长度不合法");
          }
          Parameter parameter = parameters[0];
          ParameterizedType parameterizedType = (ParameterizedType) parameter
            .getParameterizedType();
          Class<?> typeClass = (Class<?>) parameterizedType.getRawType();
          if (typeClass != EventContext.class) {
            log.error(className + "#" + methodName
              + " 入参必须是" + EventContext.class.getName() + "类型");
            continue;
          }
          Type[] typeArguments = parameterizedType.getActualTypeArguments();
          if (typeArguments.length == 0) {
            log.error(className + "#" + methodName + "{}#{} 入参缺少泛型");
            continue;
          }
          JavaType javaType = JacksonUtils.constructJavaType(typeArguments[0]);
          String condition = remoteEventHandler.condition();
          EventCondition eventCondition = EventCondition.parse(condition);
          String listenerName = remoteEventHandler.name();
          MethodRemoteEventProcessor handler
            = new MethodRemoteEventProcessor(listenerName, bean, method, javaType, eventCondition);
          RemoteEventProcessorFactory.register(topic, listenerName, handler);
        }
        LocalEventHandler localEventHandler = method.getAnnotation(LocalEventHandler.class);
        if (localEventHandler != null) {
          String topic = localEventHandler.topic();
          if (StringUtils.isBlank(topic)) {
            log.error(className + "#" + methodName + " 未指定 topic");
            continue;
          }
          Parameter[] parameters = method.getParameters();
          if (parameters.length != 1) {
            log.error(className + "#" + methodName + " 参数列表长度不合法");
          }
          Parameter parameter = parameters[0];
          ParameterizedType parameterizedType = (ParameterizedType) parameter
            .getParameterizedType();
          Class<?> typeClass = (Class<?>) parameterizedType.getRawType();
          if (typeClass != EventMessage.class) {
            log.error(className + "#" + methodName
              + " 入参必须是" + EventMessage.class.getName() + "类型");
            continue;
          }
          String name = className + "#" + methodName;
          String condition = localEventHandler.condition();
          EventCondition eventCondition = EventCondition.parse(condition);
          MethodLocalEventProcessor processor
            = new MethodLocalEventProcessor(name, bean, method, eventCondition);
          LocalEventProcessorFactory.register(topic, name, processor);
        }
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void initRemoteClassEventProcessor() {
    Map<String, RemoteEventListener> beans
      = applicationContext.getBeansOfType(RemoteEventListener.class);
    beans.forEach((k, bean) -> {
      String listenerName = bean.listenerName();
      String topic = bean.listeningTopic();
      Method method;
      Class<? extends RemoteEventListener> beanClass = bean.getClass();
      try {
        method = beanClass.getMethod("handleEvent", EventContext.class);
      } catch (NoSuchMethodException e) {
        log.error(beanClass.getName()
          + " no such method {void receiveEvent(DomainEventContext<T> eventMessage)}");
        return;
      }
      Parameter[] parameters = method.getParameters();
      Parameter parameter = parameters[0];
      ParameterizedType parameterizedType = (ParameterizedType) parameter
        .getParameterizedType();
      Type[] typeArguments = parameterizedType.getActualTypeArguments();
      JavaType javaType = JacksonUtils.constructJavaType(typeArguments[0]);
      EventCondition condition = bean.condition();
      if (condition == null) {
        condition = EventCondition.empty();
      }
      @SuppressWarnings("unchecked") ClassRemoteEventProcessor handler
        = new ClassRemoteEventProcessor(listenerName, javaType, condition, bean);
      RemoteEventProcessorFactory.register(topic, listenerName, handler);
    });
  }

  @SuppressWarnings("rawtypes")
  private void initLocalClassEventProcessor() {
    Map<String, LocalEventListener> beans
      = applicationContext.getBeansOfType(LocalEventListener.class);
    beans.forEach((k, bean) -> {
      String name = bean.getClass().getName();
      String topic = bean.listeningTopic();
      EventCondition condition = bean.condition();
      if (condition == null) {
        condition = EventCondition.empty();
      }
      ClassLocalEventProcessor processor = new ClassLocalEventProcessor(name, condition, bean);
      LocalEventProcessorFactory.register(topic, name, processor);
    });
  }

  private void initIndiscriminateEventProcessor() {
    Map<String, IndiscriminateEventListener> beans
      = applicationContext.getBeansOfType(IndiscriminateEventListener.class);
    beans.forEach((k, bean) -> {
      String listenerName = bean.listenerName();
      EventCondition condition = bean.condition();
      if (condition == null) {
        condition = EventCondition.empty();
      }
      IndiscriminateEventProcessor processor = new ClassIndiscriminateEventProcessor(listenerName, condition, bean);
      IndiscriminateEventProcessorFactory.register(listenerName, processor);
    });
  }

  @Override
  public void setApplicationContext(@Nonnull ApplicationContext applicationContext)
    throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterSingletonsInstantiated() {
    this.initialize();
  }
}
