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
import java.io.PrintStream;
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
  * This example demonstrates fetching data from the Wikidata.org API including fetching entity data and applying various filters to reduce the volume of data returned. 
  * 
  * This example does not download any dump files.
 *<ul>
  * <li> Fetching data for a single entity and multiple entities, using entity Qids (e.g. "Q42" and "P31"). </li>
  * <li> Fetching by Page Title: Fetches data for a single entity with the page titles (e.g. "Terry Pratchett" on English Wikipedia), then prints the correspinding Qid. </li>
  * <li> Searching for Entities: Searches for entities using a search term (e.g. "Douglas Adams") and which language Wiki (or "iri") to search (e.g. "fr"), then prints the Qids and labels of the search results. </li>
  * <li> Fetching data for entities and applying filters to limit the volume of data returned. It sets filters for: site links, language, and propertities, then fetches data for a single entity with Qid "Q8", then prints its French label and English Wikipedia page title. </li>
  *</ul>
  * 
  * @implNote The results are written to the "/results/" directory in the project root.
  *
  * @throws MediaWikiApiErrorException
  * @throws IOException

  * @author Markus Kroetzsch
 */

public class FetchOnlineDataExampleUpdate {
  private static boolean printOutputToResultsDirectory = false; // set to true to print the output in the results directory 

  public static void main(String[] args) throws MediaWikiApiErrorException, IOException {
    ExampleHelpers.configureLogging();
    FetchOnlineDataExampleUpdate.printDocumentation();

    WikibaseDataFetcher wbdf = new WikibaseDataFetcher(
        BasicApiConnection.getWikidataApiConnection(),
        Datamodel.SITE_WIKIDATA);

    fetchEntityDataByQid(wbdf); // get entities using their Qids...
    fetchEntitiesByTitles(wbdf); // get entities using their page titles...
    fetchEntityResultBySearchTerm(wbdf); // search entities using query text...
    fetchEntityAndApplyFilters(wbdf); // get entities applying filters...
  }

  /**
  * This method fetches data for a single entity from the Wikidata.org API using its QID (unique identifier).
  * The fetched data is then written to a file and the English name for the entity is printed to the console.
  *
  * @param wbdf An instance of WikibaseDataFetcher that is used to fetch the data.
  * @throws MediaWikiApiErrorException If there is an error while fetching the data from the API.
  * @throws IOException If there is an error while writing the fetched data to a file.
  
    @see #WikibaseDataFetcher.getEntityDocuments(String[] qids)
  */
  public static void fetchEntityDataByQid(WikibaseDataFetcher wbdf) throws MediaWikiApiErrorException, IOException {
    System.out.println("*** Fetching data for one entity:");

    //? Multiple entities can be fetched using the plural getEntityDocuments() method.
    EntityDocument q42 = wbdf.getEntityDocument("Q42");

    if (q42 instanceof ItemDocument) {
      writeEntityDataToFile(q42, "entity-Q42.txt");
      System.out.println("The English name for entity Q42 is "
          + ((ItemDocument) q42).getLabels().get("en").getText());
      System.out.println("Raw data for entity Q42 written to file entity-Q42.txt");
    } else {
      System.out.println("Entity Q42 was not found!");
    }
  }

  /**
  * This method fetches data for a single entity from the Wikidata.org API using an entities title.
  * The fetched data is then written to a file and the Qid's and labels of the search results are printed to the console.
  * 
  * @param wbdf An instance of WikibaseDataFetcher that is used to fetch the data.
  * @throws MediaWikiApiErrorException If there is an error while fetching the data from the API.
  * @throws IOException If there is an error while writing the fetched data to a file.
  * 
  * @see #WikibaseDataFetcher.getEntityDocumentByTitle(String siteIri, String pageTitle)
  */
  public static void fetchEntitiesByTitles(WikibaseDataFetcher wbdf) throws MediaWikiApiErrorException, IOException {
    System.out.println("*** Fetching data based on page title:");

    //? Singular entities, similar to above, can be fetched using the singular getEntityDocumentByTitle() method.
    Map<String, EntityDocument> results = wbdf.getEntityDocumentsByTitle("enwiki",
        "Terry Pratchett", "Neil Gaiman");
    for (Entry<String, EntityDocument> entry : results.entrySet()) {
      writeEntityDataToFile(entry.getValue(), "entity-" + entry.getKey() + ".txt");
      System.out.println("The Qid of " + entry.getKey() + " is " + entry.getValue().getEntityId().getId());
      System.out
          .println("Raw data for entity " + entry.getKey() + " written to file entity-" + entry.getKey() + ".txt");
    }
  }

