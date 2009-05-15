package test.ccn.network.daemons.repo;

import java.io.File;

import org.junit.BeforeClass;

import com.parc.ccn.data.ContentName;

import test.ccn.library.LibraryTestBase;

/**
 * 
 * @author rasmusse
 *
 */

public class RepoTestBase extends LibraryTestBase {
	
	public static final String TOP_DIR = "ccn.test.topdir";
	
	protected static String _topdir;
	protected static String _fileTestDir = "repotest";
	protected static String _repoName = "TestRepository";
	protected static String _globalPrefix = "/parc.com/csl/ccn/repositories";
	protected static File _fileTest;
	protected static ContentName testprefix = ContentName.fromNative(new String[]{"repoTest","pubidtest"});
	protected static ContentName keyprefix = ContentName.fromNative(testprefix,"keys");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Let default logging level be set centrally so it can be overridden by property
		_topdir = System.getProperty(TOP_DIR);
		if (null == _topdir)
			_topdir = ".";
	}

}
