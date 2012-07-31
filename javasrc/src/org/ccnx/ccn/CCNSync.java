/*
 * Part of the CCNx Java Library.
 *
 * Copyright (C) 2012 Palo Alto Research Center, Inc.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.ccnx.ccn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.ccnx.ccn.config.ConfigurationException;
import org.ccnx.ccn.impl.support.Log;
import org.ccnx.ccn.impl.sync.ProtocolBasedSyncMonitor;
import org.ccnx.ccn.impl.sync.SyncMonitor;
import org.ccnx.ccn.io.content.ConfigSlice;
import org.ccnx.ccn.io.content.ConfigSlice.Filter;
import org.ccnx.ccn.protocol.ContentName;

public class CCNSync {
	
	protected SyncMonitor syncMon = null;
	
	public void startSync(CCNHandle handle, ConfigSlice syncSlice, CCNSyncHandler syncCallback, byte[] startHash, ContentName startName) throws IOException, ConfigurationException{
		try {
			syncSlice.checkAndCreate(handle);
			if (syncMon == null)
				syncMon = new ProtocolBasedSyncMonitor(handle);
			syncMon.registerCallback(syncCallback, syncSlice);
		} catch (Exception e) {
			Log.warning(Log.FAC_REPO, "Error when starting sync for slice: {0}", syncSlice);
			throw new IOException("Unable to create sync slice: "+e.getMessage());
		}
	}
		
	public ConfigSlice startSync(CCNHandle handle, ContentName topo, ContentName prefix, Collection<ContentName> filters, CCNSyncHandler syncCallback) throws IOException, ConfigurationException{
		if (handle == null)
			handle = CCNHandle.getHandle();
		Collection<Filter> f = new ArrayList<Filter>();
		if (filters!=null) {
			for (ContentName cn: filters)
				f.add(new Filter(cn));
		}
		try {
			ConfigSlice slice = ConfigSlice.checkAndCreate(topo, prefix, f, handle);
			if (syncMon == null)
				syncMon = new ProtocolBasedSyncMonitor(handle);
			syncMon.registerCallback(syncCallback, slice);
			return slice;
		} catch (Exception e) {
			Log.warning(Log.FAC_REPO, "Error when starting sync for slice with prefix: {0}", prefix);
			throw new IOException("Unable to create sync slice: "+e.getMessage());
		}
	}
	
	public ConfigSlice startSync(ContentName topo, ContentName prefix, Collection<ContentName> filters, CCNSyncHandler syncCallback) throws IOException, ConfigurationException{
		return startSync(null, topo, prefix, filters, syncCallback);
	}
	
	public void stopSync(CCNSyncHandler syncHandler, ConfigSlice syncSlice) throws IOException{
		//will unregister the callback here
		syncMon.removeCallback(syncHandler, syncSlice);
	}

}
