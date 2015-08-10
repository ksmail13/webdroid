package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.handler.sockjs.impl.SockJSSocketBase;
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

    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        //initRouter(server);


        /*mDBConnector = new DBConnector(vertx, aBoolean -> {
            Log.logging("DB " + (aBoolean ? "Connected":"Unconnected"));
            if(aBoolean) {
            }
        });*/

        Router router =  Router.router(vertx);
        SockJSHandlerOptions Options = new SockJSHandlerOptions().setHeartbeatInterval(2000);
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx,Options);

        sockJSHandler.socketHandler(sockJSSocket -> {
            Log.logging(sockJSSocket.remoteAddress().host());
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
            });
            sockJSSocket.handler(buffer -> {

               // sockJSSocket.write(buffer);
                System.out.println(buffer.getByte(0)-48);
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



            });
        });
        router.route("/myapp").handler(routingContext -> {
            routingContext.response().sendFile("./webroot/static/device_displayer.html");
        });

        router.route("/myapp/*").handler(sockJSHandler);
        router.route("/sockjs-0.3.4.js").handler(routingContext -> {
            routingContext.response().sendFile("./webroot/static/sockjs-0.3.4.js").end();
        });
        router.route().handler(StaticHandler.create("./webroot/static/"));
        server.requestHandler(router::accept);
        server.listen(8080);
    }
}
