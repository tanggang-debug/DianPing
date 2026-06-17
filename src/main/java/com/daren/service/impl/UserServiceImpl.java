package com.daren.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daren.dto.LoginFormDTO;
import com.daren.dto.Result;
import com.daren.entity.User;
import com.daren.mapper.UserMapper;
import com.daren.service.IUserService;
import com.daren.utils.RegexUtils;
import com.daren.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.daren.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone, HttpSession session) {
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号码不合法");
        }
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set("user:code:" + phone, code);
        log.debug("发送短信验证码成功!!!验证码：{}", code);
        stringRedisTemplate.expire("user:code:" + phone, 2, TimeUnit.MINUTES);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        String phone = loginForm.getPhone();
        String code = stringRedisTemplate.opsForValue().get("user:code:" + loginForm.getPhone());
        if (code == null || !code.equals(loginForm.getCode())) {
            return Result.fail("验证码不一致");
        }
        User user = query().eq("phone", phone).one();
        if (user == null) {
            user = createUserWithPhone(phone);
        }
        String token = RandomUtil.randomString(10);
        Map<String, Object> userMap =
                BeanUtil.beanToMap(user, new HashMap<>(), CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue == null ? "" : fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll("user:login:" + token, userMap);
//        stringRedisTemplate.expire("user:login:" + token, 240, TimeUnit.MINUTES);
        return Result.ok(token);
    }

    @Override
    public Result sign() {
        Long userId = UserHolder.getUser().getId();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern(":yyyy:MM:"));
        String key = "user:sign:" + userId + date;
        stringRedisTemplate.opsForValue().setBit(key, LocalDate.now().getDayOfMonth() - 1, true);
//        stringRedisTemplate.opsForValue().setBit(key, 1000, true);
        return Result.ok();
    }

    @Override
    public Result signCount() {
        Long userId = UserHolder.getUser().getId();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern(":yyyy:MM:"));
        String key = "user:sign:" + userId + date;
        int today = LocalDate.now().getDayOfMonth();
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(today)).valueAt(0);
        List<Long> results = stringRedisTemplate.opsForValue().bitField(key, bitFieldSubCommands);
        if (results == null || results.isEmpty()) {
            return Result.ok(0);
        }
        log.debug("结果：{}", results.get(0));
        int maxLen = 0;
        int count = 0;
        long result = results.get(0);
        while (result != 0) {
            if ((result & 1) == 1) {
                count++;
            } else {
                maxLen = Math.max(maxLen, count);
                count = 0;
            }
            result >>= 1;
        }
        maxLen = Math.max(maxLen, count);
        return Result.ok(maxLen);
    }

    @Transactional(rollbackFor = Exception.class)
    public User createUserWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        save(user);
        return user;
    }
}
