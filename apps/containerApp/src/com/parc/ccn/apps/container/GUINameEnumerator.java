package com.parc.ccn.apps.container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import org.junit.BeforeClass;
import org.junit.Test;

import com.parc.ccn.Library;
import com.parc.ccn.config.ConfigurationException;
import com.parc.ccn.data.ContentName;
import com.parc.ccn.data.MalformedContentNameStringException;
import com.parc.ccn.data.query.BasicNameEnumeratorListener;
import com.parc.ccn.library.CCNLibrary;
import com.parc.ccn.library.CCNNameEnumerator;

import junit.framework.Assert;

//public class NameEnumeratorTest extends BasePutGetTest implements BasicNameEnumeratorListener{
public class GUINameEnumerator implements BasicNameEnumeratorListener{
	
	//static CCNLibrary _library;
	static CCNLibrary putLibrary;
	static CCNLibrary getLibrary;
	static CCNNameEnumerator putne;
	static CCNNameEnumerator getne;
	static GUINameEnumerator net;
	
	
	public static Random rand = new Random();

	String namespaceString = "/parc.com";
	ContentName namespace;
	String name1String = "/parc.com/registerTest/name1";
	ContentName name1;
	String name2String = "/parc.com/registerTest/name2";
	ContentName name2;
	String name2aString = "/parc.com/registerTest/name2/namea";
	ContentName name2a;
	String name1StringDirty = "/parc.com/registerTest/name1TestDirty";
	ContentName name1Dirty;
	
	String prefix1String = "/parc.com/registerTest";
	String prefix1StringError = "/park.com/registerTest";
	//ArrayList<LinkReference> names;
	ArrayList<ContentName> names;
	ContentName prefix1;
	ContentName c1;
	ContentName c2;
	

	public GUINameEnumerator() 
	{	
		net = this;
		
		//this.RegisterName();
		//this.registerPrefix();
		//this.getCallback();
		//this.getCallbackDirty();	
	}
	
