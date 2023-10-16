package com.lvboaa.design.signle;

/**
 * Description: 懒汉式单例模式-线程不安全
 *  调用的时候才会创建实例，最大的问题就是不支持多线程模式， 因为没加锁
 *
 *  单例模式：内存里只有一个实例，减少了内存的开销，尤其是频繁的创建和销毁实例
 *      比如说一个班级只有一个班主任
 *
 * @author lv.bo
 * @date 2023/10/16 23:32
 */
public class LazySingle {
    private static LazySingle instance;

    private LazySingle(){

    }

    /**
     *  线程不安全
     * @return
     */
    public static LazySingle getInstance(){
        if (instance == null){
            instance = new LazySingle();
        }
        return instance;
    }

    /**
     *  线程安全
     * @return
     */
    public static synchronized LazySingle getSyncInstance(){
        if (instance == null){
            instance = new LazySingle();
        }
        return instance;
    }
}
