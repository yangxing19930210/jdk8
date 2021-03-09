/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 1999, 2003, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.corba.se.impl.naming.pcosnaming;

import java.io.File;
import java.util.Properties;

import org.omg.CORBA.Policy;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.PortableServer.*;

import com.sun.corba.se.spi.orb.ORB ;

import com.sun.corba.se.impl.orbutil.ORBConstants ;

/**
/* <p>
/* 
 * @author      Hemanth Puttaswamy
 * @since       JDK1.2
 */

public class NameService
{
    private NamingContext rootContext = null;
    private POA nsPOA = null;
    private ServantManagerImpl contextMgr;
    private ORB theorb;

    /**
     * Create NameService which starts the Root Naming Context in Persistent CosNaming
     * <p>
     *  创建NameService,它在持久性CosNaming中启动根命名上下文
     * 
     * 
     * @param orb an ORB object.
     * @param logDir a File
     * @exception java.lang.Exception a Java exception.
     */
    public NameService(ORB orb, File logDir)
        throws Exception
    {
        theorb = orb;

        // Moved this to the creation of the ORB that is passed into this
        // constructor.
        //
        // This is required for creating Persistent Servants under this ORB
        // Right now the Persistent NameService and ORBD are launched together
        // Find out a better way of doing this, Since ORBD is an important
        // process which should not be killed because of some external process
        // orb.setPersistentServerId( (int) 1000 );

        // get and activate the root naming POA
        POA rootPOA = (POA)orb.resolve_initial_references(
            ORBConstants.ROOT_POA_NAME ) ;
        rootPOA.the_POAManager().activate();

        // create a new POA for persistent Naming Contexts
        // With Non-Retain policy, So that every time Servant Manager
        // will be contacted when the reference is made for the context
        // The id assignment is made by the NameServer, The Naming Context
        // id's will be in the format NC<Index>
        int i=0;
        Policy[] poaPolicy = new Policy[4];
        poaPolicy[i++] = rootPOA.create_lifespan_policy(
                         LifespanPolicyValue.PERSISTENT);
        poaPolicy[i++] = rootPOA.create_request_processing_policy(
                         RequestProcessingPolicyValue.USE_SERVANT_MANAGER);
        poaPolicy[i++] = rootPOA.create_id_assignment_policy(
                         IdAssignmentPolicyValue.USER_ID);
        poaPolicy[i++] = rootPOA.create_servant_retention_policy(
                         ServantRetentionPolicyValue.NON_RETAIN);


        nsPOA = rootPOA.create_POA("NameService", null, poaPolicy);
        nsPOA.the_POAManager().activate( );

        // create and set the servant manager
        contextMgr = new
            ServantManagerImpl(orb, logDir, this );

        // The RootObject key will be NC0
        String rootKey = contextMgr.getRootObjectKey( );
        // initialize the root Naming Context
        NamingContextImpl nc =
                new NamingContextImpl( orb, rootKey, this, contextMgr );
        nc = contextMgr.addContext( rootKey, nc );
        nc.setServantManagerImpl( contextMgr );
        nc.setORB( orb );
        nc.setRootNameService( this );

        nsPOA.set_servant_manager(contextMgr);
        rootContext = NamingContextHelper.narrow(
        nsPOA.create_reference_with_id( rootKey.getBytes( ),
        NamingContextHelper.id( ) ) );
    }

    /**
     * This method returns the Root Naming Context
     * <p>
     *  此方法返回根命名上下文
     * 
     */
    public NamingContext initialNamingContext()
    {
        return rootContext;
    }

    /**
     * This method returns nsPOA which is the only POA that we use for
     * Persistent Naming Contexts.
     * <p>
     *  此方法返回nsPOA,这是我们用于持久命名上下文的唯一POA。
     * 
     */
    POA getNSPOA( ) {
        return nsPOA;
    }


    /**
     * This method  creates a NewContext, This will internally invoked from
     * NamingContextImpl. It is not a public API. NewContext is in this class
     * because a Persiten reference has to be created with Persistent NameService
     * POA.
     * <p>
     *  此方法创建一个NewContext,这将从NamingContextImpl内部调用。它不是一个公共API。
     *  NewContext在此类中,因为必须使用Persistent NameService POA创建Persiten引用。
     * 
     */
    public NamingContext NewContext( ) throws org.omg.CORBA.SystemException
    {
        try
        {
                // Get the new Naming Context Key from
                // the ServantManager
                String newKey =
                contextMgr.getNewObjectKey( );
                // Create the new Naming context and create the Persistent
                // reference
                NamingContextImpl theContext =
                new NamingContextImpl( theorb, newKey,
                    this, contextMgr );
                NamingContextImpl tempContext = contextMgr.addContext( newKey,
                                                 theContext );
                if( tempContext != null )
                {
                        theContext = tempContext;
                }
                // If the context is read from the File, The following three entries
                // will be null. So a fresh setup may be required.
                theContext.setServantManagerImpl( contextMgr );
                theContext.setORB( theorb );
                theContext.setRootNameService( this );
                NamingContext theNewContext =
                NamingContextHelper.narrow(
                nsPOA.create_reference_with_id( newKey.getBytes( ),
                NamingContextHelper.id( )) );
                return theNewContext;
        }
        catch( org.omg.CORBA.SystemException e )
        {
                throw e;
        }
        catch( java.lang.Exception e )
        {
                //throw e;
        }
        return null;
    }

    /**
     * getObjectReferenceFromKey returns the Object reference from the objectkey using POA.create_reference_with_id method
     * <p>
     *  getObjectReferenceFromKey使用POA.create_reference_with_id方法从objectkey返回Object引用
     * 
     * 
     * @param Object Key as String
     * @returns reference an CORBA.Object.
     */
    org.omg.CORBA.Object getObjectReferenceFromKey( String key )
    {
        org.omg.CORBA.Object theObject = null;
        try
        {
                theObject = nsPOA.create_reference_with_id( key.getBytes( ), NamingContextHelper.id( ) );
        }
        catch (Exception e )
        {
                theObject = null;
        }
        return theObject;
    }

    /**
     * getObjectKey gets the Object Key from the reference using POA.reference_to_id method
     * <p>
     *  getObjectKey使用POA.reference_to_id方法从引用中获取对象键
     * 
     * @param reference an CORBA.Object.
     * @returns Object Key as String
     */
    String getObjectKey( org.omg.CORBA.Object reference )
    {
        byte theId[];
        try
        {
                theId = nsPOA.reference_to_id( reference );
        }
        catch( org.omg.PortableServer.POAPackage.WrongAdapter e )
        {
                return null;
        }
        catch( org.omg.PortableServer.POAPackage.WrongPolicy e )
        {
                return null;
        }
        catch( Exception e )
        {
                return null;
        }
        String theKey = new String( theId );
        return theKey;
    }


}
