
package odin.common;

@FunctionalInterface
public interface MessageAction<T, K> {
    K executeAction(T msg);
}
