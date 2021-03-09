/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 2007, 2015, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * <p>
 *  根据一个或多个贡献者许可协议授予Apache软件基金会(ASF)。有关版权所有权的其他信息,请参阅随此作品分发的NOTICE文件。
 *  ASF根据Apache许可证2.0版("许可证")向您授予此文件;您不能使用此文件,除非符合许可证。您可以通过获取许可证的副本。
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  除非适用法律要求或书面同意,否则根据许可证分发的软件按"原样"分发,不附带任何明示或暗示的担保或条件。请参阅管理许可证下的权限和限制的特定语言的许可证。
 * 
 */
package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RetrievalMethod extends SignatureElementProxy implements KeyInfoContent {

    /** DSA retrieval */
    public static final String TYPE_DSA     = Constants.SignatureSpecNS + "DSAKeyValue";
    /** RSA retrieval */
    public static final String TYPE_RSA     = Constants.SignatureSpecNS + "RSAKeyValue";
    /** PGP retrieval */
    public static final String TYPE_PGP     = Constants.SignatureSpecNS + "PGPData";
    /** SPKI retrieval */
    public static final String TYPE_SPKI    = Constants.SignatureSpecNS + "SPKIData";
    /** MGMT retrieval */
    public static final String TYPE_MGMT    = Constants.SignatureSpecNS + "MgmtData";
    /** X509 retrieval */
    public static final String TYPE_X509    = Constants.SignatureSpecNS + "X509Data";
    /** RAWX509 retrieval */
    public static final String TYPE_RAWX509 = Constants.SignatureSpecNS + "rawX509Certificate";

    /**
     * Constructor RetrievalMethod
     *
     * <p>
     *  构造方法检索方法
     * 
     * 
     * @param element
     * @param BaseURI
     * @throws XMLSecurityException
     */
    public RetrievalMethod(Element element, String BaseURI) throws XMLSecurityException {
        super(element, BaseURI);
    }

    /**
     * Constructor RetrievalMethod
     *
     * <p>
     *  构造方法检索方法
     * 
     * 
     * @param doc
     * @param URI
     * @param transforms
     * @param Type
     */
    public RetrievalMethod(Document doc, String URI, Transforms transforms, String Type) {
        super(doc);

        this.constructionElement.setAttributeNS(null, Constants._ATT_URI, URI);

        if (Type != null) {
            this.constructionElement.setAttributeNS(null, Constants._ATT_TYPE, Type);
        }

        if (transforms != null) {
            this.constructionElement.appendChild(transforms.getElement());
            XMLUtils.addReturnToElement(this.constructionElement);
        }
    }

    /**
     * Method getURIAttr
     *
     * <p>
     *  方法getURIAttr
     * 
     * 
     * @return the URI attribute
     */
    public Attr getURIAttr() {
        return this.constructionElement.getAttributeNodeNS(null, Constants._ATT_URI);
    }

    /**
     * Method getURI
     *
     * <p>
     *  方法getURI
     * 
     * 
     * @return URI string
     */
    public String getURI() {
        return this.getURIAttr().getNodeValue();
    }

    /** @return the type*/
    public String getType() {
        return this.constructionElement.getAttributeNS(null, Constants._ATT_TYPE);
    }

    /**
     * Method getTransforms
     *
     * <p>
     *  方法getTransforms
     * 
     * @throws XMLSecurityException
     * @return the transformations
     */
    public Transforms getTransforms() throws XMLSecurityException {
        try {
            Element transformsElem =
                XMLUtils.selectDsNode(
                    this.constructionElement.getFirstChild(), Constants._TAG_TRANSFORMS, 0);

            if (transformsElem != null) {
                return new Transforms(transformsElem, this.baseURI);
            }

            return null;
        } catch (XMLSignatureException ex) {
            throw new XMLSecurityException("empty", ex);
        }
    }

    /** @inheritDoc */
    public String getBaseLocalName() {
        return Constants._TAG_RETRIEVALMETHOD;
    }
}
