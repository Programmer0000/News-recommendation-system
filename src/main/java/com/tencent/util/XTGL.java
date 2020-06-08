package com.tencent.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XTGL {

    public double xtgl(int u,int v){
        double sim = 1/Math.sqrt(u*v);
        return sim;
    }

    public boolean isRepeat(List list,int num){
        int count = 0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(num)){
                count++;
            }
        }
        if(count>0){
            return true;
        }else{
            return false;
        }
    }
}
