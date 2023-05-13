package com.tan.hadoop.yarn.state.machine;

import java.util.*;

/**
 * 人一生可能出现的正常和意外情况下，导致人的状态发生改变
 */
enum ManEventType {
    GROWUP,             // 正常长大
    SICK,               // 生病
    REJUVENATION,       // 返老还童
    INCURABLE_DISEASE   // 绝症
}

/**
 * 人一生的状态
 */
enum ManState {
    BIRTH(1),               // 出生
    BABY(2),                // 婴儿
    CHILD(3),               // 儿童
    YOUNG(4),               // 少年
    YOUTH(5),               // 青年
    ADULT(6),               // 成年
    OLD(7),                 // 老年
    DEATH(8);               // 死亡

    int value;

    ManState(int value) {
        this.value = value;
    }

    static ManState valueOf(int value) throws Exception {
        switch (value) {
            case 1:
                return BIRTH;
            case 2:
                return BABY;
            case 3:
                return CHILD;
            case 4:
                return YOUNG;
            case 5:
                return YOUTH;
            case 6:
                return ADULT;
            case 7:
                return OLD;
            case 8:
                return DEATH;
            default:
                throw new Exception("异常状态值");
        }
    }

    static List<ManState> getStateList() {
        return new ArrayList<ManState>(Arrays.asList(ManState.values()));
    }
}

/**
 * 状态机模拟实现，用来管理一个 人类对象实例的 一生状态
 */
public class ManStateMachine implements EventHandler<ManEvent> {

    // 状态机实体对象
    private final StateMachine<ManState, ManEventType, ManEvent> stateMachine;

    // 状态机工厂实例
    // ManStateMachine, ManState, ManEventType, ManEvent
    private static final StateMachineFactory<
            ManStateMachine, ManState, ManEventType, ManEvent> stateMachineFactory;

    static {
        stateMachineFactory = new StateMachineFactory<>(ManState.BIRTH);
    }

