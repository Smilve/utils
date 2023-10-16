package com.lvboaa.design.signle;

/**
 * Description: 饿汉式单例模式-线程安全  - 但是可以通过反射进行破坏
 *  初始化直接创建，可能会消耗过多的资源，
 *
 * @author lv.bo
 * @date 2023/10/16 23:36
 */
public class HungrySingle {

    public static HungrySingle instance = new HungrySingle();

    private HungrySingle(){

    }

    public static HungrySingle getInstance(){
        return instance;
    }
}
