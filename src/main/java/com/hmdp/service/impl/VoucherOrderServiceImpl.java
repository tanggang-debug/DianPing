package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.IVoucherOrderService;
import com.hmdp.utils.FastRedisScript;
import com.hmdp.utils.SnowflakeIdWorker;
import com.hmdp.utils.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {
    private static final Logger log = LoggerFactory.getLogger(VoucherOrderServiceImpl.class);

    private final StringRedisTemplate stringRedisTemplate;
    //    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
//    private final BlockingQueue<VoucherOrder> blockingQueue = new ArrayBlockingQueue<>(1024 * 1024);
    @Autowired
    FastRedisScript<Long> fastRedisScript;


    //    private final RocketMQTemplate rocketMQTemplate;
    private final SnowflakeIdWorker snowflakeIdWorker;

//    private final RabbitTemplate rabbitTemplate;

//    @PostConstruct
//    private void init() {
//        executorService.submit(() -> {
//            while (true) {
//                VoucherOrder voucherOrder = blockingQueue.take();
//            }
//        });
//    }

    public VoucherOrderServiceImpl(StringRedisTemplate stringRedisTemplate, SnowflakeIdWorker snowflakeIdWorker) {
        this.stringRedisTemplate = stringRedisTemplate;
//        this.rabbitTemplate = rabbitTemplate;
//        this.rocketMQTemplate = rocketMQTemplate;
        this.snowflakeIdWorker = snowflakeIdWorker;
    }

    @Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        Long orderId = snowflakeIdWorker.nextId();
        String stockKey = "seckill:voucher:" + voucherId;
        String orderKey = "seckill:order:" + voucherId;
        Long ans = stringRedisTemplate.execute(fastRedisScript, Arrays.asList(stockKey, orderKey), userId.toString());
        if (ans != null && ans != 0) {
            return Result.fail(ans == 1 ? "库存不足" : "请勿重复下单");
        }
        VoucherOrder voucherOrder = new VoucherOrder();
        // 2.3.订单id
        voucherOrder.setId(orderId);
        // 2.4.用户id
        voucherOrder.setUserId(userId);
        // 2.5.代金券id
        voucherOrder.setVoucherId(voucherId);
        // 2.6.放入阻塞队列
/*        blockingQueue.add(voucherOrder);
        rabbitTemplate.convertAndSend("order.create.queue", voucherOrder);
        Message<String> message = MessageBuilder.withPayload(JSONUtil.toJsonStr(voucherOrder)).build();
        rocketMQTemplate.send("orderCreateTopic", message);*/
        return Result.ok(orderId);
    }

//    @Override
//    public Result seckillVoucher(Long voucherId) {
//        SeckillVoucher seckillVoucher = seckillVoucherServiceImpl.getById(voucherId);
//        if (LocalDateTime.now().isBefore(seckillVoucher.getBeginTime()) || LocalDateTime.now().isAfter(seckillVoucher.getEndTime())) {
//            return Result.fail("秒杀尚未开始或已结束");
//        }
//        return createVoucherOrder(voucherId);
//    }

/*
    public Result createVoucherOrder(Long voucherId) {
//        RedisLock redisLock = new RedisLock(stringRedisTemplate);
        RLock redisLock = redissonClient.getLock("lock:order:" + UserHolder.getUser());
        boolean ans = redisLock.tryLock();
        if (!ans) {
            return Result.fail("请勿重复下单");
        }
        try {
            Long userId = UserHolder.getUser().getId();
            return transactionTemplate.execute(status -> {
                Integer userCount = query().eq("user_id", userId).count();
                if (userCount >= 1) {
                    return Result.fail("您已抢购过一次");
                }
                boolean success = seckillVoucherServiceImpl.update().setSql("stock=stock-1").eq("voucher_id", voucherId).gt("stock", 0).update();
                if (!success) {
                    return Result.fail("库存不足");
                }
                VoucherOrder voucherOrder = new VoucherOrder();
                voucherOrder.setUserId(userId);
                voucherOrder.setVoucherId(voucherId);
                voucherOrder.setId(redisIdWorker.nextId());
                save(voucherOrder);
//                throw new RuntimeException();
                return Result.ok("秒杀成功");
            });
        } finally {
            redisLock.unlock();
        }
    }

    public void handleVoucherOrder(VoucherOrder voucherOrder) {
//        RedisLock redisLock = new RedisLock(stringRedisTemplate);
        RLock redisLock = redissonClient.getLock("lock:order:" + voucherOrder.getUserId());
        boolean ans = redisLock.tryLock();
        if (!ans) {
            return;
        }
        try {
            Long voucherId = voucherOrder.getVoucherId();
            Long userId = voucherOrder.getUserId();
            transactionTemplate.execute(status -> {
                Integer userCount = query().eq("user_id", userId).count();
                if (userCount >= 1) {
                    return null;
                }
                boolean success = seckillVoucherServiceImpl.update().setSql("stock=stock-1").eq("voucher_id", voucherId).gt("stock", 0).update();
                if (!success) {
                    return null;
                }
                save(voucherOrder);
//                throw new RuntimeException();
                return null;
            });
        } finally {
            redisLock.unlock();
        }
    }
*/
}
