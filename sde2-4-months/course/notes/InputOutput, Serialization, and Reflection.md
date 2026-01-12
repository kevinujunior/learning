# Java I/O (java.io, java.nio, java.nio.file)

Java I/O is a fundamental concept for any SDE-2 role, dealing with how programs interact with external resources like files, networks, and other processes. Understanding the evolution from classic I/O to NIO and NIO.2 is crucial.

## 1. Classic I/O (`java.io`)

Classic I/O is stream-based, blocking, and primarily byte-oriented.

### 1.1 Byte Streams vs. Character Streams

The core distinction in `java.io` is between byte streams and character streams.

**Why:** Byte streams handle raw binary data (images, audio, executables), while character streams handle text data, respecting character encodings (UTF-8, UTF-16, etc.). Using byte streams for text can lead to corruption if encoding isn't handled correctly.

#### **Byte Streams (for raw binary data)**

-   Abstract base classes: `InputStream`, `OutputStream`
-   Read/write 8-bit bytes.

**Important Classes & Methods:**

| Class                  | Purpose                                                                             | Key Methods                                                                                                                                                                                                                                                                                               |
| :--------------------- | :---------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `FileInputStream`      | Reads bytes from a file.                                                            | `read()`: Reads a single byte. Returns -1 at EOF. <br/> `read(byte[] b)`: Reads into a byte array. Returns number of bytes read or -1. <br/> `read(byte[] b, int off, int len)`: Reads `len` bytes into `b` starting at `off`. <br/> `close()`: Releases system resources.                                    |
| `FileOutputStream`     | Writes bytes to a file.                                                             | `write(int b)`: Writes a single byte. <br/> `write(byte[] b)`: Writes an array of bytes. <br/> `write(byte[] b, int off, int len)`: Writes `len` bytes from `b` starting at `off`. <br/> `flush()`: Flushes buffered output. <br/> `close()`: Flushes and releases resources.                               |
| `BufferedInputStream`  | Adds buffering to an `InputStream` for performance.                                 | Inherits `read()` methods. Internal buffer reduces physical I/O operations.                                                                                                                                                                                                                               |
| `BufferedOutputStream` | Adds buffering to an `OutputStream` for performance.                                | Inherits `write()` methods. Internal buffer reduces physical I/O operations. `flush()` is crucial here.                                                                                                                                                                                                  |
| `DataInputStream`      | Reads primitive Java data types in a machine-independent way.                       | `readInt()`, `readDouble()`, `readUTF()`, etc.                                                                                                                                                                                                                                                            |
| `DataOutputStream`     | Writes primitive Java data types in a machine-independent way.                      | `writeInt()`, `writeDouble()`, `writeUTF()`, etc.                                                                                                                                                                                                                                                         |
| `ObjectInputStream`    | Deserializes objects.                                                               | `readObject()`: Reads an object from the stream. Throws `ClassNotFoundException`, `InvalidClassException`.                                                                                                                                                                                              |
| `ObjectOutputStream`   | Serializes objects (see Serialization section).                                     | `writeObject(Object obj)`: Writes an object to the stream.                                                                                                                                                                                                                                                |

**Practical Usage Example (Byte Streams):**

```java
import java.io.*;

public class ByteStreamExample {
    public static void main(String[] args) {
        String data = "Hello, Java I/O!";
        String filePath = "byte_output.txt";

        // Writing bytes
        try (FileOutputStream fos = new FileOutputStream(filePath);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(data.getBytes("UTF-8")); // Specify encoding for text
            System.out.println("Bytes written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reading bytes
        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            int byteRead;
            System.out.print("Bytes read: ");
            while ((byteRead = bis.read()) != -1) {
                System.out.print((char) byteRead);
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

#### **Character Streams (for text data)**

-   Abstract base classes: `Reader`, `Writer`
-   Read/write 16-bit Unicode characters.
-   Automatically handle character encoding conversions.

**Important Classes & Methods:**

| Class                 | Purpose                                                                                | Key Methods                                                                                                                                                                                                                                                                             |
| :-------------------- | :------------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `FileReader`          | Reads characters from a file, using default charset or specified.                        | `read()`: Reads a single character. Returns -1 at EOF. <br/> `read(char[] cbuf)`: Reads into a char array. <br/> `read(char[] cbuf, int off, int len)`: Reads `len` chars into `cbuf` starting at `off`. <br/> `close()`: Releases system resources.                                   |
| `FileWriter`          | Writes characters to a file, using default charset or specified.                         | `write(int c)`: Writes a single character. <br/> `write(char[] cbuf)`: Writes an array of characters. <br/> `write(String str)`: Writes a string. <br/> `flush()`: Flushes buffered output. <br/> `close()`: Flushes and releases resources.                                              |
| `BufferedReader`      | Adds buffering to a `Reader` and provides `readLine()` for efficient line-by-line reading. | `readLine()`: Reads a line of text. Returns null at EOF. <br/> Inherits `read()` methods.                                                                                                                                                                                               |
| `BufferedWriter`      | Adds buffering to a `Writer` for performance.                                          | `newLine()`: Writes a line separator. <br/> Inherits `write()` methods. `flush()` is important.                                                                                                                                                                                          |
| `InputStreamReader`   | Bridge from byte streams to character streams. Decodes bytes into characters.            | Constructor takes an `InputStream` and an optional `Charset` or charset name. `read()` methods.                                                                                                                                                                                        |
| `OutputStreamWriter`  | Bridge from character streams to byte streams. Encodes characters into bytes.            | Constructor takes an `OutputStream` and an optional `Charset` or charset name. `write()` methods.                                                                                                                                                                                      |
| `PrintWriter`         | Writes formatted representations of objects to a text-output stream.                     | `print()`, `println()`, `printf()`: Overloaded methods for various types. Optional auto-flushing.                                                                                                                                                                                       |

**Practical Usage Example (Character Streams):**

```java
import java.io.*;

