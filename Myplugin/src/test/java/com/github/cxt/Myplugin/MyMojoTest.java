package com.github.cxt.Myplugin;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;


public class MyMojoTest extends AbstractMojoTestCase{

	/**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        // required for mojo lookups to work
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
 
    /**
     * @throws Exception
     */
    public void testMojoGoal() throws Exception {
        File testPom = new File( getBasedir(), "src/test/resources/pom.xml" );
 
        MyMojo mojo = (MyMojo) lookupMojo( "touch", testPom );
 
        assertNotNull( mojo );
        
        mojo.execute();
    }
}
