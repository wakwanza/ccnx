package test.ccn.data.content;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import com.parc.ccn.data.ContentName;
import com.parc.ccn.data.ContentObject;
import com.parc.ccn.data.content.CollectionData;
import com.parc.ccn.data.security.PublisherKeyID;
import com.parc.ccn.data.util.CCNEncodableObject;
import com.parc.ccn.library.CCNLibrary;

public class CCNEncodableCollectionData extends CCNEncodableObject<CollectionData> {

	public CCNEncodableCollectionData(CCNLibrary library) {
		super(CollectionData.class, library);
	}
	
	public CCNEncodableCollectionData() {
		super(CollectionData.class, null);
	}

	public CCNEncodableCollectionData(CollectionData data, CCNLibrary library) {
		super(CollectionData.class, data, library);
	}
	
	public CCNEncodableCollectionData(CollectionData data) {
		super(CollectionData.class, data, null);
	}

	/**
	 * Construct an object from stored CCN data.
	 * @param type
	 * @param content The object to recover, or one of its fragments.
	 * @param library
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public CCNEncodableCollectionData(ContentObject content, CCNLibrary library) throws XMLStreamException, IOException {
		super(CollectionData.class, content, library);
	}
	
	public CCNEncodableCollectionData(ContentObject content) throws XMLStreamException, IOException {
		super(CollectionData.class, content, null);
	}

	/**
	 * Ambiguous. Are we supposed to pull this object based on its name,
	 *   or merely attach the name to the object which we will then construct
	 *   and save. Let's assume the former, and allow the name to be specified
	 *   for save() for the latter.
	 * @param type
	 * @param name
	 * @param library
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public CCNEncodableCollectionData(ContentName name, PublisherKeyID publisher, CCNLibrary library) throws XMLStreamException, IOException {
		super(CollectionData.class, name, publisher, library);
	}
	
	public CCNEncodableCollectionData(ContentName name, CCNLibrary library) throws XMLStreamException, IOException {
		super(CollectionData.class, name, library);
	}

	public CCNEncodableCollectionData(ContentName name) throws XMLStreamException, IOException {
		super(CollectionData.class, name, null);
	}
}