package com.lvboaa.design.signle;

/**
 * Description: 加标志位，可防止通过反射破坏单例模式
 *
 * @author lv.bo
 * @date 2023/10/16 23:46
 */
public class FinalSingle {

    public static volatile FinalSingle instance;

    private static boolean flag =false;

    private FinalSingle(){
        synchronized (FinalSingle.class){
            if (flag == false){
                flag = true;
            } else {
                throw new RuntimeException("不要试图通过反射破坏单例模式");
            }
        }
    }

    public static FinalSingle getInstance(){
        if (instance == null){
            synchronized (FinalSingle.class){
                if (instance == null){
                    instance = new FinalSingle();
                }
            }
        }
        return instance;
    }
}
