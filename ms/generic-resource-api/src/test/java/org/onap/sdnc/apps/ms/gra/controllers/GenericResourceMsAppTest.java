package org.onap.sdnc.apps.ms.gra.controllers;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.sdnc.apps.ms.gra.core.GenericResourceMsApp;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GenericResourceMsAppTest {

    GenericResourceMsApp app;

    @Before
    public  void setUp() throws Exception {
        app = new GenericResourceMsApp();
        System.out.println("GenericResourceMsAppTest: Setting serviceLogicProperties, serviceLogicDirectory and sdnc.config.dir");
        System.setProperty("serviceLogicProperties", "src/test/resources/svclogic.properties");
        System.setProperty("serviceLogicDirectory", "src/test/resources/svclogic");
        System.setProperty("sdnc.config.dir", "src/test/resources");
   
    }

    @Test
    public void realm() {
        Realm realm = app.realm();
        assertTrue(realm instanceof PropertiesRealm);


    }

    @Test
    public void shiroFilterChainDefinition() {
        ShiroFilterChainDefinition chainDefinition = app.shiroFilterChainDefinition();
        Map<String, String> chainMap = chainDefinition.getFilterChainMap();
        assertEquals("anon", chainMap.get("/**"));


    }
}