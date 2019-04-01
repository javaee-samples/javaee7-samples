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

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    @Id
    private Integer id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "gross_price"))
    private Money grossPrice;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "net_price"))
    private Money netPrice;

    public Product() {
    }

    public Product(Double initialPrice) {
        this.id = COUNTER.incrementAndGet();

        Money money = new Money(BigDecimal.valueOf(initialPrice));
        this.grossPrice = money;
        this.netPrice = money;
    }

    public Money getGrossPrice() {
        return grossPrice;
    }

    public Money getNetPrice() {
        return netPrice;
    }

    public void setGrossPrice(Money grossPrice) {
        this.grossPrice = grossPrice;
    }

    public void setNetPrice(Money netPrice) {
        this.netPrice = netPrice;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", grossPrice=" + grossPrice + ", netPrice=" + netPrice + '}';
    }
}