    // addTransition = 注册 状态转移四元组
    // 1 状态机此时的状态，转移之前的状态
    // 2 状态之后得到的新状态
    // 3 发生的事件
    // 4 状态转移器
    // 某个状态机实体对象此时状态是 A, 发生了事件 C, 然后调用 D 来执行状态转移得到状态 B
    // 状态转移四元组：(A, B, C, D)
    {
        // 长大
        stateMachineFactory.addTransition(ManState.BIRTH, ManState.BABY, ManEventType.GROWUP, new GrowupTransition());
        stateMachineFactory.addTransition(ManState.BABY, ManState.CHILD, ManEventType.GROWUP, new GrowupTransition());
        stateMachineFactory.addTransition(ManState.CHILD, ManState.YOUNG, ManEventType.GROWUP, new GrowupTransition());
        stateMachineFactory.addTransition(ManState.YOUNG, ManState.YOUTH, ManEventType.GROWUP, new GrowupTransition());
        stateMachineFactory.addTransition(ManState.YOUTH, ManState.ADULT, ManEventType.GROWUP, new GrowupTransition());
        stateMachineFactory.addTransition(ManState.ADULT, ManState.OLD, ManEventType.GROWUP, new GrowupTransition());
        stateMachineFactory.addTransition(ManState.OLD, ManState.DEATH, ManEventType.GROWUP, new GrowupTransition());

        // 返老还童，情况有点多, 这里没有补充完整，如果报空指针异常，自己到这里来加情况
        stateMachineFactory.addTransition(ManState.ADULT, ManState.BABY, ManEventType.REJUVENATION, new RejuvenationTransition());
        stateMachineFactory.addTransition(ManState.YOUTH, ManState.YOUNG, ManEventType.REJUVENATION, new RejuvenationTransition());
        stateMachineFactory.addTransition(ManState.OLD, ManState.CHILD, ManEventType.REJUVENATION, new RejuvenationTransition());
        stateMachineFactory.addTransition(ManState.YOUNG, ManState.BABY, ManEventType.REJUVENATION, new RejuvenationTransition());
        stateMachineFactory.addTransition(ManState.YOUNG, ManState.CHILD, ManEventType.REJUVENATION, new RejuvenationTransition());
        stateMachineFactory.addTransition(ManState.YOUNG, ManState.BIRTH, ManEventType.REJUVENATION, new RejuvenationTransition());

        // 生病情况
        stateMachineFactory.addTransition(ManState.BABY, ManState.BABY, ManEventType.SICK, new SickTransition());
        stateMachineFactory.addTransition(ManState.CHILD, ManState.CHILD, ManEventType.SICK, new SickTransition());
        stateMachineFactory.addTransition(ManState.YOUNG, ManState.YOUNG, ManEventType.SICK, new SickTransition());
        stateMachineFactory.addTransition(ManState.YOUTH, ManState.YOUTH, ManEventType.SICK, new SickTransition());
        stateMachineFactory.addTransition(ManState.ADULT, ManState.ADULT, ManEventType.SICK, new SickTransition());
        stateMachineFactory.addTransition(ManState.OLD, ManState.OLD, ManEventType.SICK, new SickTransition());

        // 绝症
        stateMachineFactory.addTransition(ManState.BIRTH, ManState.DEATH, ManEventType.INCURABLE_DISEASE,
                new IncurableDiseaseTransition()
        );
        stateMachineFactory.addTransition(ManState.BABY, ManState.DEATH, ManEventType.INCURABLE_DISEASE,
                new IncurableDiseaseTransition()
        );
        stateMachineFactory.addTransition(ManState.CHILD, ManState.DEATH, ManEventType.INCURABLE_DISEASE,
                new IncurableDiseaseTransition()
        );
        stateMachineFactory.addTransition(ManState.YOUNG, ManState.DEATH, ManEventType.INCURABLE_DISEASE,
                new IncurableDiseaseTransition()
        );
        stateMachineFactory.addTransition(ManState.YOUTH, ManState.DEATH, ManEventType.INCURABLE_DISEASE,
                new IncurableDiseaseTransition()
        );
        stateMachineFactory.addTransition(ManState.ADULT, ManState.DEATH, ManEventType.INCURABLE_DISEASE,
                new IncurableDiseaseTransition()
        );
        stateMachineFactory.addTransition(ManState.OLD, ManState.DEATH, ManEventType.INCURABLE_DISEASE,
                new IncurableDiseaseTransition()
        );
    }

