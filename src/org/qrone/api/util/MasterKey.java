package org.qrone.api.util;

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
	public static Token token;

	public static Token getSecret(){
		
		// try static
		if(token != null){
			return token;
		}
		
		// try memcache
		Object secreto = mem.get("secret");
		if(secreto != null){
			token = Token.parse(secreto.toString());
		}
		
		// try datastore
		Key key = KeyFactory.createKey("qrone.setting", "secret");
		try {
			Entity e = store.get(key);
			String k = (String)e.getProperty("key");
			token = Token.parse(k);
			
			// store memcache
			mem.put("secret", token.toString());
			
		} catch (EntityNotFoundException e) {
			token = Token.generate("MASTER", null);
			Entity e2 = new Entity(key);
			e2.setProperty("key", token.toString());
			store.put(e2);

			// store memcache
			mem.put("secret", token.toString());
		}
		
		return token;
	}
	
}
