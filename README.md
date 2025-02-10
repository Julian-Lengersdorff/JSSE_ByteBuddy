# **JSSE_ByteBuddy**
Extracting TLS keying material from JSSE client applications.

## **Usage**
Attach the **HookingAgent** to the process with **Process ID (PID)**:
```bash
java -cp ./build/classes/java/main org.example.attacher.Attacher <PID> ./build/libs/HookingAgent.jar
```

If **agent attachment fails**, try running the JSSE client application with:
```bash
--add-opens java.base/sun.security.ssl=ALL-UNNAMED
```