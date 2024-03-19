/**
 * Copyright (C) 2022-2023 Red Hat, Inc. (https://github.com/Commonjava/indy-repository-service)
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
package org.commonjava.indy.service.repository.ftests.admin;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.commonjava.indy.service.repository.ftests.AbstractStoreManagementTest;
import org.commonjava.indy.service.repository.ftests.matchers.RepoEqualMatcher;
import org.commonjava.indy.service.repository.ftests.profile.MemoryFunctionProfile;
import org.commonjava.indy.service.repository.model.HostedRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.METHOD_NOT_ALLOWED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.commonjava.indy.service.repository.model.pkg.MavenPackageTypeDescriptor.MAVEN_PKG_KEY;

/**
 * This case test if the readonly hosted repo can be deleted
 * when: <br />
 * <ul>
 *      <li>creates readonly hosted repo</li>
 *      <li>deletes this readonly hosted repo once</li>
 *      <li>updates the hosted repo to non-readonly</li>
 *      <li>deletes it again</li>
 * </ul>
 * then: <br />
 * <ul>
 *     <li>the hosted repo can not be deleted with 405 error first time</li>
 *     <li>the hosted repo can be deleted successfully with no error second time</li>
 * </ul>
 */
@QuarkusTest
@TestProfile( MemoryFunctionProfile.class )
@Tag( "function" )
public class ReadonlyHostedRepoDeleteTest
        extends AbstractStoreManagementTest
{

    @Test
    public void addReadonlyHostedAndDelete()
            throws Exception
    {
        final String nameHosted = newName();
        final HostedRepository repo = new HostedRepository( MAVEN_PKG_KEY, nameHosted );
        repo.setReadonly( true );
        String json = mapper.writeValueAsString( repo );
        given().body( json )
               .contentType( APPLICATION_JSON )
               .post( getRepoTypeUrl( repo.getKey() ) )
               .then()
               .body( new RepoEqualMatcher<>( mapper, repo, HostedRepository.class ) );

        final String repoUrl = getRepoUrl( repo.getKey() );

        given().delete( repoUrl ).then().statusCode( METHOD_NOT_ALLOWED.getStatusCode() );

        given().head( repoUrl ).then().statusCode( OK.getStatusCode() );

        repo.setReadonly( false );
        json = mapper.writeValueAsString( repo );
        given().body( json ).contentType( APPLICATION_JSON ).put( repoUrl ).then().statusCode( OK.getStatusCode() );

        given().delete( repoUrl ).then().statusCode( NO_CONTENT.getStatusCode() );

        given().head( repoUrl ).then().statusCode( NOT_FOUND.getStatusCode() );

    }

}
