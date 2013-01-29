package org.qrone.api.util;

import java.util.UUID;

import org.qrone.util.Token;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MasterKey {
	public static MemcacheService mem
		= MemcacheServiceFactory.getMemcacheService("qrone.setting");
	public static DatastoreService store
		= DatastoreServiceFactory.getDatastoreService();
	public static UUID clientid;
	public static UUID clientsecret;

	public static UUID getClientid(){
		if(clientid == null){
			init();
		}
		return clientid;
	}

	public static UUID getSecret(){
		if(clientsecret == null){
			init();
		}
		return clientid;
	}
	
	public static void init(){
		
		// try memcache
		Object memobj = mem.get("secret");
		if(memobj != null){
			init(memobj.toString());
			return;
		}
		
		// try datastore
		Key key = KeyFactory.createKey("qrone.setting", "secret");
		try {
			Entity e = store.get(key);
			init( (String)e.getProperty("key") );
			
		} catch (EntityNotFoundException e) {
			clientid = UUID.randomUUID();
			clientsecret = UUID.randomUUID();
			Entity e2 = new Entity(key);
			String memstr = clientid.toString() + ":" + clientsecret.toString();
			
			e2.setProperty("key", memstr);
			store.put(e2);

			// store memcache
			mem.put("secret", memstr);
		}
		
		return;
	}
	
	public static void init(String memstr){
		String[] memary = memstr.split(":");
		clientid = UUID.fromString(memary[0]);
		clientsecret = UUID.fromString(memary[1]);
		return;
	}
	
}
