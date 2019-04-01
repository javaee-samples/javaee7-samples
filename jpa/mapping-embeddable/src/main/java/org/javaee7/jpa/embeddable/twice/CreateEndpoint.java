/*
 *    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 *    Copyright (c) [2019] Payara Foundation and/or its affiliates. All rights reserved.
 *
 *    The contents of this file are subject to the terms of either the GNU
 *    General Public License Version 2 only ("GPL") or the Common Development
 *    and Distribution License("CDDL") (collectively, the "License").  You
 *    may not use this file except in compliance with the License.  You can
 *    obtain a copy of the License at
 *    https://github.com/payara/Payara/blob/master/LICENSE.txt
 *    See the License for the specific
 *    language governing permissions and limitations under the License.
 *
 *    When distributing the software, include this License Header Notice in each
 *    file and include the License file at glassfish/legal/LICENSE.txt.
 *
 *    GPL Classpath Exception:
 *    The Payara Foundation designates this particular file as subject to the "Classpath"
 *    exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 *    file that accompanied this code.
 *
 *    Modifications:
 *    If applicable, add the following below the License Header, with the fields
 *    enclosed by brackets [] replaced by your own identifying information:
 *    "Portions Copyright [year] [name of copyright owner]"
 *
 *    Contributor(s):
 *    If you wish your version of this file to be governed by only the CDDL or
 *    only the GPL Version 2, indicate your decision by adding "[Contributor]
 *    elects to include this software in this distribution under the [CDDL or GPL
 *    Version 2] license."  If you don't indicate a single choice of license, a
 *    recipient has the option to distribute your version of this file under
 *    either the CDDL, the GPL Version 2 or to extend the choice of license to
 *    its licensees as provided above.  However, if you add GPL Version 2 code
 *    and therefore, elected the GPL Version 2 license, then the option applies
 *    only if the new code is made subject to such option by the copyright
 *    holder.
 */

package org.javaee7.jpa.embeddable.twice;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/create")
@RequestScoped
public class CreateEndpoint {

    private static final Logger LOG = Logger.getLogger(CreateEndpoint.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    @POST
    @Transactional
    public void createProduct(@FormParam("price") Double price){
        Product newProduct = new Product(price);
        entityManager.persist(newProduct);
        entityManager.flush();
        LOG.log(Level.INFO, "Persisted new product: {0}", newProduct);

        Money money = new Money(price + 15.5);
        newProduct.setGrossPrice(money);
        newProduct.setNetPrice(money);
        LOG.log(Level.INFO, "Updated product: {0}", newProduct);
        entityManager.flush();
    }

    @Transactional
    public void createProductNotSharingEmbedded(Double price){
        Product newProduct = new Product(price);
        entityManager.persist(newProduct);
        entityManager.flush();
        LOG.log(Level.INFO, "Persisted new product: {0}", newProduct);

        Money money = new Money(price + 15.5);
        newProduct.setGrossPrice(money);
        newProduct.setNetPrice(new Money(money));
        LOG.log(Level.INFO, "Updated product: {0}", newProduct);
        entityManager.flush();
    }
}
