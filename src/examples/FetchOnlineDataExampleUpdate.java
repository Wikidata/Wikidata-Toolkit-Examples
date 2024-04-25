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
 * This example demonstrates fetching data from Wikidata.org and covers finding: single entities, multiple entites, and applying various filters to reduce the volume of data fetched. 
 * 
 *<ul>
  * <li> Fetching data for one entity: Fetches data for a single entity with the Qid "Q42" and prints the data. If this entity is an item it also prints the English label. </li>
  * <li> Fetching data for several entities: Fetches data for multiple entities with the Qids "Q80" and "P31" and prints their IDs. </li>
  * <li> Fetching data for entities applying filters: This example shows how to apply filters to reduce the volume of fetched data. It sets filters for: site links, language, and propertities, then fetches data for a single entity with Qid "Q8", then prints its French label and English Wikipedia page title. </li>
  * <li>Fetching by Page Title: Fetches data for a single entity with the page titles (e.g. "Terry Pratchett" on English Wikipedia), then prints the correspinding Qid. </li>
  *<li>Fetching by Multiple Page Titles: Fetches data for multiple entities with the page titles (e.g. "Wikidata" and "Wikipedia" on English Wikipedia), then prints the correspinding Qids. </li>
  *<li> Searching for Entities: Searches for entities with a specific label (e.g. "Douglas Adams") and prints the Qids and labels of the search results. </li>
  *</ul>
  *
  * @throws MediaWikiApiErrorException
  * @throws IOException

  * @author Markus Kroetzsch
 */

public class FetchOnlineDataExampleUpdate {

  public static void main(String[] args) throws MediaWikiApiErrorException, IOException {
    ExampleHelpers.configureLogging();
    FetchOnlineDataExampleUpdate.printDocumentation();
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
        .println("*** This application demonstrates fetching data from Wikidata.org and covers finding: single entities, multiple entites, and applying various filters to reduce the volume of data fetched.");
    System.out.println("*** It does not download any dump files.");
    System.out
        .println("********************************************************************");

  }
}
