package org.webdroid.server;

import io.vertx.core.AbstractVerticle;
import org.webdroid.util.ConsoleLogger;

/**
 * Created by 민규 on 2015-08-07.
 */
public abstract class WebdroidVerticle extends AbstractVerticle {
    protected ConsoleLogger logger = ConsoleLogger.createLogger(this.getClass());
}
