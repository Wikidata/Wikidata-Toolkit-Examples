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
 * This example demonstrates fetching data from the Wikidata.org API and covers fetching entity data, and applying various filters to reduce the volume of data returned. 
 * 
 *<ul>
  * <li> Fetching data for a single entity, or multiple entities, using entity Qids (e.g. "Q42" and "P31"). </li>
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

    WikibaseDataFetcher wbdf = new WikibaseDataFetcher(
        BasicApiConnection.getWikidataApiConnection(),
        Datamodel.SITE_WIKIDATA);

    fetchEntityDataByQid(wbdf);
    fetchEntitiesDataByQid(wbdf);
  }

  /**
  * This method fetches data for a single entity from the Wikidata.org API using its QID (unique identifier).
  * The fetched data is then written to a file and the English name for the entity is printed to the console.
  *
  * @param wbdf An instance of WikibaseDataFetcher that is used to fetch the data.
  * @throws MediaWikiApiErrorException If there is an error while fetching the data from the API.
  * @throws IOException If there is an error while writing the fetched data to a file.
  */
  public static void fetchEntityDataByQid(WikibaseDataFetcher wbdf) throws MediaWikiApiErrorException, IOException {
    System.out.println("*** Fetching data for one entity:");
    EntityDocument q42 = wbdf.getEntityDocument("Q42");
    writeEntityDataToFile(q42, "entity-Q42.txt");
    if (q42 instanceof ItemDocument) {
      System.out.println("The English name for entity Q42 is "
          + ((ItemDocument) q42).getLabels().get("en").getText());
    }
    System.out.println("Raw data for entity Q42 written to file entity-Q42.txt");
  }

  /**
  * This method fetches data for multiple entities from the Wikidata.org API using their QID's (unique identifier).
  * The fetched data is then written to a file and the Qid's are printed to the console.
  *
  * @param wbdf An instance of WikibaseDataFetcher that is used to fetch the data.
  * @throws MediaWikiApiErrorException If there is an error while fetching the data from the API.
  * @throws IOException If there is an error while writing the fetched data to a file.
  */
  public static void fetchEntitiesDataByQid(WikibaseDataFetcher wbdf) throws MediaWikiApiErrorException, IOException {
    System.out.println("*** Fetching data for several entities:");
    Map<String, EntityDocument> results = wbdf.getEntityDocuments("Q80",
        "P31");
    // Keys of this map are Qids, but we only use the values here:
    for (EntityDocument ed : results.values()) {
      System.out.println("Successfully retrieved data for "
          + ed.getEntityId().getId());
    }
    writeEntityDataToFile(results.values().toArray(new EntityDocument[0]), "entities-Q80-P31.txt");
    System.out.println("Raw data for entities Q80 and P31 written to file entities-Q80-P31.txt");
  }

  /**
   * This method writes an array of EntityDocument objects to a file. It uses a PrintStream to write each 
   * EntityDocument to the file specified by fileName. If an IOException occurs during this process, it prints 
   * the stack trace.
   *
   * @param entityDocuments An array of EntityDocument objects to be written to the file.
   * @param fileName The name of the file to which the entity data will be written.
   */
  private static void writeEntityDataToFile(EntityDocument[] entityDocuments, String fileName) {
    try (PrintStream out = new PrintStream(ExampleHelpers.openExampleFileOuputStream(fileName))) {
      for (EntityDocument entityDocument : entityDocuments) {
        out.println(entityDocument);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method writes a single EntityDocument object to a file. It uses a PrintStream to write the 
   * EntityDocument to the file specified by fileName. If an IOException occurs during this process, it prints 
   * the stack trace.
   *
   * @param entityDocument An EntityDocument object to be written to the file.
   * @param fileName The name of the file to which the entity data will be written.
   */
  private static void writeEntityDataToFile(EntityDocument entityDocument, String fileName) {
    try (PrintStream out = new PrintStream(ExampleHelpers.openExampleFileOuputStream(fileName))) {
      out.println(entityDocument);
    } catch (IOException e) {
      e.printStackTrace();
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
