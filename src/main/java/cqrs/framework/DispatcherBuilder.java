// Copyright 2019 Peter Jansen
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package cqrs.framework;

import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IDispatcherBuilder;
import cqrs.concepts.common.IMessageAction;

import java.util.HashMap;
import java.util.Map;

public class DispatcherBuilder implements IDispatcherBuilder {
	
	private final Map<Class<?>, IMessageAction<?>> msgActions = new HashMap<>();
	
	@Override
	public <T> IDispatcherBuilder dispatch(Class<T> msgClazz, IMessageAction<T> msgAction) {
		msgActions.put(msgClazz, msgAction);
		return this;
	}

	@Override
	public IDispatcher build() {
		return new Dispatcher(msgActions);
	}
}