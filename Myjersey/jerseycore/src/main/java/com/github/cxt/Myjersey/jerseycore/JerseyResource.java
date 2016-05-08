/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.github.cxt.Myjersey.jerseycore;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.cxt.Myjersey.jerseycore.server.Server;


/**
 * Jersey2 Spring integration example.
 * Demonstrate how to inject a Spring bean into a Jersey managed JAX-RS resource class.
 *
 * @author Marko Asplund (marko.asplund at gmail.com)
 */
@Produces(Constants.MEDIA_TYPE)
@Consumes(Constants.MEDIA_TYPE)
@Path("jersey")
@Singleton//with singleton scope 不加则是prototype
public class JerseyResource {
    
    private int count;
    
    @Autowired
    private Server server;

    @Path("demo")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        return server.returnContent();
    }
    
    @Path("visitCount")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getVisitCount() {
        return (count++) + "" +  this;
    }
    
    @Path("searchKey")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String searchKey(@NotBlank(message = "{search.string.empty}") @QueryParam("key") final String key) {
        return "success";
    }
    
    @Path("user")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String searchKey(@Valid User user) {
    	System.out.println(user.toString());
    	if(user.getName() == null){
    		throw new RuntimeException();
    	}
        return "success";
    }

}
