algorithmia-android
===================

Java client for accessing Algorithmia's algorithm marketplace and data APIs from Android applications.

Note: because our java client depends on Apache HTTP Client, it is not compatible with the Android Runtime. In early versions of android, a legacy version of apache http client came pre-bundled, but was not updated over time. In recent android versions (6.0+) it was removed entirely. The algorithmia-android client uses native HttpURLConnection as its underlying client, as recommended by the Android documentation.

For API documentation, see the [JavaDocs](https://algorithmia.com/docs/lang/java)

[![Build Status](https://api.shippable.com/projects/557f23a8edd7f2c052184a2d/badge/master)](https://app.shippable.com/projects/557f23a8edd7f2c052184a2d)

[![Latest Release](https://img.shields.io/maven-central/v/com.algorithmia/algorithmia-client.svg)](http://repo1.maven.org/maven2/com/algorithmia/algorithmia-client/)

# Getting started

The Algorithmia android client is published to Maven central and can be added as a dependency in Android Studio. To add the Algorithmia android client, add the following line to your app/build.gradle file:

```
  compile "com.algorithmia:algorithmia-android:1.0.1"
```

Instantiate a client using your API Key:

```java
AlgorithmiaClient client = Algorithmia.client(apiKey);
```

Notes:
- API key may be omitted only when making calls from algorithms running on the Algorithmia cluster
- Using version range `[,1.1.0)` is recommended as it implies using the latest backward-compatible bugfixes.

## Calling Algorithms

Algorithms are called with the `pipe` method using

```java
Algorithm addOne = client.algo("docs/JavaAddOne");
AlgoResponse response = addOne.pipe(41);
Integer result = response.as(new TypeToken<Integer>(){});
Double durationInSeconds = response.getMetadata().duration;
```

If you already have serialzied JSON, you can call call `pipeJson` instead:

```java
Algorithm foo = client.algo("")
String jsonWords = "[\"transformer\", \"terraforms\", \"retransform\"]"
AlgoResponse response = addOne.pipeJson(jsonWords)
```

## Working with Data

The Algorithmia Java client also provides a way to manage both Algorithmia hosted data
and data from Dropbox or S3 accounts that you've connected to you Algorithmia account.

This client provides a `DataFile` type (generally created by `client.file(uri)`)
and a `DataDir` type (generally created by `client.dir(uri)`) that provide
methods for managing your data.

### Create directories

Create directories by instantiating a `DataDirectory` object and calling `create()`:

```java
DataDirectory robots = client.dir("data://.my/robots");
robots.create();

DataDirectory dbxRobots = client.dir("dropbox://robots");
dbxRobots.create();
```

### Upload files to a directory

Upload files by calling `put` on a `DataFile` object, or by calling `putFile` on a `DataDirectory` object.

```java
DataDirectory robots = client.dir("data://.my/robots");

// Upload local file
robots.putFile(new File("/path/to/Optimus_Prime.png"));
// Write a text file
robots.file("Optimus_Prime.txt").put("Leader of the Autobots");
// Write a binary file
robots.file("Optimus_Prime.key").put(new byte[] { (byte)0xe0, 0x4f, (byte)0xd0, 0x20 });
```

### Download contents of file

Download files by calling `getString`, `getBytes`, or `getFile` on a DataFile object:

```java
DataDirectory robots = client.dir("data://.my/robots");

// Download file and get the file handle
File t800File = robots.file("T-800.png").getFile();

// Get the file's contents as a string
String t800Text = robots.file("T-800.txt").getString();

// Get the file's contents as a byte array
byte[] t800Bytes = robots.file("T-800.png").getBytes();
```

### Delete files and directories

Delete files and directories by calling `delete` on their respective `DataFile` or `DataDirectory` object.
`DataDirectories` take an optional `force` parameter that indicates whether the directory should be deleted
if it contains files or other directories.

```java
client.file("data://.my/robots/C-3PO.txt").delete();
client.dir("data://.my/robots").delete(false);
```

### List directory contents

Iterate over the contents of a directory using the iterator returned by calling `files`, or `dirs` on a `DataDirectory` object:

```java
// List top level directories
DataDirectory myRoot = client.dir("data://.my");
for(DataDirectory dir : myRoot.dirs()) {
    System.out.println("Directory " + dir.toString() + " at URL " + dir.url());
}

// List files in the 'robots' directory
DataDirectory robots = client.dir("data://.my/robots");
for(DataFile file : robots.files()) {
    System.out.println("File " + file.toString() + " at URL: " + file.url());
}
```

### Manage directory permissions

Directory permissions may be set when creating a directory, or may be updated on already existing directories.

```java
DataDirectory fooLimited = client.dir("data://.my/fooLimited");

// Create the directory as private
fooLimited.create(DataAcl.PRIVATE);

// Update a directory to be public
fooLimited.updatePermissions(DataAcl.PUBLIC);

// Check a directory's permissions
if (fooLimited.getPermissions().getReadPermissions() == DataAclType.PRIVATE) {
    System.out.println("fooLimited is private");
}
```
