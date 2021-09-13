/**
 * Copyright © 2019, Wen Hao <wenhao@126.com>.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.wenhao.jpa.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.utils.CheckUtil;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

abstract class AbstractSpecification<T> implements Specification<T>, Serializable {
    public String getProperty(String property) {
        if (property.contains(".")) {
        	property = StringUtils.split(property, ".")[1];
        	return getProperty(property);
        }
        return property;
    }
    
    AbstractSpecification(){
    	Date date = new Date(1635650341000L);
    }

    public From getRoot(String property, Root<T> root) {
        if (property.contains(".")) {
            String joinProperty = StringUtils.split(property, ".")[0];
            From from = root.join(joinProperty, JoinType.LEFT);
            return getRoot(StringUtils.split(property, ".")[1], from);
        }
        return root;
    }
    public From getRoot(String property, From from) {
    	if (property.contains(".")) {
    		String joinProperty = StringUtils.split(property, ".")[0];
    		from = from.join(joinProperty, JoinType.LEFT);
    		return getRoot(StringUtils.split(property, ".")[1], from);
    	}
    	return from;
    }
}
