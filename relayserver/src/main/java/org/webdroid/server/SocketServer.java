package org.webdroid.server;

import io.vertx.core.AsyncResult;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.util.Optional;

/**
 * Created by Seho on 2015-09-07.
 */
public class SocketServer extends WebdroidVerticle {

    NetClient netClient;
    Optional<NetSocket> socket = Optional.empty();
    public void start(){
        EventBus eb = vertx.eventBus();
        netClient = vertx.createNetClient();
        netClient.connect(1113, "211.243.108.156", this::connectHandler);
        eb.consumer("socket", this::vmEventHandler);
    }

    private void connectHandler(AsyncResult<NetSocket> netSocketAsyncResult) {
        if(netSocketAsyncResult.succeeded()){
            socket = Optional.ofNullable(netSocketAsyncResult.result());
            socket.ifPresent(sock-> {
                // data receive
                sock.handler(this::recvHandler);

                // closed
                //sock.closeHandler();

                // error
                //sock.exceptionHandler();

                //
                //sock.endHandler();
            });
        } else {
            logger.error("", netSocketAsyncResult.cause());
        }
    }

    private void recvHandler(Buffer buffer) {
        if(buffer.toString().equals("success")){
            socket.ifPresent(sock -> sock.write("install_apk" + "#abc"));
            System.out.println("send install_apk success");
        }else{
            System.out.println(buffer);
        }
    }

    private void vmEventHandler(Message<Object> objectMessage) {
        System.out.println(objectMessage.body()); // "run_vm"
        socket.ifPresent(sock -> sock.write(objectMessage.body().toString()));

    }
}
