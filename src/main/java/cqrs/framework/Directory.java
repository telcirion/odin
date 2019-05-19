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

import cqrs.concepts.common.IMessageHandler;
import cqrs.concepts.infra.IDirectory;

import java.util.ArrayList;
import java.util.List;

public class Directory implements IDirectory {
	
	class DispatcherEntry {
		final IMessageHandler dispatcher;
		DispatcherEntry(IMessageHandler dispatcher) {
			this.dispatcher = dispatcher;
		}
	}
	
	private final List<DispatcherEntry> directory=new ArrayList<>();
	
	@Override
	public List<IMessageHandler> getDispatchers() {
		List<IMessageHandler> l =new ArrayList<>();
		directory.forEach(a -> l.add(a.dispatcher));
		return l;
	}

	@Override
	public <T extends IMessageHandler> void registerHandler(T messageHandler) {
		directory.add(new DispatcherEntry(messageHandler));
	}
}