  /**
  * This method fetches data for a single entity from the Wikidata.org API using a search term and language target (a.k.a iri).
  * The fetched data's Qid and labels are then printed to the console.
  * 
  * @param wbdf An instance of WikibaseDataFetcher that is used to fetch the data.
  * @throws MediaWikiApiErrorException If there is an error while fetching the data from the API.
  * @throws IOException If there is an error while writing the fetched data to a file.
  */
  public static void fetchEntityResultBySearchTerm(WikibaseDataFetcher wbdf)
      throws MediaWikiApiErrorException, IOException {
    System.out.println("*** Searching for entities matching: 'Douglas Adams' ");
    try (PrintStream out = new PrintStream(ExampleHelpers.openExampleFileOuputStream("search-results.txt"))) {
      for (WbSearchEntitiesResult result : wbdf.searchEntities("Douglas Adams", "fr")) {
        writeSearchResultsToFile(result, out);
        System.out.println("Found result " + result.getLabel() + " with Qid " + result.getTitle() + ".");
      }
    }
    System.out.println("Search results written to file search-results.txt");
  }

  /**
  * This method fetches data for a single entity, then applies filters to limit the data by selecting only site links from English Wikipedia, and labels in French which have no statements at all.
  * The fetched data is then written to a file and the French label and English Wikipedia page title are printed to the console.
  *
  * @param wbdf An instance of WikibaseDataFetcher that is used to fetch the data.
  * @throws MediaWikiApiErrorException If there is an error while fetching the data from the API.
  * @throws IOException If there is an error while writing the fetched data to a file.
  */
  public static void fetchEntityAndApplyFilters(WikibaseDataFetcher wbdf)
      throws MediaWikiApiErrorException, IOException {
    System.out.println("*** Fetching data for entities applying filters:");

    // apply filters to fetched data to limit results
    wbdf.getFilter().setSiteLinkFilter(Collections.singleton("enwiki")); // Only site links from English Wikipedia
    wbdf.getFilter().setLanguageFilter(Collections.singleton("fr")); // Only labels in French
    wbdf.getFilter().setPropertyFilter(Collections.emptySet()); // No statements at all
    EntityDocument q8 = wbdf.getEntityDocument("Q8");

    if (q8 instanceof ItemDocument) {
      writeEntityDataToFile(q8, "entity-Q8.txt");
      System.out.println("The French label for entity Q8 is "
          + ((ItemDocument) q8).getLabels().get("fr").getText()
          + "\nand its English Wikipedia page has the title "
          + ((ItemDocument) q8).getSiteLinks().get("enwiki").getPageTitle() + ".");
      System.out.println("Raw data for entity Q8 written to file entity-Q8.txt");
    } else {
      System.out.println("Entity Q8 was not found!");
    }
  }

  /**
  * This method writes a single EntityDocument object to a file. It uses a PrintStream to write the 
  * EntityDocument to the file specified by fileName. If an IOException occurs during this process, it prints 
  * the stack trace.
  *
  * @param entityDocument An EntityDocument object to be written to the file.
  * @param fileName The name of the file to which the entity data will be written.
  *
  * @implNote The file is written to the "/results/" directory in the project root.
  */
  private static void writeEntityDataToFile(EntityDocument entityDocument, String fileName) {
    if (!printOutputToResultsDirectory) {
      System.out.println(entityDocument);
    } else {
      try (PrintStream out = new PrintStream(ExampleHelpers.openExampleFileOuputStream(fileName))) {
        out.println(entityDocument);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Writes the search results to the specified PrintStream.
   *
   * @param result The WbSearchEntitiesResult object containing the search results.
   * @param out The PrintStream to write the results to.
   */
  private static void writeSearchResultsToFile(WbSearchEntitiesResult result, PrintStream out) {
    String output = "RESULT " + result.getTitle() + " DETAILS:" +
        "\nconcept_uri:" + result.getConceptUri() +
        "\ndescription:" + result.getDescription() +
        "\nentity_ID:" + result.getEntityId() +
        "\nlabel:" + result.getLabel() +
        "\npage_ID:" + result.getPageId() +
        "\nQID:" + result.getTitle() +
        "\nURL:" + result.getUrl() +
        "\n";
    if (!printOutputToResultsDirectory) {
      System.out.println(output);
    } else {
      out.println(output);
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
            "*** This example demonstrates fetching data from the Wikidata.org API and covers fetching entity data and applying various filters to reduce the volume of data returned.");
    System.out.println("*** It does not download any dump files.");
    System.out
        .println("********************************************************************");
  }
}
