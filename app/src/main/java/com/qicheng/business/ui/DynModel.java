package com.qicheng.business.ui;

import com.qicheng.business.module.DynBean;

import java.util.ArrayList;

public class DynModel {

    public static ArrayList<DynBean> getDynList(){
        ArrayList<DynBean> dynlist=new  ArrayList<DynBean>();
        for(int i=1;i<10;i++){
            DynBean bean=new DynBean();
            bean.setPortraitl(null);
            bean.setName("朕把自己戒了"+" "+i);
            bean.setContent("不要问我为什么买这些，女王大人要的！"+" "+i);
            bean.setPhoto(null);
            bean.setLiketime(i+"");
            bean.setLooktime(i+"");
            bean.setSharetime(i+"");
            bean.setPasttime(i+"秒前");
            dynlist.add(bean);
        }
        return dynlist;

    }
}
