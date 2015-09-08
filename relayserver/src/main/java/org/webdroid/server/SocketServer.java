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

    private final static int PY_PORT = 1112;
    private final static String PY_IP = "192.168.56.1";
    NetClient netClient;
    Optional<NetSocket> socket = Optional.empty();
    private boolean frameBufferRecvState = false;
    private String frameBufferRecved = "";

    public void start(){
        EventBus eb = vertx.eventBus();
        netClient = vertx.createNetClient();
        netClient.connect(PY_PORT, PY_IP, this::connectHandler);
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
        if(frameBufferRecvState){
            if(buffer.toString().startsWith("frame_buffer_end")){
                frameBufferRecvState = false;
                System.out.println(buffer);
                System.out.println(frameBufferRecved);
                vertx.eventBus().send("frameBuffer", frameBufferRecved);
            }else {
                frameBufferRecved += buffer.toString();
            }
        }else if (buffer.toString().equals("run_vm_success")){
            socket.ifPresent(sock -> sock.write("install_apk" + "#abc"));
            System.out.println("send install_apk done");
        }else if(buffer.toString().startsWith("install_apk_")){
            socket.ifPresent(sock -> sock.write("get_frame_buffer"));
            System.out.println("send get_frame_buffer done");
        }else if(buffer.toString().startsWith("frame_buffer_start")){
            frameBufferRecvState = true;
            System.out.println("frame_buffer_start");
        }
    }

    private void vmEventHandler(Message<Object> objectMessage) {
        System.out.println(objectMessage.body()); // "run_vm@userId"
        socket.ifPresent(sock -> sock.write(objectMessage.body().toString()));

    }
}
