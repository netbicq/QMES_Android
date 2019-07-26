package kkkj.android.revgoods.conn.classicbt.listener;

import kkkj.android.revgoods.conn.classicbt.Connect;

/**
 * @author AllenLiu
 * @version 1.0
 * @date 2019/5/8

 */
public interface ConnectResultlistner  {

     void connectSuccess(Connect connect);

     void connectFailed(Exception e);
     void disconnected();



}
