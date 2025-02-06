package org.example.agent;

import net.bytebuddy.asm.Advice;
import javax.crypto.SecretKey;

public class GetMasterSecretAdvice {

    @Advice .OnMethodEnter
    public static void onEnter(@Advice.Origin("#m") String methodName) {
        System.out.println("[Byte Buddy] entering method: " + methodName);
    }

    @Advice .OnMethodExit (onThrowable = Throwable.class)
    public static void onExit(@Advice.Origin("#m") String methodName, @Advice.Return(readOnly = false) SecretKey retval) {
        System.out.println("[Byte Buddy] exiting method: " + methodName + " return: " + retval);
    }
}
