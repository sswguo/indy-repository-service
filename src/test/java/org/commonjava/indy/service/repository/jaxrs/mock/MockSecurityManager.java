/**
 * Copyright (C) 2011-2022 Red Hat, Inc. (https://github.com/Commonjava/indy-repository-service)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.indy.service.repository.jaxrs.mock;

import org.commonjava.indy.service.security.common.SecurityManager;
import org.jboss.resteasy.spi.HttpRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
public class MockSecurityManager
        extends SecurityManager
{
//    @Override
//    public boolean authorized( final String path, final String httpMethod )
//    {
//        return true;
//    }

    @Override
    public String getUser( HttpRequest request )
    {
        return "systemUser";
    }
}
