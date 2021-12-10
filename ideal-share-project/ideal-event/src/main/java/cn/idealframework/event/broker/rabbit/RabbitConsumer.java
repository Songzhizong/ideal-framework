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
package cn.idealframework.event.broker.rabbit;

import cn.idealframework.event.listener.EventDeliverer;
import cn.idealframework.event.message.impl.SimpleDelivererEvent;
import cn.idealframework.json.JsonUtils;
import cn.idealframework.json.TypeReference;
import cn.idealframework.lang.StringUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

/**
 * @author 宋志宗 on 2021/4/24
 */
@CommonsLog
public class RabbitConsumer implements ChannelAwareMessageListener {
  private static final TypeReference<SimpleDelivererEvent> MESSAGE_TYPE_REFERENCE
    = new TypeReference<SimpleDelivererEvent>() {
  };
  private final String queuePrefix;
  private final EventDeliverer eventDeliverer;
  private final boolean enableLocalModel;

  public RabbitConsumer(@Nonnull String queuePrefix, EventDeliverer eventDeliverer, boolean enableLocalModel) {
    this.enableLocalModel = enableLocalModel;
    if (!queuePrefix.endsWith(".")) {
      queuePrefix = queuePrefix + ".";
    }
    this.queuePrefix = queuePrefix;
    this.eventDeliverer = eventDeliverer;
  }

  @Override
  public void onMessage(@Nonnull Message message, @Nullable Channel channel) throws Exception {
    if (channel == null) {
      log.warn("channel is null");
      return;
    }
    long deliveryTag = message.getMessageProperties().getDeliveryTag();
    String consumerQueue = message.getMessageProperties().getConsumerQueue();
    String listenerName = RabbitEventUtils.getListenerNameByQueueName(queuePrefix, consumerQueue, enableLocalModel);
    byte[] body = message.getBody();
    String value = new String(body, StandardCharsets.UTF_8);
    if (StringUtils.isBlank(value) || value.charAt(0) != '{') {
      log.warn("消息处理失败, 非json结构");
      return;
    }
    log.debug("Queue : " + consumerQueue + " message value: " + value);
    SimpleDelivererEvent domainEvent;
    try {
      domainEvent = JsonUtils.parse(value, MESSAGE_TYPE_REFERENCE);
    } catch (Exception e) {
      log.warn("反序列化消息出现异常: " + e.getClass().getName() + " " + e.getMessage());
      return;
    }
    domainEvent.setListenerName(listenerName);
    try {
      eventDeliverer.deliver(domainEvent);
      channel.basicAck(deliveryTag, false);
    } catch (Exception e) {
      e.printStackTrace();
      channel.basicNack(deliveryTag, false, true);
    }
  }
}
