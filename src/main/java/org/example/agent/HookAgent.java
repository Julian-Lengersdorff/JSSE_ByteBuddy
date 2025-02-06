package org.example.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.bytebuddy.matcher.ElementMatchers.none;

public class HookAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        installHook(inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        installHook(inst);
    }

    private static void installHook(Instrumentation inst) {

        // TODO: Set ClassReloaderStartegy to allow hooking of classes loaded by Bootstrap
        /*
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    for (Class<?> loadClass : inst.getAllLoadedClasses()){
                        if (loadClass.getName().equals("sun.security.ssl.SSLSessionImpl")) {
                            /*
                            File file = new File("/tmp/tmpjars");
                            ClassLoadingStrategy cLS = new ClassLoadingStrategy.ForBootstrapInjection(inst, file); // ???

                            new AgentBuilder.Default()
                                    .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                                    .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                                    .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                                    .disableClassFormatChanges()
                                    .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
                                    .with(AgentBuilder.InstallationListener.StreamWriting.toSystemError())
                                    .ignore(none())
                                    .type(ElementMatchers.named("sun.security.ssl.SSLSessionImpl"))
                                    .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder
                                            .visit(Advice.to(GetMasterSecretAdvice.class)
                                                    .on(ElementMatchers.named("getMasterSecret")))
                                    )
                                    .installOn(inst);
                            return;
                        }
                    }
                }, 0, 2, TimeUnit.SECONDS);
            */



        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .disableClassFormatChanges()
                .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
                .with(AgentBuilder.InstallationListener.StreamWriting.toSystemError())
                .ignore(none())
                .type(ElementMatchers.named("javax.net.ssl.SSLContext"))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder
                        .visit(Advice.to(GetMasterSecretAdvice.class)
                                .on(ElementMatchers.named("getInstance")))
                )
                .installOn(inst);

        /*
        * [Byte Buddy] ERROR javax.net.ssl.SSLContext [null, module java.base, Thread[#1,main,5,main], loaded=false]
java.lang.IllegalStateException: Cannot assign class javax.net.ssl.SSLContext to interface javax.crypto.SecretKey
        at net.bytebuddy.asm.Advice$OffsetMapping$ForReturnValue.resolve(Advice.java:4533)
        at net.bytebuddy.asm.Advice$Dispatcher$Inlining$Resolved$ForMethodExit.doApply(Advice.java:10263)
        at net.bytebuddy.asm.Advice$Dispatcher$Inlining$Resolved$ForMethodExit.apply(Advice.java:10221)
        at net.bytebuddy.asm.Advice$Dispatcher$Inlining$Resolved$AdviceMethodInliner.visitMethod(Advice.java:9621)
        at net.bytebuddy.jar.asm.ClassReader.readMethod(ClassReader.java:1354)
        at net.bytebuddy.jar.asm.ClassReader.accept(ClassReader.java:745)
        at net.bytebuddy.utility.AsmClassReader$ForAsm.accept(AsmClassReader.java:225)
        at net.bytebuddy.asm.Advice$Dispatcher$Inlining$Resolved$AdviceMethodInliner.apply(Advice.java:9614)
        at net.bytebuddy.asm.Advice$AdviceVisitor$WithExitAdvice.onUserEnd(Advice.java:12176)
        at net.bytebuddy.asm.Advice$AdviceVisitor.visitMaxs(Advice.java:11955)
        at net.bytebuddy.jar.asm.ClassReader.readCode(ClassReader.java:2664)
        at net.bytebuddy.jar.asm.ClassReader.readMethod(ClassReader.java:1512)
        at net.bytebuddy.jar.asm.ClassReader.accept(ClassReader.java:745)
        at net.bytebuddy.utility.AsmClassReader$ForAsm.accept(AsmClassReader.java:225)
        at net.bytebuddy.dynamic.scaffold.TypeWriter$Default$ForInlining.create(TypeWriter.java:4034)
        at net.bytebuddy.dynamic.scaffold.TypeWriter$Default.make(TypeWriter.java:2246)
        at net.bytebuddy.dynamic.DynamicType$Builder$AbstractBase$UsingTypeWriter.make(DynamicType.java:4092)
        at net.bytebuddy.agent.builder.AgentBuilder$Default$ExecutingTransformer.doTransform(AgentBuilder.java:12726)
        at net.bytebuddy.agent.builder.AgentBuilder$Default$ExecutingTransformer.transform(AgentBuilder.java:12661)
        at net.bytebuddy.agent.builder.AgentBuilder$Default$ExecutingTransformer.access$1800(AgentBuilder.java:12370)
        at net.bytebuddy.agent.builder.AgentBuilder$Default$ExecutingTransformer$Java9CapableVmDispatcher.run(AgentBuilder.java:13152)
        at net.bytebuddy.agent.builder.AgentBuilder$Default$ExecutingTransformer$Java9CapableVmDispatcher.run(AgentBuilder.java:13082)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:400)
        at net.bytebuddy.agent.builder.AgentBuilder$Default$ExecutingTransformer.doPrivileged(AgentBuilder.java)
        at net.bytebuddy.agent.builder.AgentBuilder$Default$ExecutingTransformer.transform(AgentBuilder.java:12604)
        at net.bytebuddy.agent.builder.AgentBuilder$Default$ExecutingTransformer$ByteBuddy$ModuleSupport.transform(Unknown Source)
        at java.instrument/sun.instrument.TransformerManager.transform(TransformerManager.java:188)
        at java.instrument/sun.instrument.InstrumentationImpl.transform(InstrumentationImpl.java:610)
        at java.base/java.lang.ClassLoader.findBootstrapClass(Native Method)
        at java.base/java.lang.ClassLoader.findBootstrapClassOrNull(ClassLoader.java:1277)
        at java.base/java.lang.System$2.findBootstrapClassOrNull(System.java:2397)
        at java.base/jdk.internal.loader.ClassLoaders$BootClassLoader.loadClassOrNull(ClassLoaders.java:140)
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClassOrNull(BuiltinClassLoader.java:700)
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClassOrNull(BuiltinClassLoader.java:669)
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:639)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:526)
        at com.example.test_client_12_jsse.TestClient12JSSE.main(TestClient12JSSE.java:21)
        * */




        /*
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .type(ElementMatchers.named("javax.net.ssl.SSLContext"))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder
                        .visit(Advice.to(ContextInitAdvice.class)
                                .on(ElementMatchers.named("getInstance"))))
                .installOn(inst);


        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .type(ElementMatchers.named("com.example.test_client_12_jsse.TestClient12JSSE"))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder
                        .visit(Advice.to(MainAdvice.class)
                                .on(ElementMatchers.named("main")))
                )
                .installOn(inst);

         */


    }
}
