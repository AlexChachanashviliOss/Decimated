package io.github.achacha.examples.decimated.deadcode;

import io.github.achacha.decimated.deadcode.DeadCode;

import java.lang.reflect.Method;

public class CheckIfMethodUsed {
    /**
     * This method is only called by reflection and may not seem like it is used
     * Adding a trigger will log once when this method is used and how it is used
     */
    public static void methodStillUsed() {
        DeadCode.trigger((accessPoint)-> System.out.println("This method is still used.\n"+accessPoint));
    }

    public void someMethod() {
        // Do stuff

        // Dynamically invoke method which may not be caught by static analysis and 'methodStillUsed' may show up as unused
        for (int i=0; i<10; ++i) {
            try {
                Method dynamicMethod = CheckIfMethodUsed.class.getMethod("methodStillUsed");
                dynamicMethod.invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Do more stuff
    }

    public static void main(String[] args) {
        DeadCode.initialize("io.github.achacha.decimated.deadcode");
        CheckIfMethodUsed self = new CheckIfMethodUsed();
        self.someMethod();
    }
}
