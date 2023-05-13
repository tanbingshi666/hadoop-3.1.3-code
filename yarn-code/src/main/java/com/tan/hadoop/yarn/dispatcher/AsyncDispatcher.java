package com.tan.hadoop.yarn.dispatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 事件分发器
 */
public class AsyncDispatcher {

    // 事件队列
    private final BlockingQueue<Event> eventQueue;

    // 通用 EventHandler
    private final EventHandler<Event> handlerInstance = new GenericEventHandler();

    // 事件和事件处理器关系注册表
    protected final Map<Class<? extends Enum>, EventHandler> eventDispatchers;

    // 队列消费线程
    private final Thread eventHandlingThread;

    // 停止标志
    private volatile boolean stopped = false;

    public AsyncDispatcher() {

        this.eventQueue = new LinkedBlockingQueue<>();
        this.eventDispatchers = new HashMap<>();

        // 启动线程
        eventHandlingThread = new Thread(createThread());
        eventHandlingThread.start();
    }

    Runnable createThread() {
        return () -> {
            while (!stopped) {
                // 获取事件
                Event event;
                try {
                    event = eventQueue.take();
                } catch (InterruptedException ie) {
                    return;
                }
                // 执行分发
                try {
                    dispatch(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void dispatch(Event event) throws Exception {
        Class<? extends Enum> type = event.getType().getDeclaringClass();
        // 1 读取注册表，获取 event 对应的 eventHandler
        EventHandler handler = eventDispatchers.get(type);
        if (handler != null) {
            // 2 调用 eventHandler 的 handle 方法来执行事件处理
            handler.handle(event);
        } else {
            throw new Exception("No handler for registered for " + type);
        }
    }

    // Event 和 EventHandler 注册
    @SuppressWarnings("unchecked")
    public void register(Class<? extends Enum> eventType, EventHandler handler) {
        EventHandler<Event> registeredHandler = (EventHandler<Event>) eventDispatchers.get(eventType);
        if (registeredHandler == null) {
            eventDispatchers.put(eventType, handler);
        } else {
            System.out.println("注册的事件已存在");
        }
    }

    public EventHandler<Event> getEventHandler() {
        return handlerInstance;
    }

    // 通用处理器
    class GenericEventHandler implements EventHandler<Event> {
        @Override
        public void handle(Event event) {
            try {
                eventQueue.put(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // AsyncDispatcher 停止工作
    void stop() {
        this.stopped = true;
    }

    public static void main(String[] args) throws InterruptedException {

        // 1 构建 AsyncDispatcher 事件分发器
        AsyncDispatcher asyncDispatcher = new AsyncDispatcher();

        // 2 注册事件和事件处理器
        SportEventHandler sportEventHandler = new SportEventHandler();
        LearnEventHandler learnEventHandler = new LearnEventHandler();
        asyncDispatcher.register(LearnEventType.class, learnEventHandler);
        asyncDispatcher.register(SportEventType.class, sportEventHandler);

        // 3 休息 3s 等待线程启动成功
        Thread.sleep(3000);

        // 4 开始提交事件
        asyncDispatcher.getEventHandler().handle(new SportEvent(SportEventType.RUN));
        Thread.sleep(1000);

        asyncDispatcher.getEventHandler().handle(new SportEvent(SportEventType.BIKE));
        Thread.sleep(1000);

        asyncDispatcher.getEventHandler().handle(new LearnEvent(LearnEventType.READ));
        Thread.sleep(1000);

        asyncDispatcher.getEventHandler().handle(new LearnEvent(LearnEventType.LISTEN));
        Thread.sleep(1000);

        asyncDispatcher.getEventHandler().handle(new LearnEvent(LearnEventType.SPEAK));
        Thread.sleep(1000);

        // 5 测试结束关闭线程 (线程处于阻塞关闭不了)
        // asyncDispatcher.stop();
    }
}

// 事件抽象
interface Event<TYPE extends Enum<TYPE>> {

    TYPE getType();

    long getTimestamp();

    String toString();
}

// Event 的抽象实现
abstract class AbstractEvent<TYPE extends Enum<TYPE>> implements Event<TYPE> {
    private final TYPE type;
    private final long timestamp;

    public AbstractEvent(TYPE type) {
        this.type = type;
        timestamp = -1L;
    }

    public AbstractEvent(TYPE type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public TYPE getType() {
        return type;
    }

    @Override
    public String toString() {
        return "EventType: " + getType();
    }
}

// 事件处理器抽象
interface EventHandler<T extends Event> {
    void handle(T event);
}

// Sport 事件类实现
class SportEvent extends AbstractEvent<SportEventType> {
    public SportEvent(SportEventType type) {
        super(type);
    }
}

// Sport 事件类型
enum SportEventType {
    RUN,
    BIKE,
    SWIM,
    CLIMB;
}

// Sport 事件对应的处理器
class SportEventHandler implements EventHandler<SportEvent> {

    @Override
    public void handle(SportEvent event) {
        switch (event.getType()) {
            case RUN:
                System.out.println("RUN...");
                break;
            case BIKE:
                System.out.println("BIKE...");
                break;
            case SWIM:
                System.out.println("SWIM...");
                break;
            case CLIMB:
                System.out.println("CLIMB...");
                break;
            default:
                break;
        }
    }
}

// Learn 事件实现
class LearnEvent extends AbstractEvent<LearnEventType> {
    public LearnEvent(LearnEventType type) {
        super(type);
    }
}

// Learn 事件类型
enum LearnEventType {
    READ,
    LISTEN,
    WRITE,
    SPEAK
}

// Learn 事件对应的事件处理器
class LearnEventHandler implements EventHandler<LearnEvent> {

    @Override
    public void handle(LearnEvent event) {
        switch (event.getType()) {
            case READ:
                System.out.println("READ...");
                break;
            case LISTEN:
                System.out.println("LISTEN...");
                break;
            case WRITE:
                System.out.println("WRITE...");
                break;
            case SPEAK:
                System.out.println("SPEAK...");
                break;
            default:
                break;
        }
    }
}

