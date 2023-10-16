package com.lvboaa.design.signle;

/**
 * Description: 双重检测锁模式 --线程安全、效率最高、推荐
 *  但是能通过反射进行创建
 *
 * @author lv.bo
 * @date 2023/10/16 23:39
 */
public class DCLSingle {

    //被volatile修饰的变量能够保证每个线程能够获取该变量的最新值，从而避免出现数据脏读的现象
    // 主要是防止指令重排
    public static volatile DCLSingle instance;

    private DCLSingle(){

    }

    public static DCLSingle getInstance(){
        if (instance == null){
            synchronized (DCLSingle.class){
                if (instance == null){
                    instance = new DCLSingle();
                    //不是原子性操作（这种操作一旦开始，就一直运行到结束，中间不会换到另一个线程）
                    /**
                     * 操作
                     * 1.分配内存空间
                     * 2.执行构造方法，初始化对象
                     * 3.把这个对象指向这个空间
                     *
                     * 期望是1 2 3
                     * 实际可能 1 3 2（在cpu是可以实现的）
                     *
                     * 这时来了线程A 走到了3 layMan执行了空间 lazyMan不为null
                     * 下一个线程B  进来发现lazyMan不为null 直接return 但是还没有初始化值 错误
                     * 这时就需要使用volatile 关键字修饰单例变量
                     */
                }
            }
        }
        return instance;
    }
}
