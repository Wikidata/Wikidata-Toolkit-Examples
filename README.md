# Wikidata Toolkit Examples

This is an example project that shows how to set up a Java project that
uses [Wikidata Toolkit](https://github.com/Wikidata/Wikidata-Toolkit).
It contains several simple example programs and bots in the source directory.

What's found in this repository
-------------------------------

The individual examples are documented in the README file of each package.


Running examples using an IDE
-----------------------------

You can import the project into any Java IDE that supports Maven (and maybe git)
and run the example programs from there. Wikidata Toolkit provides detailed
[instructions on how to set up Eclipse for using Maven and git](https://www.mediawiki.org/wiki/Wikidata_Toolkit/Eclipse_setup).


Running examples directly using Maven
-------------------------------------

You can also run the code directly using Maven from the command line. For this,
you need to have Maven and (obviously) Java installed. To compile the project
and obtain necessary dependencies, run

```mvn compile```

Thereafter, you can run any individual example using its Java class name, for
example:

```mvn exec:java -Dexec.mainClass="examples.FetchOnlineDataExample"```

Credits and License
-------------------

This project is copied from the [Wikidata Toolkit](https://github.com/Wikidata/Wikidata-Toolkit) examples module.
Authors can be found there.

License: [Apache 2.0](LICENSE)

