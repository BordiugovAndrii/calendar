package com.bordisoft;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.rmi.RemoteException;
import java.util.logging.Logger;

public class Main {

    public static final Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args)  throws RemoteException {
        logger.info("Trying to launch service...");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/bordisoft/applicationContext.xml");

        logger.info("Service has been launched");
    }
}
