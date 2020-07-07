package com.lww.mina.session;

import com.lww.mina.util.Const;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.mina.core.session.IoSession;

/**
 * @author lww
 * @date 2020-07-06 23:21
 */
public class SessionManager {

    /**
     * 存放session的线程安全的map集合
     */
    private static ConcurrentHashMap<String, IoSession> sessions = new ConcurrentHashMap<>();

    /**
     * 线程安全的自增类，用于统计连接数
     */
    private static final AtomicInteger CONNECTIONS_COUNTER = new AtomicInteger(0);

    /**
     * 添加session
     */
    public static void addSession(String account, IoSession session) {
        sessions.put(account, session);
        CONNECTIONS_COUNTER.incrementAndGet();
    }

    /**
     * 获取session
     */
    public static IoSession getSession(String key) {
        return sessions.get(key);
    }

    /**
     * 替换session，通过账号
     */
    public static void replaceSession(String key, IoSession session) {
        sessions.put(key, session);
    }

    /**
     * 移除session通过账号
     */
    public static void removeSession(String key) {
        sessions.remove(key);
        CONNECTIONS_COUNTER.decrementAndGet();
    }

    /**
     * 移除session通过session
     */
    public static void removeSession(IoSession session) {
        String key = (String) session.getAttribute(Const.SESSION_KEY);
        removeSession(key);
    }

    public static ConcurrentHashMap<String, IoSession> getSessions() {
        return sessions;
    }
}
