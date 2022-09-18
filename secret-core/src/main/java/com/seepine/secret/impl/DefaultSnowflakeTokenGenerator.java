package com.seepine.secret.impl;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.seepine.secret.interfaces.TokenGenerator;
import com.seepine.secret.util.RedisUtil;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 雪花id token生成
 *
 * @author seepine
 */
public class DefaultSnowflakeTokenGenerator implements TokenGenerator {
  private static final String SNOWFLAKE = "snowflake_worker:";
  private Integer workerId = null;
  /** 默认值6，限制每毫秒生成的ID个数。若生成速度超过5万个/秒，建议加大 SeqBitLength 到 10。 */
  private static final byte MAX_SEQ_BIT_LENGTH = 10;
  /** 默认值6，限定 WorkerId 最大值为2^6-1，即默认最多支持64个节点。 */
  private static final byte MAX_WORKER_ID_BIT_LENGTH = 10;

  public DefaultSnowflakeTokenGenerator() {
    RedisUtil.sync(SNOWFLAKE, this::init);
  }

  private void init() {
    int maxWorkerNum = (int) Math.pow(2, MAX_WORKER_ID_BIT_LENGTH);
    for (int i = 0; i < maxWorkerNum; i++) {
      if (!RedisUtil.isExists(SNOWFLAKE + i)) {
        RedisUtil.set(SNOWFLAKE + i, "working", 40, TimeUnit.SECONDS);
        workerId = i;
        break;
      }
    }
    if (workerId == null) {
      System.exit(1);
      return;
    }
    // 创建 IdGeneratorOptions 对象，可在构造函数中输入 WorkerId：[0,1023]
    IdGeneratorOptions options = new IdGeneratorOptions(workerId.shortValue());
    options.WorkerIdBitLength = MAX_WORKER_ID_BIT_LENGTH;
    options.SeqBitLength = MAX_SEQ_BIT_LENGTH;
    YitIdHelper.setIdGenerator(options);
    // 开启守护线程，30秒刷新一次过期时间
    new ScheduledThreadPoolExecutor(
            1,
            runnable -> {
              Thread thread = new Thread(runnable, "secret-snowflake-worker-daemon");
              thread.setDaemon(true);
              return thread;
            })
        .scheduleAtFixedRate(
            () -> RedisUtil.set(SNOWFLAKE + workerId, "working", 40, TimeUnit.SECONDS),
            30,
            30,
            TimeUnit.SECONDS);
  }

  @Override
  public String gen() {
    return String.valueOf(YitIdHelper.nextId());
  }
}
