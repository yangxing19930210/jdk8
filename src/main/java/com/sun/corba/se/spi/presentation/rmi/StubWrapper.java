/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.corba.se.spi.presentation.rmi ;

import java.rmi.RemoteException ;

import org.omg.CORBA.portable.Delegate ;
import org.omg.CORBA.ORB ;
import org.omg.CORBA.Request ;
import org.omg.CORBA.Context ;
import org.omg.CORBA.NamedValue ;
import org.omg.CORBA.NVList ;
import org.omg.CORBA.ContextList ;
import org.omg.CORBA.ExceptionList ;
import org.omg.CORBA.Policy ;
import org.omg.CORBA.DomainManager ;
import org.omg.CORBA.SetOverrideType ;

import org.omg.CORBA.portable.OutputStream ;

/** Wrapper that can take any stub (object x such that StubAdapter.isStub(x))
 * and treat it as a DynamicStub.
 * <p>
 *  并将其视为DynamicStub。
 */
public class StubWrapper implements DynamicStub
{
    private org.omg.CORBA.Object object ;

    public StubWrapper( org.omg.CORBA.Object object )
    {
        if (!(StubAdapter.isStub(object)))
            throw new IllegalStateException() ;

        this.object = object ;
    }

    public void setDelegate( Delegate delegate )
    {
        StubAdapter.setDelegate( object, delegate ) ;
    }

    public Delegate getDelegate()
    {
        return StubAdapter.getDelegate( object ) ;
    }

    public ORB getORB()
    {
        return StubAdapter.getORB( object ) ;
    }

    public String[] getTypeIds()
    {
        return StubAdapter.getTypeIds( object ) ;
    }

    public void connect( ORB orb ) throws RemoteException
    {
        StubAdapter.connect( object, (com.sun.corba.se.spi.orb.ORB)orb ) ;
    }

    public boolean isLocal()
    {
        return StubAdapter.isLocal( object ) ;
    }

    public OutputStream request( String operation, boolean responseExpected )
    {
        return StubAdapter.request( object, operation, responseExpected ) ;
    }

    public boolean _is_a(String repositoryIdentifier)
    {
        return object._is_a( repositoryIdentifier ) ;
    }

    public boolean _is_equivalent(org.omg.CORBA.Object other)
    {
        return object._is_equivalent( other ) ;
    }

    public boolean _non_existent()
    {
        return object._non_existent() ;
    }

    public int _hash(int maximum)
    {
        return object._hash( maximum ) ;
    }

    public org.omg.CORBA.Object _duplicate()
    {
        return object._duplicate() ;
    }

    public void _release()
    {
        object._release() ;
    }

    public org.omg.CORBA.Object _get_interface_def()
    {
        return object._get_interface_def() ;
    }

    public Request _request(String operation)
    {
        return object._request( operation ) ;
    }

    public Request _create_request( Context ctx, String operation, NVList arg_list,
        NamedValue result)
    {
        return object._create_request( ctx, operation, arg_list, result ) ;
    }

    public Request _create_request( Context ctx, String operation, NVList arg_list,
        NamedValue result, ExceptionList exclist, ContextList ctxlist)
    {
        return object._create_request( ctx, operation, arg_list, result,
            exclist, ctxlist ) ;
    }

    public Policy _get_policy(int policy_type)
    {
        return object._get_policy( policy_type ) ;
    }

    public DomainManager[] _get_domain_managers()
    {
        return object._get_domain_managers() ;
    }

    public org.omg.CORBA.Object _set_policy_override( Policy[] policies,
        SetOverrideType set_add)
    {
        return object._set_policy_override( policies, set_add ) ;
    }
}
