package org.example.agent;

import net.bytebuddy.asm.Advice;

public class MainAdvice {
    @Advice.OnMethodEnter
    public static void onEnter(@Advice.Origin("#m") String methodName) {
        System.out.println("[Byte Buddy] entering method: " + methodName);
    }
}
