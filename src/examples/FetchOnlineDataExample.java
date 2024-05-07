package examples;

/*
 * #%L
 * Wikidata Toolkit Examples
 * %%
 * Copyright (C) 2014 - 2015 Wikidata Toolkit Developers
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

/**
  * This example demonstrates fetching data from the Wikidata.org API by their QID, title, or query and applying various filters to reduce the volume of data returned. 
  * 
  * This example does not download any dump files.
 *<ul>
  * <li> Fetches data for a single entity (& multiple entities) using a QID (unique Wikimedia identifier) and prints its title.</li>
  * <li> Fetches data for multiple entities using site-key ("enwiki") and multiple titles and prints their QID's </li>
  * <li> Searches for entities using a query (e.g. "Douglas Adams") and which language Wiki (or "IRI") to search, then prints the QID's and labels of the results. </li>
  * <li> Fetches data for a single entity using its QID, but limits the volume of data returned by first applying site-link, language, and property filters</li>
  *</ul>
  * 
  *
  * @throws MediaWikiApiErrorException
  * @throws IOException 

  * @author Markus Kroetzsch
 */

public class FetchOnlineDataExample {
	public static void main(String[] args) throws MediaWikiApiErrorException, IOException {
		ExampleHelpers.configureLogging();
		FetchOnlineDataExample.printDocumentation();

		WikibaseDataFetcher wbdf = new WikibaseDataFetcher(
				BasicApiConnection.getWikidataApiConnection(),
				Datamodel.SITE_WIKIDATA);

		FetchOnlineDataExample.fetchSingleEntityByQID(wbdf);
		FetchOnlineDataExample.fetchEntitiesByTitles(wbdf);
		FetchOnlineDataExample.fetchEntityResultsBySearchTerm(wbdf);
		FetchOnlineDataExample.fetchEntityWithAppliedFilters(wbdf);
	}

	/**
	 * Fetches data for a single entity using its QID (unique identifier).
	 * The fetched data is then written to selected ouputs and prints the 'en' text title to the console.	
	 */
	public static void fetchSingleEntityByQID(WikibaseDataFetcher wbdf) throws MediaWikiApiErrorException, IOException {
		System.out.println("*** Fetching data for one entity:");

		// Multiple entities can be fetched in one go by providing a list of QID's & using the getEntityDocuments() method.
		EntityDocument entityDocument = wbdf.getEntityDocument("Q42");

		if (entityDocument instanceof ItemDocument) {
			System.out.println("The English name for entity Q42 is: "
					+ ((ItemDocument) entityDocument).getLabels().get("en").getText());
		}
	}

	/**
	 * Fetches data for multiple enetities using their titles.
	 * The fetched data is then written to selected ouputs and prints the unique QID to the console.
	 */
	public static void fetchEntitiesByTitles(WikibaseDataFetcher wbdf) throws MediaWikiApiErrorException, IOException {
		System.out.println("*** Fetching data for entities by page titles:");

		// Similar to above, single entities can be fetched by providing a single title & using the getEntityDocumentsByTitle() method.
		Map<String, EntityDocument> entityDocuments = wbdf.getEntityDocumentsByTitle("enwiki", "Terry Pratchett",
				"Neil Gaiman");

		for (Entry<String, EntityDocument> entry : entityDocuments.entrySet()) {
			System.out.println("The QID for the entity with page title \""
					+ entry.getKey() + "\" is: " + entry.getValue().getEntityId().getId());
		}
	}

	/**
	 * Fetches results data for whichever entities match the search term for language code provided.
	 * The resultant data is then written to selected ouputs and prints the QID and label of the search results to the console.
	 */
	public static void fetchEntityResultsBySearchTerm(WikibaseDataFetcher wbdf)
			throws MediaWikiApiErrorException, IOException {
		System.out.println("*** Doing search on Wikidata for: Douglas Adams");

		// Search for entities using a search term and language code.
		List<WbSearchEntitiesResult> searchResults = wbdf.searchEntities("Douglas Adams", "fr");
		for (WbSearchEntitiesResult result : searchResults) {
			System.out.println("Found entity with QID " + result.getEntityId() + " and label \""
					+ result.getLabel() + "\".");
		}
	}

	/**
	 * Fetches data for a single entity using its QID and applies filters to reduce the volume of data returned.
	 * The fetched data is then written to selected ouputs and prints the French label and English Wikipedia page title to the console.
	 */
	public static void fetchEntityWithAppliedFilters(WikibaseDataFetcher wbdf)
			throws MediaWikiApiErrorException, IOException {
		System.out.println("*** Fetching data using filters to reduce data volume:");

		// apply filters to data to get only what we want...
		wbdf.getFilter().setSiteLinkFilter(Collections.singleton("enwiki")); // Only site links from English Wikipedia
		wbdf.getFilter().setLanguageFilter(Collections.singleton("fr")); // Only labels in French
		wbdf.getFilter().setPropertyFilter(Collections.emptySet()); // No statements at all
		// Fetch the entity data
		EntityDocument q8 = wbdf.getEntityDocument("Q8");

		if (q8 instanceof ItemDocument) {
			System.out.println("The French label for entity Q8 is "
					+ ((ItemDocument) q8).getLabels().get("fr").getText()
					+ "\nand its English Wikipedia page has the title "
					+ ((ItemDocument) q8).getSiteLinks().get("enwiki").getPageTitle() + ".");
		}
	}

	/**
	* Prints some basic documentation about this program.
	*/
	public static void printDocumentation() {
		System.out
				.println("********************************************************************");
		System.out.println("*** Wikidata Toolkit: FetchOnlineDataExample");
		System.out.println("*** ");
		System.out
				.println(
						"*** This example demonstrates fetching data from the Wikidata.org API by their QID, title, or query and applying various filters to reduce the volume of data returned.");
		System.out.println("*** It does not download any dump files.");
		System.out
				.println("********************************************************************");
	}
}