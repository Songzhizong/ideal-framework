```yaml
ideal:
  event:
    broker:
      type: rabbit
      rabbit:
        exchange: 'event.exchange'
        queue-prefix: 'event.queue'
        prefetch-count: 10
        concurrent-consumers: 16
        max-concurrent-consumers: 64
      kafka:
        client-id: client-1
        bootstrap-servers: 127.0.0.1:9001
        producer:
          acks: 1
          batch-size: 16KB
          linger-ms: 20ms
          compression-type: none
          buffer-memory: 32MB
          retries: 2147483647
          default-event-topic: ideal_event
          topic-mapping:
            test: test1
        receive:
          topics: ideal_event,test1
          core-pool-size: 32
          maximum-pool-size: 54
    publisher:
      persistent:
        enabled: false
      transaction:
        enabled: true
    idempotent:
      type: redis
      redis:
        key-prefix: 'ideal:event:idempotent'
        key-timeout: 10m
      memory:
        maximum-size: 10000
        timeout: 10m
```

