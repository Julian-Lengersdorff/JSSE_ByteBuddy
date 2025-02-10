# **JSSE_ByteBuddy**
Hook JSSE client applications with Byte Buddy and extract TLS keying material for TLS verisons
1.2 and 1.3.

## 📥 Installation & Build
Clone the repository and build the JAR:
```bash
git clone https://github.com/Julian-Lengersdorff/JSSE_ByteBuddy.git
cd jsse-bytebuddy
./gradlew build
```

## **Running the Java Agent**
Attach the **HookingAgent** to the process with **Process ID (PID)**:
```bash
java -cp ./build/classes/java/main org.example.attacher.Attacher <PID> ./build/libs/HookingAgent.jar
```

If **agent attachment fails**, try running the JSSE client application with:
```bash
--add-opens java.base/sun.security.ssl=ALL-UNNAMED
```