	public void RegisterName(){
		
		try{
			net.namespace = ContentName.fromNative(namespaceString);
			net.name1 = ContentName.fromNative(name1String);
			net.name2 = ContentName.fromNative(name2String);
			net.name2a = ContentName.fromNative(name2aString);
		}
		catch(Exception e){
			Assert.fail("Could not create ContentName from "+name1String +" or "+name2String);
		}
		
		putne.registerNameSpace(net.namespace);
		putne.registerNameForResponses(net.name1);
		putne.registerNameForResponses(net.name2);
		putne.registerNameForResponses(net.name2a);
		ContentName nullName = null;
		putne.registerNameForResponses(nullName);
		
		try{
			while(!putne.containsRegisteredName(net.name2a)){
				Thread.sleep(rand.nextInt(50));
			}
			
			//the names are registered...
			System.out.println("the names are now registered");
		}
		catch(InterruptedException e){
			System.err.println("error waiting for names to be registered by name enumeration responder");
		
		}
		
	}
	
	
	public void registerPrefix(){
		
		
		try{
			net.prefix1 = ContentName.fromNative(prefix1String);
		}
		catch(Exception e){
			System.err.println("Could not create ContentName from "+prefix1String);
		}
		
		System.out.println("registering prefix: "+net.prefix1.toString());
		
		try{
			getne.registerPrefix(net.prefix1);
		}
		catch(IOException e){
			System.err.println("error registering prefix");
			e.printStackTrace();
			
		}
		
	}
	
	
	public void getCallback(){
		
		int attempts = 0;
		try{
			while(net.names==null && attempts < 500){
				Thread.sleep(rand.nextInt(50));
				attempts++;
			}
			
			//we either broke out of loop or the names are here
			System.out.println("done waiting for results to arrive");
		}
		catch(InterruptedException e){
			System.err.println("error waiting for names to be registered by name enumeration responder");
			
		}
				
		for(ContentName cn: net.names){
			System.out.println("got name: "+cn.toString());
			//Assert.assertTrue(cn.toString().equals("/name1") || cn.toString().equals("/name2"));
		}
		
		//now add new name
		try{
			net.name1Dirty = ContentName.fromNative(name1StringDirty);
			putne.registerNameForResponses(net.name1Dirty);
			
			while(!putne.containsRegisteredName(net.name1Dirty)){
				Thread.sleep(rand.nextInt(50));
			}
				
			//the names are registered...
			System.out.println("the new name is now registered to trigger the dirty flag");
		}
		catch(InterruptedException e){
			System.err.println("error waiting for names to be registered by name enumeration responder");
			Assert.fail();
		}
		catch (MalformedContentNameStringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
	}

	
	public void getCallbackDirty(){

		
		net.names = null;
		int attempts = 0;
		try{
			while(net.names==null && attempts < 1000){
				Thread.sleep(rand.nextInt(50));
				attempts++;
			}
			
			//we either broke out of loop or the names are here
			System.out.println("done waiting for results to arrive");
		}
		catch(InterruptedException e){
			System.err.println("error waiting for names to be registered by name enumeration responder");

		}		

		for(ContentName cn: net.names){

			System.out.println("got name: "+cn.toString());

		}
	}
	

	public void cancelPrefix(){
		
		System.out.println("testing prefix cancel");
		
		
		ContentName prefix1Error = null;
		
		try{
		
			prefix1Error = ContentName.fromNative(prefix1StringError);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Could not create ContentName from "+prefix1String);
		}
		
		//try to remove a prefix not registered
		getne.cancelPrefix(prefix1Error);
		//remove the registered name
		getne.cancelPrefix(net.prefix1);
		//try to remove the registered name again
		getne.cancelPrefix(prefix1);
		
		net.names = null;
	}
	
	
	
	public void getCallbackNoResponse(){
				
		net.names = null;
		ContentName p1 = null;
		
		try{
			p1 = ContentName.fromNative(prefix1String+"NoNames");
		}
		catch(Exception e){
			System.err.println("Could not create ContentName from "+prefix1String+"NoNames");
		}
		
		System.out.println("registering prefix: "+p1.toString());
		
		try{
			getne.registerPrefix(p1);
		}
		catch(IOException e){
			System.err.println("error registering prefix");
			e.printStackTrace();
			
		}
	
		
		int attempts = 0;
		try{
			while(net.names==null && attempts < 100){
				Thread.sleep(rand.nextInt(50));
				attempts++;
			}
			//we either broke out of loop or the names are here
			System.out.println("done waiting for results to arrive");
			getne.cancelPrefix(p1);
		}
		catch(InterruptedException e){
			System.err.println("error waiting for names to be registered by name enumeration responder");
		}
		
	}
	
	
	
	public void setLibraries(CCNLibrary l1, CCNLibrary l2){
		putLibrary = l1;
		getLibrary = l2;
		putne = new CCNNameEnumerator(l1, net);
		getne = new CCNNameEnumerator(l2, net);
	}
    
	
	
	
	public static void setup(){
		System.out.println("Starting CCNNameEnumerator Test");
		net = new GUINameEnumerator();
		try {
			net.setLibraries(CCNLibrary.open(), CCNLibrary.open());
			//net.setLibrary(CCNLibrary.open());
			Library.logger().setLevel(Level.FINEST);
			//net.nameEnumeratorSetup();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public int handleNameEnumerator(ContentName p, ArrayList<ContentName> n) {
		
		System.out.println("got a callback!");
		
		net.names = n;
		System.out.println("here are the returned names: ");
	
		for(ContentName cn: net.names)
			System.out.println(cn.toString()+" ("+p.toString()+cn.toString()+")");
		
		return 0;
	}
	
	
	public ArrayList<ContentName> createNameList(){
	
		ArrayList<ContentName> n = new ArrayList<ContentName>();
		
		try{
			ContentName c1 = ContentName.fromNative("/name1");
			ContentName c2 = ContentName.fromNative("/name2");
			
			n.add(c1);
			n.add(c2);
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Could not create ContentName from "+prefix1String);
		}
		return n;
	}
	
}