    @Override
    public void handle(ManEvent event) {
        Class<? extends ManEventType> type = event.getType().getDeclaringClass();
        try {
            doTransition(event.getType(), event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ManStateMachine() {
        // 从状态机工厂创建具体的状态机
        this.stateMachine = stateMachineFactory.make(this);
    }

    public ManState doTransition(ManEventType eventType, ManEvent event) throws Exception {
        // 由具体的状态机进行转换状态
        return stateMachine.doTransition(eventType, event);
    }

    public static void main(String[] args) throws Exception {

        // 创建一个状态机实体对象，内部有一个状态机成员变量
        ManStateMachine manStateMachine = new ManStateMachine();
        ManState manState = null;

        // A = ManState.BIRTH， C = ManEventType.GROWUP -> B = BABY
        manState = manStateMachine.doTransition(ManEventType.GROWUP, new ManEvent(ManEventType.GROWUP));
        // B = CHILD
        manState = manStateMachine.doTransition(ManEventType.GROWUP, new ManEvent(ManEventType.GROWUP));

        // 改造之后的结果： 上下两句代码等价的，区别在于
        // 第一种方式： 自己做测试方便，直接调用方法
        // 第二种方式： 常规方式，正常方式，YARN 实现方式，当其他组件提交一个事件过来的时候，有可能就是让这个 EventHandler 来处理
        // 只不过这个 EventHandler 同时还是一个 StateMachine
        // manState = manStateMachine.doTransition(ManEventType.GROWUP, new ManEvent(ManEventType.GROWUP));
        // B = YOUNG
        manStateMachine.handle(new ManEvent(ManEventType.GROWUP));

        // B = YOUNG
        manState = manStateMachine.doTransition(ManEventType.SICK, new ManEvent(ManEventType.SICK));

        // A = YOUNG, C = REJUVENATION, B = BABY
        manState = manStateMachine.doTransition(ManEventType.REJUVENATION, new ManEvent(ManEventType.REJUVENATION));
        // A = BABY, C = INCURABLE_DISEASE, B = DEATH
        manState = manStateMachine.doTransition(ManEventType.INCURABLE_DISEASE, new ManEvent(ManEventType.INCURABLE_DISEASE));

        /**
         * 这个地方的测试，不是特别完美！
         *  改造：
         *  1 类似于 YARN ，把 ManStateMachine 做成 EventHandler
         *  2 在 ManStateMachine 的 handle 方法的实现中，调用
         *  manStateMachine.doTransition(ManEventType.GROWUP, new ManEvent(ManEventType.GROWUP));
         *  这种代码，来让状态机来执行响应处理
         *  3 最后的效果就变成了，如果其他的组件提交了事件给异步事件分发器 AsyncDispatcher,
         *  AsyncDispatcher 根据事件类型找到 EventHandler 可能就是找到一个 StateMachine
         */
    }
}

/**
 * 事件处理器抽象
 */
interface EventHandler<T extends Event> {
    void handle(T event);
}

/**
 * 事件接口
 */
interface Event<TYPE extends Enum<TYPE>> {
    TYPE getType();

    long getTimestamp();

    String toString();
}

/**
 * 事件抽象
 */
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

/**
 * 事件实现类
 */
class ManEvent extends AbstractEvent<ManEventType> {
    public ManEvent(ManEventType type) {
        super(type);
    }
}

/**
 * 状态机顶级抽象定义
 */
interface StateMachine<STATE extends Enum<STATE>, EVENTTYPE extends Enum<EVENTTYPE>, EVENT> {

    public STATE getCurrentState();

    public STATE doTransition(EVENTTYPE eventType, EVENT event) throws Exception;
}

/**
 * 转换器抽象定义
 */
interface Transition<OPERAND, STATE extends Enum<STATE>, EVENTTYPE extends Enum<EVENTTYPE>, EVENT> {
    STATE doTransition(OPERAND operand, STATE oldState, EVENT event, EVENTTYPE eventType);
}

/**
 * 状态机工厂， 四个泛型了解一下：
 * 1 OPERAND 状态机实体对象
 * 2 STATE extends Enum<STATE>  状态枚举类
 * 3 EVENTTYPE extends Enum<EVENTTYPE> 事件枚举类
 * 4 EVENT 事件
 */
class StateMachineFactory<OPERAND, STATE extends Enum<STATE>, EVENTTYPE extends Enum<EVENTTYPE>, EVENT> {

    // 初始状态
    private STATE defaultInitialState;

    // 状态转换注册表
    private Map<STATE, Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>>> stateMachineTable;

    // 构造方法
    public StateMachineFactory(STATE defaultInitialState) {
        // 初始状态
        this.defaultInitialState = defaultInitialState;
        // 初始化状态转换表
        /**
         * Map<A, Map<B, C> >
         * A -> 枚举状态(比如 BIRTH)
         * B -> 枚举事件类型(比如 GROWUP)
         * C -> 状态转换动作
         */
        stateMachineTable = new HashMap<>();
    }

    // 添加一个状态转移四元组
    public void addTransition(
            STATE preState,      // 当前状态
            STATE postState,     // 结果状态
            EVENTTYPE eventType, // 事件类型
            Transition<OPERAND, STATE, EVENTTYPE, EVENT> hook // 转换动作 (事件处理)
    ) {
        // 检查该状态的转移表是否存在
        Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>> eventTypeTransitionMap =
                stateMachineTable.computeIfAbsent(
                        preState, k -> new HashMap<>());
        eventTypeTransitionMap.put(eventType, hook);
    }

    public StateMachine<STATE, EVENTTYPE, EVENT> make(OPERAND operand) {
        // 封装具体的状态机
        return new InternalStateMachine(operand, defaultInitialState, stateMachineTable);
    }

    // StateMachine 的唯一实现类
    class InternalStateMachine implements StateMachine<STATE, EVENTTYPE, EVENT> {

        private final OPERAND operand;
        private STATE currentState;
        private Map<STATE, Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>>> stateMachineTable;

        InternalStateMachine(OPERAND operand, STATE initialState,
                             Map<STATE, Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>>> stateMachineTable) {
            this.operand = operand;
            this.currentState = initialState;
            this.stateMachineTable = stateMachineTable;
        }

        @Override
        public STATE getCurrentState() {
            return currentState;
        }

        @Override
        public STATE doTransition(EVENTTYPE eventType, EVENT event) throws Exception {
            // 1 首先到注册表中进行查询 找到 Transition
            Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>> eventTypeTransitionMap =
                    // 从当前状态获取注册状态转换的所有情况
                    stateMachineTable.get(getCurrentState());
            // 根据事件类型获取对应的状态转换器
            Transition<OPERAND, STATE, EVENTTYPE, EVENT> transition = eventTypeTransitionMap.get(eventType);

            // 2 调用 Transition 的 doTransition 方法来执行转换
            STATE state = transition.doTransition(operand, getCurrentState(), event, eventType);

            // 3 返回新的状态
            currentState = state;
            return state;
        }
    }
}

/**
 * 正常长大
 */
class GrowupTransition implements Transition<ManStateMachine, ManState, ManEventType, ManEvent> {
    @Override
    public ManState doTransition(ManStateMachine manStateMachine, ManState oldState, ManEvent manEvent, ManEventType eventType) {
        ManState targetManState = null;
        int newValue = oldState.value + 1;
        try {
            targetManState = ManState.valueOf(newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(
                "EventType：" + eventType + ", Old State：" + oldState + ", New State：" + targetManState + ", Event：" + manEvent);
        return targetManState;
    }
}

/**
 * 生病了，治好
 */
class SickTransition implements Transition<ManStateMachine, ManState, ManEventType, ManEvent> {
    @Override
    public ManState doTransition(ManStateMachine manStateMachine, ManState oldState, ManEvent manEvent, ManEventType eventType) {
        System.out.println(
                "EventType：" + eventType + ", Old State：" + oldState + ", New State：" + oldState + ", Event：" + manEvent);
        return oldState;
    }
}

/**
 * 返老还童
 */
class RejuvenationTransition implements Transition<ManStateMachine, ManState, ManEventType, ManEvent> {
    @Override
    public ManState doTransition(ManStateMachine manStateMachine, ManState oldState, ManEvent manEvent, ManEventType eventType) {
        // 随机挑一个比当前状态小的，非出生状态
        List<ManState> stateList = ManState.getStateList();
        Random random = new Random();
        int i = random.nextInt(stateList.size());
        ManState targetState = stateList.get(i);
        while (targetState.value >= oldState.value && targetState.value != 1) {
            i = random.nextInt(stateList.size());
            targetState = stateList.get(i);
        }
        System.out.println(
                "EventType：" + eventType + ", Old State：" + oldState + ", New State：" + targetState + ", Event：" + manEvent);
        return targetState;
    }
}

/**
 * 绝症死亡
 */
class IncurableDiseaseTransition implements Transition<ManStateMachine, ManState, ManEventType, ManEvent> {
    @Override
    public ManState doTransition(ManStateMachine manStateMachine, ManState oldState, ManEvent manEvent, ManEventType eventType) {
        System.out.println(
                "EventType：" + eventType + ", Old State：" + oldState + ", New State：" + ManState.DEATH + ", Event：" + manEvent);
        return ManState.DEATH;
    }
}