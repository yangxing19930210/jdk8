/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.security.auth.module;

/**
 * <p> This class implementation retrieves and makes available Unix
 * UID/GID/groups information for the current user.
 *
 * <p>
 *  <p>此类实现检索并为当前用户提供Unix UID / GID /组信息。
 * 
 */
@jdk.Exported
public class UnixSystem {

    private native void getUnixInfo();

    protected String username;
    protected long uid;
    protected long gid;
    protected long[] groups;

    /**
     * Instantiate a <code>UnixSystem</code> and load
     * the native library to access the underlying system information.
     * <p>
     *  实例化<code> UnixSystem </code>并加载本机库以访问基础系统信息。
     * 
     */
    public UnixSystem() {
        System.loadLibrary("jaas_unix");
        getUnixInfo();
    }

    /**
     * Get the username for the current Unix user.
     *
     * <p>
     *
     * <p>
     *  获取当前Unix用户的用户名。
     * 
     * <p>
     * 
     * 
     * @return the username for the current Unix user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the UID for the current Unix user.
     *
     * <p>
     *
     * <p>
     *  获取当前Unix用户的UID。
     * 
     * <p>
     * 
     * 
     * @return the UID for the current Unix user.
     */
    public long getUid() {
        return uid;
    }

    /**
     * Get the GID for the current Unix user.
     *
     * <p>
     *
     * <p>
     *  获取当前Unix用户的GID。
     * 
     * <p>
     * 
     * 
     * @return the GID for the current Unix user.
     */
    public long getGid() {
        return gid;
    }

    /**
     * Get the supplementary groups for the current Unix user.
     *
     * <p>
     *
     * <p>
     *  获取当前Unix用户的补充组。
     * 
     * 
     * @return the supplementary groups for the current Unix user.
     */
    public long[] getGroups() {
        return groups == null ? null : groups.clone();
    }
}
