JSON_Utilities

This tool includes 2 simple functions that, when combined, provide a powerful tool for converting multiple csv files that have a common key into nested JSON files.
The 2 functions are:

1 - convert csv to JSON.  It does what many others do here, although it does bring over 1 set of nested data as well.

2 - merge 2 JSON files.  Where 2 JSON files have a common key field, nested data from 1 can be merged into the other using this function.

By implementing a command line script input mechanism multiple operations can be strung together with a single command line parameter.  Use this to convert a single csv to JSON, or to take a set of different tables (with a common key) from your database and merge them into a single JSON document.

There's also a handy UI - if you run the JAR without passing in a parameter you get a Swing UI (hand cranked, so be nice!).  Here you can perform the operations manually.