public class CharacterStreamExample {
    public static void main(String[] args) {
        String content = "This is a test line.\nAnother line for character stream.";
        String filePath = "char_output.txt";

        // Writing characters
        try (FileWriter fw = new FileWriter(filePath);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
            System.out.println("Characters written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reading characters line by line
        try (FileReader fr = new FileReader(filePath);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            System.out.println("Characters read (line by line):");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 1.2 Blocking I/O

All `java.io` operations are **blocking**. When a thread initiates a read or write operation, it will halt execution until the I/O operation is complete.

**Why:** Simpler to program for sequential operations.
**Pitfalls:** Can lead to poor performance and scalability in applications handling many concurrent connections (e.g., servers), as each connection often requires its own thread blocked on I/O.

### 1.3 Common Traps & Best Practices for `java.io`

-   **Resource Management:** Always close streams to release system resources, even if exceptions occur. Use **try-with-resources** (Java 7+) to ensure automatic closing.
    ```java
    try (FileInputStream fis = new FileInputStream("file.txt")) {
        // use fis
    } catch (IOException e) {
        // handle exception
    } // fis is automatically closed here
    ```
-   **Buffering:** Always wrap raw streams (`FileInputStream`, `FileOutputStream`, `FileReader`, `FileWriter`) with buffered streams (`BufferedInputStream`, `BufferedOutputStream`, `BufferedReader`, `BufferedWriter`) for performance. Direct I/O operations are expensive.
-   **Character Encoding:** When dealing with text data and byte streams, *always* specify the character encoding (e.g., `data.getBytes("UTF-8")`, `new InputStreamReader(fis, "UTF-8")`). Not doing so relies on the platform's default encoding, which can lead to non-portable and corrupted data.
-   **`flush()`:** For `OutputStream` and `Writer`, remember to call `flush()` if you need to ensure data is written to the underlying device *before* the stream is closed or the buffer is full. `close()` automatically calls `flush()`.
-   **Error Handling:** I/O operations commonly throw `IOException`. Handle these gracefully.

### 1.4 Interview Questions

1.  What is the difference between `InputStream`/`OutputStream` and `Reader`/`Writer`? When would you use one over the other?
2.  Explain the purpose of `BufferedInputStream` and `BufferedReader`. How do they improve performance?
3.  Why is `try-with-resources` important for I/O operations?
4.  Describe a scenario where failing to specify character encoding could lead to data corruption.
5.  What does `flush()` do, and when is it important to call it explicitly?

## 2. New I/O (`java.nio`) - Non-blocking I/O

`java.nio` (New I/O), introduced in Java 1.4, provides a more flexible and efficient way to handle I/O, particularly for high-volume, concurrent operations like network servers. Its core components are Channels and Buffers.

**Why:** Addresses the scalability limitations of `java.io`'s blocking nature. Allows a single thread to manage multiple I/O operations.

### 2.1 Channels and Buffers

#### **Channels**

-   Represents an open connection to an I/O device (e.g., a file, a network socket).
-   Unlike streams, channels can be bi-directional (read and write).
-   Operations are primarily `read()` and `write()` which interact with `Buffer` objects.
-   Channels can operate in non-blocking mode.

**Important Interfaces & Classes:**

| Class/Interface      | Purpose                                                                     | Key Methods                                                                                                                                                                                                                                                         |
| :------------------- | :-------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `Channel`            | Base interface for all channels. `close()` is important.                    | `close()`: Closes the channel. <br/> `isOpen()`: Checks if the channel is open.                                                                                                                                                                                     |
| `ReadableByteChannel` | Reads bytes from a channel into a buffer.                                   | `read(ByteBuffer dst)`: Reads bytes from this channel into `dst`. Returns number of bytes read or -1 at EOF.                                                                                                                                                        |
| `WritableByteChannel` | Writes bytes from a buffer to a channel.                                    | `write(ByteBuffer src)`: Writes bytes from `src` to this channel. Returns number of bytes written.                                                                                                                                                                   |
| `FileChannel`        | For file I/O. Can perform direct byte transfers between files and memory (memory-mapped files) or other channels. | `open(Path path, OpenOption... options)`: Opens/creates a file channel. <br/> `read(ByteBuffer dst)`, `write(ByteBuffer src)`: Read/write to the channel. <br/> `position()`, `position(long newPosition)`: Get/set channel's file position. <br/> `truncate(long size)`: Truncates file. <br/> `force(boolean metaData)`: Ensures updates are written to storage. <br/> `map(...)`: Memory mapping. <br/> `transferFrom()`, `transferTo()`: Efficient direct data transfers. |
| `SocketChannel`      | For TCP network sockets. Can be set to non-blocking.                        | `open()`: Creates a new `SocketChannel`. <br/> `connect(SocketAddress remote)`, `finishConnect()`: For connecting. <br/> `read(ByteBuffer dst)`, `write(ByteBuffer src)`: Read/write to the socket. <br/> `configureBlocking(boolean block)`: Sets blocking mode.  |
| `ServerSocketChannel` | For listening for incoming TCP connections. Can be set to non-blocking.     | `open()`: Creates a new `ServerSocketChannel`. <br/> `bind(SocketAddress local)`: Binds the channel's socket to a local address. <br/> `accept()`: Accepts a new socket connection. Returns a `SocketChannel`. Can be non-blocking.                                  |
| `DatagramChannel`    | For UDP network sockets. Can be set to non-blocking.                        | `open()`: Creates a new `DatagramChannel`. <br/> `receive(ByteBuffer dst)`, `send(ByteBuffer src, SocketAddress target)`: Receive/send UDP datagrams.                                                                                                              |

#### **Buffers**

-   A container for a fixed amount of primitive data (e.g., `ByteBuffer`, `CharBuffer`, `IntBuffer`).
-   Provides a structured way to read/write data to/from channels.
-   Key properties: `capacity`, `limit`, `position`.
-   Supports direct and non-direct buffers. Direct buffers are often faster for I/O as they interact directly with native I/O operations without intermediate copies.

**Key Buffer Properties:**

-   **`capacity`**: The maximum number of elements the buffer can contain. (Fixed)
-   **`limit`**: The index of the first element that *should not be read or written*. When reading, `limit` is the number of elements available to read. When writing, `limit` is equal to `capacity`.
-   **`position`**: The index of the next element to be read or written.

**Buffer Operations (Lifecycle):**

1.  **Creation:** `ByteBuffer buffer = ByteBuffer.allocate(1024);` (creates a non-direct buffer)
    `ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024);` (creates a direct buffer)
2.  **Writing data:**
    -   `buffer.put(byte b);`
    -   `buffer.put(byte[] src);`
    -   `buffer.put(byte[] src, int offset, int length);`
3.  **`flip()`**: Prepares the buffer for reading. Sets `limit` to `position` and `position` to 0.
4.  **Reading data:**
    -   `byte b = buffer.get();`
    -   `buffer.get(byte[] dst);`
    -   `buffer.get(byte[] dst, int offset, int length);`
5.  **`clear()` / `compact()`**:
    -   `clear()`: Prepares the buffer for writing. Sets `position` to 0, `limit` to `capacity`. Discards any unread data.
    -   `compact()`: If there's unread data, copies it to the beginning of the buffer, then prepares for writing. Preserves unread data.

**Why different buffers?**
`ByteBuffer` is most common for channel I/O. `CharBuffer` for text processing. `IntBuffer`, `LongBuffer`, etc., for specific primitive arrays.

**Practical Usage Example (NIO File Copy):**

```java
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NioFileCopyExample {
    public static void main(String[] args) {
        Path sourcePath = Paths.get("source.txt");
        Path destPath = Paths.get("dest.txt");

        // Create a dummy source file
        try {
            java.nio.file.Files.write(sourcePath, "Hello from NIO!".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileChannel inChannel = FileChannel.open(sourcePath, StandardOpenOption.READ);
             FileChannel outChannel = FileChannel.open(destPath,
                     StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {

            ByteBuffer buffer = ByteBuffer.allocate(1024); // Allocate a buffer

            while (inChannel.read(buffer) != -1) { // Read from channel into buffer
                buffer.flip(); // Flip buffer for reading (limit = position, position = 0)
                outChannel.write(buffer); // Write from buffer to channel
                buffer.clear(); // Clear buffer for next write (position = 0, limit = capacity)
            }
            System.out.println("File copied using NIO Channels and Buffers.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 2.2 Non-blocking I/O (`Selector`)

`java.nio` introduces the concept of Non-blocking I/O, primarily via the `Selector` class.

**Why:** A `Selector` allows a single thread to monitor multiple `Channel`s for I/O events (e.g., data ready to be read, channel ready to accept new data, connection established). This is ideal for server applications that need to handle many client connections with minimal threads.

**How it works:**
1.  Register multiple `SelectableChannel`s (e.g., `SocketChannel`, `ServerSocketChannel`) with a `Selector`.
2.  Each registration returns a `SelectionKey`, which represents the channel's registration with the selector.
3.  Specify `interest sets` for each `SelectionKey` (what events you're interested in: `OP_READ`, `OP_WRITE`, `OP_CONNECT`, `OP_ACCEPT`).
4.  Call `selector.select()`: This method blocks until one or more registered channels are ready for an event (or a timeout occurs).
5.  Get the `selected keys` (channels ready for an event) and process them.

**Important Classes & Methods:**

| Class/Interface      | Purpose                                                                                | Key Methods                                                                                                                                                                                                                                                                                                            |
| :------------------- | :------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Selector`           | A multiplexor of `SelectableChannel` objects. Allows monitoring multiple channels with one thread. | `open()`: Creates a new selector. <br/> `select()`: Blocks until at least one channel is ready. <br/> `select(long timeout)`: Blocks with timeout. <br/> `selectNow()`: Non-blocking check for ready channels. <br/> `selectedKeys()`: Returns a set of `SelectionKey` objects for ready channels. <br/> `wakeup()`: Causes a blocked `select()` to return. <br/> `close()`: Closes the selector. |
| `SelectableChannel`  | The base class for channels that can be multiplexed by a selector.                     | `configureBlocking(boolean block)`: Sets channel blocking mode (false for non-blocking). <br/> `register(Selector sel, int ops, Object att)`: Registers the channel with the selector for specific operations, with an optional attachment. <br/> `keyFor(Selector sel)`: Retrieves the key for this channel's registration. |
| `SelectionKey`       | Represents a `SelectableChannel`'s registration with a `Selector`.                     | `channel()`: Returns the channel for this key. <br/> `selector()`: Returns the selector for this key. <br/> `interestOps()`: Returns the interest set. <br/> `readyOps()`: Returns the ready set (what operations are currently ready). <br/> `attach(Object ob)`, `attachment()`: Attach/retrieve an object (e.g., a buffer, a state object) to the key. <br/> `isAcceptable()`, `isConnectable()`, `isReadable()`, `isWritable()`: Convenience methods for checking ready operations. |
| `ServerSocketChannel`| Can be configured as non-blocking to accept connections using a Selector.              | (See above) `configureBlocking(false)`, `register(selector, SelectionKey.OP_ACCEPT)`                                                                                                                                                                                                                                    |
| `SocketChannel`      | Can be configured as non-blocking to read/write data using a Selector.                 | (See above) `configureBlocking(false)`, `register(selector, SelectionKey.OP_READ \| SelectionKey.OP_WRITE)`                                                                                                                                                                                                             |

**Practical Usage Example (NIO Echo Server - simplified concept):**

```java
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;

public class NioEchoServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 8080));
        serverSocket.configureBlocking(false); // Crucial for non-blocking

        serverSocket.register(selector, SelectionKey.OP_ACCEPT); // Register for accept events

        System.out.println("NIO Echo Server started on port 8080.");

        ByteBuffer buffer = ByteBuffer.allocate(256); // Reusable buffer

        while (true) {
            selector.select(); // Blocks until an event occurs

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    // A new client connection is ready to be accepted
                    SocketChannel client = serverSocket.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ); // Register client for read events
                    System.out.println("Accepted connection from " + client.getRemoteAddress());
                } else if (key.isReadable()) {
                    // Data is ready to be read from a client channel
                    SocketChannel client = (SocketChannel) key.channel();
                    buffer.clear(); // Clear buffer to prepare for reading
                    int bytesRead = client.read(buffer);

                    if (bytesRead == -1) { // Client disconnected
                        client.close();
                        System.out.println("Client disconnected: " + client.getRemoteAddress());
                    } else if (bytesRead > 0) {
                        buffer.flip(); // Flip buffer for writing
                        client.write(buffer); // Echo back the data
                        System.out.println("Echoed " + bytesRead + " bytes to " + client.getRemoteAddress());
                    }
                }
                keyIterator.remove(); // Remove key to avoid re-processing
            }
        }
    }
}
```

### 2.3 Performance and Security Considerations for `java.nio`

-   **Performance:**
    -   **Scalability:** `java.nio` with `Selector` is highly scalable for handling many concurrent connections with few threads, making it suitable for high-performance servers.
    -   **Direct Buffers:** `ByteBuffer.allocateDirect()` creates buffers outside the JVM heap. This can improve performance by avoiding data copying between the JVM and native operating system I/O calls, but they are generally slower to allocate and deallocate.
    -   **`transferTo()`/`transferFrom()`:** `FileChannel`'s `transferTo()` and `transferFrom()` methods are highly optimized for direct data transfer between channels (e.g., file to socket), leveraging OS kernel features (zero-copy) to avoid user-space buffering and copying, leading to significant performance gains.
-   **Security:**
    -   NIO channels still operate under Java Security Manager rules (if enabled). Accessing files or network resources requires appropriate permissions.
    -   Direct buffers can sometimes be trickier to debug as their memory is outside the JVM heap, but this doesn't inherently pose a security risk itself.
    -   As with any network application, proper input validation and access control are paramount.

### 2.4 Common Traps & Best Practices for `java.nio`

-   **Buffer Lifecycle:** Mismanaging `position`, `limit`, `flip()`, `clear()`, `compact()` is a common source of bugs. Understand the state transitions.
-   **Channel Closing:** Always close `Channel`s and `Selector`s when done using `try-with-resources`.
-   **Non-blocking Mode:** Remember to call `channel.configureBlocking(false)` for channels you intend to use with a `Selector`.
-   **`keyIterator.remove()`:** Always remove `SelectionKey` from `selectedKeys` set after processing to prevent reprocessing the same event.
-   **Partial Reads/Writes:** In non-blocking mode, `read()` or `write()` might not transfer all requested data at once. You must loop until all data is read/written or the operation completes.
-   **`wakeup()`:** If a thread needs to unblock a `select()` call from another thread (e.g., to register a new channel), use `selector.wakeup()`.

### 2.5 Interview Questions

1.  What problems does `java.nio` solve compared to `java.io`?
2.  Explain the role of `Channels` and `Buffers` in `java.nio`.
3.  Describe the lifecycle of a `ByteBuffer` (from allocation to reading and resetting).
4.  How does `Selector` enable non-blocking I/O, and why is it important for server applications?
5.  What are direct buffers, and when would you use them? What are their trade-offs?
6.  Explain "zero-copy" I/O and how `FileChannel.transferTo()` achieves it.

## 3. New I/O 2 (NIO.2 - `java.nio.file`)

Introduced in Java 7, NIO.2 focuses on improved file system access and manipulation, offering a more robust, object-oriented, and platform-independent API than `java.io.File`.

**Why:** Addresses limitations of `java.io.File` (e.g., poor error handling, limited symbolic link support, lack of atomicity for certain operations, no watch service for file changes). Provides a cleaner, more powerful API.

### 3.1 `Path`, `Paths`, `Files`

These are the three central classes in NIO.2 for file system operations.

#### **`Path`**

-   Represents a path to a file or directory. It does *not* represent the file itself, but its location.
-   More robust than `java.io.File` for path manipulation (normalization, relativization).
-   Paths can be absolute or relative.

**Important Methods:**

| Method                                      | Purpose                                                                                                                                                 |
| :------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `getFileName()`                             | Returns the name element of the path.                                                                                                                   |
| `getParent()`                               | Returns the parent path, or null if this path has no parent.                                                                                            |
| `getRoot()`                                 | Returns the root component of this path, or null if this path does not have a root.                                                                     |
| `isAbsolute()`                              | Checks if this path is absolute.                                                                                                                        |
| `toAbsolutePath()`                          | Returns an absolute path for this path.                                                                                                                 |
| `normalize()`                               | Returns a path that is in normal form (removes redundant `.` and `..` components).                                                                      |
| `resolve(Path other)` / `resolve(String other)` | Joins two paths.                                                                                                                                        |
| `relativize(Path other)`                    | Constructs a relative path between this path and a given path.                                                                                          |
| `toUri()`                                   | Returns a `URI` representation of this path.                                                                                                            |
| `toFile()`                                  | Converts this `Path` to a `java.io.File` object (useful for integration with older APIs).                                                               |
| `iterator()`                                | Returns an iterator over the name elements of this path.                                                                                                |

#### **`Paths`**

-   A utility class with static factory methods to create `Path` objects.

**Important Methods:**

| Method                               | Purpose                                                                               |
| :----------------------------------- | :------------------------------------------------------------------------------------ |
| `get(String first, String... more)`  | Converts a path string or a sequence of strings into a `Path`.                        |
| `get(URI uri)`                       | Converts a `URI` to a `Path`.                                                         |

#### **`Files`**

-   A utility class providing static methods for common file system operations.
-   Many operations can take `OpenOption`s and `CopyOption`s for fine-grained control (e.g., `CREATE_NEW`, `APPEND`, `REPLACE_EXISTING`, `ATOMIC_MOVE`).

**Important Methods:**

| Method                                                                            | Purpose                                                                                                                                                                                                                                                                                                                                                          |
| :-------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `exists(Path path, LinkOption... options)`                                        | Tests whether a file exists.                                                                                                                                                                                                                                                                                                                                     |
| `notExists(Path path, LinkOption... options)`                                     | Tests whether the file does not exist.                                                                                                                                                                                                                                                                                                                           |
| `isReadable(Path path)`, `isWritable(Path path)`, `isExecutable(Path path)`       | Tests file access permissions.                                                                                                                                                                                                                                                                                                                                   |
| `createFile(Path path, FileAttribute<?>... attrs)`                                | Creates a new empty file. Throws `FileAlreadyExistsException` if the file exists.                                                                                                                                                                                                                                                                                |
| `createDirectory(Path dir, FileAttribute<?>... attrs)`                            | Creates a new directory. Throws `FileAlreadyExistsException` if the directory exists.                                                                                                                                                                                                                                                                            |
| `createDirectories(Path dir, FileAttribute<?>... attrs)`                          | Creates a directory and any necessary but nonexistent parent directories.                                                                                                                                                                                                                                                                                        |
| `delete(Path path)`                                                               | Deletes a file or empty directory. Throws `NoSuchFileException` if the file does not exist.                                                                                                                                                                                                                                                                      |
| `deleteIfExists(Path path)`                                                       | Deletes a file or directory if it exists. Returns `true` if deleted, `false` if not found.                                                                                                                                                                                                                                                                       |
| `copy(Path source, Path target, CopyOption... options)`                           | Copies a file or directory. Options like `REPLACE_EXISTING`, `COPY_ATTRIBUTES`, `ATOMIC_MOVE` (for moves only, usually).                                                                                                                                                                                                                                        |
| `move(Path source, Path target, CopyOption... options)`                           | Moves or renames a file or directory. Options like `REPLACE_EXISTING`, `ATOMIC_MOVE`.                                                                                                                                                                                                                                                                            |
| `readAllBytes(Path path)`                                                         | Reads all bytes from a file into a byte array. (Convenience for small files).                                                                                                                                                                                                                                                                                    |
| `write(Path path, byte[] bytes, OpenOption... options)`                           | Writes bytes to a file. Options like `CREATE`, `APPEND`, `TRUNCATE_EXISTING`.                                                                                                                                                                                                                                                                                    |
| `readAllLines(Path path, Charset cs)`                                             | Reads all lines from a file as a `List<String>`. (Convenience for small text files).                                                                                                                                                                                                                                                                             |
| `walk(Path start, FileVisitOption... options)`                                    | Returns a `Stream<Path>` that is a depth-first traversal of the file tree. Very powerful for recursive operations.                                                                                                                                                                                                                                               |
| `find(Path start, int maxDepth, BiPredicate<Path, BasicFileAttributes> matcher, FileVisitOption... options)` | Returns a `Stream<Path>` that is lazily populated with `Path`s that match the given `BiPredicate`. More flexible than `walk`.                                                                                                                                                                                                                              |
| `list(Path dir)`                                                                  | Returns a `Stream<Path>` of entries in a directory (not recursive).                                                                                                                                                                                                                                                                                              |
| `newInputStream(Path path, OpenOption... options)`                                | Opens a file for reading, returning an `InputStream`.                                                                                                                                                                                                                                                                                                            |
| `newOutputStream(Path path, OpenOption... options)`                               | Opens or creates a file for writing, returning an `OutputStream`.                                                                                                                                                                                                                                                                                                |
| `newBufferedReader(Path path, Charset cs)`                                        | Opens a file for reading, returning a `BufferedReader`.                                                                                                                                                                                                                                                                                                          |
| `newBufferedWriter(Path path, Charset cs, OpenOption... options)`                 | Opens or creates a file for writing, returning a `BufferedWriter`.                                                                                                                                                                                                                                                                                               |
| `getFileAttributeView(Path path, Class<V> type, LinkOption... options)`           | Retrieves a file attribute view of a given type (e.g., `BasicFileAttributeView`, `PosixFileAttributeView`). Enables more detailed attribute manipulation.                                                                                                                                                                                                 |
| `getLastModifiedTime(Path path, LinkOption... options)`                           | Returns the last modified time.                                                                                                                                                                                                                                                                                                                                  |
| `size(Path path)`                                                                 | Returns the size of a file in bytes.                                                                                                                                                                                                                                                                                                                             |
| `isSameFile(Path path1, Path path2)`                                              | Tests if two paths locate the same file (resolving symbolic links).                                                                                                                                                                                                                                                                                              |

**Practical Usage Example (NIO.2 File Operations):**

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class Nio2FileOperations {
    public static void main(String[] args) {
        Path dirPath = Paths.get("data");
        Path filePath = dirPath.resolve("my_file.txt");
        Path copiedFilePath = dirPath.resolve("my_file_copy.txt");
        Path movedFilePath = dirPath.resolve("moved_file.txt");

        try {
            // 1. Create directory if not exists
            if (Files.notExists(dirPath)) {
                Files.createDirectory(dirPath);
                System.out.println("Directory created: " + dirPath.toAbsolutePath());
            }

            // 2. Write content to a file
            String content = "Hello, NIO.2!\nThis is a new file.";
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("File written: " + filePath.toAbsolutePath());

            // 3. Read content from a file
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            System.out.println("File content:");
            lines.forEach(System.out::println);

            // 4. Copy a file
            Files.copy(filePath, copiedFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied to: " + copiedFilePath.toAbsolutePath());

            // 5. Move/Rename a file (atomic move is possible on some file systems)
            Files.move(filePath, movedFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved to: " + movedFilePath.toAbsolutePath());

            // 6. List directory contents (Java 8 Stream API)
            System.out.println("Directory contents of " + dirPath.getFileName() + ":");
            try (Stream<Path> stream = Files.list(dirPath)) {
                stream.forEach(p -> System.out.println(" - " + p.getFileName()));
            }

            // 7. Delete files
            Files.deleteIfExists(copiedFilePath);
            Files.deleteIfExists(movedFilePath);
            System.out.println("Copied and moved files deleted.");

            // 8. Delete directory (must be empty)
            Files.deleteIfExists(dirPath);
            System.out.println("Directory deleted if empty.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 3.2 `WatchService`

NIO.2 introduces the `WatchService` API for monitoring changes in directories (file creation, deletion, modification).

**Why:** Essential for applications that need to react to file system events (e.g., configuration watchers, log file processors). `java.io.File` has no built-in mechanism for this.

**How it works:**
1.  Create a `WatchService`.
2.  Register one or more `Path`s (directories) with the `WatchService`, specifying the event types to monitor (`ENTRY_CREATE`, `ENTRY_DELETE`, `ENTRY_MODIFY`).
3.  Periodically poll the `WatchService` for `WatchKey`s (which represent pending events).
4.  Retrieve and process the `WatchEvent`s from each `WatchKey`.
5.  Reset the `WatchKey` to continue receiving events.

**Important Classes & Methods:**

| Class/Interface      | Purpose                                                                                | Key Methods                                                                                                                                                                                                                                                                                               |
| :------------------- | :------------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `WatchService`       | Monitors registered objects for changes and events.                                    | `newWatchService()`: Factory method on `FileSystem` (or `Paths.get(".").getFileSystem().newWatchService()`). <br/> `take()`: Retrieves and removes the next `WatchKey`, blocking if none are present. <br/> `poll()`: Retrieves and removes the next `WatchKey`, returning null if none are present immediately. <br/> `close()`: Closes the watch service. |
| `Watchable`          | Interface implemented by objects that can be registered with a `WatchService` (e.g., `Path`). | `register(WatchService watcher, WatchEvent.Kind<?>... events)`: Registers this object with the given watch service for the specified event types.                                                                                                                                                             |
| `WatchKey`           | Represents the registration of a `Watchable` object with a `WatchService`.             | `pollEvents()`: Retrieves all pending events for this key. <br/> `reset()`: Resets the key, allowing it to be queued again for future events. Crucial. <br/> `isValid()`: Checks if the key is still valid. <br/> `watchable()`: Returns the `Watchable` object for which this key was created. |
| `WatchEvent.Kind`    | Defines the type of event to watch for.                                                | `ENTRY_CREATE`, `ENTRY_DELETE`, `ENTRY_MODIFY`: For files/directories. <br/> `OVERFLOW`: Indicates events might have been lost.                                                                                                                                                                              |

**Practical Usage Example (WatchService):**

```java
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.*;

public class WatchServiceExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path dir = Paths.get("watch_dir");
        Files.createDirectories(dir); // Ensure directory exists

        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            System.out.println("Watching directory: " + dir.toAbsolutePath());
            System.out.println("Create, modify, or delete files in 'watch_dir' to see events.");
            System.out.println("Press Ctrl+C to exit.");

            while (true) {
                WatchKey key;
                try {
                    key = watcher.take(); // Blocks until an event is available
                } catch (InterruptedException x) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == OVERFLOW) {
                        System.err.println("WARNING: File system event overflow, some events might have been lost.");
                        continue;
                    }

                    // The filename is the context of the event
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    // Resolve the full path
                    Path child = dir.resolve(filename);

                    System.out.printf("Event: %s on %s (Full path: %s)%n", kind.name(), filename, child.toAbsolutePath());
                }

                boolean valid = key.reset(); // Reset the key to receive further events
                if (!valid) {
                    System.out.println("WatchKey is no longer valid, exiting.");
                    break;
                }
            }
        } finally {
            // Clean up
            Files.deleteIfExists(dir);
        }
    }
}
```

### 3.3 Performance and Security Considerations for `java.nio.file`

-   **Performance:**
    -   `Files.walk()` and `Files.find()` use `Stream` API, enabling efficient, lazy processing of large file trees.
    -   Atomic operations (e.g., `ATOMIC_MOVE`) can improve reliability and potentially performance by leveraging OS capabilities.
    -   `newBufferedReader`/`newBufferedWriter` from `Files` implicitly use buffering, offering good performance for typical file operations.
-   **Security:**
    -   All operations are subject to the Java Security Manager (if present).
    -   `WatchService` might have platform-specific limitations (e.g., recursive watching isn't directly supported on all platforms, requiring manual traversal and registration of subdirectories).
    -   `LinkOption.NOFOLLOW_LINKS` is important for security when dealing with symbolic links to prevent "path traversal" vulnerabilities or accessing unintended files.

### 3.4 Common Traps & Best Practices for `java.nio.file`

-   **`Path` vs. `File`:** Prefer `Path` and `Files` for all new file system code. Only use `java.io.File` for interoperability with older APIs, converting via `Path.toFile()` or `File.toPath()`.
-   **Resource Management:** Always use `try-with-resources` for streams returned by `Files.newInputStream()`, `Files.newBufferedReader()`, and `WatchService`.
-   **Error Handling:** NIO.2 throws more specific exceptions (e.g., `NoSuchFileException`, `FileAlreadyExistsException`, `AccessDeniedException`), allowing for more precise error handling than generic `IOException`.
-   **`WatchService` - `reset()`:** Forgetting to call `key.reset()` is a very common bug that prevents further events for that directory.
-   **`WatchService` - Non-recursive:** `WatchService` only monitors the registered directory itself, not its subdirectories. For recursive monitoring, you need to manually traverse the directory tree and register each subdirectory.
-   **Symbolic Links:** Be aware of how symbolic links are handled. `LinkOption.NOFOLLOW_LINKS` can be crucial.

### 3.5 Interview Questions

1.  What are the key improvements of `java.nio.file` over `java.io.File`?
2.  Explain the purpose of `Path`, `Paths`, and `Files` classes.
3.  How would you perform a recursive search for all `.java` files in a directory using NIO.2?
4.  Describe a use case for `WatchService`. What are its limitations?
5.  What is `LinkOption.NOFOLLOW_LINKS`, and why is it important?
6.  How do `Files.readAllBytes()` and `Files.readAllLines()` differ from `newBufferedReader()` in terms of use cases and performance implications?

---

# Serialization & Deserialization

Serialization is the process of converting an object's state into a byte stream so that it can be stored in a file, transmitted across a network, or recreated later in the same or another JVM. Deserialization is the reverse process.

**Why:** Enables persistence and remote communication (e.g., RMI, saving user preferences, transferring objects between microservices).

## 1. The `Serializable` Interface

-   A **marker interface** in `java.io`. It has no methods.
-   A class must implement `Serializable` for its objects to be eligible for default serialization.
-   All fields of a `Serializable` object (unless `transient` or `static`) are serialized by default.

### 1.1 `ObjectOutputStream` and `ObjectInputStream`

These are the primary classes for performing serialization and deserialization.

**Important Classes & Methods:**

| Class             | Purpose                                                                                                                                               | Key Methods                                                                                                                                                                                                                                                                                              |
| :---------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `ObjectOutputStream` | Writes primitive data types and graphs of Java objects to an `OutputStream`.                                                                          | `writeObject(Object obj)`: Writes the specified object to the `OutputStream`. <br/> `flush()`: Flushes the stream. <br/> `close()`: Closes the stream. <br/> `defaultWriteObject()`: Performs default serialization for current object (used in custom serialization). <br/> `writeFields()`: Used for custom serialization. |
| `ObjectInputStream`  | Reads primitive data types and graphs of Java objects from an `InputStream`.                                                                          | `readObject()`: Reads an object from the `InputStream`. Returns `Object`, requires casting. Throws `ClassNotFoundException`, `InvalidClassException`. <br/> `close()`: Closes the stream. <br/> `defaultReadObject()`: Performs default deserialization (used in custom deserialization). <br/> `readFields()`: Used for custom deserialization. |

**Practical Usage Example:**

```java
import java.io.*;

// Make sure the class implements Serializable
class User implements Serializable {
    private static final long serialVersionUID = 1L; // Important!

    String username;
    // transient field will not be serialized
    transient String password;
    int age;
    // static fields are not part of object state, so not serialized
    static String company = "MyTechCo";

    public User(String username, String password, int age) {
        this.username = username;
        this.password = password;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", password='" + password + '\'' + // password will be null after deserialization
               ", age=" + age +
               ", company='" + company + '\'' +
               '}';
    }
}

public class SerializationExample {
    public static void main(String[] args) {
        User user = new User("alice", "securePass123", 30);
        System.out.println("Original object: " + user);

        // --- Serialization ---
        try (FileOutputStream fileOut = new FileOutputStream("user.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(user);
            System.out.println("User object serialized to user.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

        // --- Deserialization ---
        User deserializedUser = null;
        try (FileInputStream fileIn = new FileInputStream("user.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            deserializedUser = (User) in.readObject();
            System.out.println("User object deserialized from user.ser");
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("User class not found");
            c.printStackTrace();
            return;
        }

        System.out.println("Deserialized object: " + deserializedUser);
        // Note: password is null as it was marked transient
        // Note: company is still "MyTechCo" because static fields are not serialized/deserialized
        //       It retains its value from the current JVM's class state.
    }
}
```

### 1.2 `serialVersionUID`

-   A `static final long` field, e.g., `private static final long serialVersionUID = 1L;`.
-   **Why:** During deserialization, the `serialVersionUID` of the class being loaded is compared with the `serialVersionUID` stored in the serialized object. If they don't match, `InvalidClassException` is thrown. This mechanism ensures that a serialized object is compatible with the version of the class used to deserialize it.
-   **If omitted:** The Java compiler will generate one automatically based on class details (fields, methods, etc.). This auto-generated ID is highly sensitive to changes. Even adding a private method can change it, breaking deserialization.
-   **Best Practice:** Always explicitly declare `serialVersionUID`.
    -   If compatibility across versions is needed, keep the `serialVersionUID` constant.
    -   If a class undergoes incompatible changes and older serialized objects should no longer be deserialized, update `serialVersionUID` to a new value.

### 1.3 `transient` Keyword

-   **Purpose:** Marks a field to indicate that it should *not* be serialized.
-   **Why:**
    -   **Security:** To prevent sensitive information (like passwords, credit card numbers) from being persisted or transmitted.
    -   **Performance:** To exclude large, derived, or reconstructible fields that don't need to be saved/restored.
    -   **State:** Fields that are specific to the current JVM state (e.g., open file handles, thread objects).
-   **Behavior:** During deserialization, `transient` fields are initialized to their default values (e.g., `null` for objects, `0` for numeric primitives, `false` for booleans).

### 1.4 `static` Fields

-   **Behavior:** `static` fields belong to the class, not to any specific object instance. Therefore, they are **not serialized**.
-   **Consequence:** When an object is deserialized, its `static` fields will retain the values they currently hold in the JVM where deserialization is occurring, not the values they had when the object was serialized.

### 1.5 Custom Serialization Methods (`readObject`, `writeObject`, `readResolve`, `writeReplace`)

For fine-grained control over the serialization process, a class can define special methods that are invoked by `ObjectOutputStream` and `ObjectInputStream`.

**Why:**
-   **Validation:** Perform validation on deserialized data.
-   **Security:** Encrypt/decrypt sensitive fields, or apply access controls.
-   **Data Transformation:** Serialize/deserialize a different representation of an object.
-   **Version Compatibility:** Handle changes to the class structure across different versions.
-   **Singleton Preservation:** Ensure only a single instance of a class exists after deserialization.

#### `private void writeObject(ObjectOutputStream out) throws IOException`

-   Called by `ObjectOutputStream` to write the object's state.
-   Inside this method, you can:
    -   Call `out.defaultWriteObject()` to perform default serialization for non-transient fields.
    -   Manually write additional data using `out.writeInt()`, `out.writeObject()`, etc.

#### `private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException`

-   Called by `ObjectInputStream` to read the object's state.
-   Inside this method, you can:
    -   Call `in.defaultReadObject()` to perform default deserialization for non-transient fields.
    -   Manually read additional data in the *same order* it was written using `in.readInt()`, `in.readObject()`, etc.
    -   Perform validation or complex post-deserialization setup.

#### `protected Object writeReplace() throws ObjectStreamException`

-   **Why:** Allows an object to substitute a replacement object during serialization.
-   **When:** If a class wants to designate a proxy object to be written to the stream instead of itself.
-   **Behavior:** If this method exists, it's called *before* the object is serialized. The return value (the replacement object) is then serialized instead of the original object.
-   **Use Case:** Preventing serialization of internal data structures, or representing an object more efficiently.

#### `protected Object readResolve() throws ObjectStreamException`

-   **Why:** Allows an object to substitute a replacement object during deserialization.
-   **When:** If a class wants to designate a proxy object to be returned from `readObject()` instead of the object that was just deserialized.
-   **Behavior:** If this method exists, it's called *after* `readObject()` has completed. The return value (the replacement object) is then returned to the caller.
-   **Use Case:** Crucial for preserving the singleton property of a class during deserialization. If `readObject()` always creates a new instance, `readResolve()` can return the existing singleton instance.

**Practical Example (Custom Serialization for `User`):**

```java
import java.io.*;

class SecureUser implements Serializable {
    private static final long serialVersionUID = 2L;

    String username;
    // Store password securely as a byte array, not directly as String
    private byte[] encryptedPassword;
    int age;

    public SecureUser(String username, String password, int age) {
        this.username = username;
        this.age = age;
        this.setPassword(password); // Encrypt on construction
    }

    public void setPassword(String password) {
        // In a real app, use strong encryption, not just simple byte conversion
        this.encryptedPassword = password.getBytes();
    }

    public String getPassword() {
        return new String(this.encryptedPassword); // Decrypt on retrieval
    }

    // Custom serialization logic
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Serialize non-transient, non-static fields
        // Custom logic: write password length and then the bytes
        out.writeInt(encryptedPassword.length);
        out.write(encryptedPassword);
    }

    // Custom deserialization logic
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Deserialize non-transient, non-static fields
        // Custom logic: read password length and then the bytes
        int passwordLength = in.readInt();
        encryptedPassword = new byte[passwordLength];
        in.readFully(encryptedPassword); // Ensure all bytes are read
    }

    @Override
    public String toString() {
        return "SecureUser{" +
               "username='" + username + '\'' +
               ", password='[ENCRYPTED, NOT PRINTED]'" + // Don't print actual password
               ", age=" + age +
               '}';
    }
}

public class CustomSerializationExample {
    public static void main(String[] args) {
        SecureUser user = new SecureUser("bob", "topSecretPwd", 40);
        System.out.println("Original user: " + user.username + ", Age: " + user.age + ", Password: " + user.getPassword());

        // Serialization
        try (FileOutputStream fileOut = new FileOutputStream("secureUser.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(user);
            System.out.println("SecureUser object serialized.");
        } catch (IOException i) {
            i.printStackTrace();
        }

        // Deserialization
        SecureUser deserializedUser = null;
        try (FileInputStream fileIn = new FileInputStream("secureUser.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            deserializedUser = (SecureUser) in.readObject();
            System.out.println("SecureUser object deserialized.");
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }

        System.out.println("Deserialized user: " + deserializedUser.username + ", Age: " + deserializedUser.age + ", Password: " + deserializedUser.getPassword());
        System.out.println("Are objects same? " + (user == deserializedUser)); // false, new object created
    }
}
```

## 2. The `Externalizable` Interface

-   Extends `Serializable` and the `java.io.DataInput` and `java.io.DataOutput` interfaces.
-   **Why:** Provides complete control over the serialization and deserialization process.
-   **When to use:**
    -   When `Serializable`'s default behavior is insufficient or inefficient.
    -   To serialize only specific parts of an object.
    -   To optimize the serialized form (e.g., make it smaller, faster).
    -   To handle versioning across incompatible class changes.
    -   **Performance:** Can be faster than `Serializable` if done efficiently, as it avoids some of the overhead of `ObjectOutputStream` introspection.

### 2.1 `writeExternal()` and `readExternal()`

Classes implementing `Externalizable` *must* implement these two methods.

#### `void writeExternal(ObjectOutput out) throws IOException`

-   This method is responsible for writing the entire state of the object.
-   You must explicitly write all fields you want to serialize using `out.writeUTF()`, `out.writeInt()`, `out.writeObject()`, etc.

#### `void readExternal(ObjectInput in) throws IOException, ClassNotFoundException`

-   This method is responsible for reading and restoring the entire state of the object.
-   You must explicitly read all fields in the *same order* they were written in `writeExternal()` using `in.readUTF()`, `in.readInt()`, `in.readObject()`, etc.
-   **Important:** The `Externalizable` class **must have a public no-arg constructor**. During deserialization, `ObjectInputStream` first creates an instance of the class using its no-arg constructor, then calls `readExternal()` on that instance. If a no-arg constructor is missing, `InvalidClassException` is thrown.

**Practical Usage Example (`Externalizable`):**

```java
import java.io.*;

class Config implements Externalizable {
    private String setting1;
    private int value;
    private transient String temporaryData; // transient is ignored by Externalizable

    // MANDATORY: Public no-arg constructor for Externalizable
    public Config() {
        System.out.println("Config: No-arg constructor called during deserialization.");
    }

    public Config(String setting1, int value, String temporaryData) {
        this.setting1 = setting1;
        this.value = value;
        this.temporaryData = temporaryData;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("Config: writeExternal called.");
        // Manually write state. Order is crucial!
        out.writeUTF(setting1);
        out.writeInt(value);
        // temporaryData is transient, but we can still choose to serialize it here if needed
        // For this example, we explicitly don't write it, mimicking the default transient behavior.
        // If we wanted to serialize it, we would add: out.writeUTF(temporaryData);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        System.out.println("Config: readExternal called.");
        // Manually read state. Order must match writeExternal!
        this.setting1 = in.readUTF();
        this.value = in.readInt();
        // temporaryData remains null as it was not written
    }

    @Override
    public String toString() {
        return "Config{" +
               "setting1='" + setting1 + '\'' +
               ", value=" + value +
               ", temporaryData='" + temporaryData + '\'' + // will be null after deserialization
               '}';
    }
}

public class ExternalizationExample {
    public static void main(String[] args) {
        Config originalConfig = new Config("AppMode", 10, "Ignored Temp Data");
        System.out.println("Original config: " + originalConfig);

        // Serialization
        try (FileOutputStream fileOut = new FileOutputStream("config.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(originalConfig);
            System.out.println("Config object externalized to config.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

        // Deserialization
        Config deserializedConfig = null;
        try (FileInputStream fileIn = new FileInputStream("config.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            deserializedConfig = (Config) in.readObject();
            System.out.println("Config object deserialized from config.ser");
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }

        System.out.println("Deserialized config: " + deserializedConfig);
        // Note: temporaryData is null because it was not explicitly written/read in writeExternal/readExternal
    }
}
```

## 3. Serialization Security & Performance Considerations

### 3.1 Security Concerns

-   **Deserialization Vulnerabilities:** Deserializing untrusted data is extremely dangerous. Malicious actors can craft serialized objects that, when deserialized, execute arbitrary code (remote code execution - RCE), manipulate application logic, or cause denial of service.
    -   **Why:** Deserialization often involves calling methods on the object (e.g., `readObject`, setters, custom logic). If an attacker controls the serialized stream, they can exploit gadgets (vulnerable methods in classes present on the classpath) to achieve RCE.
    -   **Mitigation:**
        -   **NEVER deserialize untrusted data.** If input is from an external source, assume it's untrusted.
        -   **Whitelist/Blacklist:** Use `ObjectInputFilter` (Java 9+) to restrict which classes can be deserialized. This is the most effective defense.
        -   **Replace Java Serialization:** For network communication or persisting data, consider safer alternatives like JSON, Protocol Buffers, Avro, or custom binary formats.
-   **Information Leakage:** Unless fields are marked `transient` or handled via custom serialization/`Externalizable`, sensitive data can be inadvertently serialized and persisted.
-   **Mutable Objects:** If mutable objects are part of the serialized state, an attacker might modify them after deserialization if references are exposed.
-   **Class Evolution:** Changes to a `Serializable` class (adding/removing fields, changing field types, incompatible API changes) without updating `serialVersionUID` or handling compatibility correctly can lead to `InvalidClassException` or silent data corruption.

### 3.2 Performance Implications

-   **Reflection Overhead:** Java's default serialization mechanism uses reflection heavily to inspect class fields, which incurs performance overhead.
-   **Object Graph Traversal:** It recursively serializes the entire object graph accessible from the root object, which can be slow and memory-intensive for large or complex graphs.
-   **Data Size:** The default format includes class metadata, which can make the serialized data larger than necessary.
-   **`Externalizable` for Performance:** `Externalizable` can offer better performance and smaller payload sizes because you have complete control over what is written and how. This eliminates the reflection overhead and allows for more compact data representation.
-   **Alternatives for Performance:** For high-performance scenarios, especially in distributed systems, consider alternatives like Protocol Buffers, Apache Thrift, or Apache Avro. These are often faster and produce smaller serialized forms.

### 3.3 Common Traps & Best Practices

-   **`serialVersionUID`:** Always declare it explicitly.
-   **Security:** Avoid deserializing untrusted data.
-   **`transient`:** Use it for sensitive data, derived fields, or fields holding resources.
-   **No-arg Constructor for `Externalizable`:** Mandatory.
-   **Order of Operations for `Externalizable`:** The order of writing and reading fields in `writeExternal()` and `readExternal()` *must* be identical.
-   **Immutable Objects:** If a class is designed to be immutable, ensure its serialized form also preserves immutability (e.g., by making defensive copies of mutable components during deserialization).
-   **Singleton Preservation:** For singletons, always implement `readResolve()` to return the actual singleton instance. Otherwise, deserialization will create a new instance, breaking the singleton pattern.
-   **Inheritance:**
    -   If a superclass is `Serializable`, its fields will be serialized.
    -   If a superclass is *not* `Serializable`, its constructor (the no-arg one) will be invoked during the deserialization of its `Serializable` subclass. This means the non-serializable superclass's state must be correctly initialized by its constructor.

### 3.4 Interview Questions

1.  What is serialization in Java? Why is it used?
2.  Explain the difference between `Serializable` and `Externalizable`. When would you choose one over the other?
3.  What is `serialVersionUID` and why is it important? What happens if you don't declare it?
4.  How do `transient` and `static` keywords affect serialization?
5.  Describe the purpose of `writeObject()`/`readObject()` and `writeReplace()`/`readResolve()`. Provide scenarios where they would be used.
6.  Discuss the security implications of deserialization. How can you mitigate deserialization vulnerabilities?
7.  How does serialization impact performance? What are alternatives for high-performance data transfer?
8.  You have a `Serializable` class with a non-`Serializable` superclass. How is the superclass state handled during deserialization?

---

# Reflection (`java.lang.reflect`)

Reflection is a powerful feature in Java that allows a program to inspect and manipulate (at runtime) its own structure, including classes, methods, fields, and constructors.

**Why:** Enables dynamic behavior, allowing code to operate on objects whose types are not known at compile time. Essential for frameworks (Spring, Hibernate, JUnit), IDEs, debuggers, and various meta-programming tasks.

## 1. Class Loading Basics

Before reflection can inspect a class, the class must be loaded into the JVM.

-   **Class Loaders:** Responsible for loading `.class` files into the JVM.
    -   **Bootstrap Class Loader:** Loads core Java API classes (`rt.jar`).
    -   **Extension Class Loader:** Loads classes from the `ext` directory.
    -   **System/Application Class Loader:** Loads classes from the application's classpath.
    -   **Custom Class Loaders:** Developers can create their own class loaders for specific needs (e.g., hot deployment, loading classes from a network).
-   **`Class.forName(String className)`:**
    -   Loads the class identified by `className` into the JVM.
    -   Crucially, it also **initializes** the class (runs static initializers).
    -   Returns a `Class` object.
    -   Throws `ClassNotFoundException`.
-   **`MyClass.class`:**
    -   Obtains the `Class` object for `MyClass`.
    -   Does *not* cause class initialization if it hasn't happened already.
-   **`object.getClass()`:**
    -   Returns the `Class` object of the runtime type of `object`.
    -   The class must already be loaded.

## 2. Core Reflection APIs (`Class`, `Method`, `Field`, `Constructor`)

The `java.lang.Class` object is the entry point for all reflection operations. From it, you can obtain `Method`, `Field`, and `Constructor` objects.

### 2.1 The `Class` Class

-   Represents classes and interfaces in a running Java application.
-   The source of all reflection information.

**Important Methods:**

| Method                                      | Purpose                                                                                                                                                                                                                                                              |
| :------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `forName(String className)`                 | Returns the `Class` object associated with the class or interface with the given string name. Loads and initializes the class.                                                                                                                                         |
| `forName(String name, boolean initialize, ClassLoader loader)` | More controlled version. Allows specifying whether to initialize and which `ClassLoader` to use.                                                                                                                                                      |
| `getSuperclass()`                           | Returns the `Class` representing the superclass of the entity represented by this `Class`.                                                                                                                                                                           |
| `getInterfaces()`                           | Returns an array of `Class` objects representing the interfaces implemented by the class or interface represented by this `Class`.                                                                                                                                   |
| `getCanonicalName()`                        | Returns the canonical name of the underlying class as defined by the Java Language Specification.                                                                                                                                                                    |
| `getPackage()`                              | Returns the `Package` of this class.                                                                                                                                                                                                                               |
| `newInstance()` (Deprecated since Java 9)   | Creates a new instance of the class represented by this `Class` object. Assumes a no-arg constructor. Throws `InstantiationException`, `IllegalAccessException`. **Prefer `Constructor.newInstance()`**.                                                              |
| `getConstructor(Class<?>... parameterTypes)`| Returns a `Constructor` object that reflects the specified public constructor.                                                                                                                                                                                       |
| `getConstructors()`                         | Returns an array containing `Constructor` objects reflecting all the public constructors of the class.                                                                                                                                                               |
| `getDeclaredConstructor(Class<?>... parameterTypes)` | Returns a `Constructor` object that reflects the specified constructor of the class or interface represented by this `Class` object. *Includes non-public constructors*.                                                                                               |
| `getDeclaredConstructors()`                 | Returns an array of `Constructor` objects reflecting all the constructors declared by the class or interface represented by this `Class` object. *Includes non-public constructors*.                                                                                     |
| `getMethod(String name, Class<?>... parameterTypes)` | Returns a `Method` object that reflects the specified public method of the class or interface. *Includes inherited public methods*.                                                                                                                              |
| `getMethods()`                              | Returns an array containing `Method` objects reflecting all the public methods of the class or interface. *Includes inherited public methods*.                                                                                                                         |
| `getDeclaredMethod(String name, Class<?>... parameterTypes)` | Returns a `Method` object that reflects the specified declared method of the class or interface. *Only methods explicitly declared by this class, regardless of access modifier*.                                                                      |
| `getDeclaredMethods()`                      | Returns an array of `Method` objects reflecting all the methods declared by the class or interface. *Only methods explicitly declared by this class, regardless of access modifier*.                                                                                    |
| `getField(String name)`                     | Returns a `Field` object that reflects the specified public field of the class or interface. *Includes inherited public fields*.                                                                                                                                     |
| `getFields()`                               | Returns an array containing `Field` objects reflecting all the public fields of the class or interface. *Includes inherited public fields*.                                                                                                                            |
| `getDeclaredField(String name)`             | Returns a `Field` object that reflects the specified declared field of the class or interface. *Only fields explicitly declared by this class, regardless of access modifier*.                                                                                             |
| `getDeclaredFields()`                       | Returns an array of `Field` objects reflecting all the fields declared by the class or interface. *Only fields explicitly declared by this class, regardless of access modifier*.                                                                                     |
| `isAnnotationPresent(Class<? extends Annotation> annotationClass)` | Returns true if an annotation for the specified type is present on this element.                                                                                                                                                                       |
| `getAnnotation(Class<A> annotationClass)`   | Returns this element's annotation for the specified type if such an annotation is present, else null.                                                                                                                                                                |
| `getAnnotations()`                          | Returns all annotations present on this element.                                                                                                                                                                                                                     |

### 2.2 `Method`

-   Represents a method of a class or interface.
-   Allows invocation of methods dynamically.

**Important Methods:**

| Method                                      | Purpose                                                                                                                                                                                                                                                                                        |
| :------------------------------------------ | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `getName()`                                 | Returns the name of the method.                                                                                                                                                                                                                                                                |
| `getReturnType()`                           | Returns a `Class` object that represents the formal return type of the method.                                                                                                                                                                                                                 |
| `getParameterTypes()`                       | Returns an array of `Class` objects that represent the formal parameter types, in declaration order.                                                                                                                                                                                           |
| `getExceptionTypes()`                       | Returns an array of `Class` objects that represent the types of exceptions declared to be thrown by the underlying method.                                                                                                                                                                       |
| `getModifiers()`                            | Returns the Java language modifiers for this method. Use `Modifier.isPublic()`, `Modifier.isStatic()`, etc.                                                                                                                                                                                    |
| `invoke(Object obj, Object... args)`        | Invokes the underlying method represented by this `Method` object, on the specified `obj` with the specified `args`. For static methods, `obj` can be `null`. Returns the method's return value. Throws `IllegalAccessException`, `IllegalArgumentException`, `InvocationTargetException`. |
| `setAccessible(boolean flag)`               | Allows suppressed access control checks. Set to `true` to access private/protected methods/fields. **Security risk!**                                                                                                                                                                          |

### 2.3 `Field`

-   Represents a field (member variable) of a class or interface.
-   Allows dynamic getting and setting of field values.

**Important Methods:**

| Method                                      | Purpose                                                                                                                                                                                                                                               |
| :------------------------------------------ | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `getName()`                                 | Returns the name of the field.                                                                                                                                                                                                                        |
| `getType()`                                 | Returns a `Class` object that identifies the declared type for the field.                                                                                                                                                                             |
| `getModifiers()`                            | Returns the Java language modifiers for this field.                                                                                                                                                                                                   |
| `get(Object obj)`                           | Returns the value of the field represented by this `Field` object, on the specified `obj`. For static fields, `obj` can be `null`. Throws `IllegalAccessException`, `IllegalArgumentException`.                                                      |
| `set(Object obj, Object value)`             | Sets the field represented by this `Field` object, on the specified `obj` to the new `value`. For static fields, `obj` can be `null`. Throws `IllegalAccessException`, `IllegalArgumentException`.                                                |
| `setAccessible(boolean flag)`               | Allows suppressed access control checks. Set to `true` to access private/protected methods/fields. **Security risk!**                                                                                                                                 |

### 2.4 `Constructor`

-   Represents a constructor of a class.
-   Allows dynamic creation of new instances.

**Important Methods:**

| Method                                      | Purpose                                                                                                                                                                                                                                                              |
| :------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `getName()`                                 | Returns the name of this constructor (same as the class name).                                                                                                                                                                                                       |
| `getParameterTypes()`                       | Returns an array of `Class` objects that represent the formal parameter types.                                                                                                                                                                                       |
| `getModifiers()`                            | Returns the Java language modifiers for this constructor.                                                                                                                                                                                                            |
| `newInstance(Object... initargs)`           | Creates a new instance of the class represented by this `Constructor` object, with the specified initialization `initargs`. Throws `InstantiationException`, `IllegalAccessException`, `IllegalArgumentException`, `InvocationTargetException`. |
| `setAccessible(boolean flag)`               | Allows suppressed access control checks. Set to `true` to access private/protected constructors. **Security risk!**                                                                                                                                                    |

### 2.5 Dynamic Invocation

Using the `invoke()` method of `Method` and `newInstance()` method of `Constructor` to execute code or create objects whose specifics are not known at compile time.

**Practical Usage Example (Reflection):**

```java
import java.lang.reflect.*;

class ReflectionTarget {
    private String privateField = "private value";
    public String publicField = "public value";
    protected int number = 10;

    public ReflectionTarget() {}

    private ReflectionTarget(String privateField, int number) {
        this.privateField = privateField;
        this.number = number;
    }

    public void publicMethod(String param) {
        System.out.println("Public method invoked with: " + param);
    }

    private String privateMethod(String prefix) {
        return prefix + ":" + privateField;
    }

    public static void staticMethod() {
        System.out.println("Static method invoked.");
    }
}

public class ReflectionExample {
    public static void main(String[] args) throws Exception {
        // 1. Get Class object
        Class<?> targetClass = Class.forName("ReflectionTarget");
        System.out.println("Class Name: " + targetClass.getName());

        // 2. Create instance using reflection (no-arg constructor)
        ReflectionTarget instance1 = (ReflectionTarget) targetClass.getDeclaredConstructor().newInstance();
        System.out.println("Instance 1 public field (default): " + instance1.publicField);

        // 3. Access and modify public field
        Field publicField = targetClass.getField("publicField");
        System.out.println("Original public field value: " + publicField.get(instance1));
        publicField.set(instance1, "updated public value");
        System.out.println("Updated public field value: " + publicField.get(instance1));

        // 4. Access and modify private field (requires setAccessible(true))
        Field privateField = targetClass.getDeclaredField("privateField");
        privateField.setAccessible(true); // Suppress access checks!
        System.out.println("Original private field value: " + privateField.get(instance1));
        privateField.set(instance1, "updated private value via reflection");
        System.out.println("Updated private field value: " + privateField.get(instance1));

        // 5. Invoke public method
        Method publicMethod = targetClass.getMethod("publicMethod", String.class);
        publicMethod.invoke(instance1, "Hello Reflection!");

        // 6. Invoke private method (requires setAccessible(true))
        Method privateMethod = targetClass.getDeclaredMethod("privateMethod", String.class);
        privateMethod.setAccessible(true); // Suppress access checks!
        String result = (String) privateMethod.invoke(instance1, "Secret");
        System.out.println("Private method result: " + result);

        // 7. Invoke static method
        Method staticMethod = targetClass.getMethod("staticMethod");
        staticMethod.invoke(null); // 'obj' can be null for static methods

        // 8. Create instance using a private constructor
        Constructor<ReflectionTarget> privateConstructor =
                (Constructor<ReflectionTarget>) targetClass.getDeclaredConstructor(String.class, int.class);
        privateConstructor.setAccessible(true); // Suppress access checks!
        ReflectionTarget instance2 = privateConstructor.newInstance("constructor private field", 99);
        // Verify private field was set by constructor (need reflection to access it)
        Field instance2PrivateField = targetClass.getDeclaredField("privateField");
        instance2PrivateField.setAccessible(true);
        System.out.println("Instance 2 private field (from constructor): " + instance2PrivateField.get(instance2));
    }
}
```

## 3. Annotations via Reflection

Reflection is commonly used to read and process annotations at runtime.

**Why:** Allows frameworks and libraries to define custom metadata that can influence application behavior without modifying the core logic.

### Important Methods:

-   `Class.isAnnotationPresent(Class<? extends Annotation> annotationClass)`
-   `Class.getAnnotation(Class<A> annotationClass)`
-   `Class.getAnnotations()`
-   Similar methods exist on `Method`, `Field`, `Constructor`, `Parameter`.

**Practical Usage Example (Annotations with Reflection):**

```java
import java.lang.annotation.*;
import java.lang.reflect.Method;

// Custom annotation definition
@Retention(RetentionPolicy.RUNTIME) // Crucial for runtime reflection
@Target(ElementType.METHOD)
@interface MyTest {
    String name() default "defaultTest";
    int timeout() default 0;
}

class AnnotatedClass {
    @MyTest(name = "firstTest", timeout = 100)
    public void testMethod1() {
        System.out.println("Running testMethod1");
    }

    @MyTest // Using default values
    public void testMethod2() {
        System.out.println("Running testMethod2");
    }

    public void normalMethod() {
        System.out.println("This is a normal method.");
    }
}

public class AnnotationReflectionExample {
    public static void main(String[] args) throws Exception {
        Class<AnnotatedClass> clazz = AnnotatedClass.class;
        AnnotatedClass instance = new AnnotatedClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                MyTest testAnnotation = method.getAnnotation(MyTest.class);
                System.out.println("Found @MyTest annotation on method: " + method.getName());
                System.out.println("  Test Name: " + testAnnotation.name());
                System.out.println("  Timeout: " + testAnnotation.timeout() + "ms");

                // Dynamically invoke the annotated method
                try {
                    method.invoke(instance);
                } catch (Exception e) {
                    System.err.println("  Error invoking method " + method.getName() + ": " + e.getCause());
                }
            }
        }
    }
}
```

## 4. Performance Impact and When NOT to Use Reflection

### 4.1 Performance Impact

-   **Significant Overhead:** Reflection operations are generally much slower than direct (non-reflective) calls.
    -   **Why:**
        -   **Security Checks:** Reflection bypasses Java's normal access control, requiring extra security checks.
        -   **Method Lookup:** Resolving methods/fields by name and parameters involves string comparisons and searches, which is slower than direct bytecode linkage.
        -   **Boxing/Unboxing:** Primitive arguments/return values often require boxing/unboxing, adding overhead.
        -   **JIT Optimization:** Reflected calls are harder for the JIT compiler to optimize because the target method/field is not known until runtime.
-   **`setAccessible(true)`:** While it disables access checks, it doesn't entirely eliminate the overhead. Repeated reflective access to the same member can be faster after the initial `setAccessible(true)` call (due to caching in some JVMs), but still significantly slower than direct calls.

### 4.2 When NOT to Use Reflection

-   **Performance-Critical Code:** Avoid reflection in tight loops or scenarios where high throughput is essential.
-   **Directly Known Types:** If the class, method, and field are known at compile time, use direct calls. Reflection adds unnecessary complexity and overhead.
-   **Security Bypass:** Do not use `setAccessible(true)` frivolously. It breaks encapsulation and can expose internal implementation details, making code fragile and potentially insecure.
-   **Alternative Designs:** Often, Polymorphism, Interfaces, or Dependency Injection can achieve similar flexibility to