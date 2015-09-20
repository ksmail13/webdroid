package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.handler.sockjs.impl.SockJSSocketBase;
import org.webdroid.constant.WebdroidConstant;
import org.webdroid.server.handler.RouteHandler;
import org.webdroid.util.Log;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.BufferedImageOp;
import java.io.*;

/**
 * Created by 김민수 on 2015-07-27.
 */
public class SocketJSServer extends WebdroidVerticle {

    protected Session session;
    private String uid;
    SockJSHandler sockJSHandler;
    Buffer buffer = null;

    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        EventBus eb = vertx.eventBus();
        //initRouter(server);
        /*mDBConnector = new DBConnector(vertx, aBoolean -> {
            Log.logging("DB " + (aBoolean ? "Connected":"Unconnected"));
            if(aBoolean) {
            }
        });*/
        Router router =  Router.router(vertx);
        //SockJSHandlerOptions Options = new SockJSHandlerOptions().setHeartbeatInterval(2000);
        sockJSHandler = SockJSHandler.create(vertx);//,Options);

        router.route("/vm_device").handler(routingContext -> {
            routingContext.response().sendFile(WebdroidConstant.Path.TEMPL_STATIC + "/device_displayer.html");
            session = routingContext.session();
            uid = session.get("id").toString();
        });
        router.route("/vm_device/*").handler(sockJSHandler);

        final String BOOTSTRAP = "/bootstrap/(css|fonts|js)/\\S+.\\S+";
        final String CSS = "/css/\\S+.(css)";
        final String JS = "/js/\\S+.(js)";
        final String IMG = "/images/\\S+.(jpeg|png|jpg|ico)";

        StaticHandler staticHandler = StaticHandler.create(WebdroidConstant.Path.STATIC);

        router.route().pathRegex(BOOTSTRAP).handler(staticHandler);
        router.route().pathRegex(JS).handler(staticHandler);
        router.route().pathRegex(CSS).handler(staticHandler);
        router.route().pathRegex(IMG).handler(staticHandler);

        server.requestHandler(router::accept);
        server.listen(8080);

        eb.consumer("vm_event_to_clnt", this::vmEventHandler);
        sockJSHandler.socketHandler(sockJSSocket -> {
            Log.logging(sockJSSocket.remoteAddress().host());
            vertx.eventBus().send("vm_event_to_py","run_vm#"+uid); // 최초 연결시에 run_vm 메시지전달

            sockJSSocket.handler(buffer -> { // user->python 이벤트전달
                // sockJSSocket.write(buffer);
                vertx.eventBus().send("vm_event_to_py", "event_vm#" + uid + "#" + buffer);
            });
            sockJSSocket.endHandler(new Handler<Void>() {
                @Override
                public void handle(Void aVoid) {
                    vertx.eventBus().send("vm_event_to_py", "event_vm#" + uid + "#vmend");
                }
            });
        });
    }

    private void vmEventHandler(Message<Object> objectMessage) { // python->user 이벤트전달
        String[] split_array;
        split_array = objectMessage.toString().split("#");
        if(split_array[0].contains("vm_event")) {
            buffer = buffer.appendString(split_array[0]);
            sockJSHandler.socketHandler(sockJSSocket -> {
                sockJSSocket.write(buffer);
            });
        }
    }
}







/*
    public int j=0;

    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
*/

/*
            vertx.setPeriodic(33, id -> {
                Buffer buffer = Buffer.buffer();
                String buf = null;
                try {
                    j = j % 60 + 1;
                    String adrr = "C:\\Users\\Owner\\Documents\\webdroid_IDE\\relayserver\\wildlife\\" + Integer.toString(j) + ".jpg";
                    BufferedImage image1 = ImageIO.read(new File(adrr));
                    buf = encodeToString(image1, "jpg");
                }catch (IOException e){
                }
                buffer.setString(0,buf);
                sockJSSocket.write(buffer);
            });*/

/*
                int i;
                String buf = "0";
                String adrr = "";

                i = buffer.getByte(0)-48;

                if(i == 0)
                {

                }
                else if(i==1)
                {
                    try {
                        BufferedImage image2 = ImageIO.read(new File("C:\\Users\\김민수\\Desktop\\iu2.jpg"));
                        buf = encodeToString(image2,"jpg");
                    }catch (IOException e){
                    }
                    buffer.setString(0,buf);
                    sockJSSocket.write(buffer);
                }
                else if(i==2)
                {
                    try {
                        BufferedImage image3 = ImageIO.read(new File("C:\\Users\\김민수\\Desktop\\iu3.jpg"));
                        buf = encodeToString(image3,"jpg");
                    }catch (IOException e){
                    }
                    buffer.setString(0,buf);
                    sockJSSocket.write(buffer);
                }*/




/*

        router.route("/sockjs-0.3.4.js").handler(routingContext -> {
            routingContext.response().sendFile("./webroot/static/sockjs-0.3.4.js").end();
        });
        router.route().handler(StaticHandler.create("./webroot/static/"));*/