/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.kafka.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.util.Assert;

/**
 * 使用组合模式拦截器
 *
 * A {@link RecordInterceptor} that delegates to one or more {@link RecordInterceptor} in
 * order.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 *
 * @author Artem Bilan
 * @author Gary Russell
 * @since 2.3
 *
 */
public class CompositeRecordInterceptor<K, V> implements RecordInterceptor<K, V> {

	private final Collection<RecordInterceptor<K, V>> delegates = new ArrayList<>();

	@SafeVarargs
	@SuppressWarnings("varargs")
	public CompositeRecordInterceptor(RecordInterceptor<K, V>... delegates) {
		Assert.notNull(delegates, "'delegates' cannot be null");
		Assert.noNullElements(delegates, "'delegates' cannot have null entries");
		// 委派
		this.delegates.addAll(Arrays.asList(delegates));
	}

	@Override
	public ConsumerRecord<K, V> intercept(ConsumerRecord<K, V> record) {
		ConsumerRecord<K, V> recordToIntercept = record;
		for (RecordInterceptor<K, V> delegate : this.delegates) {
			recordToIntercept = delegate.intercept(recordToIntercept);
		}
		return recordToIntercept;
	}

}
