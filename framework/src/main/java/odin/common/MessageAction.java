
package odin.common;

@FunctionalInterface
public interface MessageAction<T, Z> {
    Z executeAction(T msg);
}
